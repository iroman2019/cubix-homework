package hu.cubix.hr.iroman.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;

import hu.cubix.hr.iroman.dto.RequestDto;
import hu.cubix.hr.iroman.mapper.RequestMapper;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Request;
import hu.cubix.hr.iroman.model.RequestExample;
import hu.cubix.hr.iroman.model.RequestStatus;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import hu.cubix.hr.iroman.repository.RequestRepository;
import hu.cubix.hr.iroman.service.InitDbService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class RequestControllerIT {

	@Autowired
	InitDbService initDbService;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	RequestMapper requestMapper;

	@Autowired
	WebTestClient webtestClient;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	private static final String TEST_USERNAME = "Boss";
	private static final String TEST_USERNAME2 = "Chris";
	private static final String TEST_PASS = "123";
	private static final String API_REQUESTS = "/api/requests";
	private static final String API_REQUESTS_ACCEPT_ID = "/api/requests/accept/{id}";
	private static final String API_REQUESTS_EXAMPLE = "/api/requests/example";

	@BeforeEach
	public void init() {
		initDbService.clearDb();
		initDbService.insertTestData();
	}

	@Test
	void testThatRequestIsAddedTo() {

		Employee requester = employeeRepository.findByNameStartingWithNamePrefix("Chris").get(0);
		Employee approver = employeeRepository.findByNameStartingWithNamePrefix("Han").get(0);
		Request newRequest = new Request(requester, approver, LocalDate.of(2023, Month.JULY, 30),
				LocalDate.of(2023, Month.AUGUST, 20), LocalDateTime.now(), RequestStatus.PENDING_APPROVAL);

		// ACT
		RequestDto modifiedRequest = getRequest(requestMapper.requestToDto(newRequest));

		String requesterName = modifiedRequest.getRequester().name();
		String approverName = modifiedRequest.getApprover().name();
		assertEquals(requesterName, "Christine");
		assertEquals(approverName, "Hannah");

	}

	private RequestDto getRequest(RequestDto request) {
		RequestDto newRequest = webtestClient
				.post()
				.uri(API_REQUESTS)
				.headers(headers -> headers.setBasicAuth(TEST_USERNAME2, TEST_PASS))
				.bodyValue(request)
				.exchange()
				.expectStatus()
				.isOk()
				.expectBody(RequestDto.class)
				.returnResult()
				.getResponseBody();

		return newRequest;
	}

	@Test
	void testThatRequestIsAccepted() {

		Long requestId = requestRepository.findAll().get(0).getId();

		// ACT
		RequestDto modifiedRequest = getAcceptedRequest(requestId);

		String newStatus = modifiedRequest.getStatus().name();

		assertEquals(newStatus, "ACCEPTED");

	}

	private RequestDto getAcceptedRequest(Long requestId) {
		RequestDto newRequest = webtestClient.put()
				.uri(uriBuilder -> uriBuilder.path(API_REQUESTS_ACCEPT_ID)
				.build(requestId))
				.headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
				.exchange()
				.expectStatus().isOk()
				.expectBody(RequestDto.class).returnResult().getResponseBody();
		return newRequest;
	}

	@Test
	void testThatRequestIsWrongAccepted() {

		Long requestId = requestRepository.findAll().get(0).getId();

		// ACT
		getAcceptedRequestForWrong(requestId);

	}

	private void getAcceptedRequestForWrong(Long requestId) {
		webtestClient
			.put()
			.uri(uriBuilder -> uriBuilder.path(API_REQUESTS_ACCEPT_ID)
			.build(requestId))
			.headers(headers -> headers.setBasicAuth(TEST_USERNAME2, TEST_PASS))
			.exchange()
			.expectStatus()
			.isEqualTo(HttpStatus.FORBIDDEN)
			.expectBody(Void.class);
	}

	@Test
	void testByExample() {

		Employee requester = employeeRepository.findByNameStartingWithNamePrefix("Ester").get(0);
		Employee approver = employeeRepository.findByNameStartingWithNamePrefix("SpecTest1").get(0);
		Request newRequest = new Request(requester, approver, LocalDate.of(2023, Month.JULY, 30),
				LocalDate.of(2023, Month.AUGUST, 20), LocalDateTime.now(), RequestStatus.PENDING_APPROVAL);

		Employee requester2 = employeeRepository.findByNameStartingWithNamePrefix("SpecTest1").get(0);
		Employee approver2 = employeeRepository.findByNameStartingWithNamePrefix("Davis").get(0);
		Request newRequest2 = new Request(requester2, approver2, LocalDate.of(2023, Month.JULY, 10),
				LocalDate.of(2023, Month.AUGUST, 10), LocalDateTime.now(), RequestStatus.PENDING_APPROVAL);
		requestRepository.save(newRequest);
		requestRepository.save(newRequest2);

		Employee requester3 = new Employee();
		requester3.setName("Ester");
		Employee approver3 = new Employee();
		approver3.setName("Davis");
		Request request = new Request(requester3, approver3, null, null, null, null);
		RequestDto requestExp = requestMapper.requestToDto(request);
		RequestExample requestExample = new RequestExample(requestExp, null, null, null, null);

		// ACT
		List<RequestDto> findByExample = findByExample(requestExample);

		assertEquals(findByExample.size(), 2);

	}

	private List<RequestDto> findByExample(RequestExample requestExample) {
		List<RequestDto> requestsByExmple = ((RequestBodySpec) webtestClient.get()
				.uri(API_REQUESTS_EXAMPLE))
				.headers(headers -> headers.setBasicAuth(TEST_USERNAME, TEST_PASS))
				.bodyValue(requestExample)
				.exchange().expectStatus()
				.isOk()
				.expectBodyList(RequestDto.class)
				.returnResult()
				.getResponseBody();
		return requestsByExmple;
	}

}
