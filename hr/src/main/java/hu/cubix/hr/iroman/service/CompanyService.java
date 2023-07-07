package hu.cubix.hr.iroman.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.CompanyRepository;


@Service
public class CompanyService {
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private EmployeeService employeeService;
	
	public Company create(Company company) {
		if(findById(company.getId()) != null) {
			return null;
		}
		return save(company);
	}
	
	public Company update(Company company) {
		if(findById(company.getId()) == null) {
			return null;
		}
		return save(company);
	}

	public Company save(Company company ) {
		return companyRepository.save(company);
	}
	
	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	public Company addNewEmployee(long companyId, Employee employee) {
		Company company = companyRepository.findById(companyId).get();
		company.addEmployee(employee);
		employeeService.save(employee);
		return companyRepository.save(company);
	}


	public Company findById(long id) {
		return companyRepository.findById(id).orElse(null);
	}

	public void delete(long id) {
		companyRepository.deleteById(id);		
	}

	public Company addNewEmployeeList(long companyId, List<Employee> list) {
		Company company = findById(companyId);
		company.setEmployees(list);
		return save(company);
	}
	
	public Company deleteEmployeeFromCompany(long id, long employeeId) {
		Company company = findById(id);
		Employee employee = employeeService.findById(employeeId);
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		employeeService.save(employee);
		
		return company;
	}
	
	public Company replaceEmployees(long id, List<Employee> employeeList) {
		Company company = findById(id);
		company.getEmployees().forEach(e -> {
			e.setCompany(null);
			employeeService.save(e);
		});
		company.getEmployees().clear();
		
		employeeList.forEach(e -> employeeService.create(e));
		employeeList.forEach(e -> {
			company.addEmployee(e);
			employeeService.save(e);
		});
		
		return company;
	}
}
