package hu.cubix.hr.iroman.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import hu.cubix.hr.iroman.dto.RequestDto;

@Component
public class RequestExample {

	private RequestDto requestDto;
	private LocalDateTime startDate;
	private LocalDateTime endDate;
	private LocalDate startPeriod;
	private LocalDate endPeriod;

	public RequestExample() {
	}

	public RequestExample(RequestDto requestDto, LocalDateTime startDate, LocalDateTime endDate, LocalDate startPeriod,
			LocalDate endPeriod) {
		super();
		this.requestDto = requestDto;
		this.startDate = startDate;
		this.endDate = endDate;
		this.startPeriod = startPeriod;
		this.endPeriod = endPeriod;
	}

	public RequestDto getRequestDto() {
		return requestDto;
	}

	public void setRequestDto(RequestDto requestDto) {
		this.requestDto = requestDto;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public LocalDate getStartPeriod() {
		return startPeriod;
	}

	public void setStartPeriod(LocalDate startPeriod) {
		this.startPeriod = startPeriod;
	}

	public LocalDate getEndPeriod() {
		return endPeriod;
	}

	public void setEndPeriod(LocalDate endPeriod) {
		this.endPeriod = endPeriod;
	}

}
