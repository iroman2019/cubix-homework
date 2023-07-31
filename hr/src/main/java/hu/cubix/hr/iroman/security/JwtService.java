package hu.cubix.hr.iroman.security;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureGenerationException;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import hu.cubix.hr.iroman.config.HrConfigurationProperties;
import hu.cubix.hr.iroman.model.Employee;
import hu.cubix.hr.iroman.repository.EmployeeRepository;

@Service
public class JwtService {

	// final String ISSUER = "HrApp";
	// private static final Algorithm algorithm = Algorithm.HMAC256(config);

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private HrConfigurationProperties config;

	public String createJwt(UserDetails userDetails) {

		//Algorithm algorithm = Algorithm.HMAC256(config.getJwt().getSecret());

		Employee employee = employeeRepository.findByUsername(userDetails.getUsername())
				.orElseThrow(() -> new UsernameNotFoundException(userDetails.getUsername()));

		List<Employee> subordinates = employeeRepository.findByManager(employee);
		Map<String, Long> subordinatesData = new HashMap<String, Long>();
		subordinates.forEach(emp -> subordinatesData.put(emp.getUsername(), emp.getId()));

		Map<String, Long> managerData = new HashMap<String, Long>();
		if (employee.getManager() != null) {
			managerData.put(employee.getManager().getUsername(), employee.getManager().getId());
		} else {
			managerData = null;
		}

		return JWT.create().withSubject(userDetails.getUsername())
				.withArrayClaim("auth",
						userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority)
								.toArray(String[]::new))
				.withExpiresAt(new Date(
						System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(config.getJwt().getExpminutes())))
				.withIssuer(config.getJwt().getIssuer()).withClaim("name", employee.getName())
				.withClaim("id", employee.getId()).withClaim("manager", managerData)
				.withClaim("subordinates", subordinatesData).sign(algorithm(config.getJwt().getAlg(), config.getJwt().getSecret()));
	}

	public UserDetails parseJwt(String jwtToken) {

		DecodedJWT decodedJwt = (DecodedJWT) JWT
				.require(algorithm(config.getJwt().getAlg(), config.getJwt().getSecret()))
				.withIssuer(config.getJwt().getIssuer()).build()
				.verify(jwtToken);
		return new User(decodedJwt.getSubject(), "dummy",
				decodedJwt.getClaim("auth").asList(String.class).stream().map(SimpleGrantedAuthority::new).toList());
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
