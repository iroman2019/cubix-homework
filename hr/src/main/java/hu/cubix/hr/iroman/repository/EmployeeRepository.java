package hu.cubix.hr.iroman.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.hr.iroman.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	Page<Employee> findBySalaryGreaterThan(int salaryLimit, Pageable pageable);

	List<Employee> findByPositionName(String job);

	@Query("SELECT DISTINCT e FROM Employee e WHERE LOWER(e.name) like CONCAT(LOWER(:prefix), '%')")
	List<Employee> findByNameStartingWithNamePrefix(String prefix);

	List<Employee> findByTimestampBetween(LocalDateTime minDate, LocalDateTime maxDate);

	Optional<Employee> findByUsername(String username);

}
