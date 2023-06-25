package hu.cubix.hr.iroman.service;

import java.time.LocalDateTime;
import java.util.List;

import hu.cubix.hr.iroman.model.Employee;

public interface EmployeeService {

	public int getPayRaisetPercent(Employee employee);
	
	public Employee create(Employee employee);
	
	public Employee update(Employee employee);
	
	public Employee save(Employee employee );
	
	public List<Employee> findAll();
	
	public Employee findById(long id);
	
	public void delete(long id);

	public List<Employee> findByJob(String jobName);
	
	public List<Employee> findByNameStartingWith(String namePrefix);
	
	public List<Employee> findByTimestampBetween(LocalDateTime minDate, LocalDateTime maxDate);

}
