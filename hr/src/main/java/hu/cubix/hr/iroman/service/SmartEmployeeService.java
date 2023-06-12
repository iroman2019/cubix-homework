package hu.cubix.hr.iroman.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hu.cubix.hr.iroman.config.HrConfigurationProperties;
import hu.cubix.hr.iroman.model.Employee;

//@Service
public class SmartEmployeeService implements EmployeeService {

//	@Value("${hr.salary.smart.limit1}")
//	private double limit1;
//	
//	@Value("${hr.salary.smart.limit2}")
//	private double limit2;
//	
//	@Value("${hr.salary.smart.limit3}")
//	private double limit3;
//	
//	@Value("${hr.salary.smart.percent1}")
//	private int percent1;
//	
//	@Value("${hr.salary.smart.percent2}")
//	private int percent2;
//	
//	@Value("${hr.salary.smart.percent3}")
//	private int percent3;
//	
//	@Value("${hr.salary.smart.percent4}")
//	private int percent4;
//	
	@Autowired
	private HrConfigurationProperties config;

	@Override
	public int getPayRaisetPercent(Employee employee) {

		// System.out.println(LocalDateTime.now()-employee.getTimestamp());
		double howManyYearsHasBeenWorking = differenceBetweenTwoDates(LocalDateTime.now(), employee.getTimestamp());

//		if (howManyYearsHasBeenWorking >= limit1) {
//			return percent1;
//		}
//		if (howManyYearsHasBeenWorking >= limit2 && howManyYearsHasBeenWorking < limit1) {
//			return percent2;
//		}
//		if (howManyYearsHasBeenWorking >= limit3 && howManyYearsHasBeenWorking < limit2) {
//			return percent3;
//		}
//		return percent4;

		int db = config.getSalary().getSmart().getLimits().size();

		List<Double> limits = config.getSalary().getSmart().getLimits();
		List<Integer> percents = config.getSalary().getSmart().getPercents();

		for (int i = 0; i < db; i++) {
			if (howManyYearsHasBeenWorking >= limits.get(i)) {
				return percents.get(i);
			}

		}
		return percents.get(db);
	}

	private double differenceBetweenTwoDates(LocalDateTime now, LocalDateTime timestamp) {
		int yearsDiff = now.getYear() - timestamp.getYear();
		int monthDiff = now.getMonthValue() - timestamp.getMonthValue();
		double diff = yearsDiff + (monthDiff / 12.0);
		System.out.println("Ennyi éve dolgozik a cégnél: " + diff);
		return diff;
	}

}
