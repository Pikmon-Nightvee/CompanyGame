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

	String machine = "";
	private String state = "InMenu";
	private double xMouse = 0;
	private double yMouse = 0;
	private boolean mousePressed = false;
	
	private int width = 0;
	private int height = 0;
	private Wall placeHolder = new Wall(xMouse, yMouse, width, height);
	
	private long waitTime = 350;
	private long currentTime = 0;
	private int scroll = 0;
	
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
	
	public int updateMachine(int scrollAmount, Company company) {
		int maximum;
		int minimum = 0;
		
		switch(company.getCompanyType()) {
		case "Foodtruck":			
			maximum = 1;
			if(scrollAmount < minimum) {
				scrollAmount = maximum;
			}
			if(scrollAmount > maximum) {
				scrollAmount = minimum;
			}
			switch(scrollAmount) {
			case 0:placeHolder.setWidth(50);placeHolder.setHeight(100);machine="Ofen";break;
			case 1:placeHolder.setWidth(50);placeHolder.setHeight(50);machine="Herd";break;
			}
			break;
		case "Craft Buisness":
			maximum = 3;
			if(scrollAmount < minimum) {
				scrollAmount = maximum;
			}
			if(scrollAmount > maximum) {
				scrollAmount = minimum;
			}
			switch(scrollAmount) {
			case 0:placeHolder.setWidth(100);placeHolder.setHeight(75);machine="Fräsmaschine";break;
			case 1:placeHolder.setWidth(75);placeHolder.setHeight(75);machine="Drehmaschine";break;
			case 2:placeHolder.setWidth(120);placeHolder.setHeight(50);machine="Bohrmaschine";break;
			case 3:placeHolder.setWidth(25);placeHolder.setHeight(25);machine="Kreissäge";break;
			}
			break;
		case "EDV-Manager":
			maximum = 0;
			if(scrollAmount < minimum) {
				scrollAmount = maximum;
			}
			if(scrollAmount > maximum) {
				scrollAmount = minimum;
			}
			switch(scrollAmount) {
			case 0:placeHolder.setWidth(75);placeHolder.setHeight(25);machine="PC";break;
			}
			break;
		}
		scroll = scrollAmount;
		System.out.println("Machine: " + machine);
		return scrollAmount;
	}
	
	//Game Loop
	public void loop(Canvas gameCanvas, GraphicsContext gamePencil, StackPane gamePane,Canvas warningCanvas, GraphicsContext warningPencil, ReadCSVFiles reader, WriteCSVFiles writer, VBox gameVBox, Company company, UIMenuManager uiMenuManager, Stage stage, ButtonManager buttonManager, VisualElementsHolder visual, Set<KeyCode> inputs, Player player, LevelHolder level, GameManager gameManager) {
		Timeline gameTimeline = new Timeline();
		
		warningPencil.setFill(Color.BLACK);
		
		buttonManager.getBankrupt().setOnAction(event->{
			uiMenuManager.startUp(gameVBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, gameManager);
			gamePencil.setFill(Color.LIGHTGREY);
			level.getWalls().clear();
			state = "InMenu";
		});
		
		//30FPS Update Loop
		gameTimeline = new Timeline(new KeyFrame(Duration.seconds(0.032), event -> {
			renderer.clearAll(gameCanvas, gamePencil,warningCanvas, warningPencil);
			
			switch(state) {
			case "InMenu":
				gamePencil.setFill(Color.LIGHTGREY);
				renderer.drawMainCanvas(gameCanvas, gamePencil);
				renderer.drawWarningCanvas(warningCanvas, warningPencil);
				break;
			case "InTopDown":
				//Render		
				buttonManager.updateMoney(gameVBox, visual, company);
				writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
				
				gamePencil.setFill(Color.GREEN);
				renderer.drawInteractable(gameCanvas, gamePencil, level.getInteract(), camera);
				gamePencil.setFill(Color.DARKGRAY);
				renderer.drawWalls(gameCanvas, gamePencil, level.getWalls(), camera);
				gamePencil.setFill(Color.DARKBLUE);
				renderer.drawWalls(gameCanvas, gamePencil, level.getMachines(), camera);
				gamePencil.setFill(Color.DARKRED);
				renderer.drawInteractableBlinking(gameCanvas, gamePencil, level.getBlinking(), camera);
				gamePencil.setFill(Color.RED);
				renderer.drawPlayer(gameCanvas, gamePencil, player, camera);
				//Player inputs
				keyboard.keyboardInputsMovement(inputs, player);
				keyboard.keyboardInputMenu(inputs, uiMenuManager, gameVBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, gameManager);
				for(Wall w : level.getWalls()) {
					colission.pushBack(player,w);
				}
				for(Wall m : level.getMachines()) {
					colission.pushBack(player,m);
				}
				//Is machine being placed?
				keyboard.keyBoardInputPlaceMachine(inputs,placeHolder,reader.readMachines("MachineBought.csv", company));
				if(waitTime + currentTime < System.currentTimeMillis()) {
					gamePencil.setFill(Color.CHOCOLATE);
				}else{
					gamePencil.setFill(Color.DARKRED);
				}
				if(keyboard.isBeingPlaced()) {
					if(reader.readMachines("MachineBought.csv", company).isEmpty()) {
						placeHolder.setWidth(0);
						placeHolder.setHeight(0);
						return;
					}
					
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
						double xFinal = placeHolder.getX()-camera.getX();
						double yFinal = placeHolder.getY()-camera.getY();
						
						boolean canBeAdded = false;
						
						for(Wall w : level.getPlaceable()) {
							if(!canBeAdded) { 
								canBeAdded = colission.AABB(w.getX(), w.getY(), w.getHeight(), w.getHeight(), xFinal, yFinal, placeHolder.getWidth(), placeHolder.getHeight());
							}
						}
						if(canBeAdded) {
							canBeAdded = !colission.AABB(player.getX(), player.getY(), player.getWidth(), player.getHeight(), xFinal, yFinal, placeHolder.getWidth(), placeHolder.getHeight());
							if(canBeAdded) {
								if(machine.isBlank()) {
									canBeAdded = false;
								}else {
									canBeAdded = amountLowEnough(reader,machine,company);
								}
							}
							if(canBeAdded) {
								for(Wall w : level.getMachines()) {
									if(canBeAdded) {
										System.out.println("Placeholder x,y,width,height: " + xFinal + "," + yFinal + "," + placeHolder.getWidth() + "," + placeHolder.getHeight() + " Machine x,y,width,height: " + w.getX() + "," + w.getY() + "," + w.getWidth() + "," + w.getHeight());
										canBeAdded = !colission.AABB(w.getX(), w.getY(), w.getWidth(), w.getHeight(), xFinal, yFinal, placeHolder.getWidth(), placeHolder.getHeight());
									}
								}
							}else {
								System.out.println("Colission with player");
							}
							if(canBeAdded) {
								for(Wall w : level.getWalls()) {
									if(canBeAdded) {
										canBeAdded = !colission.AABB(w.getX(), w.getY(), w.getWidth(), w.getHeight(), xFinal, yFinal, placeHolder.getWidth(), placeHolder.getHeight());
									}else {
										System.out.println("Colission with a wall");
									}
								}
							}else {
								System.out.println("Colission with a machine");
							}
						}else {
							System.out.println("Out of bounce");
						}
						if(canBeAdded) {
							level.machineAdd(xFinal,yFinal,placeHolder.getWidth(),placeHolder.getHeight());
							String machine = writer.machineNamePlacedAdd(scroll, company, reader);
							writer.coordinatesMachineSafe(placeHolder,xFinal,yFinal,machine);
						}else {
							mousePressed = false;
							currentTime = System.currentTimeMillis();
							return;
						}
					}
					mousePressed = false;
				}
				for(InteractableObject i : level.getInteract()) {
					if(colission.AABB(i.getX(), i.getY(), i.getWidth(), i.getHeight(), player.getX(), player.getY(), player.getWidth(), player.getHeight())){
						keyboard.keyBoardInputPlacedMachine(inputs, level.getMachines(), i, level, writer, reader, level.getBlinking(), company);
					}
				}
				for(InteractableObject i : level.getToRemove()) {
					level.getInteract().remove(i);
					level.getBlinking().remove(i);
				}
				level.getToRemove().clear();
				break;
			case "InUIState":
				gamePencil.setFill(Color.LIGHTGREY);
				renderer.drawMainCanvas(gameCanvas, gamePencil);
				renderer.drawWarningCanvas(warningCanvas, warningPencil);
				keyboard.keyboardInputMenu(inputs, uiMenuManager, gameVBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, gameManager);
				break;
			case "GameOver":
				gamePencil.setFill(Color.BLACK);
				renderer.drawMainCanvas(gameCanvas, gamePencil);
				break;
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
				state = "GameOver";
			}
		}));
		gameTimeline.setCycleCount(Timeline.INDEFINITE);
		gameTimeline.play();
	}
	public Wall getPlaceHolder() {
		return placeHolder;
	}

    private boolean amountLowEnough(ReadCSVFiles reader, String machineName, Company company) {
    	ArrayList<String[]> amountCheck = reader.machinesTopDownGet("MachinesPlaced.csv");
    	ArrayList<Machine> machines = reader.readMachines("MachineBought.csv", company);
    	
    	int amountBought = 0;
		int amountPlaced = 0;
    	for(String[] s : amountCheck) {
    		if(s[0].equals(machineName)) {
    			amountPlaced = Integer.parseInt(s[1]);
    		}
    	}
    	for(Machine m : machines) {
    		if(m.getName().equals(machineName)) {
    			amountBought = m.getAmount();
    		}
    	}
    	
    	if(amountPlaced < amountBought) {
    		System.out.println("true");
    		return true;
    	}else {
    		System.out.println("False");
    		return false;
    	}
    }
}