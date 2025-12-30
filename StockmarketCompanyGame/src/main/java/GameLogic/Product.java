package GameLogic;

import java.util.Arrays;

public class Product {
	private String name = "";
	private int amount;
	private int cost;
	private int timePerUnit;
	private int time;
	private String quality;
	private String machineNeeded;
	private String asignedEmployee;
	private String asignedCompanyType;
	private String[] resourcesNeeded;
	private int[] resourcesAmount;

	public Product(String name, int amount, int cost, int timePerUnit, int time, String quality, String machineNeeded,
			String asignedEmployee, String asignedCompanyType, String[] resourcesNeeded, int[] resourcesAmount) {
		super();
		this.name = name;
		this.amount = amount;
		this.cost = cost;
		this.timePerUnit = timePerUnit;
		this.time = time;
		this.quality = quality;
		this.machineNeeded = machineNeeded;
		this.asignedEmployee = asignedEmployee;
		this.asignedCompanyType = asignedCompanyType;
		this.resourcesNeeded = resourcesNeeded;
		this.resourcesAmount = resourcesAmount;
	}

	@Override
	public String toString() {
		return "Product [name=" + name + ", amount=" + amount + ", costPerUnit=" + cost + ", timePerUnit=" + timePerUnit
				+ ", time=" + time + ", quality=" + quality + ", machineNeeded=" + machineNeeded + ", asignedEmployee="
				+ asignedEmployee + ", asignedCompanyType=" + asignedCompanyType + ", resourcesNeeded="
				+ Arrays.toString(resourcesNeeded) + ", resourcesAmount=" + Arrays.toString(resourcesAmount) + "]";
	}

	public String toStringProduceable() {
		return "Product [name=" + name + ", amount=" + amount + ", cost=" + cost + ", timePerUnit=" + timePerUnit + ", machineNeeded=" + 
				machineNeeded + ", resourcesNeeded=" + Arrays.toString(resourcesNeeded) + ", resourcesAmount="
				+ Arrays.toString(resourcesAmount) + "]";
	}

	public String toStringInProduction() {
		return "Product [name=" + name + ", amount=" + amount + ", cost=" + cost
				+ ", time=" + time + ", asignedEmployee=" + asignedEmployee + "]";
	}

	public String toStringOnStock() {
		return "Product [name=" + name + ", amount=" + amount + ", prize=" + cost + ", quality=" + quality + "]";
	}

	public String getAsignedCompanyType() {
		return asignedCompanyType;
	}
	public int getTime() {
		return time;
	}
	public int[] getResourcesAmount() {
		return resourcesAmount;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public void setAmount(int amount) {
		this.amount = amount;
	}
	public String getName() {
		return name;
	}
	public int getAmount() {
		return amount;
	}
	public int getCost() {
		return cost;
	}
	public String getQuality() {
		return quality;
	}
	public String[] getResourcesNeeded() {
		return resourcesNeeded;
	}
	public int getTimePerUnit() {
		return timePerUnit;
	}
	public String getMachineNeeded() {
		return machineNeeded;
	}
	public String getAsignedEmployee() {
		return asignedEmployee;
	}
}