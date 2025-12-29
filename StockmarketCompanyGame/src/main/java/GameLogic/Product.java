package GameLogic;

import java.util.Arrays;

public class Product {
	private String name = "";
	private int amount;
	private int cost;
	private String quality;
	private String machineNeeded;
	private String asignedEmployee;
	private String[] resourcesNeeded;
	
	public Product(String name, int amount, int cost, String quality, String machineNeeded, String asignedEmployee,
			String[] resourcesNeeded) {
		super();
		this.name = name;
		this.amount = amount;
		this.cost = cost;
		this.quality = quality;
		this.machineNeeded = machineNeeded;
		this.asignedEmployee = asignedEmployee;
		this.resourcesNeeded = resourcesNeeded;
	}
	
	@Override
	public String toString() {
		return "Product [name=" + name + ", amount=" + amount + ", cost=" + cost + ", quality=" + quality
				+ ", machineNeeded=" + machineNeeded + ", asignedEmployee=" + asignedEmployee + ", resourcesNeeded="
				+ Arrays.toString(resourcesNeeded) + "]";
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
}