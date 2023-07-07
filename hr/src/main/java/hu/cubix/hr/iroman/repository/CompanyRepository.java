package hu.cubix.hr.iroman.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.hr.iroman.model.AverageSalaryByJob;
import hu.cubix.hr.iroman.model.Company;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	List<Company> findByRegistrationNumber(Long regNumber);
	
	Company findFirstByOrderByIdDesc();

	@Query("SELECT c FROM Company c WHERE EXISTS(SELECT e FROM Employee e WHERE e.company.id=c.id AND e.salary>?1)")
	List<Company> findCompanyWhereEmployeeSalaryHigherThanThisLimit(int salaryLimit);
	
	@Query("SELECT c FROM Company c WHERE (SELECT COUNT(e) FROM Employee e GROUP BY e.company HAVING e.company.id=c.id)>?1")
	List<Company> findCompanyWhereCountOfEmployeesHigherThanThisLimit(int limit);

	@Query("SELECT e.position.name AS job, avg(e.salary) AS averageSalary "
			+ "FROM Company c "
			+ "INNER JOIN c.employees e "
			+ "WHERE c.id = :companyId "
			+ "GROUP BY e.position.name "
			+ " ORDER BY avg(e.salary) DESC")
	public List<AverageSalaryByJob> findAverageSalariesByPosition(long companyId);
}
