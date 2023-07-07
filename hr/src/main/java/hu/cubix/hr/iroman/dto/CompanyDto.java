package hu.cubix.hr.iroman.dto;

import java.util.List;

import hu.cubix.hr.iroman.model.CompanyType;

public class CompanyDto {
	private Long id;
	private Long registrationNumber;
	private String name;
	private String addres;
	private CompanyType type;
	private List<EmployeeDto> employees;

	public CompanyDto() {

	}

	public CompanyDto(Long id, Long registrationNumber, String name, String addres) {
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.addres = addres;
	}

	public CompanyDto(Long id, Long registrationNumber, String name, String addres, CompanyType type,
			List<EmployeeDto> employees) {
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.addres = addres;
		this.type = type;
		this.employees = employees;
	}

	public CompanyDto(Long id, Long registrationNumber, String name, String addres, List<EmployeeDto> employees) {
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

	public List<EmployeeDto> getEmployees() {
		return employees;
	}

	public void setEmployees(List<EmployeeDto> employees) {
		this.employees = employees;
	}

	public CompanyType getType() {
		return type;
	}

	public void setType(CompanyType type) {
		this.type = type;
	}

}
