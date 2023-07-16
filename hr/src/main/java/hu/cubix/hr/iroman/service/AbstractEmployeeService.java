package hu.cubix.hr.iroman.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Position;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import jakarta.transaction.Transactional;

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
	
	public List<Employee> findByJob(String jobName){
		return employeeRepository.findByPositionName(jobName);
	}
	
	public List<Employee> findByNameStartingWith(String namePrefix){
		return employeeRepository.findByNameStartingWithNamePrefix(namePrefix);
	}
	
	public List<Employee> findByTimestampBetween(LocalDateTime minDate, LocalDateTime maxDate){
		return employeeRepository.findByTimestampBetween(minDate, maxDate);
	}
	
	public List<Employee> findEmployeesByExample(Employee example){
		
		Long id = example.getId();
		String name = example.getName();
		Position position = example.getPosition();
		String positionName =null;
		int salary = example.getSalary();
		LocalDateTime timestamp = example.getTimestamp();
		Company company = example.getCompany();
		String companyName=null;
		
		if(position!=null) {
			positionName=position.getName();
		}
		
		if(company!=null) {
			companyName=company.getName();
		}
		
		Specification<Employee> spec = Specification.where(null);
		
		if(id!=null && id>0) {
			spec = spec.and(EmployeeSpecifications.hasId(id));
		}
		
		if(StringUtils.hasText(name)) {
			spec=spec.and(EmployeeSpecifications.hasName(name));
		}
		
		if(timestamp!=null) {
			spec=spec.and(EmployeeSpecifications.hasTimestamp(timestamp));
		}
		
		if(StringUtils.hasText(companyName)){
			spec=spec.and(EmployeeSpecifications.hasCompanyName(companyName));
		}
		
		if(salary>0){
			spec=spec.and(EmployeeSpecifications.hasSalary(salary));
		}
		
		if(StringUtils.hasText(positionName)){
			spec=spec.and(EmployeeSpecifications.hasPositionName(positionName));
		}
		return employeeRepository.findAll(spec);
	}
	
	public List<Employee> findAllEmployee(Optional<Integer> salaryLimit, Pageable pageable) {
		List<Employee> employees = null;
		if (salaryLimit.isPresent()) {
			Page<Employee> pageOfEmployee = employeeRepository.findBySalaryGreaterThan(salaryLimit.get(), pageable);
			System.out.println("Oldalak száma: " + pageOfEmployee.getTotalPages());
			System.out.println("Összes adott employee: " + pageOfEmployee.getTotalElements());
			System.out.println("Első oldal?: " + pageOfEmployee.isFirst());
			System.out.println("Utolsó oldal?: " + pageOfEmployee.isLast());
			System.out.println("Van következő oldal?: " + pageOfEmployee.hasNext());
			System.out.println("Van előző oldal?: " + pageOfEmployee.hasPrevious());
			
			employees = pageOfEmployee.getContent();
		} else {
			employees = findAll();
		}
		return employees;
	}
	
	public Integer findPercentToEmployee(long id) {
		Employee employee = findById(id);

		if (employee == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return getPayRaisetPercent(employee);
	}
}
