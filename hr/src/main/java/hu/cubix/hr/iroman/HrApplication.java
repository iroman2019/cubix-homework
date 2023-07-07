package hu.cubix.hr.iroman;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Position;
import hu.cubix.hr.iroman.model.Qualification;
import hu.cubix.hr.iroman.model.AverageSalaryByJob;
import hu.cubix.hr.iroman.repository.CompanyRepository;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import hu.cubix.hr.iroman.repository.PositionRepository;
import hu.cubix.hr.iroman.service.InitDbService;
import hu.cubix.hr.iroman.service.SalaryService;

@SpringBootApplication
public class HrApplication implements CommandLineRunner {

	@Autowired
	SalaryService salaryService;
	
	@Autowired
	InitDbService initDbService;
	
	@Autowired
	CompanyRepository companyRepository;
	
	@Autowired
	EmployeeRepository employeeRepository;
	
	@Autowired
	PositionRepository positionRepository;

	public static void main(String[] args) {
		SpringApplication.run(HrApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		LocalDateTime startWork1 = LocalDateTime.of(2022, Month.AUGUST, 28, 14, 33, 48);

		LocalDateTime startWork2 = LocalDateTime.of(2019, Month.MARCH, 12, 14, 33, 48);

		LocalDateTime startWork3 = LocalDateTime.of(2014, Month.MARCH, 28, 14, 33, 48);

		LocalDateTime startWork4 = LocalDateTime.of(2010, Month.NOVEMBER, 20, 14, 33);
		
		Position developer = positionRepository.save(new Position("developer", Qualification.UNIVERSITY));
		Position tester = positionRepository.save(new Position("tester", Qualification.HIGH_SCHOOL));
		Position manager = positionRepository.save(new Position("manager", Qualification.UNIVERSITY));

		Employee employee1 = new Employee((long) 1, "Tom", developer, 550000, startWork1);

		Employee employee2 = new Employee((long) 2, "Hannah", manager, 650000, startWork2);

		Employee employee3 = new Employee((long) 3, "Christine", tester, 400000, startWork3);

		Employee employee4 = new Employee((long) 4, "Joe", developer, 1000000, startWork4);

		System.out.println(employee1.getName() + " új fizetése: " + salaryService.setEmployeeSalary(employee1));
		System.out.println(employee2.getName() + " új fizetése: " + salaryService.setEmployeeSalary(employee2));
		System.out.println(employee3.getName() + " új fizetése: " + salaryService.setEmployeeSalary(employee3));
		System.out.println(employee4.getName() + " új fizetése: " + salaryService.setEmployeeSalary(employee4));

		initDbService.clearDb();
		initDbService.insertTestData();
		
		int limit=700000;
		int dbLimit = 1;
		
		List<Company> companies = companyRepository.findCompanyWhereEmployeeSalaryHigherThanThisLimit(limit);
		System.out.println("A "+limit+" fizetésnél magasabb fizetésű dolgozós cég(ek): ");
		for (Company company : companies) {
			System.out.println(company.getName());
		}
		
		List<Company> companies2 = companyRepository.findCompanyWhereCountOfEmployeesHigherThanThisLimit(dbLimit);
		System.out.println("A(z) "+dbLimit+" főnél nagyobb létszámú cég(ek): ");
		for (Company company : companies2) {
			System.out.println(company.getName());
		}
		
		long companyId=companyRepository.findFirstByOrderByIdDesc().getId();
		
		List<AverageSalaryByJob> answerList = companyRepository.findAverageSalariesByPosition(companyId);
		
		System.out.println("A "+companyId+" id-jú cég job szerinti átlagfizetései: ");
		for (AverageSalaryByJob averageSalaryByJob : answerList) {
			System.out.println(averageSalaryByJob.getJob() + ": " + averageSalaryByJob.getAverageSalary());
		}
		
	}

}
