package hu.cubix.hr.iroman.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.EmployeeRepository;

@Service
public abstract class AbstractEmployeeService implements EmployeeService{
	
	//private Map<Long, Employee> employees = new HashMap<>();
	@Autowired
	private EmployeeRepository employeeRepository;
	
	@Transactional
	public Employee create(Employee employee) {
		if(findById(employee.getId()) != null) {
			return null;
		}
		return save(employee);
	}
	
	@Transactional
	public Employee update(Employee employee) {
		if(findById(employee.getId()) == null) {
			return null;
		}
		return save(employee);
	}
	
	@Transactional
	public Employee save(Employee employee ) {
		return employeeRepository.save(employee);
	}
	
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}
	
	public Employee findById(long id) {
		return employeeRepository.findById(id).orElse(null);
	}

	@Transactional
	public void delete(long id) {
		employeeRepository.deleteById(id);
	}
	
	@Transactional
	public List<Employee> findByJob(String jobName){
		return employeeRepository.findByJob(jobName);
	}
	
	public List<Employee> findByNameStartingWith(String namePrefix){
		return employeeRepository.findByNameStartingWithNamePrefix(namePrefix);
	}
	
	public List<Employee> findByTimestampBetween(LocalDateTime minDate, LocalDateTime maxDate){
		return employeeRepository.findByTimestampBetween(minDate, maxDate);
	}
}
