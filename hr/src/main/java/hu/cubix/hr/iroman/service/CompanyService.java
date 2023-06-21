package hu.cubix.hr.iroman.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import hu.cubix.hr.iroman.dto.CompanyDto;
import hu.cubix.hr.iroman.dto.EmployeeDto;

@Service
public class CompanyService {
	
	private Map<Long, CompanyDto> companies = new HashMap<>();


	public CompanyDto addNewEmployee(CompanyDto company, EmployeeDto employee) {
		company.getEmployees().put(employee.id(), employee);
		return company;
	}
}
