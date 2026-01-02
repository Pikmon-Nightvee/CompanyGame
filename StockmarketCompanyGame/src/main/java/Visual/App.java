package Visual;

import FileLogic.CreateImportantCSVFiles;
import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import GameLogic.GameManager;
import GameLogic.NextCycleStarted;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	//TODO: Fix TextArea height math, sth is wrong there.
	//TODO: Add the asignment of employees to machines in the asign to (also fixing sth broken) and also adding products
	private int width = 1000;
	private int height = 480;
	
	private ButtonManager buttonManager = new ButtonManager();
	private UIMenuManager uiMenuManager = new UIMenuManager();
	private VisualElementsHolder visualElementsHolder = new VisualElementsHolder();
	private GameManager gameManager = new GameManager();
	private NextCycleStarted nextCycle = new NextCycleStarted();
	
	private Canvas gameCanvas = new Canvas(width,height);
	private GraphicsContext gamePencil = gameCanvas.getGraphicsContext2D();
	private Canvas warningCanvas = new Canvas(width,height - 290);
	private GraphicsContext warningPencil = warningCanvas.getGraphicsContext2D();
	private StackPane gamePane = new StackPane(gameCanvas);
	private VBox gameVBox = new VBox();
	
	private Company company = new Company("", 0.0,"");
	
	private CreateImportantCSVFiles createCSV = new CreateImportantCSVFiles(); //TODO: Extend if you find time, should function as a basic safety net.
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
    	
    	buttonManager.start(gameVBox, visualElementsHolder,warningCanvas,gameCanvas,company,nextCycle,gamePane);
    	
    	uiMenuManager.startUp(gameVBox,buttonManager,visualElementsHolder,writeCSV,readCSV,stage,company,warningCanvas,gamePane,gameCanvas);
    	
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
    	
    	gameManager.loop(gameCanvas, gamePencil, gamePane, warningCanvas, warningPencil, readCSV, writeCSV, gameVBox, company, uiMenuManager, stage, buttonManager, visualElementsHolder);
        
        stage.setScene(scene);
        stage.setTitle("StarUp");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}