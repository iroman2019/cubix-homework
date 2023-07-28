package hu.cubix.hr.iroman.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.cubix.hr.iroman.dto.EmployeeDto;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class EmployeeControllerIT {
	
	private static final String TEST_USERNAME = "TestDavid";
	
	private static final String TEST_PASS = "pass";

	private static final String API_EMPLOYEES_ID = "/api/employees/{id}";

	private static final String API_EMPLOYEES = "/api/employees";

	@Autowired
	WebTestClient webtestClient;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@BeforeEach
	void init() {
		Optional<Employee> testUser = employeeRepository.findByUsername(TEST_USERNAME);
		if(testUser.isEmpty()) {
			Employee newTestUser = new Employee();
			newTestUser.setUsername(TEST_USERNAME);
			newTestUser.setPassword(passwordEncoder.encode(TEST_PASS));
			employeeRepository.save(newTestUser);
		}
	}

	@Test
	void testThatCreatedEmployeeIsListed() {

		List<EmployeeDto> employeesBefore = getAllEmployees();
	
		long newId = 100;
		if (employeesBefore.size() > 0) {
			newId = employeesBefore.get(employeesBefore.size() - 1).id() + 1;
		}
		EmployeeDto newEmployee = new EmployeeDto(newId, "testname", "developer", 400000,
				LocalDateTime.of(2010, Month.NOVEMBER, 20, 14, 33), null);

		createEmployee(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.subList(0, employeesBefore.size())).usingRecursiveFieldByFieldElementComparator()
				.containsExactlyElementsOf(employeesBefore);
		assertThat(employeesAfter.get(employeesAfter.size() - 1)).usingRecursiveComparison().isEqualTo(newEmployee);

	}

	@Test
	void testThatWrongEmployeeIsNotListed() {

		List<EmployeeDto> employeesBefore = getAllEmployees();

		long newId = 100;
		if (employeesBefore.size() > 0) {
			newId = employeesBefore.get(employeesBefore.size() - 1).id() + 1;
		}

		// The timestamp cannot be a future date
		EmployeeDto newEmployee = new EmployeeDto(newId, "testname", "test job", 400000,
				LocalDateTime.of(2025, Month.NOVEMBER, 20, 14, 33, 00), null);

		createWrongEmployee(newEmployee);
		List<EmployeeDto> employeesAfter = getAllEmployees();

		assertThat(employeesAfter.size() == employeesBefore.size());

	}

	private void createWrongEmployee(EmployeeDto newEmployee) {
		webtestClient.post()
			.uri(API_EMPLOYEES)
			.headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
			.bodyValue(newEmployee)
			.exchange()
			.expectStatus()
			.isBadRequest();

	}

	private void createEmployee(EmployeeDto newEmployee) {
		webtestClient
			.post()
			.uri(API_EMPLOYEES)
			.headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
			.bodyValue(newEmployee)
			.exchange()
			.expectStatus()
			.isOk();

	}

	private List<EmployeeDto> getAllEmployees() {
		List<EmployeeDto> allEmployees = webtestClient.get()
				.uri(API_EMPLOYEES)
				.headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
				.exchange().expectStatus()
				.isOk()
				.expectBodyList(EmployeeDto.class)
				.returnResult()
				.getResponseBody();

		Collections.sort(allEmployees, Comparator.comparing(EmployeeDto::id));

		return allEmployees;
	}

	@Test
	void shouldReturnNotFoundForBadEmployeeJob() {
		EmployeeDto employeeDto = new EmployeeDto(99, "Previous", "prev job", 400000,
				LocalDateTime.of(2020, Month.NOVEMBER, 10, 14, 33), null);

		createEmployee(employeeDto);
		EmployeeDto updatedEmployee = new EmployeeDto(99, "Nobody", "", 400000,
				LocalDateTime.of(2010, Month.NOVEMBER, 20, 14, 33), null);
		
		webtestClient
			.put()
			.uri(API_EMPLOYEES_ID, Collections.singletonMap("id", employeeDto.id()))
			.headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
			.body(Mono.just(updatedEmployee), EmployeeDto.class)
			.exchange()
			.expectStatus()
			.isBadRequest();
	}

	private void updateEmployeeWithWrongId(EmployeeDto updatedEmployee) {
		webtestClient.put()
			.uri(API_EMPLOYEES_ID, Collections.singletonMap("id", updatedEmployee.id()))
			.headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
			.body(Mono.just(updatedEmployee), EmployeeDto.class).exchange()
			.expectStatus()
			.isEqualTo(HttpStatus.NOT_FOUND);
	}
	
	@Test
	void shouldReturnNotFoundForUnknownUserId() {
	  webtestClient
	    .put()
	    .uri("/api/users/{id}", 133)
	    .headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
	    .exchange()
	    .expectStatus()
	    .isEqualTo(HttpStatus.NOT_FOUND);
	}
	 

	@Test
	public void testUpdateEmployee() {

		EmployeeDto employeeDto = new EmployeeDto(10, "Previous", "prev job", 400000,
				LocalDateTime.of(2020, Month.NOVEMBER, 10, 14, 33), null);

		createEmployee(employeeDto);

		EmployeeDto updatedEmployee = new EmployeeDto(10, "Next", "next job", 400000,
				LocalDateTime.of(2020, Month.NOVEMBER, 10, 14, 33), null);

		webtestClient.put()
				.uri(API_EMPLOYEES_ID, Collections.singletonMap("id", employeeDto.id()))
				.headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
				.body(Mono.just(updatedEmployee), EmployeeDto.class).exchange().expectStatus().isOk().expectBody()
				.consumeWith(System.out::println).jsonPath("$.id").isEqualTo(updatedEmployee.id()).jsonPath("$.name")
				.isEqualTo(updatedEmployee.name()).jsonPath("$.job").isEqualTo(updatedEmployee.job())
				.jsonPath("$.salary").isEqualTo(updatedEmployee.salary())
				.jsonPath("$.timestamp")
				.isEqualTo( updatedEmployee.timestamp().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
	}

}
