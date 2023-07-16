package hu.cubix.hr.iroman.service;

import java.time.LocalDateTime;
import java.time.LocalTime;

import org.springframework.data.jpa.domain.Specification;

import hu.cubix.hr.iroman.model.Company_;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Employee_;
import hu.cubix.hr.iroman.model.Position_;

public class EmployeeSpecifications {

	public static Specification<Employee> hasId(Long id) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.id), id);
	}

	public static Specification<Employee> hasName(String name) {
		return (root, cq, cb) -> cb.like(cb.lower(root.get(Employee_.name)), name.toLowerCase() + "%");
	}

	public static Specification<Employee> hasTimestamp(LocalDateTime timestamp) {
		LocalDateTime start = LocalDateTime.of(timestamp.toLocalDate(), LocalTime.of(0, 0));
		return (root, cq, cb) -> cb.between(root.get(Employee_.timestamp), start, start.plusDays(1));
	}

	public static Specification<Employee> hasCompanyName(String companyName) {
		return (root, cq, cb) -> cb.like(root.get(Employee_.company).get(Company_.name), companyName + "%");
	}
	
	public static Specification<Employee> hasSalary(int salary) {
		return (root, cq, cb) -> {
			Double minSalary = salary*0.95;
			Double maxSalary = salary*1.05;
			return cb.between(root.get(Employee_.salary).as(Double.class), minSalary, maxSalary);
		};
	}

	public static Specification<Employee> hasPositionName(String positionName) {
		return (root, cq, cb) -> cb.equal(root.get(Employee_.position).get(Position_.name), positionName);
	}
}
