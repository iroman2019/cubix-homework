package hu.cubix.hr.iroman.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import hu.cubix.hr.iroman.dto.CompanyDto;
import hu.cubix.hr.iroman.dto.EmployeeDto;
import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Employee;

@Mapper(componentModel = "spring")
public interface CompanyMapper {

	public CompanyDto companyToDto(Company company);

	@Mapping(target = "employees", ignore = true)
	@Named("summary")
	CompanyDto companySummaryToDto(Company company);

	public List<CompanyDto> companysToDtos(List<Company> companys);

	@IterableMapping(qualifiedByName = "summary")
	List<CompanyDto> companySummariesToDtos(List<Company> company);

	public Company dtoToCompany(CompanyDto companyDto);

	@Mapping(source = "position.name", target = "job")
	@Mapping(target = "company", ignore = true)
	EmployeeDto employeeToDto(Employee employee);

	@InheritInverseConfiguration
	Employee dtoToEmployee(EmployeeDto employeeDto);

}
