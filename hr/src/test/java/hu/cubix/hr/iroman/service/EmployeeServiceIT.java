package hu.cubix.hr.iroman.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Position;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import hu.cubix.hr.iroman.repository.PositionRepository;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class EmployeeServiceIT {

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	PositionRepository positionRepository;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	InitDbService initDbService;

	@BeforeEach
	public void init() {
		initDbService.clearDb();
		initDbService.insertTestData();
	}

	@Test
	void testFindEmployeesByExample() throws Exception {

		LocalDateTime startTime = LocalDateTime.of(2022, 5, 24, 6, 0, 2);
		Employee employee1 = employeeRepository.findByNameStartingWithNamePrefix("SpecTest1").get(0);
		Employee employee2 = employeeRepository.findByNameStartingWithNamePrefix("SpecTest2").get(0);
		Company exampleCompany = new Company("B the");

		Position position = new Position("tester", null);
		Employee example = new Employee(null, "spec", 350000, startTime, exampleCompany, position);

		List<Employee> findEmployeesByExample = employeeService.findEmployeesByExample(example);

		assertThat(findEmployeesByExample).containsExactlyInAnyOrder(employee1, employee2);

	}

}
