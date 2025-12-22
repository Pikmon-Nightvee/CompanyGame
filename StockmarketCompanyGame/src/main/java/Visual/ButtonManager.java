package Visual;

import FileLogic.WriteCSVFiles;
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
	private Button produce = new Button("Produce");

	private Button startButton = new Button("Start the game"); 	

	private Button employeeManager = new Button("Manage Employees"); 
	private Button goBack = new Button("Go Back");
	
	private Button employSelectedEmployee = new Button("Employ");
	
	private Button assignTo = new Button("Asign to");
	private Button fireEmployee = new Button("Fire");
	
	private Button buy = new Button("Buy");
	private Button sell = new Button("Sell");
	private Button changeOutput = new Button("Change");

	private Button produceProduct = new Button("Produce");
	
	private int amount = 0;
	private int height = 270;
	
	private int subSelect = 0;
	private int subHeight = 0;
	private double moneyStart = 0.0;
	
	private boolean inEmployeeManager = false;
	private boolean inUnemployedManager = false;
	private boolean changeTextRes = false;
	private boolean changeTextEqu = false;
	private boolean changeTextPro = false;
	private boolean isProduce = false;
	
	private void CSS(Button button) {
		amount += 1;
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	
	private void CSSSubSelect(Button button) {
		subSelect += 1;
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	
	private void CSSNoAddAmount(Button button) {
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	
	public void start(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas, Canvas gameCanvas, Company company) {
		CSS(resources);
		CSS(employ);
		CSS(nextCycle);
		CSS(startButton);
		CSS(employeeManager);
		CSS(equipment);
		CSSNoAddAmount(produce);
		CSSSubSelect(goBack);
		
		action(vBox, visual, warningCanvas, gameCanvas, company);
	}
	
	public void action(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas, Canvas gameCanvas, Company company) {
		WriteCSVFiles writer = new WriteCSVFiles();
		
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
			
			resourceEquipmentUI(true,vBox,visual);
		});
		
		changeOutput.setOnAction(event->{
			if(!isProduce) {
				if(changeTextRes) {
					changeTextRes = false;
					visual.changeResourceText("On stock:" + "\n");
				}else{
					changeTextRes = true;
					visual.changeResourceText("On sell:" + "\n");
				}
				if(changeTextEqu) {
					changeTextEqu = false;
					visual.changeEquipmentText("On stock:" + "\n");
				}else {
					changeTextEqu = true;
					visual.changeEquipmentText("On sell:" + "\n");
				}
				
			}else {
				if(changeTextPro) {
					changeTextPro = false;
					visual.setProductAllText("On stock:" + "\n");
				}else {
					changeTextPro = true;
					visual.setProductAllText("On sell:" + "\n");
				}
			}
		});
		
		produce.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			
			produceUI(vBox,visual);
			isProduce = true;
		});
		
		employeeManager.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			inEmployeeManager = true;
			
			vBox.getChildren().addAll(visual.getFireEmployee(),fireEmployee);
			vBox.getChildren().addAll(visual.getAssignedEmployee(),assignTo);
			vBox.getChildren().addAll(visual.getAssignEmployeeTo(),visual.getAssignEmployed());
			vBox.getChildren().addAll(visual.getHiredEmployees(),visual.getEmployedStats());
			
			CSSSubSelect(assignTo);
			CSSSubSelect(fireEmployee);
			visual.subSelectEmployed();
			changeTextAreaSize(gameCanvas,visual,"Employed");
		});
		
		employ.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			inUnemployedManager = true;
			
			CSSSubSelect(employSelectedEmployee);
			visual.subSelectUnemployed();
			
			unemployedEmployees(vBox,visual,gameCanvas);
		});
		
		equipment.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			
			resourceEquipmentUI(false,vBox,visual);
		});
		
		employSelectedEmployee.setOnAction(event->{
			if(visual.getSelectUnemployed().getValue() == null) {
				errorMessage.errorMessageComboBox(visual.getSelectUnemployed(), vBox);
				return;
			}
			writer.manageEmployeeInFiles(visual.getSelectUnemployed().getValue(), "toEmployed");
			
			visual.getSelectUnemployed().setValue(null);
			visual.employedTextArea();
			visual.unemployedTextArea();
			visual.insertUnemployed();
			visual.insertEmployed();
		});
		
		assignTo.setOnAction(event->{
			if(visual.getAssignEmployed().getValue() == null) {
				errorMessage.errorMessageComboBox(visual.getAssignEmployed(), vBox);
				return;
			}
		});
		
		fireEmployee.setOnAction(event->{
			if(visual.getAssignEmployed().getValue() == null) {
				errorMessage.errorMessageComboBox(visual.getAssignEmployed(), vBox);
				return;
			}
			writer.manageEmployeeInFiles(visual.getAssignEmployed().getValue(), "toUnemployed");
			
			visual.getAssignEmployed().setValue(null);
			visual.employedTextArea();
			visual.unemployedTextArea();
			visual.insertUnemployed();
			visual.insertEmployed();
		});
		
		goBack.setOnAction(event->{
			vBox.getChildren().clear();
			
			inUnemployedManager = true;
			inEmployeeManager = true;
			isProduce = false;
			subSelect = 1;
			
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
		visual.getSelectUnemployed().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerComboBox(visual.getSelectUnemployed(), vBox);
		});
		visual.getAssignEmployed().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerComboBox(visual.getAssignEmployed(), vBox);
		});
	}
	
	private void unemployedEmployees(VBox vBox, VisualElementsHolder visual, Canvas gameCanvas) {
		vBox.getChildren().addAll(visual.getEmploy(),employSelectedEmployee);
		vBox.getChildren().addAll(visual.getEmployUnemployed(),visual.getSelectUnemployed(),visual.getStatsOfUnemployed());

		changeTextAreaSize(gameCanvas,visual,"Unemployed");
		
		vBox.getChildren().add(visual.getUnemployedStats());
	}
	
	private void produceUI(VBox vBox, VisualElementsHolder visual) {
		vBox.getChildren().addAll(visual.getEmployeesAvailable(),visual.getAvailableEmployees());
		vBox.getChildren().addAll(visual.getSelectProductLabel(),visual.getSelectProduct());
		vBox.getChildren().addAll(visual.getStartProduction(),produceProduct);
		vBox.getChildren().addAll(visual.getAmountLabel(),visual.getAmountToProduce());
		vBox.getChildren().addAll(changeOutput);
		vBox.getChildren().addAll(visual.getProductLabel(),visual.getProductAll());
		
		visual.subSelectProduce();
		CSSSubSelect(changeOutput);
		CSSSubSelect(produceProduct);
	}
	
	private void resourceEquipmentUI(boolean isResource, VBox vBox, VisualElementsHolder visual) {
		CSSSubSelect(buy);
		CSSSubSelect(sell);
		CSSSubSelect(changeOutput);
		visual.subSelectResourceEquipment();
		
		vBox.getChildren().addAll(visual.getBuyLabel(),buy,visual.getSellLabel(),sell);
		vBox.getChildren().addAll(visual.getAmountLabel(),visual.getAmountBuySell());
		if(isResource) {
			vBox.getChildren().addAll(visual.getResourceSelect(),visual.getSelectResource());	
		}else {
			vBox.getChildren().addAll(visual.getEquipmentSelect(),visual.getSelectEquipment());
		}
		vBox.getChildren().addAll(visual.getChangeTextArea(),changeOutput);
		if(isResource) {
			vBox.getChildren().addAll(visual.getResourceAll());
			System.out.println("hi");
		}else {
			vBox.getChildren().addAll(visual.getEquipmentAll());
		}
	}
	
	public void changeTextAreaSize(Canvas gameCanvas,VisualElementsHolder visual,String textArea) {
		int accountForGapsInbetweenElements = 10;
		subHeight = visual.getSubAmount() + subSelect;
		subHeight *= 20;
		subHeight += accountForGapsInbetweenElements;
		subHeight = (int)gameCanvas.getHeight() - subHeight;
		System.out.println("Elements: " + subSelect + " Height: " + subHeight); 
		
		visual.setTextAreaSize(textArea, subHeight, gameCanvas);
	}
	
	public void startUpMain(VBox vBox, VisualElementsHolder visual) {
		HBox hBox = new HBox();
		Label name = new Label("Name of Company: ");
		Label money = new Label(" | Money of the Company (€): ");
		Label companyType = new Label(" | Company Type: ");
		Label companySpecification = new Label(visual.getSelectCompanySpecification().getValue());
		visual.CSSLabel(money);
		visual.CSSLabelNoAddAmount(name);
		visual.CSSLabelNoAddAmount(companyType);
		visual.CSSLabelNoAddAmount(companySpecification);
		
		hBox.getChildren().addAll(name,visual.getNameOfCompany(),money,visual.getMoneyOfCompany(),companyType,companySpecification);
		vBox.setAlignment(Pos.TOP_LEFT);
		vBox.getChildren().add(hBox);
	}
	
	public void startUpCompany(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas) {
		Label availableResources = new Label("Available Resources");
		Label employeeLabel = new Label("List Employee Data");
		Label produceLabel = new Label("   Produce product");
		visual.CSSLabel(availableResources);
		visual.CSSLabel(employeeLabel);
		visual.CSSLabelNoAddAmount(produceLabel);
		
		HBox hBoxLabel = new HBox();
		HBox hBoxButtons = new HBox();
		hBoxLabel.getChildren().addAll(availableResources,produceLabel);		
		hBoxButtons.getChildren().addAll(resources,produce);
		VBox vBoxAdd = new VBox();
		vBoxAdd.getChildren().addAll(hBoxLabel,hBoxButtons,equipment,employeeLabel,employ,employeeManager);
		
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

	public int getSubHeight() {
		return subHeight;
	}

	public boolean isInEmployeeManager() {
		return inEmployeeManager;
	}

	public boolean isInUnemployedManager() {
		return inUnemployedManager;
	}
}