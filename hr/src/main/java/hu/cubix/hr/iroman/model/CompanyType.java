package hu.cubix.hr.iroman.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

@Entity
public class CompanyType {

	@Id
	@GeneratedValue
	private int id;

	private String name;
	
	public CompanyType() {
	}

	public CompanyType(int id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String bt) {
		this.name = bt;
	}
}
