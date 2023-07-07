package hu.cubix.hr.iroman.model;

public enum CompanyTypeEnum {
	BT("Bt"), KFT("Kft"), NYRT("Nyrt"), ZRT("Zrt");
	
	private String cType;
	
	private CompanyTypeEnum(String companyType) {
		this.cType=companyType;
	}

	@Override
    public String toString(){
        return cType;
    } 
}
