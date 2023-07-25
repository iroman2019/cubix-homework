package hu.cubix.hr.iroman.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import hu.cubix.hr.iroman.model.RequestStatus;

public class RequestDto {

	private Long id;
	private EmployeeDto requester;
	private EmployeeDto approver;
	private LocalDate startDate;
	private LocalDate endDate;
	private LocalDateTime dateOfRequest;
	private RequestStatus status;

	public RequestDto() {

	}

	public RequestDto(Long id, EmployeeDto requester, EmployeeDto approver, LocalDate startDate, LocalDate endDate,
			LocalDateTime dateOfRequest, RequestStatus status) {
		super();
		this.id = id;
		this.requester = requester;
		this.approver = approver;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dateOfRequest = dateOfRequest;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public EmployeeDto getRequester() {
		return requester;
	}

	public void setRequester(EmployeeDto requester) {
		this.requester = requester;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public LocalDateTime getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(LocalDateTime dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public RequestStatus getStatus() {
		return status;
	}

	public void setStatus(RequestStatus status) {
		this.status = status;
	}

	public EmployeeDto getApprover() {
		return approver;
	}

	public void setApprover(EmployeeDto approver) {
		this.approver = approver;
	}

}
