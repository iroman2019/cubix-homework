package hu.cubix.hr.iroman.dto;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Component;

public class EmployeeDto {
	private Long id;

	private String job;

	private int salary;

	@DateTimeFormat(pattern = "yyyy.MM.dd HH:mm")
	private LocalDateTime timestamp;

	public EmployeeDto() {

	}

	public EmployeeDto(Long id, String job, int salary, LocalDateTime timestamp) {
		super();
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
