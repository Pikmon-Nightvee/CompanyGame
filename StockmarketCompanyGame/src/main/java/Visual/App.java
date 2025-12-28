package Visual;

import FileLogic.CreateImportantCSVFiles;
import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import GameLogic.GameManager;
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
	private int width = 1000;
	private int height = 480;
	
	private ButtonManager buttonManager = new ButtonManager();
	private UIMenuManager uiMenuManager = new UIMenuManager();
	private VisualElementsHolder visualElementsHolder = new VisualElementsHolder();
	private GameManager gameManager = new GameManager();
	
	private Canvas gameCanvas = new Canvas(width,height);
	private GraphicsContext gamePencil = gameCanvas.getGraphicsContext2D();
	private Canvas warningCanvas = new Canvas(width,height - 290);
	private GraphicsContext warningPencil = warningCanvas.getGraphicsContext2D();
	private StackPane gamePane = new StackPane(gameCanvas);
	private VBox gameVBox = new VBox();
	
	private Company company = new Company("", 0.0,"");
	
	private CreateImportantCSVFiles createCSV = new CreateImportantCSVFiles();
	private ReadCSVFiles readCSV = new ReadCSVFiles();
	private WriteCSVFiles writeCSV = new WriteCSVFiles();
	
    @Override
    public void start(Stage stage) {
    	createCSV.createImportantGameStateFiles(readCSV,writeCSV);
    	createCSV.createFilesEmployees(readCSV,writeCSV);
    	
    	Scene scene = new Scene(gamePane, width, height);
    	
    	buttonManager.start(gameVBox, visualElementsHolder,warningCanvas,gameCanvas,company);
    	
    	uiMenuManager.startUp(gameVBox,buttonManager,visualElementsHolder,writeCSV,readCSV,stage,company,warningCanvas,gamePane);
    	
    	gameVBox.setAlignment(Pos.CENTER);
    	
    	gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
    		gameCanvas.setWidth(newVal.doubleValue());
    		warningCanvas.setWidth(newVal.doubleValue());
    	});
    	gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
    		gameCanvas.setHeight(newVal.doubleValue());
    		warningCanvas.setHeight((newVal.doubleValue()- buttonManager.getHeight()));
    		System.out.println(newVal.doubleValue());
    		String text = "";
    		
    		if(buttonManager.isInEmployeeManager()) {
    			text = "Employed";
    		}else if(buttonManager.isInUnemployedManager()) {
    			text = "Unemployed";
    		}
    		
    		buttonManager.changeTextAreaSize(gameCanvas, visualElementsHolder,text);
    	});
    	
        gameManager.loop(gameCanvas,gamePencil,gamePane,warningCanvas,warningPencil);
        
        stage.setScene(scene);
        stage.setTitle("StarUp");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}