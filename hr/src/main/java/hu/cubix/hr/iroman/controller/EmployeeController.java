package hu.cubix.hr.iroman.controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import hu.cubix.hr.iroman.mapper.EmployeeMapper;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.service.EmployeeService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	@Autowired
	EmployeeService employeeService;

	@Autowired
	EmployeeMapper employeeMapper;

	// private Map<Long, EmployeeDto> employees = new HashMap<>();
	private Map<Long, Employee> employeesFromModel = new HashMap<>();

	LocalDateTime startWork1 = LocalDateTime.of(2022, Month.AUGUST, 28, 14, 33, 48);

	LocalDateTime startWork2 = LocalDateTime.of(2019, Month.MARCH, 12, 14, 33, 48);

	LocalDateTime startWork3 = LocalDateTime.of(2014, Month.MARCH, 28, 14, 33, 48);

	LocalDateTime startWork4 = LocalDateTime.of(2010, Month.NOVEMBER, 20, 14, 33);

	{
//		employees.put(1L, new EmployeeDto((long) 1, "Eric", "developer", 650000,
//				LocalDateTime.of(2022, Month.AUGUST, 28, 10, 30, 48)));
		employeesFromModel.put(1L, new Employee((long) 1, "Tom", "developer", 550000, startWork1));
		employeesFromModel.put(2L, new Employee((long) 2, "Hannah", "manager", 650000, startWork2));
		employeesFromModel.put(3L, new Employee((long) 3, "Christine", "tester", 400000, startWork3));
		employeesFromModel.put(4L, new Employee((long) 4, "Joe", "developer", 1000000, startWork4));
	}

	@GetMapping
	public List<EmployeeDto> findAll() {
		List<Employee> allEmployees = employeeService.findAll();
		return employeeMapper.employeesToDtos(allEmployees);
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

		return employeeMapper.employeeToDto(employee);

	}

	@PutMapping("/{id}")
	public EmployeeDto update(@PathVariable long id, @RequestBody @Valid EmployeeDto employeeDto) {

		// employee.setId(id);
		employeeDto = new EmployeeDto(employeeDto.id(), employeeDto.name(), employeeDto.job(), employeeDto.salary(),
				employeeDto.timestamp());

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

	@GetMapping(value = "/overthelimit", params = { "salaryLimit" })
	public List<EmployeeDto> findBySalary(@RequestParam int salaryLimit) {

		// Postman test sample: GET
		// http://localhost:8080/api/employees?salaryLimit=500000

		List<EmployeeDto> employees = employeeMapper.employeesToDtos(employeeService.findAll());

		return new ArrayList<>(employees.stream().filter(e -> e.salary() > salaryLimit).collect(Collectors.toList()));
	}

	@GetMapping("salarypercent/{id}")
	public Integer findPercent(@PathVariable long id) {

		Employee employee = employeeService.findById(id);

		if (employee == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return employeeService.getPayRaisetPercent(employee);
	}

}
