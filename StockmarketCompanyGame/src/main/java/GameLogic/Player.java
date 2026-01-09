package GameLogic;

public class Player extends Item{
	private double speed;

	public Player(double x, double y, double width, double height, double speed) {
		super(x, y, width, height);
		this.speed = speed;
	}
	
	public void moveOnX(boolean isLeft) {
		if(isLeft) {
			x -= speed;
		}else {
			x += speed;
		}
	}
	public void moveOnY(boolean isUp) {
		if(isUp) {
			y -= speed;
		}else {
			y += speed;
		}
	}

	@Override
	public String toString() {
		return super.toString() + "Player [speed=" + speed + "]";
	}

	public double getSpeed() {
		return speed;
	}
}