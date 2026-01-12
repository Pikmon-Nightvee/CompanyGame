package Visual;

import java.util.HashSet;
import java.util.Set;

import FileLogic.CreateImportantCSVFiles;
import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import GameLogic.GameManager;
import GameLogic.LevelHolder;
import GameLogic.NextCycleStarted;
import GameLogic.Player;
import javafx.application.Application;
import javafx.geometry.Pos;
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
	
    @Override
    public void start(Stage stage) {
    	boolean error = false;
    	
    	error = createCSV.createDirectories();
    	error = createCSV.createImportantGameStateFiles(readCSV,writeCSV);
    	error = createCSV.createFilesEmployees(readCSV,writeCSV);
    	error = createCSV.createFilesMachine(readCSV, writeCSV);
    	error = createCSV.createFilesProduct(readCSV, writeCSV);
    	error = createCSV.createFilesResource(readCSV, writeCSV);
    	
    	if(error) {
    		System.out.println("Important Files and Directorys missing");
    		writeCSV.resetData(readCSV);
    	}else {
    		System.out.println("All Important Files and Directorys there");
    	}
    	nextCycle.readTimeStart();
    	
    	Scene scene = new Scene(gamePane, width, height);
    	
    	buttonManager.start(gameVBox, visualElementsHolder,warningCanvas,gameCanvas,company,nextCycle,gamePane,level,player,gameManager);
    	
    	uiMenuManager.startUp(gameVBox,buttonManager,visualElementsHolder,writeCSV,readCSV,stage,company,warningCanvas,gamePane,gameCanvas,level,player,gameManager);
    	
    	gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
    		gameCanvas.setWidth(newVal.doubleValue());
    		warningCanvas.setWidth(newVal.doubleValue());
    	});
    	gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
    		gameCanvas.setHeight(newVal.doubleValue());
    		warningCanvas.setHeight((newVal.doubleValue()- buttonManager.getHeight()-20));
    		System.out.println(newVal.doubleValue());

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
    	gameManager.loop(gameCanvas, gamePencil, gamePane, warningCanvas, warningPencil, readCSV, writeCSV, gameVBox, company, uiMenuManager, stage, buttonManager, visualElementsHolder, inputs, player, level, gameManager);
        
        stage.setScene(scene);
        stage.setTitle("StarUp");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}