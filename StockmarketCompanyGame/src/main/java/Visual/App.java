package Visual;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import ExternalResources.GraphicsManager;
import ExternalResources.MusicManager;
import ExternalResources.SoundeffectManager;
import FileLogic.CreateImportantCSVFiles;
import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import GameLogic.GameManager;
import GameLogic.LevelHolder;
import GameLogic.Machine;
import GameLogic.NextCycleStarted;
import GameLogic.Player;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
//Muss fertig werden am 21.02.2026
/**
 * JavaFX App
 */
public class App extends Application {
	private int width = 1000;
	private int height = 480;
	private int scrollAmount = -1;
	
	private ButtonManager buttonManager = new ButtonManager();
	private UIMenuManager uiMenuManager = new UIMenuManager();
	private VisualElementsHolder visualElementsHolder = new VisualElementsHolder();
	private GameManager gameManager = new GameManager();
	private NextCycleStarted nextCycle = new NextCycleStarted();
	
	private final Set<KeyCode> inputs = new HashSet<>();
	private Player player = new Player(0,0,50,50,10);
	
	private Canvas gameCanvas = new Canvas(width,height);
	private GraphicsContext gamePencil = gameCanvas.getGraphicsContext2D();
	private Canvas warningCanvas = new Canvas(width,height - 290);
	private GraphicsContext warningPencil = warningCanvas.getGraphicsContext2D();
	private StackPane gamePane = new StackPane(gameCanvas);
	private VBox gameVBox = new VBox();
	private LevelHolder level = new LevelHolder();
	
	private Company company = new Company("", 0.0,"");
	
	private CreateImportantCSVFiles createCSV = new CreateImportantCSVFiles();
	private ReadCSVFiles readCSV = new ReadCSVFiles();
	private WriteCSVFiles writeCSV = new WriteCSVFiles();

	private SoundeffectManager sfx = new SoundeffectManager();
	private MusicManager music = new MusicManager();
	private GraphicsManager graphic = new GraphicsManager();
	
    @Override
    public void start(Stage stage) {
    	boolean error = false;
    	
    	error = createCSV.createDirectories();
    	error = createCSV.createImportantGameStateFiles();
    	error = createCSV.createFilesEmployees();
    	error = createCSV.createFilesMachine();
    	error = createCSV.createFilesProduct();
    	error = createCSV.createFilesResource();
    	error = createCSV.createFilesCoordinates();
    	
    	if(error) {
    		System.out.println("Important Files and Directorys missing");
    		writeCSV.resetData(readCSV);
    	}else {
    		System.out.println("All Important Files and Directorys there");
    	}
    	
    	sfx.loadSfx();
    	music.loadMusic();
    	
    	graphic.loadMenu();
    	graphic.loadUI();
    	graphic.loadTopDown();
    	uiMenuManager.imageLoader(graphic);
    	buttonManager.imageLoader(graphic);
    	visualElementsHolder.loadImage(graphic,sfx,uiMenuManager);
    	
    	visualElementsHolder.changeTextAreas();
    	
    	stage.getIcons().add(graphic.getIcon());
    	
    	nextCycle.readTimeStart();
    	
    	Scene scene = new Scene(gamePane, width, height);
    	
    	buttonManager.start(gameVBox, visualElementsHolder,warningCanvas,gameCanvas,company,nextCycle,gamePane,level,player,gameManager,sfx,uiMenuManager,warningPencil);
    	
    	uiMenuManager.startUp(gameVBox,buttonManager,visualElementsHolder,writeCSV,readCSV,stage,company,warningCanvas,gamePane,gameCanvas,level,player,gameManager,sfx);
    	
    	gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
    		gameCanvas.setWidth(newVal.doubleValue());
    		warningCanvas.setWidth(newVal.doubleValue());
    	});
    	gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
    		gameCanvas.setHeight(newVal.doubleValue());
    		warningCanvas.setHeight((newVal.doubleValue()- buttonManager.getHeight()-20));

    		buttonManager.changeTextAreaSize(gameCanvas, visualElementsHolder, gameVBox);
    	});
    	
    	scene.setOnKeyPressed(event->{
    		inputs.add(event.getCode());		
    	});
    	scene.setOnKeyReleased(event->{
    		inputs.remove(event.getCode());
    	});

    	scene.setOnMousePressed(event->{
    		gameManager.mousePressed(true);
    	});
    	scene.setOnMouseMoved(event->{
    		gameManager.updateMouseCoordinates(event.getX(), event.getY());
    	});
    	scene.setOnMouseReleased(event->{
    		gameManager.updateMouseCoordinates(event.getX(), event.getY());
    	});
    	scene.setOnScroll(event->{
    		sfx.playSelect(uiMenuManager.isSfxOn());
    		
    		ArrayList<Machine> allMachine = readCSV.readMachines("MachinesData.csv",company);
    		ArrayList<Machine> bought = readCSV.readMachines("MachineBought.csv",company);
    		int increasedTotal = 0;
    		boolean notFound = true;
    		if (event.getDeltaY() > 0) {
    			for(Machine machineAll : allMachine) {
	    			if(notFound) {
    					for(Machine machineBought : bought) {
	    					if(machineBought.getName().equals(machineAll.getName())) {
    	    					boolean result = amountLowEnough(readCSV,machineBought.getName(),machineBought.getAmount());
    	    					System.out.println("More Bought than Placed?: "+result+" Scroll right?: "+(increasedTotal < scrollAmount));
	    						if(increasedTotal > scrollAmount && result) {
	    							scrollAmount = increasedTotal;
	    							notFound = false;
	    						}
	    					}
	    				}
	    				increasedTotal++;
	    				System.out.println("Iteration: "+increasedTotal+" scrollAmount: "+scrollAmount);
	    			}
    			}
    			//At the top
    			if(notFound) {
    				System.out.println("Nothing Found");
    				increasedTotal = 0;
    				int setScroll=0;
        	    	for(Machine machineAll : allMachine) {
	        			if(notFound) {
        	    			for(Machine machineBought : bought) {
	    	    				if(machineBought.getName().equals(machineAll.getName())) {
	    	    					boolean result = amountLowEnough(readCSV,machineBought.getName(),machineBought.getAmount());
	    	    					System.out.println("More Bought than Placed?: "+result+" Scroll right?: "+(increasedTotal < scrollAmount));
	    	    					if(increasedTotal < scrollAmount && result) {
	    	    						setScroll = increasedTotal;
	    	    						notFound = false;
	    	    					}
	    	    				}
	    	    			}
	    	    			increasedTotal++;
	    	    			System.out.println("Iteration: "+increasedTotal+" scrollAmount: "+scrollAmount);
	        			}
    	    		}
        	    	if(!notFound) {
        	    		scrollAmount = setScroll;
        	    	}
    			}
    	        System.out.println("Scrolled up");
    	    } else if (event.getDeltaY() < 0) {
    	    	int setScroll=0;
    	    	for(Machine machineAll : allMachine) {
    				for(Machine machineBought : bought) {
	    				if(machineBought.getName().equals(machineAll.getName())) {
	    					boolean result = amountLowEnough(readCSV,machineBought.getName(),machineBought.getAmount());
	    					System.out.println("More Bought than Placed?: "+result+" Scroll right?: "+(increasedTotal < scrollAmount));
	    					if(increasedTotal < scrollAmount && result) {
	    						setScroll = increasedTotal;
	    						notFound = false;
	    					}
	    				}
	    			}
	    			increasedTotal++;
	    			System.out.println("Iteration: "+increasedTotal+" scrollAmount: "+scrollAmount);
	    		}
    	    	if(!notFound) {
    	    		scrollAmount = setScroll;
    	    	}
    	    	//At the bottom
    	    	if(notFound) {
    				System.out.println("Nothing Found");
    	    		increasedTotal = 0;
    				for(Machine machineAll : allMachine) {
        				for(Machine machineBought : bought) {
    	    				if(machineBought.getName().equals(machineAll.getName())) {
    	    					boolean result = amountLowEnough(readCSV,machineBought.getName(),machineBought.getAmount());
    	    					System.out.println("More Bought than Placed?: "+result+" Scroll right?: "+(increasedTotal < scrollAmount));
    	    					if(increasedTotal > scrollAmount && result) {
    	    						scrollAmount = increasedTotal;
    	    						notFound = false;
    	    					}
    	    				}
        				}
    	    			increasedTotal++;
    	    			System.out.println("Iteration: "+increasedTotal+" scrollAmount: "+scrollAmount);
    				}
    	    	}
    	        System.out.println("Scrolled down");
    	    }
    		if(!notFound) {
    			scrollAmount = gameManager.updateMachine(scrollAmount, company);
    			System.out.println("Scroll amount: " + scrollAmount);
    		}
    	});
    	gameManager.loop(gameCanvas, gamePencil, gamePane, warningCanvas, warningPencil, readCSV, writeCSV, gameVBox, company, uiMenuManager, stage, buttonManager, visualElementsHolder, inputs, player, level, gameManager, sfx, music, graphic);
        
        stage.setScene(scene);
        stage.setTitle("StarUp");
        stage.show();
    }

    private boolean amountLowEnough(ReadCSVFiles reader, String machineName, int amountBought) {
    	ArrayList<String[]> amountCheck = reader.machinesTopDownGet("MachinesPlaced.csv");
		int amountPlaced = -1;
    	for(String[] s : amountCheck) {
    		if(s[0].equals(machineName)) {
    			amountPlaced = Integer.parseInt(s[1]);
    		}
    	}
		System.out.println("Machine name: "+machineName+" amountBought: "+amountBought+" amountPlaced: "+amountPlaced);
    	if(amountPlaced < amountBought) {
    		System.out.println("True");
    		return true;
    	}else {
    		System.out.println("False");
    		return false;
    	}
    }
    
    public static void main(String[] args) {
        launch();
    }
}