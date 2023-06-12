package hu.cubix.hr.iroman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.cubix.hr.iroman.service.EmployeeService;
import hu.cubix.hr.iroman.service.SmartEmployeeService;

@Configuration
@Profile("smart")
public class SmartConfiguration {

	@Bean
	public EmployeeService EmployeeService() {
		return new SmartEmployeeService();
	}

}
