package hu.cubix.hr.iroman.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;

@Entity
public class Request {
	@Id
	@GeneratedValue
	private Long id;

//	@OneToOne//(cascade = { CascadeType.ALL })
//	@JoinColumn(name = "requester_id", insertable = false, updatable = false)
	@ManyToOne
	@JoinTable(
			  name = "approver_requester", 
			  joinColumns = @JoinColumn(name = "requester_id", referencedColumnName = "id"), 
			  inverseJoinColumns = @JoinColumn(name = "approver_id", referencedColumnName = "id"))
	//@ElementCollection
	private Employee requester;

//	@OneToOne//(cascade = { CascadeType.ALL })
//	@JoinColumn(name = "approver_id", insertable = false, updatable = false)
	@ManyToOne
//	@JoinTable(
//			  name = "approver_requester", 
//			  joinColumns = @JoinColumn(name = "approver_id"), 
//			  inverseJoinColumns = @JoinColumn(name = "requester_id"))
	//@ElementCollection
	private Employee approver;

	private LocalDate startDate;

	private LocalDate endDate;

	private LocalDateTime dateOfRequest;

	private RequestStatus requestSatus;

	public Request() {
	}

	public Request(Employee requester, Employee approver, LocalDate startDate, LocalDate endDate,
			LocalDateTime dateOfRequest, RequestStatus requestSatus) {
		super();
		this.requester = requester;
		this.approver = approver;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dateOfRequest = dateOfRequest;
		this.requestSatus = requestSatus;
	}

	public Request(Long id, Employee requester, Employee approver, LocalDate startDate, LocalDate endDate,
			LocalDateTime dateOfRequest, RequestStatus requestSatus) {
		super();
		this.id = id;
		this.requester = requester;
		this.approver = approver;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dateOfRequest = dateOfRequest;
		this.requestSatus = requestSatus;
	}

	public Request(Long id, Employee requester, LocalDate startDate, LocalDate endDate, LocalDateTime dateOfRequest,
			RequestStatus requestSatus) {
		this.id = id;
		this.requester = requester;
		this.startDate = startDate;
		this.endDate = endDate;
		this.dateOfRequest = LocalDateTime.now();
		this.requestSatus = requestSatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getRequester() {
		return requester;
	}

	public void setRequester(Employee requester) {
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

	public RequestStatus getRequestSatus() {
		return requestSatus;
	}

	public void setRequestSatus(RequestStatus requestSatus) {
		this.requestSatus = requestSatus;
	}

	public Employee getApprover() {
		return approver;
	}

	public void setApprover(Employee approver) {
		this.approver = approver;
	}

	@Override
	public int hashCode() {
		return Objects.hash(approver, id, requester);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Request other = (Request) obj;
		return Objects.equals(approver, other.approver) && Objects.equals(id, other.id)
				&& Objects.equals(requester, other.requester);
	}

}
