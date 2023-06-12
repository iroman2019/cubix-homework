package hu.cubix.hr.iroman.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigurationProperties {

	private Salary salary;

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}

	public static class Salary {

		private Smart smart;

		public Smart getSmart() {
			return smart;
		}

		public void setSmart(Smart smart) {
			this.smart = smart;
		}

		public static class Smart {

			private List<Double> limits;
			private List<Integer> percents;

			public List<Double> getLimits() {
				return limits;
			}

			public void setLimits(List<Double> limits) {
				this.limits = limits;
			}

			public List<Integer> getPercents() {
				return percents;
			}

			public void setPercents(List<Integer> percents) {
				this.percents = percents;
			}

		}

	}

}
