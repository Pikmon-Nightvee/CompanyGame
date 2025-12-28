package Visual;

import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIMenuManager {
	private Button start = new Button("New game");
	private Button load = new Button("Load game");
	private Button quit = new Button("Quit game");
	
	private void CSSNoAddAmount(Button button) {
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	
	public void startUp(VBox vBox, ButtonManager buttonManager, VisualElementsHolder visual, WriteCSVFiles writer, ReadCSVFiles reader, Stage stage, Company company, Canvas warningCanvas) {
		vBox.getChildren().add(start);
		if(reader.gameAlreadyStarted()) {
			vBox.getChildren().add(load);
		}
		vBox.getChildren().add(quit);
		
		CSSNoAddAmount(start);
		CSSNoAddAmount(load);
		CSSNoAddAmount(quit);
		
		start.setOnAction(event->{
			vBox.getChildren().clear();
			writer.resetData(reader);
			buttonManager.addStartUpScreen(visual, vBox,reader);
		});
		load.setOnAction(event->{
			vBox.getChildren().clear();
			company.setName(reader.gameAlreadyPlayedCompanyData().getName());
			company.setMoneyOfCompany(reader.gameAlreadyPlayedCompanyData().getMoneyOfCompany());
			company.setReputation(reader.gameAlreadyPlayedCompanyData().getReputation());
			company.setCompanyType(reader.gameAlreadyPlayedCompanyData().getCompanyType());
			buttonManager.loadGame(visual, vBox, company, warningCanvas, reader);
		});
		quit.setOnAction(event->{
			stage.close();
		});
	}
}