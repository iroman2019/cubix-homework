package hu.cubix.hr.iroman.controller;

import java.time.LocalDateTime;
import java.time.Month;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import hu.cubix.hr.iroman.model.Employee;

import java.text.SimpleDateFormat;

@Controller
public class EmployeeTlController {

	private List<Employee> employees = new ArrayList<>();
	
	{
		employees.add(new Employee((long) 1, "developer", 550000, LocalDateTime.of(2022, Month.AUGUST, 28, 10, 30, 48)));
		employees.add(new Employee((long) 2, "manager", 650000, LocalDateTime.of(2019, Month.MARCH, 12, 6, 0, 48)));
		employees.add(new Employee((long) 3, "tester", 400000, LocalDateTime.of(2014, Month.MARCH, 28, 9, 30, 48)));
		employees.add(new Employee((long) 4, "developer", 1000000, LocalDateTime.of(2010, Month.NOVEMBER, 20, 15, 33)));
	}

	@GetMapping("/")
	public String home(Map<String, Object> model) {
		model.put("employees", employees);
		model.put("newEmployee", new Employee());
		return "index";
	}
	
	@PostMapping("/employee")
	public String createEmployee(Employee employee, Map<String, Object> model) {
		employees.add(employee);
		return "redirect:/";
	}
	
}
