package hu.cubix.hr.iroman.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RestController;

import hu.cubix.hr.iroman.dto.LoginDto;


@RestController
public class JwtLoginController {
	
	@Autowired
	private AuthenticationManager authManager;
	
	@Autowired
	private JwtService jwtService;
	
	@PostMapping("/api/login")
	public String login(@RequestBody LoginDto loginDto) {
		Authentication authentication = authManager.authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
		
		return jwtService.createJwt((UserDetails)authentication.getPrincipal());
	}

}
