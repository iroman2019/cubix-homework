package hu.cubix.hr.iroman;

import java.time.LocalDateTime;
import java.time.Month;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		LocalDateTime startWork1 = LocalDateTime.of(2022, Month.AUGUST, 28, 14, 33, 48);

		LocalDateTime startWork2 = LocalDateTime.of(2019, Month.MARCH, 12, 14, 33, 48);

		LocalDateTime startWork3 = LocalDateTime.of(2014, Month.MARCH, 28, 14, 33, 48);

		LocalDateTime startWork4 = LocalDateTime.of(2010, Month.NOVEMBER, 20, 14, 33);

		Employee employee1 = new Employee((long) 1, "developer", 550000, startWork1);

		Employee employee2 = new Employee((long) 2, "manager", 650000, startWork2);

		Employee employee3 = new Employee((long) 3, "tester", 400000, startWork3);

		Employee employee4 = new Employee((long) 4, "developer", 1000000, startWork4);

		System.out.println("Az első új fizetése: " + salaryService.setEmployeeSalary(employee1));
		System.out.println("A második új fizetése: " + salaryService.setEmployeeSalary(employee2));
		System.out.println("A harmadik új fizetése: " + salaryService.setEmployeeSalary(employee3));
		System.out.println("A negyedik új fizetése: " + salaryService.setEmployeeSalary(employee4));

	}

}
