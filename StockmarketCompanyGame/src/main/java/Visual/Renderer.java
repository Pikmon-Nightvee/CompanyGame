package Visual;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class Renderer {
	public void drawMainCanvas(Canvas canvas, GraphicsContext pencil) {
		pencil.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}

	public void drawWarningCanvas(Canvas canvas, GraphicsContext pencil) {
		pencil.fillRect(0, 20, canvas.getWidth(), canvas.getHeight());
	}
}
