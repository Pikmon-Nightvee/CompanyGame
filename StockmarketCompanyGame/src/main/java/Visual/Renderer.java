package Visual;

import java.util.ArrayList;

import ExternalResources.GraphicsManager;
import GameLogic.Camera;
import GameLogic.Company;
import GameLogic.InteractableObject;
import GameLogic.MachinePlaceObject;
import GameLogic.Player;
import GameLogic.Wall;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class Renderer {
	private boolean notTurned = true;
	
	public void drawMainCanvas(Canvas canvas, GraphicsContext pencil, String id, GraphicsManager graphic) {
		switch(id) {
		case "InMenu": pencil.drawImage(graphic.getBackgroundMenu(),0, 0, canvas.getWidth(), canvas.getHeight()); break;
		case "InUIState": pencil.drawImage(graphic.getBackgroundUI(),0, 0, canvas.getWidth(), canvas.getHeight()); break;
		}
	}
	
	public void drawPlayer(Canvas canvas, GraphicsContext pencil, Player player, Camera camera) {
		pencil.fillRect(player.getX() + camera.getX(),player.getY() + camera.getY(),player.getWidth(),player.getHeight());
	}
	
	public void drawWalls(Canvas canvas, GraphicsContext pencil, ArrayList<Wall> walls, Camera camera, GraphicsManager graphic, Company company) {
		for(Wall w : walls) {
			if(company.getCompanyType().equals("Foodtruck")) {
				pencil.drawImage(graphic.getWallFood(),w.getX() + camera.getX(),w.getY() + camera.getY(),w.getWidth(),w.getHeight());
			}else{
				pencil.drawImage(graphic.getWall(),w.getX() + camera.getX(),w.getY() + camera.getY(),w.getWidth(),w.getHeight());
			}
		}
	}
	
	public void drawFloor(Canvas canvas, GraphicsContext pencil, ArrayList<Wall> walls, Camera camera, GraphicsManager graphic, Company company) {
		for(Wall w : walls) {
			if(company.getCompanyType().equals("Foodtruck")) {
				pencil.drawImage(graphic.getFloorFood(),w.getX() + camera.getX(),w.getY() + camera.getY(),w.getWidth(),w.getHeight());
			}else{
				pencil.drawImage(graphic.getFloor(),w.getX() + camera.getX(),w.getY() + camera.getY(),w.getWidth(),w.getHeight());
			}
		}
	}
	
	public void drawMachines(Canvas canvas, GraphicsContext pencil, ArrayList<MachinePlaceObject> machines, Camera camera, GraphicsManager graphic, Company company) {
		for(MachinePlaceObject m : machines) {
			//pencil.fillRect(m.getX() + camera.getX(),m.getY() + camera.getY(),m.getWidth(),m.getHeight());
			switchImage(graphic,m,true,false);
			boolean notTurned = true;
			if(graphic.getMachine().getWidth() != m.getWidth() && graphic.getMachine().getHeight() != m.getHeight()) {
				notTurned = false;
			}
			switchImage(graphic,m,notTurned,false);
			pencil.drawImage(graphic.getMachine(),m.getX() + camera.getX(),m.getY() + camera.getY(),m.getWidth(),m.getHeight()); 
		}
	}
	
	public void drawMachine(Canvas canvas, GraphicsContext pencil, MachinePlaceObject m, GraphicsManager graphic) {
		//pencil.fillRect(m.getX(),m.getY(),m.getWidth(),m.getHeight());
		if(graphic.getMachinePlaceHolder().getWidth() != m.getWidth() && graphic.getMachinePlaceHolder().getHeight() != m.getHeight()) {
			notTurned = !notTurned;
		}
		switchImage(graphic,m,notTurned,true);
		pencil.drawImage(graphic.getMachinePlaceHolder(),m.getX(),m.getY(),m.getWidth(),m.getHeight()); 
	}
	
	private void switchImage(GraphicsManager graphic, MachinePlaceObject m, boolean notTurned, boolean isHolder) {
		String switchTo = "";
		switch(m.getObject()) {
		case "PC": 
			if(notTurned) {
				switchTo = "PC.png";
			}else {
				switchTo = "PC1.png";
			}  
			break;
		case "Ofen": 
			if(notTurned) {
				switchTo = "Ofen.png";
			}else {
				switchTo = "OfenD.png";
			} 
			break;
		case "Herd":
			switchTo = "Oven.png";
			break;
		case "Bohrmaschine": 
			if(notTurned) {
				switchTo = "Bohrmaschine.png";
			}else {
				switchTo = "BohrmaschineD.png";
			} 
			break;
		case "Drehmaschine":
			if(notTurned) {
				switchTo = "Drehmaschine.png";
			}else {
				switchTo = "DrehmaschineD.png";
			} 
			break;
		case "Fräsmaschine": 
			if(notTurned) {
				switchTo = "Fräsmaschine.png";
			}else {
				switchTo = "FräsmaschineD.png";
			} 
			break;
		case "Kreissäge":
			switchTo = "Kreissäge.png";
			break;
		}
		if(isHolder) {
			System.out.println(switchTo);
			graphic.changeMachinePlaceHolder(switchTo);
		}else {
			graphic.changeMachine(switchTo);
		}
	}
	
	public void drawWheels(Canvas canvas, GraphicsContext pencil, ArrayList<Wall> walls, Camera camera, GraphicsManager graphic) {
		for(Wall w : walls) {
			pencil.drawImage(graphic.getWallFoodSpecial(),w.getX() + camera.getX(),w.getY() + camera.getY(),w.getWidth(),w.getHeight());
		}
	}
	
	public void drawHeadlights(Canvas canvas, GraphicsContext pencil, ArrayList<Wall> walls, Camera camera, GraphicsManager graphic) {
		for(Wall w : walls) {
			pencil.drawImage(graphic.getWallFoodSpecialLight(),w.getX() + camera.getX(),w.getY() + camera.getY(),w.getWidth(),w.getHeight());
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

	public void drawWarningCanvas(Canvas canvas, GraphicsContext pencil, GraphicsManager graphic) {
		pencil.fillRect(0, 20, canvas.getWidth(), canvas.getHeight());
		pencil.drawImage(graphic.getWarningCanvasOverlay(),0, 20, canvas.getWidth(), canvas.getHeight()-20);
	}
	
	public void clearAll(Canvas gameCanvas, GraphicsContext gamePencil, Canvas warningCanvas,
			GraphicsContext warningPencil) {
		gamePencil.clearRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
		warningPencil.clearRect(0, 0, warningCanvas.getWidth(), warningCanvas.getHeight());
	}
	
	public void allBlack(Canvas gameCanvas, GraphicsContext gamePencil) {
		gamePencil.fillRect(0, 0, gameCanvas.getWidth(), gameCanvas.getHeight());
	}
}
