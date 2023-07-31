package hu.cubix.hr.iroman.controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
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
import hu.cubix.hr.iroman.mapper.EmployeeMapper;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import hu.cubix.hr.iroman.repository.PositionRepository;
import hu.cubix.hr.iroman.service.EmployeeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeMapper employeeMapper;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PositionRepository positionRepository;


	@GetMapping
	public List<EmployeeDto> findAll(@RequestParam Optional<Integer> salaryLimit, @SortDefault("id") Pageable pageable) {
		List<Employee> employees = employeeService.findAllEmployee(salaryLimit, pageable);
		return employeeMapper.employeesToDtos(employees);
	}

	@GetMapping("/{id}")
	public EmployeeDto findById(@PathVariable long id) {

		Employee employee = employeeService.findById(id);

		if (employee == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return employeeMapper.employeeToDto(employee);

	}

	@PostMapping
	public EmployeeDto createNewEmployee(@RequestBody @Valid EmployeeDto employeeDto) {

		Employee employee = employeeMapper.dtoToEmployee(employeeDto);

		Employee savedEmployee = employeeService.save(employee);

		if (savedEmployee == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return employeeMapper.employeeToDto(savedEmployee);

	}

	@PutMapping("/{id}")
	public EmployeeDto update(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {

		// employee.setId(id);
		employeeDto = new EmployeeDto(id, employeeDto.name(), employeeDto.job(), employeeDto.salary(),
				employeeDto.timestamp(), employeeDto.company(), employeeDto.username());

		Employee employee = employeeMapper.dtoToEmployee(employeeDto);

		Employee updatedEmployee = employeeService.update(employee);

		if (updatedEmployee == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);

		return employeeMapper.employeeToDto(updatedEmployee);

	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {

		employeeService.delete(id);

	}


	@GetMapping("salarypercent/{id}")
	public Integer findPercent(@PathVariable long id) {

		return employeeService.findPercentToEmployee(id);
	}

	@GetMapping(value = "/job", params = { "jobName" })
	public List<EmployeeDto> findByJob(@RequestParam String jobName) {

		// Postman test sample: GET
		// http://localhost:8080/api/employees/job?jobName=tester

		List<EmployeeDto> employeesByJob = employeeMapper.employeesToDtos(employeeService.findByJob(jobName));
		return employeesByJob;
	}
	
	@GetMapping(value = "/name", params = { "namePrefix" })
	public List<EmployeeDto> findByNamePrefix(@RequestParam String namePrefix) {

		// Postman test sample: GET
		// http://localhost:8080/api/employees/name?namePrefix=da

		List<EmployeeDto> employeesByNamePrefix = employeeMapper.employeesToDtos(employeeService.findByNameStartingWith(namePrefix));
		return employeesByNamePrefix;
	}
	
	@GetMapping(value = "/between", params = { "minDate", "maxDate"})
	public List<EmployeeDto> findByStartJob(@RequestParam String minDate, @RequestParam String maxDate) {

		// Postman test sample: GET
		// http://localhost:8080/api/employees/between?minDate=2000-02-14 10:12&maxDate=2022-12-10 07:00
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        LocalDateTime minDateAsDate = LocalDateTime.parse(minDate, formatter);
        LocalDateTime maxDateAsDate = LocalDateTime.parse(maxDate, formatter);

		List<EmployeeDto> employeesBetween = employeeMapper.employeesToDtos(employeeService.findByTimestampBetween(minDateAsDate, maxDateAsDate));
		return employeesBetween;
	}
	
	@GetMapping("/example")
	public List<EmployeeDto> findByExample(@RequestBody EmployeeDto employeeDto){
		
		List<Employee> employeesByExample = employeeService.findEmployeesByExample(employeeMapper.dtoToEmployee(employeeDto));
		
		return employeeMapper.employeesToDtos(employeesByExample);
	}
}
