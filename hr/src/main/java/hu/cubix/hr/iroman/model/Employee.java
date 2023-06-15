package hu.cubix.hr.iroman.model;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

@Component
public class Employee {
	private Long id;

	private String name;

	private String job;

	private int salary;

	@DateTimeFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime timestamp;

	public Employee() {
		super();
	}

	public Employee(Long id, String name, String job, int salary, LocalDateTime timestamp) {
		super();
		this.id = id;
		this.name = name;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
