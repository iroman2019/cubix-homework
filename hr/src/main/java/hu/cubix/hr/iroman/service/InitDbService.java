package hu.cubix.hr.iroman.service;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Position;
import hu.cubix.hr.iroman.model.CompanyType;
import hu.cubix.hr.iroman.model.CompanyTypeEnum;
import hu.cubix.hr.iroman.model.Qualification;
import hu.cubix.hr.iroman.repository.CompanyRepository;
import hu.cubix.hr.iroman.repository.CompanyTypeRepository;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import hu.cubix.hr.iroman.repository.PositionRepository;

@Service
public class InitDbService {

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	CompanyTypeRepository companyTypeRepository;

	@Autowired
	PositionRepository positionRepository;

	public void clearDb() {
		employeeRepository.deleteAll();
		positionRepository.deleteAll();
		companyRepository.deleteAll();
		companyTypeRepository.deleteAll();
	}

	public void insertTestData() {
		LocalDateTime startWork1 = LocalDateTime.of(2022, Month.AUGUST, 28, 14, 33, 48);

		LocalDateTime startWork2 = LocalDateTime.of(2019, Month.MARCH, 12, 14, 33, 48);

		LocalDateTime startWork3 = LocalDateTime.of(2014, Month.MARCH, 28, 14, 33, 48);

		LocalDateTime startWork4 = LocalDateTime.of(2010, Month.NOVEMBER, 20, 14, 33);

		Position developer = positionRepository.save(new Position("developer", Qualification.UNIVERSITY));
		Position tester = positionRepository.save(new Position("tester", Qualification.HIGH_SCHOOL));
		Position manager = positionRepository.save(new Position("manager", Qualification.UNIVERSITY));

		List<Employee> employees = new ArrayList<>();
		employees.add(new Employee((long) 1, "Tom", developer, 750000, startWork1));
		employees.add(new Employee((long) 2, "Hannah", manager, 650000, startWork2));
		employees.add(new Employee((long) 3, "Christine", tester, 400000, startWork3));
		employees.add(new Employee((long) 4, "Joe", developer, 650000, startWork4));
		employees.add(new Employee((long) 5, "Davis", developer, 1200000, startWork4));
		employees.add(new Employee((long) 6, "Ester", developer, 780000, startWork2));
		employees.add(new Employee((long) 7, "Eric", tester, 450000, startWork4));
		employees.add(new Employee((long) 8, "Susan", tester, 550000, startWork4));

		List<Company> companies = new ArrayList<>();
		companies.add(new Company(1L, 1532654L, "SoftverQuality Company", "1111 Budapest Teszt u. 3", null));
		companies.add(
				new Company(2L, 1532234L, "InformationTechnology Company", "2310 Szigetszentmiklós Ág u. 12", null));
		companies.add(new Company(3L, 2532654L, "B the first Company", "2310 Szigetszentmiklós Harang u. 123", null));

		for (Company company : companies) {
			if (company.getId() == 1L) {

				if (companyTypeRepository.findByName(CompanyTypeEnum.BT.toString()) == null) {
					CompanyType companyType = new CompanyType();
					companyType.setName(CompanyTypeEnum.BT.toString());
					companyTypeRepository.save(companyType);
					company.setType(companyType);					
				} else {
					company.setType(companyTypeRepository.findByName(CompanyTypeEnum.BT.toString()));
				}
				companyRepository.save(company);
			} else {
				if (companyTypeRepository.findByName(CompanyTypeEnum.KFT.toString()) == null) {
					CompanyType companyType = new CompanyType();
					companyType.setName(CompanyTypeEnum.KFT.toString());
					companyTypeRepository.save(companyType);
					company.setType(companyType);
				} else {
					CompanyType companyType = new CompanyType();
					companyType.setName(CompanyTypeEnum.ZRT.toString());
					companyTypeRepository.save(companyType);
					company.setType(companyType);
				}
				companyRepository.save(company);
			}
		}

		for (Employee employee : employees) {
			if (employee.getId() < 2) {
				employee.setCompany(companyRepository.findByRegistrationNumber(1532654L).get(0));
				employeeRepository.save(employee);
			} else if (employee.getId() < 5) {
				employee.setCompany(companyRepository.findByRegistrationNumber(1532234L).get(0));
				employeeRepository.save(employee);
			} else {
				employee.setCompany(companyRepository.findByRegistrationNumber(2532654L).get(0));
				employeeRepository.save(employee);
			}
		}
	}
}
