package hu.cubix.hr.iroman.model;

import java.util.List;
import java.util.Objects;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Position {

	@Id
	@GeneratedValue
	private int id;

	private String name;
	private Qualification qualification;

	private int minSalary;

	@OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
	private List<Employee> employees;

	public Position() {
	}

	public Position(String name, Qualification qualification) {
		this.name = name;
		this.qualification = qualification;
	}

	public Position(String name, Qualification qualification, int minSalary) {
		this.name = name;
		this.qualification = qualification;
		this.minSalary = minSalary;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Qualification getQualification() {
		return qualification;
	}

	public void setQualification(Qualification qualification) {
		this.qualification = qualification;
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public void setEmployees(List<Employee> employees) {
		this.employees = employees;
	}

	public int getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(int minSalary) {
		this.minSalary = minSalary;
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Position other = (Position) obj;
		return Objects.equals(name, other.name);
	}

}
