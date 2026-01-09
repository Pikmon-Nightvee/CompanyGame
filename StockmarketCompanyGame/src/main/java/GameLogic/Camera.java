package GameLogic;

import javafx.scene.canvas.Canvas;

public class Camera {
	private double x = 0;
	private double y = 0;
	
	public void updateCamera(Player player, Canvas canvas) {
		x = -(player.getX() - canvas.getWidth()/2 + player.getWidth()/2);
		y = -(player.getY() - canvas.getHeight()/2 + player.getHeight()/2);
	}
	
	public double getX() {
		return x;
	}
	public double getY() {
		return y;
	}
}