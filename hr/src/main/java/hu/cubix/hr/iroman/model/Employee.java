package hu.cubix.hr.iroman.model;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Employee {

	@Id
	@GeneratedValue
	private Long id;

	private String name;

	private int salary;

	@DateTimeFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime timestamp;

	@ManyToOne()
	private Company company;

	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "position_id")
	private Position position;

	public Employee() {
	}
	
	public Employee(String name, Position position, int salary, LocalDateTime timestamp) {
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.timestamp = timestamp;
	}

	public Employee(Long id, String name, Position position, int salary, LocalDateTime timestamp) {
		this.id = id;
		this.name = name;
		this.position = position;
		this.salary = salary;
		this.timestamp = timestamp;
	}

	public Employee(Long id, String name, int salary, LocalDateTime timestamp, Company company, Position position) {
		this.id = id;
		this.name = name;
		this.salary = salary;
		this.timestamp = timestamp;
		this.company = company;
		this.position = position;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public int getSalary() {
		return salary;
	}

	public void setSalary(int salary) {
		this.salary = salary;
	}

	public LocalDateTime getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
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
		Employee other = (Employee) obj;
		return Objects.equals(id, other.id);
	}

}
