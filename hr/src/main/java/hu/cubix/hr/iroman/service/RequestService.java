package hu.cubix.hr.iroman.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Request;
import hu.cubix.hr.iroman.model.RequestStatus;
import hu.cubix.hr.iroman.repository.RequestRepository;
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

		requestRepository.deleteById(id);
	}

	@Transactional
	public Request acceptRequest(long requestId) {
		Request request = requestRepository.findByIdWithEmployees(requestId);
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
		// save(newRequest);
		return newRequest;
	}

	public List<Request> findRequestsByExample(Request example, LocalDateTime startDate, LocalDateTime endDate,
			LocalDate startPeriod, LocalDate endPeriod, Pageable pageable) {
		RequestStatus requestStatus = example.getRequestSatus();
		Employee requester = example.getRequester();
		String requesterName = null;
		Employee approver = example.getRequester();
		String approverName = null;
		if (requester != null) {
			requesterName = requester.getName();
		}
		if (approver != null) {
			requesterName = approver.getName();
		}
		LocalDateTime dateOfRequestSubmission = example.getDateOfRequest();
		LocalDate start = example.getStartDate();
		LocalDate end = example.getEndDate();

		Specification<Request> specAllWithAnd = Specification.where(null);
		Specification<Request> specOrForNames = Specification.where(null);
		Specification<Request> specOrForDates = Specification.where(null);

		if (requestStatus != null) {
			specAllWithAnd = specAllWithAnd.and(RequestSpecifications.hasRequestStatus(requestStatus));
		}

		if (StringUtils.hasText(requesterName)) {
			specOrForNames = specOrForNames.or(RequestSpecifications.hasRequesterName(requesterName));
		}

		if (StringUtils.hasText(approverName)) {
			specOrForNames = specOrForNames.or(RequestSpecifications.hasApproverName(approverName));
		}

		specAllWithAnd = specAllWithAnd.and(specOrForNames);

		if (dateOfRequestSubmission != null) {
			specAllWithAnd = specAllWithAnd.and(RequestSpecifications.hasDateOfRequstSubmission(startDate, endDate));
		}

		if (start != null) {
			specOrForDates = specOrForDates.or(RequestSpecifications.hasStartDate(startPeriod, endPeriod));
		}

		if (end != null) {
			specOrForDates = specOrForDates.or(RequestSpecifications.hasEndDate(startPeriod, endPeriod));
		}

		specAllWithAnd = specAllWithAnd.and(specOrForDates);

		Page<Request> allRequest = requestRepository.findAll(specAllWithAnd, pageable);
		return allRequest.getContent();
	};

}
