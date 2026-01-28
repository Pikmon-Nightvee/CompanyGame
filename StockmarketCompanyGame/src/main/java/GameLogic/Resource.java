package GameLogic;

public class Resource {
	private String name;
	private int amount;
	private int cost;
	
	public Resource(String name, int amount, int cost) {
		super();
		this.name = name;
		this.amount = amount;
		this.cost = cost;
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

	@Override
	public String toString() {
		return name + " (" + "amount: " + amount + ", cost: " + cost + ")" + "\n";
	}
}