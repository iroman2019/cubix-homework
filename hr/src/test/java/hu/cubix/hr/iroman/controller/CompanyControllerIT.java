package hu.cubix.hr.iroman.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

import hu.cubix.hr.iroman.dto.CompanyDto;
import hu.cubix.hr.iroman.dto.EmployeeDto;
import hu.cubix.hr.iroman.mapper.CompanyMapper;
import hu.cubix.hr.iroman.mapper.EmployeeMapper;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.CompanyRepository;
import hu.cubix.hr.iroman.repository.CompanyTypeRepository;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import hu.cubix.hr.iroman.repository.PositionRepository;
import hu.cubix.hr.iroman.service.InitDbService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class CompanyControllerIT {

	@Autowired
	InitDbService initDbService;

	@Autowired
	CompanyController companyController;

	@Autowired
	PositionRepository positionRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	WebTestClient webtestClient;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	EmployeeMapper employeeMapper;

	@Autowired
	CompanyTypeRepository companyTypeRepository;

	private static final String API_COMPANIES_ADD_ID = "/api/companies/add/{id}";
	private static final String API_COMPANIES_DELETE_EMP_ID = "/api/companies/deleteemp/{id}";
	private static final String API_COMPANIES_DELETE_CHANGE_EMP_LIST_ID = "/api/companies/changeemplist/{id}";

	@BeforeEach
	public void init() {
		initDbService.clearDb();
		initDbService.insertTestData();
	}

	@Test
	void testThatEmployeeIsAddedToCompany() {

		Employee newEmployee = employeeRepository.findByNameStartingWithNamePrefix("Test").get(0);
		Long companyId = companyRepository.findByRegistrationNumber(1532654L).get(0).getId();
		long employeeId = newEmployee.getId();

		// ACT
		CompanyDto modifiedCompany = getCompanyWithNewEmployee(companyId, employeeId);

		List<Long> idList = modifiedCompany.getEmployees().stream().map(EmployeeDto::id).collect(Collectors.toList());
		assertTrue(idList.contains(employeeId));

	}

	private CompanyDto getCompanyWithNewEmployee(Long companyId, long empId) {
		CompanyDto company = webtestClient.post()
				.uri(uriBuilder -> uriBuilder.path(API_COMPANIES_ADD_ID).queryParam("employeeId", empId)
						.build(companyId))
				.exchange().expectStatus().isOk().expectBody(CompanyDto.class).returnResult().getResponseBody();

		return company;
	}

	@Test
	void testDeleteEmployeeFromCompany() {

		Long companyId = companyRepository.findByRegistrationNumber(1532234L).get(0).getId();
		Employee deletedEmployee = employeeRepository.findByNameStartingWithNamePrefix("Joe").get(0);

		// ACT
		CompanyDto modifiedCompany = getCompanyAfterDeleteEmployee(companyId, deletedEmployee.getId());

		assertFalse(modifiedCompany.getEmployees().contains(employeeMapper.employeeToDto(deletedEmployee)));

	}

	private CompanyDto getCompanyAfterDeleteEmployee(Long companyId, long employeeId) {
		CompanyDto company = (CompanyDto) webtestClient.delete()
				.uri(uriBuilder -> uriBuilder.path(API_COMPANIES_DELETE_EMP_ID).queryParam("employeeId", employeeId)
						.build(companyId))
				.exchange().expectStatus().isOk().expectBody(CompanyDto.class).returnResult().getResponseBody();

		return company;
	}

	@Test
	void testChangeEmployeeListInCompany() {

		Long companyId = companyRepository.findByRegistrationNumber(2532654L).get(0).getId();

		// Két Test-tel kezdődő nevű van initdb-ben
		List<Employee> employeeList = employeeRepository.findByNameStartingWithNamePrefix("Test");

		// ACT
		CompanyDto modifiedCompany = getCompanyAfterChangeEmployeeList(companyId,
				employeeMapper.employeesToDtos(employeeList));

		assertEquals(modifiedCompany.getEmployees().size(), 2);
		assertEquals(modifiedCompany.getEmployees().get(0).name().substring(0, 4), "Test");
		for (int i = 0; i < 2; i++) {
			assertEquals(modifiedCompany.getEmployees().get(i).id(), employeeList.get(i).getId());
			assertEquals(modifiedCompany.getEmployees().get(i).name(), employeeList.get(i).getName());
			assertEquals(modifiedCompany.getEmployees().get(i).job(), employeeList.get(i).getPosition().getName());
			assertEquals(modifiedCompany.getEmployees().get(i).salary(), employeeList.get(i).getSalary());
			assertEquals(modifiedCompany.getEmployees().get(i).timestamp(), employeeList.get(i).getTimestamp());

		}

	}

	private CompanyDto getCompanyAfterChangeEmployeeList(Long companyId, List<EmployeeDto> employeeList) {
		CompanyDto company = (CompanyDto) webtestClient.put()
				.uri(API_COMPANIES_DELETE_CHANGE_EMP_LIST_ID, Collections.singletonMap("id", companyId))
				.bodyValue(employeeList).exchange().expectStatus().isOk().expectBody(CompanyDto.class).returnResult()
				.getResponseBody();

		return company;
	}

}
