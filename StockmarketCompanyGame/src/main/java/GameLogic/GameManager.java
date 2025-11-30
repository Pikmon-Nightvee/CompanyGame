package GameLogic;

import java.util.HashSet;
import java.util.Set;

import Visual.ButtonManager;
import Visual.Renderer;
import Visual.VisualElementsHolder;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Duration;

public class GameManager {
	private Renderer renderer = new Renderer();
	
	//Game Loop
	public void loop(Canvas gameCanvas, GraphicsContext gamePencil, StackPane gamePane,Canvas warningCanvas, GraphicsContext warningPencil) {
		Timeline gameTimeline = new Timeline();

		gamePencil.setFill(Color.LIGHTGREY);
		warningPencil.setFill(Color.BLACK);
		
		//30FPS Update Loop
		gameTimeline = new Timeline(new KeyFrame(Duration.seconds(0.0032), event -> {
			renderer.drawMainCanvas(gameCanvas, gamePencil);
			renderer.drawWarningCanvas(warningCanvas, warningPencil);
		}));
		gameTimeline.setCycleCount(Timeline.INDEFINITE);
		gameTimeline.play();
	}
}