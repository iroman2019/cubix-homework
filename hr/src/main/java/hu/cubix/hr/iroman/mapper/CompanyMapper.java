package hu.cubix.hr.iroman.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.cubix.hr.iroman.dto.CompanyDto;
import hu.cubix.hr.iroman.model.Company;

@Mapper(componentModel = "spring")
public interface CompanyMapper {
	
	public CompanyDto companyToDto(Company company);
	
	public List<CompanyDto> companysToDtos(List<Company> companys);
	
	public Company dtoToCompany(CompanyDto companyDto);

}
