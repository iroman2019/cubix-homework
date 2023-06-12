package hu.cubix.hr.iroman.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.hr.iroman.model.Employee;

@Service
public class SalaryService {

	@Autowired
	EmployeeService employeeService;

	public int setEmployeeSalary(Employee employee) {
		return (int) (employee.getSalary() / 100.0 * (100 + employeeService.getPayRaisetPercent(employee)));
	}

}
