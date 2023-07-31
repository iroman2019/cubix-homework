package hu.cubix.hr.iroman.config;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "hr")
@Component
public class HrConfigurationProperties {

	private Salary salary;

	private Jwt jwt;

	public Jwt getJwt() {
		return jwt;
	}

	public void setJwt(Jwt jwt) {
		this.jwt = jwt;
	}

	public Salary getSalary() {
		return salary;
	}

	public void setSalary(Salary salary) {
		this.salary = salary;
	}

	public static class Jwt {
		private String secret;
		private String issuer;
		private int expminutes;
		private String alg;

		public String getSecret() {
			return secret;
		}

		public void setSecret(String secret) {
			this.secret = secret;
		}

		public String getIssuer() {
			return issuer;
		}

		public void setIssuer(String issuer) {
			this.issuer = issuer;
		}

		public int getExpminutes() {
			return expminutes;
		}

		public void setExpminutes(int expminutes) {
			this.expminutes = expminutes;
		}

		public String getAlg() {
			return alg;
		}

		public void setAlg(String alg) {
			this.alg = alg;
		}

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
