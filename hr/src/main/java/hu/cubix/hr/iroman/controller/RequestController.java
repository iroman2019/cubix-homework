package hu.cubix.hr.iroman.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import hu.cubix.hr.iroman.dto.RequestDto;
import hu.cubix.hr.iroman.mapper.RequestMapper;
import hu.cubix.hr.iroman.model.Request;
import hu.cubix.hr.iroman.model.RequestExample;
import hu.cubix.hr.iroman.service.RequestService;

@RestController
@RequestMapping("/api/requests")
public class RequestController {

	@Autowired
	RequestService requestService;

	@Autowired
	RequestMapper requestMapper;

	@GetMapping
	public List<RequestDto> findAll(@SortDefault("id") Pageable pageable) {
		List<Request> requests = requestService.findAllRequest(pageable);
		return requestMapper.requestsToDtos(requests);
	}

	@GetMapping("/{id}")
	public RequestDto findById(@PathVariable long id) {

		Request request = requestService.findById(id);

		if (request == null) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND);
		}

		return requestMapper.requestToDto(request);

	}

	@PostMapping
	public RequestDto createNewRequest(@RequestBody RequestDto requestDto) {

		Request request = requestMapper.dtoToRequest(requestDto);

		request.setDateOfRequest(LocalDateTime.now());

		Request savedRequest = requestService.save(request);

		if (savedRequest == null)
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

		return requestMapper.requestToDto(savedRequest);

	}

	@PutMapping
	public RequestDto updateRequest(@RequestBody RequestDto request) {
		Request modifiedRequest = requestService.updateRequest(requestMapper.dtoToRequest(request));
		return requestMapper.requestToDto(modifiedRequest);

	}

	@DeleteMapping("/{id}")
	public void deleteRequest(@PathVariable long id) {
		requestService.delete(id);
	}

	@PutMapping("/accept/{id}")
	public RequestDto acceptRequest(@PathVariable long id) {
		Request savedRequest = requestService.acceptRequest(id);
		return requestMapper.requestToDto(savedRequest);
	}

	@GetMapping("/example")
	public List<RequestDto> findByExample(@RequestBody RequestExample requestExample, Pageable pageable) {
		RequestDto requestDto = requestExample.getRequestDto();
		List<Request> requestByExample = requestService.findRequestsByExample(requestMapper.dtoToRequest(requestDto),
				requestExample.getStartDate(), requestExample.getEndDate(), requestExample.getStartPeriod(),
				requestExample.getEndPeriod(), pageable);

		return requestMapper.requestsToDtos(requestByExample);
	}

}
