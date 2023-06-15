package hu.cubix.hr.iroman.dto;

import java.util.Map;

public class CompanyDto {
	private Long id;
	private Long registrationNumber;
	private String name;
	private String addres;
	private Map<Long, EmployeeDto> employees;

	public CompanyDto() {

	}
	
	public CompanyDto(Long id, Long registrationNumber, String name, String addres) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.addres = addres;
	}

	public CompanyDto(Long id, Long registrationNumber, String name, String addres, Map<Long, EmployeeDto> employees) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.addres = addres;
		this.employees = employees;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRegistrationNumber() {
		return registrationNumber;
	}

	public void setRegistrationNumber(Long registrationNumber) {
		this.registrationNumber = registrationNumber;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddres() {
		return addres;
	}

	public void setAddres(String addres) {
		this.addres = addres;
	}

	public Map<Long, EmployeeDto> getEmployees() {
		return employees;
	}

	public void setEmployees(Map<Long, EmployeeDto> employees) {
		this.employees = employees;
	}

}
