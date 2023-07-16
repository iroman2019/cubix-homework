package hu.cubix.hr.iroman.model;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedSubgraph;
import jakarta.persistence.OneToMany;

@NamedEntityGraph(name = "Company.full", attributeNodes = {
		@NamedAttributeNode(value = "employees", subgraph = "employees-subgraph"),
		@NamedAttributeNode("type"), }, subgraphs = {
				@NamedSubgraph(name = "employees-subgraph", attributeNodes = { @NamedAttributeNode("position") }) })
@Entity
public class Company {

	@Id
	@GeneratedValue
	private Long id;
	private Long registrationNumber;
	private String name;
	private String addres;

	@OneToMany(mappedBy = "company")
	private List<Employee> employees;

	@ManyToOne
	private CompanyType type;

	public Company() {

	}

	public Company(String name) {
		super();
		this.name = name;
	}

	public Company(Long id, Long registrationNumber, String name, String addres, List<Employee> employees) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.addres = addres;
		this.employees = employees;
	}

	public Company(Long id, Long registrationNumber, String name, String addres, CompanyType type,
			List<Employee> employees) {
		super();
		this.id = id;
		this.registrationNumber = registrationNumber;
		this.name = name;
		this.addres = addres;
		this.type = type;
		this.employees = employees;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		return Objects.equals(id, other.id);
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

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public void addEmployee(Employee employee) {
		if (this.employees == null)
			this.employees = new ArrayList<>();
		employee.setCompany(this);
		employees.add(employee);
	}

	public CompanyType getType() {
		return type;
	}

	public void setType(CompanyType type) {
		this.type = type;
	}
}
