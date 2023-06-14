package hu.cubix.hr.iroman.controller;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

	private Map<Long, EmployeeDto> employees = new HashMap<>();

	{
		employees.put(1L,
				new EmployeeDto((long) 1, "developer", 550000, LocalDateTime.of(2022, Month.AUGUST, 28, 10, 30, 48)));
	}

	@GetMapping
	public List<EmployeeDto> findAll() {
		return new ArrayList<>(employees.values());
	}

	@GetMapping("/{id}")
	public ResponseEntity<EmployeeDto> findById(@PathVariable long id) {

		EmployeeDto employeetDto = employees.get(id);

		if (employeetDto == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(employeetDto);

	}

	@PostMapping
	public ResponseEntity<EmployeeDto> createNewEmployee(@RequestBody EmployeeDto employee) {

		if (employees.containsKey(employee.getId()))
			return ResponseEntity.badRequest().build();

		employees.put(employee.getId(), employee);

		return ResponseEntity.ok(employee);

	}

	@PutMapping("/{id}")
	public ResponseEntity<EmployeeDto> update(@PathVariable long id, @RequestBody EmployeeDto employee) {

		employee.setId(id);

		if (!employees.containsKey(id))
			return ResponseEntity.notFound().build();

		employees.put(id, employee);

		return ResponseEntity.ok(employee);

	}

	@DeleteMapping("/{id}")
	public void deleteEmployee(@PathVariable long id) {

		employees.remove(id);

	}
	
	@GetMapping(params = { "salaryLimit" })
	public List<EmployeeDto> findBySalary(@RequestParam int salaryLimit) {
		
		//Postman test sample: GET http://localhost:8080/api/employees?salaryLimit=500000
		
		return new ArrayList<>(employees.values()
				  .stream()
				  .filter(e -> e.getSalary() > salaryLimit)
				  .collect(Collectors.toList()));
	}
}
