package hu.cubix.hr.iroman.service;

import org.springframework.security.access.AccessDeniedException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Request;
import hu.cubix.hr.iroman.model.RequestStatus;
import hu.cubix.hr.iroman.repository.RequestRepository;
import hu.cubix.hr.iroman.security.HrUser;
import jakarta.transaction.Transactional;

@Service
public class RequestService {

	@Autowired
	private RequestRepository requestRepository;

	public Request create(Request request) {

		if (findById(request.getId()) != null) {
			return null;
		}
		return save(request);

	}

	@Transactional
	public Request save(Request request) {
		return requestRepository.save(request);
	}

	public Request findById(long requestId) {

		return requestRepository.findByIdWithEmployees(requestId);
	}

	public List<Request> findAllRequest(Pageable pageable) {
		Page<Request> pageOfRequest = requestRepository.findAllWithEmployees(pageable);

		return pageOfRequest.getContent();
	}

	@Transactional
	public void delete(long id) {

		String status = requestRepository.findRequestSatusById(id).toString();

		if (status.equals(RequestStatus.ACCEPTED.toString()) || status.equals(RequestStatus.REJECTED.toString())) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		Request request = requestRepository.findById(id).get();

		if (!request.getRequester().getId().equals(getCurrentHrUser().getEmployee().getId())) {
			throw new AccessDeniedException("Cannot delete other user's holiday request");
		}

		requestRepository.deleteById(id);
	}

	private HrUser getCurrentHrUser() {
		return (HrUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	@Transactional
	public Request acceptRequest(long requestId) {
		Request request = requestRepository.findByIdWithEmployees(requestId);
		if (!request.getRequester().getManager().getId().equals(getCurrentHrUser().getEmployee().getId())) {
			throw new AccessDeniedException("Only the manager of the requester can approve the request");
		}
		request.setRequestSatus(RequestStatus.ACCEPTED);
		return request;
	}

	@Transactional
	public Request updateRequest(Request request) {
		Request newRequest = findById(request.getId());

		if (newRequest == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		if (request.getRequestSatus() == RequestStatus.ACCEPTED
				|| request.getRequestSatus() == RequestStatus.REJECTED) {
			throw new ResponseStatusException(HttpStatus.FORBIDDEN);
		}

		newRequest.setStartDate(request.getStartDate());
		newRequest.setEndDate(request.getEndDate());
		newRequest.setDateOfRequest(LocalDateTime.now());

		if (request.getRequestSatus() != null && (request.getRequestSatus() == RequestStatus.PENDING_APPROVAL
				|| request.getRequestSatus() == RequestStatus.UNDER_RECORDING)) {
			newRequest.setRequestSatus(request.getRequestSatus());
		}
		return newRequest;
	}

	public Page<Request> findRequestsByExample(Request example, LocalDateTime startDate, LocalDateTime endDate,
			LocalDate startPeriod, LocalDate endPeriod, Pageable pageable) {
		RequestStatus requestStatus = example.getRequestSatus();
		Employee requester = example.getRequester();
		String requesterName = null;
		Employee approver = example.getApprover();
		String approverName = null;
		if (requester != null) {
			requesterName = requester.getName();
		}
		if (approver != null) {
			approverName = approver.getName();
		}
		LocalDateTime dateOfRequestSubmission = example.getDateOfRequest();

		Specification<Request> spec = Specification.where(null);

		if (requestStatus != null) {
			spec = spec.and(RequestSpecifications.hasRequestStatus(requestStatus));
		}

		if (StringUtils.hasText(requesterName) && !StringUtils.hasText(approverName)) {
			spec = spec.and(RequestSpecifications.hasRequesterName(requesterName));
		}

		if (StringUtils.hasText(approverName) && !StringUtils.hasText(requesterName)) {
			spec = spec.and(RequestSpecifications.hasApproverName(approverName));
		}

		if (StringUtils.hasText(requesterName) && StringUtils.hasText(approverName)) {
			spec = spec.and(RequestSpecifications.hasRequesterOrApproverName(requesterName, approverName));
		}

		if (dateOfRequestSubmission != null) {
			spec = spec.and(RequestSpecifications.hasDateOfRequstSubmission(startDate, endDate));
		}

		if (startPeriod != null || endPeriod != null) {
			spec = spec.and(RequestSpecifications.hasStartDateOrEndDateBetweenDates(startPeriod, endPeriod));
		}

		Page<Request> allRequest = requestRepository.findAll(spec, pageable);
		return allRequest;
	};

}
