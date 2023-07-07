package hu.cubix.hr.iroman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import hu.cubix.hr.iroman.model.CompanyType;

public interface CompanyTypeRepository  extends JpaRepository<CompanyType, Long>{

	CompanyType findByName(String string);

}
