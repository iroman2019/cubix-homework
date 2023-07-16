package hu.cubix.hr.iroman.mapper;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.cubix.hr.iroman.dto.EmployeeDto;
import hu.cubix.hr.iroman.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

	@Mapping(source = "position.name", target = "job")
	@Mapping(target = "company.employees", ignore = true)
	public EmployeeDto employeeToDto(Employee employee);

	public List<EmployeeDto> employeesToDtos(List<Employee> employees);

	@Mapping(source = "job", target = "position.name")
	public Employee dtoToEmployee(EmployeeDto employeeDto);

	public List<Employee> DtosToEmployees(List<EmployeeDto> employeeDtos);
}
