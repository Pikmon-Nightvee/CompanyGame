package Visual;

import java.util.ArrayList;
import java.util.Set;

import ExternalResources.GraphicsManager;
import GameLogic.Camera;
import GameLogic.Company;
import GameLogic.InteractableObject;
import GameLogic.MachinePlaceObject;
import GameLogic.Player;
import GameLogic.Wall;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.paint.Color;

public class Renderer {
	private boolean notTurned = true;
	private long walkTimer = 0;
	private long waitWalk = 250;
	private KeyCode lastPressed = KeyCode.COLORED_KEY_0;
	
	public void drawMainCanvas(Canvas canvas, GraphicsContext pencil, String id, GraphicsManager graphic) {
		switch(id) {
		case "InMenu": pencil.drawImage(graphic.getBackgroundMenu(),0, 0, canvas.getWidth(), canvas.getHeight()); break;
		case "InUIState": pencil.drawImage(graphic.getBackgroundUI(),0, 0, canvas.getWidth(), canvas.getHeight()); break;
		case "GameOver": pencil.fillRect(0, 0, canvas.getWidth(), canvas.getHeight()); break;
		}
	}
	
	public void drawPlayer(Canvas canvas, GraphicsContext pencil, Player player, Camera camera, GraphicsManager graphic, Set<KeyCode> inputs) {		
		//pencil.fillRect(player.getX() + camera.getX(),player.getY() + camera.getY(),player.getWidth(),player.getHeight());
		boolean notMoving = true;
		if(inputs.contains(KeyCode.A) && ! inputs.contains(KeyCode.D)) {
			lastPressed = KeyCode.A;
			notMoving = false;
			if(walkTimer + waitWalk < System.currentTimeMillis()) {
				graphic.changePlayer("Walk1PlayerA.png");
			}
			if(walkTimer + (waitWalk*2) < System.currentTimeMillis()) {
				graphic.changePlayer("StandPlayerA.png");
			}
			if(walkTimer + (waitWalk*3) < System.currentTimeMillis()) {
				graphic.changePlayer("Walk2PlayerA.png");
			}
			if(walkTimer + (waitWalk*4) < System.currentTimeMillis()) {
				graphic.changePlayer("StandPlayerA.png");
				walkTimer = System.currentTimeMillis();
			}
		}else if(inputs.contains(KeyCode.W) && !inputs.contains(KeyCode.S)) {
			lastPressed = KeyCode.W;
			notMoving = false;
			if(walkTimer + waitWalk < System.currentTimeMillis()) {
				graphic.changePlayer("Walk1PlayerW.png");
			}
			if(walkTimer + (waitWalk*2) < System.currentTimeMillis()) {
				graphic.changePlayer("StandPlayerW.png");
			}
			if(walkTimer + (waitWalk*3) < System.currentTimeMillis()) {
				graphic.changePlayer("Walk2PlayerW.png");
			}
			if(walkTimer + (waitWalk*4) < System.currentTimeMillis()) {
				graphic.changePlayer("StandPlayerW.png");
				walkTimer = System.currentTimeMillis();
			}
		}else if(inputs.contains(KeyCode.S) && !inputs.contains(KeyCode.W)) {
			lastPressed = KeyCode.S;
			notMoving = false;
			if(walkTimer + waitWalk < System.currentTimeMillis()) {
				graphic.changePlayer("Walk1PlayerS.png");
			}
			if(walkTimer + (waitWalk*2) < System.currentTimeMillis()) {
				graphic.changePlayer("StandPlayerS.png");
			}
			if(walkTimer + (waitWalk*3) < System.currentTimeMillis()) {
				graphic.changePlayer("Walk2PlayerS.png");
			}
			if(walkTimer + (waitWalk*4) < System.currentTimeMillis()) {
				graphic.changePlayer("StandPlayerS.png");
				walkTimer = System.currentTimeMillis();
			}
		}else if(inputs.contains(KeyCode.D) && !inputs.contains(KeyCode.A)) {
			lastPressed = KeyCode.D;
			notMoving = false;
			if(walkTimer + waitWalk < System.currentTimeMillis()) {
				graphic.changePlayer("Walk1PlayerD.png");
			}
			if(walkTimer + (waitWalk*2) < System.currentTimeMillis()) {
				graphic.changePlayer("StandPlayerD.png");
			}
			if(walkTimer + (waitWalk*3) < System.currentTimeMillis()) {
				graphic.changePlayer("Walk2PlayerD.png");
			}
			if(walkTimer + (waitWalk*4) < System.currentTimeMillis()) {
				graphic.changePlayer("StandPlayerD.png");
				walkTimer = System.currentTimeMillis();
			}
		}
		if(notMoving) {
			if(lastPressed.equals(KeyCode.A)) {
				graphic.changePlayer("StandPlayerA.png");
			}else if(lastPressed.equals(KeyCode.D)){
				graphic.changePlayer("StandPlayerD.png");
			}else if(lastPressed.equals(KeyCode.W)){
				graphic.changePlayer("StandPlayerW.png");
			}else if(lastPressed.equals(KeyCode.S)){
				graphic.changePlayer("StandPlayerS.png");
			}
		}
		pencil.drawImage(graphic.getPlayer(), player.getX() + camera.getX(),player.getY() + camera.getY(),player.getWidth(),player.getHeight());
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
	
	public void drawMachine(Canvas canvas, GraphicsContext pencil, MachinePlaceObject m, GraphicsManager graphic, boolean isOutofbounce) {
		//pencil.fillRect(m.getX(),m.getY(),m.getWidth(),m.getHeight());
		if(graphic.getMachinePlaceHolder().getWidth() != m.getWidth() && graphic.getMachinePlaceHolder().getHeight() != m.getHeight()) {
			notTurned = !notTurned;
		}
		switchImage(graphic,m,notTurned,true);
		pencil.drawImage(graphic.getMachinePlaceHolder(),m.getX(),m.getY(),m.getWidth(),m.getHeight()); 
		
		if(isOutofbounce) { 
			pencil.setGlobalAlpha(0.5);
			pencil.setFill(Color.RED);
			pencil.fillRect(m.getX(),m.getY(),m.getWidth(),m.getHeight());
		}
		pencil.setGlobalAlpha(1);
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
