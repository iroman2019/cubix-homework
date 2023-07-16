package hu.cubix.hr.iroman.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.CompanyRepository;
import hu.cubix.hr.iroman.repository.CompanyTypeRepository;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import hu.cubix.hr.iroman.repository.PositionRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class CompanyServiceIT {

	@Autowired
	CompanyService companyService;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PositionRepository positionRepository;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	CompanyTypeRepository companyTypeRepository;
	
	
	@Test
	void addNewEmployee() throws Exception {
		
		Long companyId = 1L;
		String employeeName = "Hanna";
		Employee employeeForCompany = findEmployeeById(employeeName);
		long employeeId = employeeForCompany.getId();
		
		//ACT
		Company modifiedCompany = companyService.addNewEmployee(companyId, employeeId);
		
		//ASSERT
		List<String> newListOfEmployeeNames = modifiedCompany.getEmployees().stream().map(Employee::getName).collect(Collectors.toList());
		assertThat(newListOfEmployeeNames.contains(employeeName));
		
	}

	private Employee findEmployeeById(String employeeName) {
		Employee employee = employeeRepository.findByNameStartingWithNamePrefix("Hanna").get(0);
		return employee;
	}

}
