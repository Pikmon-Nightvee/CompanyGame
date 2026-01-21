package Visual;

import java.util.ArrayList;

import GameLogic.Camera;
import GameLogic.InteractableObject;
import GameLogic.Player;
import GameLogic.Wall;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
	public void drawMainCanvas(Canvas canvas, GraphicsContext pencil) {
		pencil.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
	
	public void drawPlayer(Canvas canvas, GraphicsContext pencil, Player player, Camera camera) {
		pencil.fillRect(player.getX() + camera.getX(),player.getY() + camera.getY(),player.getWidth(),player.getHeight());
	}
	
	public void drawWalls(Canvas canvas, GraphicsContext pencil, ArrayList<Wall> walls, Camera camera) {
		for(Wall w : walls) {
			pencil.fillRect(w.getX() + camera.getX(),w.getY() + camera.getY(),w.getWidth(),w.getHeight());
		}
	}
	
	public void drawInteractable(Canvas canvas, GraphicsContext pencil, ArrayList<InteractableObject> interact, Camera camera) {
		for(InteractableObject i : interact) {
			pencil.fillRect(i.getX() + camera.getX(),i.getY() + camera.getY(),i.getWidth(),i.getHeight());
		}
	}
	
	public void drawInteractableBlinking(Canvas canvas, GraphicsContext pencil, ArrayList<InteractableObject> interact, Camera camera) {
		for(InteractableObject i : interact) {
			if(i.isBroken()) {
				pencil.setGlobalAlpha(i.blinking());
				pencil.fillRect(i.getX() + camera.getX(),i.getY() + camera.getY(),i.getWidth(),i.getHeight());
			}
		}
		pencil.setGlobalAlpha(1);
	}
	
	public void drawWall(Canvas canvas, GraphicsContext pencil, Wall w) {
		pencil.fillRect(w.getX(),w.getY(),w.getWidth(),w.getHeight());
	}

	public void drawWarningCanvas(Canvas canvas, GraphicsContext pencil) {
		pencil.fillRect(0, 20, canvas.getWidth(), canvas.getHeight());
	}
	
	public void clearAll(Canvas gameCanvas, GraphicsContext gamePencil, Canvas warningCanvas,
			GraphicsContext warningPencil) {
		gamePencil.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
		warningPencil.clearRect(0, 0, warningCanvas.getWidth(), warningCanvas.getHeight());
	}
}
