package hu.cubix.hr.iroman.controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import hu.cubix.hr.iroman.dto.EmployeeDto;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.service.CompanyService;
import hu.cubix.hr.iroman.dto.CompanyDto;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	private Map<Long, EmployeeDto> employees = new HashMap<>();

	private Map<Long, EmployeeDto> employees2 = new HashMap<>();

	private Map<Long, CompanyDto> companies = new HashMap<>();

	{
		employees.put(1L, new EmployeeDto((long) 1, "Eric", "developer", 550000,
				LocalDateTime.of(2022, Month.AUGUST, 28, 10, 30, 48)));

		employees.put(2L, new EmployeeDto((long) 2, "Milan", "tester", 450000,
				LocalDateTime.of(2022, Month.SEPTEMBER, 05, 07, 30, 48)));

		employees2.put(1L, new EmployeeDto((long) 1, "Mary", "tester", 350000,
				LocalDateTime.of(2020, Month.APRIL, 04, 10, 30, 48)));

		employees2.put(2L, new EmployeeDto((long) 2, "David", "developer", 650000,
				LocalDateTime.of(2017, Month.APRIL, 20, 10, 30, 48)));

		employees2.put(3L, new EmployeeDto((long) 3, "Peter", "director", 2000000,
				LocalDateTime.of(2010, Month.JULY, 22, 10, 30, 48)));

		companies.put(1L,
				new CompanyDto(1L, 1532654L, "SoftverQuality Company", "1111 Budapest Teszt u. 3", employees));
		companies.put(2L, new CompanyDto(2L, 1532234L, "InformationTechnology Company",
				"2310 Szigetszentmiklós Ág u. 12", employees2));
	}

	@GetMapping
	public List<CompanyDto> findAll(@RequestParam("full") Optional<Boolean> full) {

		if (full.orElse(false)) {
			return new ArrayList<>(companies.values());
		}
		List<CompanyDto> modifiedList = new ArrayList<>();
		for (CompanyDto companyDto : companies.values()) {
			modifiedList.add(new CompanyDto(companyDto.getId(), companyDto.getId(), companyDto.getName(),
					companyDto.getAddres()));
		}

		return modifiedList;

	}

	@GetMapping("/{id}")
	public ResponseEntity<CompanyDto> findById(@PathVariable long id) {

		CompanyDto companyDto = companies.get(id);

		if (companyDto == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(companyDto);

	}

	@PostMapping
	public ResponseEntity<CompanyDto> createNewCompany(@RequestBody CompanyDto company) {

		if (companies.containsKey(company.getId()))
			return ResponseEntity.badRequest().build();

		companies.put(company.getId(), company);

		return ResponseEntity.ok(company);

	}

	@PutMapping("/{id}")
	public ResponseEntity<CompanyDto> update(@PathVariable long id, @RequestBody CompanyDto company) {

		company.setId(id);

		if (!companies.containsKey(id))
			return ResponseEntity.notFound().build();

		companies.put(id, company);

		return ResponseEntity.ok(company);

	}

	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companies.remove(id);
	}

	@PostMapping("add/{id}")
	public ResponseEntity<CompanyDto> addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employee) {
		if (!companies.containsKey(id))
			return ResponseEntity.notFound().build();

		if (companies.get(id).getEmployees().containsKey(employee.getId()))
			return ResponseEntity.badRequest().build();

		CompanyDto modifiedCompany = companyService.addNewEmployee(companies.get(id), employee);
		companies.put(id, modifiedCompany);

		return ResponseEntity.ok(modifiedCompany);
	}

	@DeleteMapping("deleteemp/{id}")
	public void deleteEmployeaAtCompany(@PathVariable long id, @RequestParam("employeeId") long empId) {
		companies.get(id).getEmployees().remove(empId);
	}

	@PutMapping("changeemplist/{id}")
	public ResponseEntity<CompanyDto> update(@PathVariable long id, @RequestBody Map<Long, EmployeeDto> employeeList) {

		if (!companies.containsKey(id))
			return ResponseEntity.notFound().build();

		companies.get(id).setEmployees(employeeList);

		return ResponseEntity.ok(companies.get(id));

	}
}
