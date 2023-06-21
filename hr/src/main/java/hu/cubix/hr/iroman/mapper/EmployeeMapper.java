package hu.cubix.hr.iroman.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import hu.cubix.hr.iroman.dto.EmployeeDto;
import hu.cubix.hr.iroman.model.Employee;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
	
	public EmployeeDto employeeToDto(Employee employee);
	
	public List<EmployeeDto> employeesToDtos(List<Employee> employees);
	
	public Employee dtoToEmployee(EmployeeDto employeeDto);

}
