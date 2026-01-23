package GameLogic;

import java.util.ArrayList;
import java.util.Set;

import ExternalResources.SoundeffectManager;
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
	
	public void keyboardInputsMovement(Set<KeyCode> inputs, Player player, UIMenuManager UI, SoundeffectManager sfx) {
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
		if(inputs.contains(KeyCode.A) || inputs.contains(KeyCode.D) || inputs.contains(KeyCode.W) || inputs.contains(KeyCode.D)) {
			sfx.playWalkingPlay(UI.isSfxOn());
		}else {
			sfx.playWalkingStop();
		}
	}
	
	public void keyBoardInputPlaceMachine(Set<KeyCode> inputs, Wall placeHolder, ArrayList<Machine> machines, UIMenuManager UI, SoundeffectManager sfx) {
		if(!inputs.contains(KeyCode.G)) {
			isBeingPlaced = false;
		}
		
		if(isBeingPlaced) {
			if(timeSinceLastPressed + timeWait < System.currentTimeMillis()) {
				if(inputs.contains(KeyCode.R)){
					sfx.playRotation(UI.isSfxOn());
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
				sfx.playSelect(UI.isSfxOn());
				isBeingPlaced = true;
				System.out.println("Object is being placed");
			}
		}
	}
	
	public void keyBoardInputPlacedMachine(Set<KeyCode> inputs, ArrayList<Wall> walls, InteractableObject interact, LevelHolder level, WriteCSVFiles writer, ReadCSVFiles reader, ArrayList<InteractableObject> toRepair, Company company, UIMenuManager UI, SoundeffectManager sfx) {
		if(timeSinceLastPressed + timeWait < System.currentTimeMillis()) {
			if(inputs.contains(KeyCode.F)){
				sfx.playPickUp(UI.isSfxOn());
				timeSinceLastPressed = System.currentTimeMillis();
				System.out.println("Remove machine");
				level.removeMachine(walls, interact);
				writer.machineNamePlaceRemove(reader,level.getToRemove(),interact.getExtra());
				writer.machineRemoved(walls,reader);
			}else if(inputs.contains(KeyCode.C)){
				timeSinceLastPressed = System.currentTimeMillis();
				System.out.println("Repair machine");
				InteractableObject compare = new InteractableObject((interact.getX()+interact.getExtra()),(interact.getY()+interact.getExtra()), 
						(interact.getWidth()-(interact.getExtra()*2)), (interact.getHeight()-(interact.getExtra()*2)), false, false);
				
				for(InteractableObject i : toRepair) {
					if(i.isBroken()) {
						if(i.equals(compare)) {
							sfx.playRepair(UI.isSfxOn());
							i.setBroken(false);
							double moneySpent = 1250;
							company.setMoneyOfCompany(company.getMoneyOfCompany()-moneySpent);
							System.out.println("Money was spent");
						}else {
							System.out.println("Not it");
						}
					}else {
						System.out.println("Not broken");
					}
				}
			}
		}
	}
	
	public void keyboardInputMenu(Set<KeyCode> inputs, UIMenuManager uiMenu, VBox vBox, ButtonManager buttonManager, VisualElementsHolder visual, WriteCSVFiles writer, ReadCSVFiles reader, Stage stage, Company company, Canvas warningCanvas, StackPane gamePane, Canvas gameCanvas, LevelHolder level, Player player, GameManager game, SoundeffectManager sfx) {
		if(inputs.contains(KeyCode.E)) {
			uiMenu.loadMenu(vBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, game, sfx);
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
