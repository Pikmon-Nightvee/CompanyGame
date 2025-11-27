package Visual;

import GameLogic.Company;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ButtonManager {
	private Button resources = new Button("Resources");
	private Button listEmployees = new Button("List Employees");
	private Button nextCycle = new Button("Next Cycle"); 

	private Button startButton = new Button("Start the game"); 	

	private Button employeeManager = new Button("Manage Employees"); 
	private int amount = 0;
	private int height = 270;
	private double moneyStart = 0.0;
	
	private void CSS(Button button) {
		amount += 1;
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	
	public void start(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas, Company company) {
		CSS(resources);
		CSS(listEmployees);
		CSS(nextCycle);
		CSS(startButton);
		CSS(employeeManager);
		
		action(vBox, visual, warningCanvas, company);
	}
	
	public void action(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas, Company company) {
		startButton.setOnAction(event -> {
			if(visual.getInsertName().getText().isBlank()) {
				return;
			}
			if(visual.getSelectCompanyType() == null) {
				return;
			}
			if(visual.getSelectCompanySpecification() == null) {
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
			
			startUpMain(vBox,visual,company);
			startUpStockMarket(vBox,visual,warningCanvas);
			System.out.println(companyNameSet);
			height = (amount + visual.getAmount()) * 20 + 10;
			System.out.println(height);
		});
	}
	
	private void startUpMain(VBox vBox, VisualElementsHolder visual,Company company) {
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
	
	private void startUpStockMarket(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas) {
		Label availableResources = new Label("Available Resources");
		Label employeeLabel = new Label("List Employee Data");
		visual.CSSLabel(availableResources);
		visual.CSSLabel(employeeLabel);
		
		VBox vBoxAdd = new VBox();
		vBoxAdd.getChildren().addAll(availableResources,resources,employeeLabel,listEmployees,employeeManager);
		
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