package hu.cubix.hr.iroman.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public record EmployeeDto (@PositiveOrZero long id, @NotEmpty String name, @NotEmpty String job, @Positive int salary, @Past LocalDateTime timestamp, CompanyDto company) {
	
	public EmployeeDto() {
		this(0, null, null, 0, null, null);
	}
}
