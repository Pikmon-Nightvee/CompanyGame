package GameLogic;

import java.util.ArrayList;
import java.util.Set;

import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import Visual.ButtonManager;
import Visual.UIMenuManager;
import Visual.VisualElementsHolder;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KeyboardManager {
	private boolean isBeingPlaced = false;
	private long timeWait = 500;
	private long timeSinceLastPressed = 0;
	
	public void keyboardInputsMovement(Set<KeyCode> inputs, Player player) {
		if(inputs.contains(KeyCode.A)) {
			player.moveOnX(true);
		}
		if(inputs.contains(KeyCode.D)) {
			player.moveOnX(false);
		}
		if(inputs.contains(KeyCode.W)) {
			player.moveOnY(true);
		}
		if(inputs.contains(KeyCode.S)) {
			player.moveOnY(false);
		}
	}
	
	public void keyBoardInputPlaceMachine(Set<KeyCode> inputs, Wall placeHolder) {
		if(!inputs.contains(KeyCode.G)) {
			isBeingPlaced = false;
		}
		
		if(isBeingPlaced) {
			if(timeSinceLastPressed + timeWait < System.currentTimeMillis()) {
				if(inputs.contains(KeyCode.R)){
					double width = placeHolder.getWidth();
					double height = placeHolder.getHeight();
					placeHolder.setHeight(width);
					placeHolder.setWidth(height);
					timeSinceLastPressed = System.currentTimeMillis();
					System.out.println("Rotate Object");
				}
			}
		}else {
			if(inputs.contains(KeyCode.G)) {
				isBeingPlaced = true;
				System.out.println("Object is being placed");
			}
		}
	}
	
	public void keyBoardInputPlacedMachine(Set<KeyCode> inputs, ArrayList<Wall> walls, InteractableObject interact, LevelHolder level, WriteCSVFiles writer) {
		if(timeSinceLastPressed + timeWait < System.currentTimeMillis()) {
			if(inputs.contains(KeyCode.F)){
				timeSinceLastPressed = System.currentTimeMillis();
				System.out.println("Remove machine");
				level.removeMachine(walls, interact);
				writer.machineRemoved(walls);
			}else if(inputs.contains(KeyCode.C)){
				timeSinceLastPressed = System.currentTimeMillis();
				System.out.println("Repair machine");
			}
		}
	}
	
	public void keyboardInputMenu(Set<KeyCode> inputs, UIMenuManager uiMenu, VBox vBox, ButtonManager buttonManager, VisualElementsHolder visual, WriteCSVFiles writer, ReadCSVFiles reader, Stage stage, Company company, Canvas warningCanvas, StackPane gamePane, Canvas gameCanvas, LevelHolder level, Player player, GameManager game) {
		if(inputs.contains(KeyCode.E)) {
			uiMenu.loadMenu(vBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, game);
			game.updateState("InMenu");
		}
	}

	public boolean isBeingPlaced() {
		return isBeingPlaced;
	}

	public void setBeingPlaced(boolean isBeingPlaced) {
		this.isBeingPlaced = isBeingPlaced;
	}
}
