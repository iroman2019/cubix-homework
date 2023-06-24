package hu.cubix.hr.iroman.service;

import java.util.List;

import org.w3c.dom.views.AbstractView;

import hu.cubix.hr.iroman.model.Employee;

public interface EmployeeService {

	public int getPayRaisetPercent(Employee employee);
	
	public Employee create(Employee employee);
	
	public Employee update(Employee employee);
	
	public Employee save(Employee employee );
	
	public List<Employee> findAll();
	
	public Employee findById(long id);
	
	public void delete(long id);

}
