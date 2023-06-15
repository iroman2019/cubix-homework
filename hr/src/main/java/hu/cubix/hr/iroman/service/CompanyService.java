package hu.cubix.hr.iroman.service;

import hu.cubix.hr.iroman.dto.CompanyDto;
import hu.cubix.hr.iroman.dto.EmployeeDto;

public interface CompanyService {
		
	CompanyDto addNewEmployee(CompanyDto company, EmployeeDto emplyee);

}
