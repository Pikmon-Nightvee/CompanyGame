package GameLogic;

import java.util.ArrayList;
import java.util.Set;

import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import Visual.ButtonManager;
import Visual.Renderer;
import Visual.UIMenuManager;
import Visual.VisualElementsHolder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameManager {
	private Renderer renderer = new Renderer();
	private KeyboardManager keyboard = new KeyboardManager();
	private Camera camera = new Camera();
	private ColissionHandler colission = new ColissionHandler();
	
	private String state = "InMenu";
	private double xMouse = 0;
	private double yMouse = 0;
	private boolean mousePressed = false;
	
	private int width = 50;
	private int height = 50;
	private Wall placeHolder = new Wall(xMouse, yMouse, width, height);
	
	private long waitTime = 350;
	private long currentTime = 0;
	
	public void updateMouseCoordinates(double xMouse, double yMouse) {
		this.xMouse = xMouse;
		this.yMouse = yMouse;
	}
	public void mousePressed(boolean mousePressed) {
		this.mousePressed = mousePressed;
	}
		
	public void updateState(String update) {
		state = update;
	}
	
	//Game Loop
	public void loop(Canvas gameCanvas, GraphicsContext gamePencil, StackPane gamePane,Canvas warningCanvas, GraphicsContext warningPencil, ReadCSVFiles reader, WriteCSVFiles writer, VBox gameVBox, Company company, UIMenuManager uiMenuManager, Stage stage, ButtonManager buttonManager, VisualElementsHolder visual, Set<KeyCode> inputs, Player player, LevelHolder level, GameManager gameManager) {
		Timeline gameTimeline = new Timeline();
		
		warningPencil.setFill(Color.BLACK);
		
		buttonManager.getBankrupt().setOnAction(event->{
			uiMenuManager.startUp(gameVBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, gameManager);
			gamePencil.setFill(Color.LIGHTGREY);
			level.getWalls().clear();
		});
		
		//30FPS Update Loop
		gameTimeline = new Timeline(new KeyFrame(Duration.seconds(0.032), event -> {
			renderer.clearAll(gameCanvas, gamePencil,warningCanvas, warningPencil);
			//System.out.println("x: " + xMouse + " y: " + yMouse);
			
			switch(state) {
			case "InMenu":
				gamePencil.setFill(Color.LIGHTGREY);
				renderer.drawMainCanvas(gameCanvas, gamePencil);
				renderer.drawWarningCanvas(warningCanvas, warningPencil);
				break;
			case "InTopDown":
				//Render
				gameVBox.getChildren().clear();				
				gamePencil.setFill(Color.DARKGRAY);
				renderer.drawWalls(gameCanvas, gamePencil, level.getWalls(), camera);
				gamePencil.setFill(Color.DARKBLUE);
				renderer.drawWalls(gameCanvas, gamePencil, level.getMachines(), camera);
				gamePencil.setFill(Color.RED);
				renderer.drawPlayer(gameCanvas, gamePencil, player, camera);
				//Player inputs
				keyboard.keyboardInputsMovement(inputs, player);
				keyboard.keyboardInputMenu(inputs, uiMenuManager, gameVBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, gameManager);
				for(Wall w : level.getWalls()) {
					colission.pushBack(player,w);
				}
				//Is machine being placed?
				keyboard.keyBoardInputPlaceMachine(inputs);
				if(waitTime + currentTime < System.currentTimeMillis()) {
					gamePencil.setFill(Color.CHOCOLATE);
				}else{
					gamePencil.setFill(Color.DARKRED);
				}
				if(keyboard.isBeingPlaced()) {
					double xPos = xMouse;
					double yPos = yMouse;
					xPos -= placeHolder.getWidth()/2;
					yPos -= placeHolder.getHeight()/2;
					
					placeHolder.setX(xPos);
					placeHolder.setY(yPos);
					renderer.drawWall(gameCanvas, gamePencil, placeHolder);
				}
				
				if(mousePressed) {
					System.out.println("Mouse was pressed");
					if(keyboard.isBeingPlaced()) {
						System.out.println("Machine placed");
						double xFinal = placeHolder.getX();
						double yFinal = placeHolder.getY();
						
						boolean canBeAdded = true;
						
						canBeAdded = !colission.AABB(player.getX(), player.getY(), player.getHeight(), player.getHeight(), placeHolder.getX(), placeHolder.getY(), placeHolder.getWidth(), placeHolder.getHeight());
						for(Wall w : level.getWalls()) {
							if(canBeAdded) {
								canBeAdded = !colission.AABB(w.getX(), w.getY(), w.getHeight(), w.getHeight(), placeHolder.getX(), placeHolder.getY(), placeHolder.getWidth(), placeHolder.getHeight());
							}
						}
						for(Wall w : level.getMachines()) {
							if(canBeAdded) {
								canBeAdded = !colission.AABB(w.getX(), w.getY(), w.getHeight(), w.getHeight(), placeHolder.getX(), placeHolder.getY(), placeHolder.getWidth(), placeHolder.getHeight());
							}
						}
						
						if(canBeAdded) {
							level.machineAdd(xFinal,yFinal,placeHolder.getWidth(),placeHolder.getHeight());
						}else {
							mousePressed = false;
							currentTime = System.currentTimeMillis();
							return;
						}
					}
					mousePressed = false;
				}
				
				break;
			case "InUIState":
				gamePencil.setFill(Color.LIGHTGREY);
				renderer.drawMainCanvas(gameCanvas, gamePencil);
				renderer.drawWarningCanvas(warningCanvas, warningPencil);
				keyboard.keyboardInputMenu(inputs, uiMenuManager, gameVBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, gameManager);
			}
			camera.updateCamera(player, gameCanvas);
			
			if(company.getMoneyOfCompany() < 0) {
				writer.resetData(reader);
				company.setMoneyOfCompany(0);
				gameVBox.getChildren().clear();
				gamePane.getChildren().clear();
				VBox gameOver = new VBox();
				gameOver.setAlignment(Pos.TOP_CENTER);
				gameOver.getChildren().add(visual.getBankrupt());
				gameVBox.getChildren().add(buttonManager.getBankrupt());
				gameVBox.setAlignment(Pos.CENTER);
				gamePencil.setFill(Color.BLACK);
				gamePane.getChildren().addAll(gameCanvas,gameOver,gameVBox);
			}
		}));
		gameTimeline.setCycleCount(Timeline.INDEFINITE);
		gameTimeline.play();
	}
}