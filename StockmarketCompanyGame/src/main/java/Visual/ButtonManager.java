package Visual;

import GameLogic.Company;
import GameLogic.ErrorMessageHandler;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ButtonManager {
	private ErrorMessageHandler errorMessage = new ErrorMessageHandler();
	
	private Button resources = new Button("Resources");
	private Button equipment = new Button("Equipment");
	private Button employ = new Button("Employ");
	private Button nextCycle = new Button("Next Cycle"); 

	private Button startButton = new Button("Start the game"); 	

	private Button employeeManager = new Button("Manage Employees"); 
	private Button goBack = new Button("Go Back");
	
	private int amount = 0;
	private int height = 270;
	private double moneyStart = 0.0;
	
	private enum companyType{Foodtruck,EDVManager,CraftBuisness};
	
	private void CSS(Button button) {
		amount += 1;
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	
	private void CSSNonFungible(Button button) {
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	
	public void start(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas, Company company) {
		CSS(resources);
		CSS(employ);
		CSS(nextCycle);
		CSS(startButton);
		CSS(employeeManager);
		CSS(equipment);
		CSSNonFungible(goBack);
		
		action(vBox, visual, warningCanvas, company);
	}
	
	public void action(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas, Company company) {
		startButton.setOnAction(event -> {
			boolean errorOccured = false;
			if(visual.getInsertName().getText().isBlank()) {
				errorMessage.errorMessageText(visual.getInsertName(), vBox);
				errorOccured = true;
			}
			if(visual.getSelectCompanyType().getValue() == null) {
				errorMessage.errorMessageComboBox(visual.getSelectCompanyType(), vBox);
				errorOccured = true;
			}
			if(visual.getSelectCompanySpecification().getValue() == null) {
				errorMessage.errorMessageComboBox(visual.getSelectCompanySpecification(), vBox);
				errorOccured = true;
			}
			if(errorOccured) {
				return;
			}
			
			String difficulty = visual.getSelectDifficulty().getValue();
			switch(difficulty) {
			case "Easy": moneyStart = 15000.00; break;
			case "Normal": moneyStart = 7500.00; break;
			case "Hard": moneyStart = 3000.00; break;
			}
			
			if(visual.getSelectCompanyType().getValue().equals("GmbH")) {
				company.setReputation(company.getReputation() + 25);
				moneyStart -= 2000;
			}
			
			vBox.getChildren().clear();
			String companyNameSet = "";
			companyNameSet = visual.getInsertName().getText();
			companyNameSet = companyNameSet + " " + visual.getSelectCompanyType().getValue();
			visual.setNameOfCompany(companyNameSet);

			company.setMoneyOfCompany(moneyStart);
			company.setName(companyNameSet);
			
			Label money = new Label();
			visual.CSSLabel(money);
			money.setText(String.valueOf(company.getMoneyOfCompany()));
			visual.setMoneyOfCompany(money);
			
			startUpMain(vBox,visual);
			startUpCompany(vBox,visual,warningCanvas);
			System.out.println(companyNameSet);
			height = (amount + visual.getAmount()) * 20 + 10;
			System.out.println(height);
			System.out.println(company.toString());
		});
		
		resources.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
		});
		
		employeeManager.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
		});
		
		employ.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
		});
		
		equipment.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
		});
		
		goBack.setOnAction(event->{
			vBox.getChildren().clear();
			
			startUpMain(vBox,visual);
			startUpCompany(vBox,visual,warningCanvas);
		});
		
		visual.getInsertName().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerText(visual.getInsertName(), vBox);
		});
		visual.getSelectCompanyType().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerComboBox(visual.getSelectCompanyType(), vBox);
		});
		visual.getSelectCompanySpecification().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerComboBox(visual.getSelectCompanySpecification(), vBox);
		});
	}
	
	public void startUpMain(VBox vBox, VisualElementsHolder visual) {
		HBox hBox = new HBox();
		Label name = new Label("Name of Company: ");
		Label money = new Label(" | Money of the Company (€): ");
		Label companyType = new Label(" | Company Type: ");
		Label companySpecification = new Label(visual.getSelectCompanySpecification().getValue());
		visual.CSSLabel(money);
		visual.CSSLabelNonFungible(name);
		visual.CSSLabelNonFungible(companyType);
		visual.CSSLabelNonFungible(companySpecification);
		
		hBox.getChildren().addAll(name,visual.getNameOfCompany(),money,visual.getMoneyOfCompany(),companyType,companySpecification);
		vBox.setAlignment(Pos.TOP_LEFT);
		vBox.getChildren().add(hBox);
	}
	
	public void startUpCompany(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas) {
		Label availableResources = new Label("Available Resources");
		Label employeeLabel = new Label("List Employee Data");
		visual.CSSLabel(availableResources);
		visual.CSSLabel(employeeLabel);
		
		VBox vBoxAdd = new VBox();
		vBoxAdd.getChildren().addAll(availableResources,resources,equipment,employeeLabel,employ,employeeManager);
		
		Label nextCycleLabel = new Label("Next Cycle");
		visual.CSSLabel(nextCycleLabel);
		
		vBox.getChildren().addAll(vBoxAdd,nextCycleLabel);
		
		HBox hBox = new HBox();
		hBox.getChildren().addAll(visual.getSelectCycleAmount(),nextCycle);
		vBox.getChildren().addAll(hBox,warningCanvas);
	}
	
	public void addButtonsStart(VBox vBox) {
		vBox.getChildren().addAll(startButton);
	}

	public int getHeight() {
		return height;
	}

	public double getMoneyStart() {
		return moneyStart;
	}
}