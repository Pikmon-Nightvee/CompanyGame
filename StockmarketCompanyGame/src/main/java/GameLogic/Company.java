package GameLogic;

public class Company {
	private String name = "";
	private double moneyOfCompany = 0;
	private int reputation = 50;
	
	public Company(String name, double moneyOfCompany) {
		super();
		this.name = name;
		this.moneyOfCompany = moneyOfCompany;
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
}