package hu.cubix.hr.iroman.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.cubix.hr.iroman.dto.EmployeeDto;
import hu.cubix.hr.iroman.mapper.CompanyMapper;
import hu.cubix.hr.iroman.mapper.EmployeeMapper;
import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.service.CompanyService;
import hu.cubix.hr.iroman.dto.CompanyDto;

@RestController
@RequestMapping("/api/companies")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	@Autowired
	CompanyMapper companyMapper;

	@Autowired
	EmployeeMapper employeeMapper;

	private Map<Long, CompanyDto> companies = new HashMap<>();

	{

		companies.put(1L, new CompanyDto(1L, 1532654L, "SoftverQuality Company", "1111 Budapest Teszt u. 3", null));
		companies.put(2L,
				new CompanyDto(2L, 1532234L, "InformationTechnology Company", "2310 Szigetszentmiklós Ág u. 12", null));
	}

	@GetMapping
	public List<CompanyDto> findAll(@RequestParam("full") Optional<Boolean> full) {

		List<Company> allCompanies = companyService.findAll();

		return full.orElse(false) ?

				companyMapper.companysToDtos(allCompanies) : companyMapper.companySummariesToDtos(allCompanies);

	}

	@GetMapping("/{id}")
	public CompanyDto findById(@PathVariable long id, @RequestParam("full") Optional<Boolean> full) {

		Company company = companyService.findById(id);

		if (company == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		CompanyDto companyToDto = companyMapper.companyToDto(company);

		return full.orElse(false) ? companyToDto : companyMapper.companySummaryToDto(company);

	}

	@PostMapping
	public CompanyDto createNewCompany(@RequestBody CompanyDto companyDto) {

		return companyMapper.companyToDto(companyService.save(companyMapper.dtoToCompany(companyDto)));

	}

	@PutMapping("/{id}")
	public CompanyDto update(@PathVariable long id, @RequestBody CompanyDto companyDto) {

		companyDto.setId(id);

		Company company = companyMapper.dtoToCompany(companyDto);

		Company updatedCompany = companyService.update(company);

		if (updatedCompany == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		return companyMapper.companyToDto(updatedCompany);

	}

	@DeleteMapping("/{id}")
	public void deleteCompany(@PathVariable long id) {
		companyService.delete(id);
	}

	@PostMapping("/add/{id}")
	public CompanyDto addNewEmployee(@PathVariable long id, @RequestBody EmployeeDto employeeDto) {

		Company company = companyService.findById(id);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		Employee newEmployee = companyMapper.dtoToEmployee(employeeDto);
		Company modifiedCompany = companyService.addNewEmployee(id, newEmployee);

		return companyMapper.companyToDto(modifiedCompany);
	}

	@DeleteMapping("/deleteemp/{id}")
	public CompanyDto deleteEmployeaAtCompany(@PathVariable long id, @RequestParam("employeeId") long empId) {
		Company company = companyService.deleteEmployeeFromCompany(id, empId);
		return companyMapper.companyToDto(company);
	}

	@PutMapping("/changeemplist/{id}")
	public CompanyDto update(@PathVariable long id, @RequestBody List<EmployeeDto> employeeList) {

		Company company = companyService.findById(id);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		Company modyfiedCompany = companyService.addNewEmployeeList(id, employeeMapper.DtosToEmployees(employeeList));

		return companyMapper.companyToDto(modyfiedCompany);

	}
}
