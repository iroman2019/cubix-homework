package hu.cubix.hr.iroman.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.EmployeeRepository;

@Service
public abstract class AbstractEmployeeService implements EmployeeService{
	
	@Autowired
	private EmployeeRepository employeeRepository;
	
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
		return employeeRepository.save(employee);
	}
	
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}
	
	public Employee findById(long id) {
		return employeeRepository.findById(id).orElse(null);
	}

	public void delete(long id) {
		employeeRepository.deleteById(id);
	}
	
	public List<Employee> findByJob(String jobName){
		return employeeRepository.findByPositionName(jobName);
	}
	
	public List<Employee> findByNameStartingWith(String namePrefix){
		return employeeRepository.findByNameStartingWithNamePrefix(namePrefix);
	}
	
	public List<Employee> findByTimestampBetween(LocalDateTime minDate, LocalDateTime maxDate){
		return employeeRepository.findByTimestampBetween(minDate, maxDate);
	}
}
