package GameLogic;

import java.util.Set;

import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import Visual.ButtonManager;
import Visual.UIMenuManager;
import Visual.VisualElementsHolder;
import javafx.scene.canvas.Canvas;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class KeyboardManager {
	public void keyboardInputs(Set<KeyCode> inputs, Player player) {
		if(inputs.contains(KeyCode.A)) {
			player.moveOnX(true);
		}
		if(inputs.contains(KeyCode.D)) {
			player.moveOnX(false);
		}
		if(inputs.contains(KeyCode.W)) {
			player.moveOnY(true);
		}
		if(inputs.contains(KeyCode.S)) {
			player.moveOnY(false);
		}
	}
	
	public void keyboardInputMenu(Set<KeyCode> inputs, UIMenuManager uiMenu, VBox vBox, ButtonManager buttonManager, VisualElementsHolder visual, WriteCSVFiles writer, ReadCSVFiles reader, Stage stage, Company company, Canvas warningCanvas, StackPane gamePane, Canvas gameCanvas, LevelHolder level, Player player, GameManager game) {
		if(inputs.contains(KeyCode.E)) {
			uiMenu.loadMenu(vBox, buttonManager, visual, writer, reader, stage, company, warningCanvas, gamePane, gameCanvas, level, player, game);
			game.updateState("InMenu");
		}
	}
}
