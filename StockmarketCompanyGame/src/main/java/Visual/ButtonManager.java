package Visual;

import java.util.ArrayList;

import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import GameLogic.ErrorMessageHandler;
import GameLogic.Machine;
import GameLogic.Resource;
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
	private int changePro = 2;
	private double moneyStart = 0.0;
	
	private boolean inEmployeeManager = false;
	private boolean inUnemployedManager = false;
	private boolean changeTextResEqu = false;
	private boolean isProduce = false;
	private boolean isResource = false;
	
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
		ReadCSVFiles reader = new ReadCSVFiles();
		
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
			company.setCompanyType(visual.getSelectCompanySpecification().getValue());
			
			writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
			writer.gameWasPlayed();
			
			Label money = new Label();
			visual.CSSLabel(money);
			money.setText(String.valueOf(company.getMoneyOfCompany()));
			visual.setMoneyOfCompany(money);
			
			startUpMain(vBox,visual,company);
			startUpCompany(vBox,visual,warningCanvas);
			System.out.println(companyNameSet);
			height = (amount + visual.getAmount()) * 20 + 10;
			System.out.println(height);
			System.out.println(company.toString());
		});
		
		resources.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			
			resourceEquipmentUI(true,vBox,visual,changeTextResEqu);
			isResource = true;
			
			String resourcePath = "";
			if(changeTextResEqu) {
				resourcePath = "ResourcesOnSell.csv";
			}else {
				resourcePath = "ResourcesBought.csv";
			}
			
			visual.insertResource(resourcePath,company);
		});
		
		changeOutput.setOnAction(event->{
			if(!isProduce) {
				changeBuySell(vBox,changeTextResEqu,visual);
				if(changeTextResEqu) {
					changeTextResEqu = false;
				}else{
					changeTextResEqu = true;
				}
				visual.updateResourceEquipment(reader, changeTextResEqu, company);
			}else {
				String currentCase = "";
				switch(changePro) {
				case 1: currentCase = "Produceable:"; changePro++; break;
				case 2: currentCase = "In Production:"; changePro++; break;
				case 3: currentCase = "On stock:"; changePro=1; break;
				}
				visual.productText(currentCase,company);
			}
		});
		
		produce.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			
			produceUI(vBox,visual);
			isProduce = true;
			visual.insertIntoProduct(company);
		});
		
		employeeManager.setOnAction(event->{
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			inEmployeeManager = true;
			
			HBox hBox = new HBox();
			hBox.getChildren().addAll(assignTo, visual.getAssignToMachine());

			vBox.getChildren().addAll(visual.getAssignEmployeeTo(),visual.getAssignEmployed());
			vBox.getChildren().addAll(visual.getAssignedEmployee(),hBox);
			vBox.getChildren().addAll(visual.getFireEmployee(),fireEmployee);
			vBox.getChildren().addAll(visual.getHiredEmployees(),visual.getEmployedStats());
			
			CSSSubSelect(assignTo);
			CSSSubSelect(fireEmployee);
			visual.subSelectEmployed(company);
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
			
			resourceEquipmentUI(false,vBox,visual,changeTextResEqu);
			if(changeTextResEqu) {
				visual.insertEquipment("MachineNotBought.csv",company,visual.getSelectEquipment());
			}else{
				visual.insertEquipment("MachineBought.csv",company,visual.getSelectEquipment());
			}
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
			
			writer.setEmployeeMachine(visual.getAssignEmployed().getValue(), visual.getAssignToMachine().getValue());
			visual.getAssignEmployed().setValue(null);
			visual.employedTextArea();
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
			visual.clearTextFields();
			
			inUnemployedManager = true;
			inEmployeeManager = true;
			isProduce = false;
			isResource = false;
			subSelect = 1;

			visual.getAmountBuySell().setStyle("-fx-font-size:15px;");	
			
			Label money = new Label();
			visual.CSSLabel(money);
			money.setText(String.valueOf(company.getMoneyOfCompany()));
			visual.setMoneyOfCompany(money);
			
			startUpMain(vBox,visual,company);
			startUpCompany(vBox,visual,warningCanvas);
			System.out.println("Money of company:"+company.getMoneyOfCompany());
		});
		
		buy.setOnAction(event->{
			boolean errorOccured = false;
			
			if(visual.getAmountBuySell().getText().isBlank()){
				errorMessage.errorMessageText(visual.getAmountBuySell(), vBox);
				errorOccured = true;
			}
			int buyAmount = 0;
			try {
				buyAmount = Integer.parseInt(visual.getAmountBuySell().getText());
			}catch(Exception e) {
				errorMessage.errorMessageText(visual.getAmountBuySell(), vBox);
				errorOccured = true;
				System.out.println(e.getMessage());
			}
			if(buyAmount <= 0){
				errorMessage.errorMessageText(visual.getAmountBuySell(), vBox);
				errorOccured = true;
			}
			
			if(isResource) {
				if(visual.getSelectResource().getValue() == null){
					errorMessage.errorMessageComboBox(visual.getSelectResource(), vBox);
					errorOccured = true;
				}
			}else {
				if(visual.getSelectEquipment().getValue() == null){
					errorMessage.errorMessageComboBox(visual.getSelectEquipment(), vBox);
					errorOccured = true;
				}
			}
			if(errorOccured) {
				return;
			}
			
			if(isResource) {
				writer.buySellResources(visual.getSelectResource().getValue(), "Bought", company, buyAmount);
				visual.getSelectResource().setValue(null);
			}else {
				writer.buySellMachines(visual.getSelectEquipment().getValue(), "Bought", company, buyAmount);
				visual.getSelectEquipment().setValue(null);
			}
			
			visual.updateResourceEquipment(reader,changeTextResEqu,company);
			writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
		});
		
		sell.setOnAction(event->{
			boolean errorOccured = false;
			if(visual.getAmountBuySell().getText().isBlank()){
				errorMessage.errorMessageText(visual.getAmountBuySell(), vBox);
				errorOccured = true;
			}
			
			int sellAmount = 0;
			try {
				sellAmount = Integer.parseInt(visual.getAmountBuySell().getText());
			}catch(Exception e) {
				errorMessage.errorMessageText(visual.getAmountBuySell(), vBox);
				errorOccured = true;
				System.out.println(e.getMessage());
			}
			if(sellAmount <= 0){
				errorMessage.errorMessageText(visual.getAmountBuySell(), vBox);
				errorOccured = true;
			}
			
			if(isResource) {
				if(visual.getSelectResource().getValue() == null){
					errorMessage.errorMessageComboBox(visual.getSelectResource(), vBox);
					errorOccured = true;
				}
			}else {
				if(visual.getSelectEquipment().getValue() == null){
					errorMessage.errorMessageComboBox(visual.getSelectEquipment(), vBox);
					errorOccured = true;
				}
			}
			if(errorOccured) {
				return;
			}
			
			if(isResource) {
				writer.buySellResources(visual.getSelectResource().getValue(), "Sold", company, sellAmount);
				visual.getSelectResource().setValue(null);
			}else {
				writer.buySellMachines(visual.getSelectEquipment().getValue(), "Sold", company, sellAmount);
				visual.getSelectEquipment().setValue(null);
			}
			
			visual.updateResourceEquipment(reader,changeTextResEqu,company);
			writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
		});
		produceProduct.setOnAction(event->{
			boolean errorOccured = false;
			if(visual.getAmountToProduce().getText().isBlank()){
				errorMessage.errorMessageText(visual.getAmountToProduce(), vBox);
				errorOccured = true;
			}
			try {
				if(Integer.parseInt(visual.getAmountToProduce().getText()) < 1){
					errorMessage.errorMessageText(visual.getAmountToProduce(), vBox);
					errorOccured = true;
				}
			}catch(Exception e) {
				errorMessage.errorMessageText(visual.getAmountToProduce(), vBox);
				errorOccured = true;
			}
			
			if(visual.getAvailableEmployees().getValue() == null){
				errorMessage.errorMessageComboBox(visual.getAvailableEmployees(), vBox);
				errorOccured = true;
			}
			if(visual.getSelectProduct().getValue() == null){
				errorMessage.errorMessageComboBox(visual.getSelectProduct(), vBox);
				errorOccured = true;
			}
			if(errorOccured) {
				return;
			}
			
		});
		visual.getSelectProduct().setOnAction(event ->{
			visual.insertAvailableEmployees(company, visual.getSelectProduct().getValue());
		});
		
		visual.getAmountBuySell().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerText(visual.getAmountBuySell(), vBox);
		});
		visual.getInsertName().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerText(visual.getInsertName(), vBox);
		});
		visual.getAmountToProduce().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerText(visual.getAmountToProduce(), vBox);
		});
		visual.getSelectProduct().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerComboBox(visual.getSelectProduct(), vBox);
		});
		visual.getAvailableEmployees().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerComboBox(visual.getAvailableEmployees(), vBox);
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
		visual.getSelectResource().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerComboBox(visual.getSelectResource(), vBox);
		});
		visual.getSelectEquipment().setOnMouseClicked(event ->{
			errorMessage.errorMessageHandlerComboBox(visual.getSelectEquipment(), vBox);
		});
	}
	
	private void changeBuySell(VBox vBox, boolean boughtButton, VisualElementsHolder visual) {
		if(boughtButton) {
			int index = vBox.getChildren().indexOf(buy);
			vBox.getChildren().set(index, sell);
			vBox.getChildren().set(index-1,visual.getSellLabel());
		}else {
			int index = vBox.getChildren().indexOf(sell);
			vBox.getChildren().set(index, buy);
			vBox.getChildren().set(index-1,visual.getBuyLabel());
		}
	}
	
	private void unemployedEmployees(VBox vBox, VisualElementsHolder visual, Canvas gameCanvas) {
		vBox.getChildren().addAll(visual.getEmploy(),employSelectedEmployee);
		vBox.getChildren().addAll(visual.getEmployUnemployed(),visual.getSelectUnemployed(),visual.getStatsOfUnemployed());

		changeTextAreaSize(gameCanvas,visual,"Unemployed");
		
		vBox.getChildren().add(visual.getUnemployedStats());
	}
	
	private void produceUI(VBox vBox, VisualElementsHolder visual) {
		vBox.getChildren().addAll(visual.getSelectProductLabel(),visual.getSelectProduct());
		vBox.getChildren().addAll(visual.getAmountLabel(),visual.getAmountToProduce());
		vBox.getChildren().addAll(visual.getStartProduction(),produceProduct);
		vBox.getChildren().addAll(visual.getEmployeesAvailable(),visual.getAvailableEmployees());
		vBox.getChildren().addAll(changeOutput);
		vBox.getChildren().addAll(visual.getProductLabel(),visual.getProductAll());
		
		visual.subSelectProduce();
		CSSSubSelect(changeOutput);
		CSSSubSelect(produceProduct);
	}
	
	private void resourceEquipmentUI(boolean isResource, VBox vBox, VisualElementsHolder visual, boolean isNotBuy) {
		CSSSubSelect(buy);
		CSSSubSelect(sell);
		CSSSubSelect(changeOutput);
		visual.subSelectResourceEquipment();
		
		if(isNotBuy) {
			vBox.getChildren().addAll(visual.getBuyLabel(),buy);
		}else {
			vBox.getChildren().addAll(visual.getSellLabel(),sell);
		}
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
		int accountForGapsInbetweenElements = 20;
		subHeight = visual.getSubAmount() + subSelect;
		subHeight *= 20;
		subHeight += accountForGapsInbetweenElements;
		subHeight = (int)gameCanvas.getHeight() - subHeight;
		System.out.println("Elements: " + (visual.getSubAmount() + subSelect) + " Height: " + subHeight + " HeightCanvas: " + gameCanvas.getHeight()); 
		
		visual.setTextAreaSize(textArea, subHeight, gameCanvas);
	}
	
	public void startUpMain(VBox vBox, VisualElementsHolder visual, Company company) {
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
		vBox.setAlignment(Pos.TOP_LEFT);
		vBox.getChildren().add(hBox);
	}
	
	public void updateMoney(VBox vBox, VisualElementsHolder visual, Company company) {
		Label money = new Label();
		money.setText(String.valueOf(company.getMoneyOfCompany()));
		visual.setMoneyOfCompany(money);
		
		HBox hBox = new HBox();
		Label name = new Label("Name of Company: ");
		money = new Label(" | Money of the Company (€): ");
		Label companyType = new Label(" | Company Type: ");
		Label companySpecification = new Label(visual.getSelectCompanySpecification().getValue());
		visual.CSSLabel(money);
		visual.CSSLabelNoAddAmount(name);
		visual.CSSLabelNoAddAmount(companyType);
		visual.CSSLabelNoAddAmount(companySpecification);
		
		hBox.getChildren().addAll(name,visual.getNameOfCompany(),money,visual.getMoneyOfCompany(),companyType,companySpecification);
		vBox.setAlignment(Pos.TOP_LEFT);
		vBox.getChildren().set(0,hBox);
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
	
	private void startMaster(VisualElementsHolder visual, ReadCSVFiles reader, Company company) {
		visual.insertDifficulty();
		visual.insertCycleDates();
		visual.insertSpecifications();
		visual.insertTypes();
		visual.getSelectDifficulty().setValue("Easy");
		visual.getSelectCycleAmount().setValue("Day");
		visual.start(reader,company);
	}
	
	public void addStartUpScreen(VisualElementsHolder visual, VBox vBox, ReadCSVFiles reader, Company company) {
		vBox.getChildren().addAll(visual.getEnterName(),visual.getInsertName());
		vBox.getChildren().addAll(visual.getDifficulty(),visual.getSelectDifficulty());
		vBox.getChildren().addAll(visual.getChooseCompanyType(), visual.getSelectCompanyType(), visual.getChooseCompanyWork(), visual.getSelectCompanySpecification());
		vBox.getChildren().addAll(startButton);
		startMaster(visual,reader,company);
	}
	
	public void loadGame(VisualElementsHolder visual, VBox vBox, Company company, Canvas warningCanvas, ReadCSVFiles reader){
		Label money = new Label();
		visual.CSSLabel(money);
		money.setText(String.valueOf(company.getMoneyOfCompany()));
		visual.setMoneyOfCompany(money);
		
		startUpMain(vBox,visual,company);
		startUpCompany(vBox,visual,warningCanvas);
		System.out.println("Money of company:"+company.getMoneyOfCompany());
		startMaster(visual,reader,company);
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