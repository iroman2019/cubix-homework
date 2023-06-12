package hu.cubix.hr.iroman.model;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

@Component
public class Employee {
	private Long id;

	private String job;

	private int salary;

	private LocalDateTime timestamp;

	public Employee() {
		super();
	}

	public Employee(Long id, String job, int salary, LocalDateTime timestamp) {
		this.id = id;
		this.job = job;
		this.salary = salary;
		this.timestamp = timestamp;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
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
}
