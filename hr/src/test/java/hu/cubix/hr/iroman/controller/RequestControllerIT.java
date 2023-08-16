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
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.reactive.server.WebTestClient.RequestBodySpec;

import hu.cubix.hr.iroman.dto.LoginDto;
import hu.cubix.hr.iroman.dto.RequestDto;
import hu.cubix.hr.iroman.mapper.RequestMapper;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.model.Request;
import hu.cubix.hr.iroman.model.RequestExample;
import hu.cubix.hr.iroman.model.RequestStatus;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import hu.cubix.hr.iroman.repository.RequestRepository;
import hu.cubix.hr.iroman.security.JwtService;
import hu.cubix.hr.iroman.service.InitDbService;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@AutoConfigureWebTestClient(timeout = "360000")
public class RequestControllerIT {

	private static final String APPROVER = "Han";

	private static final String REQUESTER = "Chris";

	@Autowired
	InitDbService initDbService;

	@Autowired
	EmployeeRepository employeeRepository;

	@Autowired
	RequestMapper requestMapper;

	@Autowired
	WebTestClient webTestClient;

	@Autowired
	RequestRepository requestRepository;

	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	JwtService jwtTokenProvider;

	private static final String TEST_USERNAME = "Boss";
	private static final String TEST_USERNAME2 = REQUESTER;
	private static final String TEST_PASS = "123";
	private static final String API_REQUESTS = "/api/requests";
	private static final String API_REQUESTS_ACCEPT_ID = "/api/requests/accept/{id}";
	private static final String API_REQUESTS_EXAMPLE = "/api/requests/example";
	private static final String API_LOGIN = "/api/login";
	
	private String jwt;

	@BeforeEach
	public void init() {
		initDbService.clearDb();
		initDbService.insertTestData();
	}

	@Test
	void testThatRequestIsAddedTo() {

		Employee requester = employeeRepository.findByNameStartingWithNamePrefix(REQUESTER).get(0);
		Employee approver = employeeRepository.findByNameStartingWithNamePrefix(APPROVER).get(0);
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
		
		RequestDto newRequest = webTestClient
				.post()
				.uri(API_REQUESTS)
				//.headers(headers -> headers.setBasicAuth(TEST_USERNAME2, TEST_PASS))
				.headers(headers -> headers.setBearerAuth(getToken(TEST_USERNAME2, TEST_PASS)))
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
		RequestDto newRequest = webTestClient.put()
				.uri(uriBuilder -> uriBuilder.path(API_REQUESTS_ACCEPT_ID)
				.build(requestId))
				.headers(headers -> headers.setBearerAuth(getToken(TEST_USERNAME, TEST_PASS)))
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
		webTestClient
			.put()
			.uri(uriBuilder -> uriBuilder.path(API_REQUESTS_ACCEPT_ID)
			.build(requestId))
			.headers(headers -> headers.setBasicAuth(TEST_USERNAME2, TEST_PASS))
			.exchange()
			.expectStatus()
			.isEqualTo(HttpStatus.FORBIDDEN)
			.expectBody(Void.class);
	}
	
	private String getToken(String username, String password) {
		LoginDto loginDto=new LoginDto();
		loginDto.setUsername(username);
		loginDto.setPassword(password);
		jwt = webTestClient.post()
				.uri(API_LOGIN)
				.bodyValue(loginDto)
				.exchange()
				.expectBody(String.class)
				.returnResult()
				.getResponseBody();
		return jwt;
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
		List<RequestDto> requestsByExmple = ((RequestBodySpec) webTestClient.get()
				.uri(API_REQUESTS_EXAMPLE))
				.headers(headers -> headers.setBearerAuth(getToken(TEST_USERNAME, TEST_PASS)))
				.bodyValue(requestExample)
				.exchange().expectStatus()
				.isOk()
				.expectBodyList(RequestDto.class)
				.returnResult()
				.getResponseBody();
		return requestsByExmple;
	}

}
