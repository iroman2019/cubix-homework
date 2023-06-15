package hu.cubix.hr.iroman.service;

import org.springframework.stereotype.Service;

import hu.cubix.hr.iroman.dto.CompanyDto;
import hu.cubix.hr.iroman.dto.EmployeeDto;

@Service
public class CompanyServiceImpl implements CompanyService{

	@Override
	public CompanyDto addNewEmployee(CompanyDto company, EmployeeDto employee) {
		company.getEmployees().put(employee.getId(), employee);
		return company;
	}
}
