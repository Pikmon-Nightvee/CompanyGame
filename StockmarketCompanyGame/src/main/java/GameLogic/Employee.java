package GameLogic;

public class Employee {
	private String name;
	private int accuracy;
	private int speed;
	private int reliability;
	private int cost;
	private String machine;
	
	public Employee(String name, int accuracy, int speed, int reliability, int cost, String machine) {
		super();
		this.name = name;
		this.accuracy = accuracy;
		this.speed = speed;
		this.reliability = reliability;
		this.cost = cost;
		this.machine = machine;
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
	public void setReliability(int reliability) {
		this.reliability = reliability;
	}

	public int getCost() {
		return cost;
	}

	public String getMachine() {
		return machine;
	}

	@Override
	public String toString() {
		return "Employee [name=" + name + ", accuracy=" + accuracy + ", speed=" + speed + ", reliability=" + reliability
				+ ", cost=" + cost + ", machine=" + machine + "]" + "\n";
	}
}