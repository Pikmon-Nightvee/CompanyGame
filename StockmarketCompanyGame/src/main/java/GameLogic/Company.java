package GameLogic;

public class Company {
	private String name = "";
	private double moneyOfCompany = 0;
	private int reputation = 50;
	private String companyType = "";
	
	public Company(String name, double moneyOfCompany, String companyType) {
		super();
		this.name = name;
		this.moneyOfCompany = moneyOfCompany;
		this.companyType = companyType;
	}

	@Override
	public String toString() {
		return "Company [name=" + name + ", moneyOfCompany=" + moneyOfCompany + ", reputation=" + reputation
				+ ", companyType=" + companyType + "]";
	}

	public int getReputation() {
		return reputation;
	}

	public void setReputation(int reputation) {
		this.reputation = reputation;
	}

	public String getName() {
		return name;
	}

	public double getMoneyOfCompany() {
		return moneyOfCompany;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMoneyOfCompany(double moneyOfCompany) {
		this.moneyOfCompany = moneyOfCompany;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}
}