package hu.cubix.hr.iroman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import hu.cubix.hr.iroman.service.DefaultEmployeeService;
import hu.cubix.hr.iroman.service.EmployeeService;

@Configuration
@Profile("!smart")
public class DefaultConfiguration {

	@Bean
	public EmployeeService discountService() {
		return new DefaultEmployeeService();
	}

}
