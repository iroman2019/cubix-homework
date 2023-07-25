package hu.cubix.hr.iroman.service;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import hu.cubix.hr.iroman.model.Employee_;
import hu.cubix.hr.iroman.model.Request;
import hu.cubix.hr.iroman.model.RequestStatus;
import hu.cubix.hr.iroman.model.Request_;

public class RequestSpecifications {

	public static Specification<Request> hasRequestStatus(RequestStatus status) {
		return (root, cq, cb) -> cb.equal(root.get(Request_.requestSatus), status);
	}

	public static Specification<Request> hasRequesterName(String requesterName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Request_.requester).get(Employee_.name)),
				(requesterName + "%").toLowerCase());
	}

	public static Specification<Request> hasApproverName(String approverName) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Request_.approver).get(Employee_.name)),
				(approverName + "%").toLowerCase());
	}

	public static Specification<Request> hasRequesterOrApproverName(String requesterName, String approverName) {

		return (root, cq, cb) -> Specification.anyOf(hasRequesterName(requesterName), hasApproverName(approverName))
				.toPredicate(root, cq, cb);
	}

	public static Specification<Request> hasDateOfRequstSubmission(LocalDateTime startExampleDate,
			LocalDateTime enDExampleDate) {
		return (root, cq, cb) -> cb.between(root.get(Request_.dateOfRequest), startExampleDate, enDExampleDate);
	}

	public static Specification<Request> hasStartDate(LocalDate startExampleDate, LocalDate endExampleDate) {
		return (root, cq, cb) -> cb.between(root.get(Request_.startDate), startExampleDate, endExampleDate);
	}

	public static Specification<Request> hasEndDate(LocalDate startExampleDate, LocalDate enDExampleDate) {
		return (root, cq, cb) -> cb.between(root.get(Request_.endDate), startExampleDate, enDExampleDate);
	}

	public static Specification<Request> hasStartDateOrEndDateBetweenDates(LocalDate startExampleDate,
			LocalDate endExampleDate) {

		return (root, cq, cb) -> Specification
				.anyOf(hasStartDate(startExampleDate, endExampleDate), hasEndDate(startExampleDate, endExampleDate))
				.toPredicate(root, cq, cb);
	}
}
