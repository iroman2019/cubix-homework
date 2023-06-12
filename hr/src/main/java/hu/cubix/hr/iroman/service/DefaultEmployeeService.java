package hu.cubix.hr.iroman.service;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import hu.cubix.hr.iroman.model.Employee;

//@Service
public class DefaultEmployeeService implements EmployeeService {

	@Override
	public int getPayRaisetPercent(Employee employee) {
		return 5;
	}

}
