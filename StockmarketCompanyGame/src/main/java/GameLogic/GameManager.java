package GameLogic;

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
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class GameManager {
	private Renderer renderer = new Renderer();
	
	//Game Loop
	public void loop(Canvas gameCanvas, GraphicsContext gamePencil, StackPane gamePane,Canvas warningCanvas, GraphicsContext warningPencil, ReadCSVFiles reader, WriteCSVFiles writer, VBox gameVBox, Company company, UIMenuManager uiMenuManager, Stage stage, ButtonManager buttonManager, VisualElementsHolder visual) {
		Timeline gameTimeline = new Timeline();

		gamePencil.setFill(Color.LIGHTGREY);
		warningPencil.setFill(Color.BLACK);
		
		buttonManager.getBankrupt().setOnAction(event->{
			uiMenuManager.startUp(gameVBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas);
			gamePencil.setFill(Color.LIGHTGREY);
		});
		
		//30FPS Update Loop
		gameTimeline = new Timeline(new KeyFrame(Duration.seconds(0.032), event -> {
			renderer.drawMainCanvas(gameCanvas, gamePencil);
			renderer.drawWarningCanvas(warningCanvas, warningPencil);

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