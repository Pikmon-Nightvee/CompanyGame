package Visual;

import java.util.HashSet;
import java.util.Set;

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
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX App
 */
public class App extends Application {
	private int width = 1000;
	private int height = 480;
	
	private ButtonManager buttonManager = new ButtonManager();
	private VisualElementsHolder visualElementsHolder = new VisualElementsHolder();
	private GameManager gameManager = new GameManager();
	
	private Canvas gameCanvas = new Canvas(width,height);
	private GraphicsContext gamePencil = gameCanvas.getGraphicsContext2D();
	private Canvas warningCanvas = new Canvas(width,height - 290);
	private GraphicsContext warningPencil = warningCanvas.getGraphicsContext2D();
	private StackPane gamePane = new StackPane(gameCanvas);
	private VBox gameVBox = new VBox();
	
	private Company company = new Company(null, 0.0);
	
	private CreateImportantCSVFiles createCSV = new CreateImportantCSVFiles();
	private ReadCSVFiles readCSV = new ReadCSVFiles();
	private WriteCSVFiles writeCSV = new WriteCSVFiles();
	
    @Override
    public void start(Stage stage) {
    	createCSV.createImportantGameStateFiles(readCSV,writeCSV);
    	createCSV.createFilesEmployees(readCSV,writeCSV);
    	
    	gamePane.getChildren().addAll(gameVBox);
    	visualElementsHolder.insertDifficulty();
    	visualElementsHolder.insertCycleDates();
    	visualElementsHolder.insertSpecifications();
    	visualElementsHolder.insertTypes();
    	
    	Scene scene = new Scene(gamePane, width, height);
    	
    	gameVBox.getChildren().addAll(visualElementsHolder.getEnterName(),visualElementsHolder.getInsertName());
    	visualElementsHolder.start();
    	buttonManager.start(gameVBox, visualElementsHolder,warningCanvas,company);
    	
    	visualElementsHolder.getSelectDifficulty().setValue("Easy");
    	visualElementsHolder.getSelectCycleAmount().setValue("Day");
    	gameVBox.getChildren().addAll(visualElementsHolder.getDifficulty(),visualElementsHolder.getSelectDifficulty());
    	
    	gameVBox.getChildren().addAll(visualElementsHolder.getChooseCompanyType(), visualElementsHolder.getSelectCompanyType(), visualElementsHolder.getChooseCompanyWork(), visualElementsHolder.getSelectCompanySpecification());
    	
    	buttonManager.addButtonsStart(gameVBox);
    	
    	gameVBox.setAlignment(Pos.CENTER);
    	
    	gamePane.widthProperty().addListener((obs, oldVal, newVal) -> {
    		gameCanvas.setWidth(newVal.doubleValue());
    		warningCanvas.setWidth(newVal.doubleValue());
    	});
    	gamePane.heightProperty().addListener((obs, oldVal, newVal) -> {
    		gameCanvas.setHeight(newVal.doubleValue());
    		warningCanvas.setHeight((newVal.doubleValue()- buttonManager.getHeight()));
    	});
    	
        gameManager.loop(gameCanvas,gamePencil,gamePane,warningCanvas,warningPencil);
        
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}