package GameLogic;

public class Product {
	private String name = "";
	private int amount;
	private int cost;
	private int quality;
	private String[] resourcesNeeded;
	
	public Product(String name, int amount, int cost, int quality, String[] resourcesNeeded) {
		super();
		this.name = name;
		this.amount = amount;
		this.cost = cost;
		this.quality = quality;
		this.resourcesNeeded = resourcesNeeded;
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
	public int getQuality() {
		return quality;
	}
	public String[] getResourcesNeeded() {
		return resourcesNeeded;
	}
}