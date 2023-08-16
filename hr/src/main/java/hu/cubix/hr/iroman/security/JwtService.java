package hu.cubix.hr.iroman.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.cubix.hr.iroman.config.HrConfigurationProperties;
import hu.cubix.hr.iroman.config.HrConfigurationProperties.Jwt;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;

@Service
public class JwtService {

	private static final String AUTH = "auth";
	private static final String SUBORDINATES = "subordinates";
	private static final String NAME = "name";
	private static final String USERNAME = "username";
	private static final String MANAGER = "manager";
	private static final String ID = "id";
	private Algorithm algorithm;

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private HrConfigurationProperties config;

	@PostConstruct
	public void init() {
		Jwt jwtData = config.getJwt();
		String algName = jwtData.getAlg();
		String secret = jwtData.getSecret();
		try {
			this.algorithm = (Algorithm) Algorithm.class.getMethod(algName, String.class).invoke(null, secret);
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			e.printStackTrace();
		}
	}
	
	public String createJwt(UserDetails userDetails) {

		Employee employee = employeeRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException(userDetails.getUsername()));
		Employee manager = employee.getManager();

		List<Employee> subordinatesData = employee.getSubordinates();

		Map<String, Long> managerData = new HashMap<String, Long>();
		if (employee.getManager() != null) {
			managerData.put(employee.getManager().getUsername(), employee.getManager().getId());
		} else {
			managerData = null;
		}

		Builder jwtBuilder = JWT.create()
				.withSubject(userDetails.getUsername())
				.withArrayClaim(AUTH,
						userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
								.toArray(String[]::new))
				.withClaim(NAME, employee.getName())
				.withClaim(ID, employee.getId());
		
		if(managerData!=null && !managerData.isEmpty()) {
			jwtBuilder.withClaim(MANAGER, createMapFromEmployee(manager));
		}
		if(subordinatesData!=null && !subordinatesData.isEmpty()) {
			jwtBuilder.withClaim(SUBORDINATES,
					subordinatesData.stream().map(this::createMapFromEmployee).toList());
		}
		
		Jwt jwtData = config.getJwt();
		
		return jwtBuilder		
				.withExpiresAt(new Date(
						System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(config.getJwt().getExpminutes())))
				.withIssuer(jwtData.getIssuer())
				.sign(algorithm);
	}
	
	private Map<String, Object> createMapFromEmployee(Employee employee) {
		return Map.of(
				ID, employee.getId(),
				USERNAME, employee.getUsername()
			);
	}
	
	private Employee createEmployeeFromMap(Map<String, Object> employeeData) {
		if(employeeData == null || employeeData.isEmpty())
			return null;
		Employee employee = new Employee();
		employee.setId(((Integer)employeeData.get(ID)).longValue());
		employee.setUsername((String)employeeData.get(USERNAME));
		return employee;
	}

	public UserDetails parseJwt(String jwtToken) {
		
		Employee employee = new Employee();

		DecodedJWT decodedJwt = (DecodedJWT) JWT
				.require(algorithm(config.getJwt().getAlg(), config.getJwt().getSecret()))
				.withIssuer(config.getJwt().getIssuer())
				.build()
				.verify(jwtToken);
		
		employee.setId(decodedJwt.getClaim(ID).asLong());
		employee.setUsername(decodedJwt.getSubject());
		employee.setName(decodedJwt.getClaim(NAME).asString());
		
		Claim managerClaim = decodedJwt.getClaim(MANAGER);
		if(managerClaim != null) {
			Map<String, Object> managerData = managerClaim.asMap();
			employee.setManager(createEmployeeFromMap(managerData));
		}
		
		Claim managedEmployeesClaim = decodedJwt.getClaim(SUBORDINATES);
		if(managedEmployeesClaim != null) {
			List<HashMap> managedEmployees = managedEmployeesClaim.asList(HashMap.class);
			if(managedEmployees != null) {
				employee.setSubordinates(
						managedEmployees.stream()
						.map(employeeData -> createEmployeeFromMap(employeeData))
						.toList()
				);
			}
		}
		
		return new HrUser(decodedJwt.getSubject(), "dummy",
				decodedJwt.getClaim(AUTH).asList(String.class).stream().map(SimpleGrantedAuthority::new).toList(), employee);
	}

	private Algorithm algorithm(String alg, String secret) {
		// Algorithm algorithm = null;
		switch (alg) {
		case "HMAC256": {
			return Algorithm.HMAC256(config.getJwt().getSecret());
		}
		case "HMAC384": {
			return Algorithm.HMAC384(config.getJwt().getSecret());
		}
		case "HMAC512": {
			return Algorithm.HMAC512(config.getJwt().getSecret());
		}
		default:
			throw new IllegalArgumentException("Unexpected value: " + alg);
		}

	}
}
