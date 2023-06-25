package hu.cubix.hr.iroman.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import hu.cubix.hr.iroman.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{
	
	 List<Employee> findByJob(String job);
	 
	 @Query("SELECT DISTINCT e FROM Employee e WHERE LOWER(e.name) like CONCAT(LOWER(:prefix), '%')")
	 List<Employee> findByNameStartingWithNamePrefix(String prefix);
	 
	 List<Employee> findByTimestampBetween(LocalDateTime minDate, LocalDateTime maxDate);

}
