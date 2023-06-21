package hu.cubix.hr.iroman.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import hu.cubix.hr.iroman.model.Employee;

@Component
public abstract class AbstractEmployeeService implements EmployeeService{
	
	private Map<Long, Employee> employees = new HashMap<>();
	
	public Employee create(Employee employee) {
		if(findById(employee.getId()) != null) {
			return null;
		}
		return save(employee);
	}
	
	public Employee update(Employee employee) {
		if(findById(employee.getId()) == null) {
			return null;
		}
		return save(employee);
	}
	
	public Employee save(Employee employee ) {
		employees.put(employee.getId(), employee);
		return employee;
	}
	
	public List<Employee> findAll() {
		return new ArrayList<>(employees.values());
	}
	
	public Employee findById(long id) {
		return employees.get(id);
	}

	public void delete(long id) {
		employees.remove(id);
	}

}
