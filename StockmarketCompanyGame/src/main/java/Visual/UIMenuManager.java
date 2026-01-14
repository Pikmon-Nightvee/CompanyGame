package Visual;

import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import GameLogic.GameManager;
import GameLogic.LevelHolder;
import GameLogic.Player;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class UIMenuManager {
	private Button start = new Button("New game");
	private Button load = new Button("Load game");
	private Button quit = new Button("Quit game");

	private Button backToMainMenu = new Button("To main menu");
	private Button openUIGame = new Button("To UI view");
	private Button openTopDown = new Button("To top down view");
	
	private VBox vBox = new VBox();
	private Label nameLabel = new Label("StarUp");
	
	private void CSSNoAddAmount(Button button) {
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	private void CSSLabelNoAddAmount(Label label) {
		label.setStyle("-fx-font-size: 100px;-fx-font-weight: bold;");
	}

	private void changeGameState(String gameState, String insert) {
		gameState = insert;
	}
	
	public void loadMenu(VBox vBox, ButtonManager buttonManager, VisualElementsHolder visual, WriteCSVFiles writer, ReadCSVFiles reader, Stage stage, Company company, Canvas warningCanvas, StackPane gamePane, Canvas gameCanvas, LevelHolder level, Player player, GameManager game) {
		vBox.getChildren().clear();
		gamePane.getChildren().clear();
		visual.getSelectDifficulty().getItems().clear();
		visual.getSelectCompanyType().getItems().clear();
		visual.getSelectCompanySpecification().getItems().clear();
		visual.getSelectCycleAmount().getItems().clear();
		
		vBox.getChildren().add(openTopDown);
		vBox.getChildren().add(openUIGame);
		vBox.getChildren().add(backToMainMenu);
		vBox.getChildren().add(quit);
		
		CSSNoAddAmount(openTopDown);
		CSSNoAddAmount(openUIGame);
		CSSNoAddAmount(backToMainMenu);
		CSSNoAddAmount(quit);
		
		vBox.setAlignment(Pos.CENTER);
		gamePane.getChildren().add(gameCanvas);
		gamePane.getChildren().add(vBox);
		
		openTopDown.setOnAction(event->{
			vBox.getChildren().clear();
			
			HBox hBox = new HBox();
			Label name = new Label("Name of Company: ");
			Label money = new Label(" | Money of the Company (€): ");
			Label companyType = new Label(" | Company Type: ");
			Label companySpecification = new Label(company.getCompanyType());
			visual.CSSLabel(money);
			visual.CSSLabelNoAddAmount(name);
			visual.CSSLabelNoAddAmount(companyType);
			visual.CSSLabelNoAddAmount(companySpecification);
			
			visual.getNameOfCompany().setText(company.getName());
			
			hBox.getChildren().addAll(name,visual.getNameOfCompany(),money,visual.getMoneyOfCompany(),companyType,companySpecification);
			hBox.setAlignment(Pos.TOP_CENTER);
			gamePane.getChildren().add(hBox);
			
			game.updateState("InTopDown");
		});
		openUIGame.setOnAction(event->{
			vBox.getChildren().clear();
			game.updateState("InUIState");
			buttonManager.loadGame(visual, vBox, company, warningCanvas, reader);
			buttonManager.changeTextAreaSize(gameCanvas, visual, vBox);
		});
		backToMainMenu.setOnAction(event->{
			level.getWalls().clear();
			game.updateState("InMenu");
			startUp(vBox,buttonManager,visual,writer,reader,stage,company,warningCanvas,gamePane,gameCanvas,level,player,game);
		});
		quit.setOnAction(event->{
			stage.close();
		});
	}
	
	public void startUp(VBox vBox, ButtonManager buttonManager, VisualElementsHolder visual, WriteCSVFiles writer, ReadCSVFiles reader, Stage stage, Company company, Canvas warningCanvas, StackPane gamePane, Canvas gameCanvas, LevelHolder level, Player player, GameManager game) {
		vBox.getChildren().clear();
		this.vBox.getChildren().clear();
		gamePane.getChildren().clear();
		visual.getSelectDifficulty().getItems().clear();
		visual.getSelectCompanyType().getItems().clear();
		visual.getSelectCompanySpecification().getItems().clear();
		visual.getSelectCycleAmount().getItems().clear();
		
		vBox.getChildren().add(start);
		if(reader.gameAlreadyStarted()) {
			vBox.getChildren().add(load);
		}
		vBox.getChildren().add(quit);
		
		CSSNoAddAmount(start);
		CSSNoAddAmount(load);
		CSSNoAddAmount(quit);
		CSSLabelNoAddAmount(nameLabel);
		
		this.vBox.setAlignment(Pos.TOP_CENTER);
		vBox.setAlignment(Pos.CENTER);
		this.vBox.getChildren().add(nameLabel);
		gamePane.getChildren().add(gameCanvas);
		gamePane.getChildren().add(this.vBox);
		gamePane.getChildren().add(vBox);
		
		start.setOnAction(event->{
			this.vBox.getChildren().clear();
			vBox.getChildren().clear();
			writer.resetData(reader);
			buttonManager.addStartUpScreen(visual, vBox,reader,company);
			buttonManager.changeTextAreaSize(gameCanvas, visual, vBox);
			level.getMachines().clear();
			level.getPlaceable().clear();
			level.getWalls().clear();
		});
		load.setOnAction(event->{
			this.vBox.getChildren().clear();
			vBox.getChildren().clear();
			level.getWalls().clear();
			level.getMachines().clear();
			level.getPlaceable().clear();
			level.getToRemove().clear();
			level.getInteract().clear();
			game.updateState("InUIState");
			company.setName(reader.gameAlreadyPlayedCompanyData().getName());
			company.setMoneyOfCompany(reader.gameAlreadyPlayedCompanyData().getMoneyOfCompany());
			company.setReputation(reader.gameAlreadyPlayedCompanyData().getReputation());
			company.setCompanyType(reader.gameAlreadyPlayedCompanyData().getCompanyType());
			buttonManager.loadGame(visual, vBox, company, warningCanvas, reader);
			buttonManager.changeTextAreaSize(gameCanvas, visual, vBox);
			level.loadLevel(company.getCompanyType(), player);
			level.machinesLoad(reader.machinesPlaced());
			level.interactLoad(level.getMachines());
		});
		quit.setOnAction(event->{
			stage.close();
		});
	}
}