package hu.cubix.hr.iroman.controller;

import java.time.LocalDateTime;
import java.time.Month;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import hu.cubix.hr.iroman.dto.EmployeeDto;
import hu.cubix.hr.iroman.model.Employee;

import java.text.SimpleDateFormat;

@Controller
public class EmployeeTlController {

	private List<Employee> employees = new ArrayList<>();

	{
		employees.add(new Employee((long) 1, "Tom", "developer", 550000,
				LocalDateTime.of(2022, Month.AUGUST, 28, 10, 30, 48)));
		employees.add(
				new Employee((long) 2, "Hannah", "manager", 650000, LocalDateTime.of(2019, Month.MARCH, 12, 6, 0, 48)));
		employees.add(new Employee((long) 3, "Christine", "tester", 400000,
				LocalDateTime.of(2014, Month.MARCH, 28, 9, 30, 48)));
		employees.add(new Employee((long) 4, "Joe", "developer", 1000000,
				LocalDateTime.of(2010, Month.NOVEMBER, 20, 15, 33)));
	}

	@GetMapping("/")
	public String home(Map<String, Object> model) {
		model.put("employees", employees);
		model.put("newEmployee", new Employee());
		model.put("modifiedEmployee", new Employee());
		return "index";
	}

	@PostMapping("/employee")
	public String createEmployee(Employee employee, Map<String, Object> model) {
		employees.add(employee);
		return "redirect:/";
	}

	@PostMapping("/modify")
	public String modifyEmployee(Employee employee, Map<String, Object> model) {

		long id = employee.getId();

		employees.forEach(e -> {
			if (e.getId() == id) {
				e.setName(employee.getName());
				e.setJob(employee.getJob());
				e.setSalary(employee.getSalary());
				e.setTimestamp(employee.getTimestamp());
			}
		});

		return "redirect:/";
	}

	@GetMapping("/details/{id}")
	public String details(@PathVariable long id, Map<String, Object> model) {
		model.put("modifiedEmployee", findEmployeeById(employees, id));

		return "details";
	}

	private Object findEmployeeById(List<Employee> employeesArray, long id) {
		for (Employee emp : employeesArray) {
			if (emp.getId() == id) {
				return emp;
			}
		}
		return null;
	}

	@GetMapping("/delete/{id}")
	public String delete(@PathVariable long id) {

		employees.removeIf((e) -> e.getId() == id);

		return "redirect:/";
	}

}
