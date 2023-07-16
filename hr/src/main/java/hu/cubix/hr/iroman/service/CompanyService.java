package hu.cubix.hr.iroman.service;

import java.util.List;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Position;
import hu.cubix.hr.iroman.repository.CompanyRepository;
import hu.cubix.hr.iroman.repository.PositionRepository;
import jakarta.transaction.Transactional;

@Service
public class CompanyService {

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	PositionRepository positionRepository;

	public Company create(Company company) {
		if (findById(company.getId()) != null) {
			return null;
		}
		return save(company);
	}

	public Company update(Company company) {
		if (findById(company.getId()) == null) {
			return null;
		}
		return save(company);
	}

	@Transactional
	public Company save(Company company) {
		return companyRepository.save(company);
	}

	public List<Company> findAll() {
		return companyRepository.findAll();
	}

	@Transactional
	public Company addNewEmployee(long companyId, long employeeId) {
		Company company = companyRepository.findByIdWithEmployees(companyId);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		
		Employee employee = employeeService.findById(employeeId);
		
		if(employee==null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}
		
		company.addEmployee(employee);
		
		//employee-nak megváltozik a company-ja, menteni kell
		employeeService.save(employee);
		return companyRepository.save(company);
	}

	public Company findById(long id) {
		return companyRepository.findByIdWithEmployees(id);
	}

	@Transactional
	public void delete(long id) {
		companyRepository.deleteById(id);
	}

	@Transactional
	public Company addNewEmployeeList(long companyId, List<Employee> list) {
		Company company = findById(companyId);
		if (company == null)
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		company.getEmployees().forEach(e -> {
			e.setCompany(null);
			employeeService.save(e);
		});
		company.getEmployees().clear();

		List<Position> positions = positionRepository.findAll();
		List<String> positionNames = new ArrayList<String>();
		positions.forEach(p -> {
			positionNames.add(p.getName());
		});

		list.forEach(e -> {
			String posName = e.getPosition().getName();
			if (positionNames.contains(posName)) {
				Position existingPosition = positionRepository.findByName(posName);
				e.setPosition(existingPosition);
			} else {
				Position newPosition = new Position(e.getPosition().getName(), null);
				positionRepository.save(newPosition);
				e.setPosition(newPosition);
			}
			employeeService.create(e);
		});

		list.forEach(e -> {
			company.addEmployee(e);
			employeeService.save(e);
		});

		return company;
	}

	@Transactional
	private void save(Employee employee) {
		employeeService.update(employee);
	}

	@Transactional
	public Company deleteEmployeeFromCompany(long id, long employeeId) {
		Company company = findById(id);
		Employee employee = employeeService.findById(employeeId);
		employee.setCompany(null);
		company.getEmployees().remove(employee);
		employeeService.save(employee);

		return company;
	}

	@Transactional
	public Company replaceEmployees(long id, List<Employee> employeeList) {
		Company company = findById(id);
		company.getEmployees().forEach(e -> {
			e.setCompany(null);
			employeeService.save(e);
		});
		company.getEmployees().clear();

		employeeList.forEach(e -> employeeService.create(e));
		employeeList.forEach(e -> {
			company.addEmployee(e);
			employeeService.save(e);
		});

		return company;
	}

	public List<Company> findAllCompany(Boolean orElse) {
		List<Company> allCompanies = orElse ? companyRepository.findAllWithEmployees() : findAll();
		return allCompanies;
	}

	public Company findCompanyById(long id, Boolean orElse) {
		Company company = orElse ? companyRepository.findByIdWithEmployees(id) : findById(id);
		return company;
	}
}
