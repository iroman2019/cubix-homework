package hu.cubix.hr.iroman.mapper;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import hu.cubix.hr.iroman.dto.CompanyDto;
import hu.cubix.hr.iroman.dto.RequestDto;
import hu.cubix.hr.iroman.model.Company;
import hu.cubix.hr.iroman.model.Request;

@Mapper(componentModel = "spring")
public interface RequestMapper {

	@Mapping(source = "requester.position.name", target = "requester.job")
	@Mapping(target = "approver.position.employees", ignore = true)
	@Mapping(target = "approver.company.employees", ignore = true)
	@Mapping(target = "requester.position.employees", ignore = true)
	@Mapping(target = "requester.company.employees", ignore = true)
	@Mapping(source = "requestSatus", target = "status")
	public RequestDto requestToDto(Request request);

	public List<RequestDto> requestsToDtos(List<Request> requests);

	@InheritInverseConfiguration
	@Mapping(source = "requester.job", target = "requester.position.name")
	@Mapping(source = "status", target = "requestSatus")
	public Request dtoToRequest(RequestDto requestDto);

	public List<Request> dtosToRequests(List<RequestDto> requestDtos);

	@Mapping(target = "employees", ignore = true)
	CompanyDto companyToDto(Company company);

}
