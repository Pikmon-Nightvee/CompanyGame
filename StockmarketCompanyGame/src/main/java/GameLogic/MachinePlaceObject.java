package GameLogic;

public class MachinePlaceObject extends Item{
	private String object;
	
	public MachinePlaceObject(double x, double y, double width, double height, String object) {
		super(x, y, width, height);
		this.object = object;
		// TODO Auto-generated constructor stub
	}

	public String getObject() {
		return object;
	}

	public void setObject(String object) {
		this.object = object;
	}	
}