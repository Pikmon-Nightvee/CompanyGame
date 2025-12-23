package GameLogic;

public class Machine {
	private String name;
	private int amount;
	private int cost;
	private int condition;
	
	public Machine(String name, int amount, int cost, int condition) {
		super();
		this.name = name;
		this.amount = amount;
		this.cost = cost;
		this.condition = condition;
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
	public int getCondition() {
		return condition;
	}

	@Override
	public String toString() {
		return "Machine [name=" + name + ", amount=" + amount + ", cost=" + cost + ", condition=" + condition + "]" + "\n";
	}
}