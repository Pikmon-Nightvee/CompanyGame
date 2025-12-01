package GameLogic;

public class Employee {
	private String name;
	private int accuracy;
	private int speed;
	private int reliability;
	private int cost;
	
	public Employee(String name, int accuracy, int speed, int reliability, int cost) {
		super();
		this.name = name;
		this.accuracy = accuracy;
		this.speed = speed;
		this.reliability = reliability;
		this.cost = cost;
	}
	
	public String getName() {
		return name;
	}
	public int getAccuracy() {
		return accuracy;
	}
	public int getSpeed() {
		return speed;
	}
	public int getReliability() {
		return reliability;
	}
	public int getCost() {
		return cost;
	}
}