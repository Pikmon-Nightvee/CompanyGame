package Visual;

import java.util.ArrayList;

import ExternalResources.GraphicsManager;
import ExternalResources.SoundeffectManager;
import FileLogic.ReadCSVFiles;
import FileLogic.WriteCSVFiles;
import GameLogic.Company;
import GameLogic.Employee;
import GameLogic.ErrorMessageHandler;
import GameLogic.GameManager;
import GameLogic.LevelHolder;
import GameLogic.Machine;
import GameLogic.NextCycleStarted;
import GameLogic.Player;
import GameLogic.Product;
import GameLogic.Resource;
import javafx.geometry.Pos;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
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
	private Button repair = new Button("Repair");

	private Button produceProduct = new Button("Produce");

	private Button bankrupt = new Button("Back to menu");
	private Button nextDay = new Button("Go back to work");
	
	private int amount = 0;
	private int height = 270;
	
	private int subSelect = 0;
	private int subHeight = 0;
	private int changePro = 2;
	private double moneyStart = 0.0;
	
	private boolean changeTextResEqu = false;
	private boolean isProduce = false;
	private boolean isResource = false;
	
	private void setButtonBackground(GraphicsManager graphic, Button button) {
		try {
			button.setOnMousePressed(event->{
				button.setBackground(graphic.getButtonPressed());
			});
			button.setOnMouseMoved(event->{
				button.setBackground(graphic.getButtonHover());
			});
			button.setOnMouseExited(event->{
				button.setBackground(graphic.getButtonUnpressed());
			});
			button.setOnMouseReleased(event->{
				button.setBackground(graphic.getButtonUnpressed());
			});
			button.setBackground(graphic.getButtonUnpressed());
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void imageLoader(GraphicsManager graphic) {
		try {
			setButtonBackground(graphic,resources);
			setButtonBackground(graphic,equipment);
			setButtonBackground(graphic,employ);
			setButtonBackground(graphic,nextCycle);
			setButtonBackground(graphic,produce);

			setButtonBackground(graphic,startButton);
			
			setButtonBackground(graphic,employeeManager);
			setButtonBackground(graphic,goBack);

			setButtonBackground(graphic,employSelectedEmployee);
			
			setButtonBackground(graphic,assignTo);
			setButtonBackground(graphic,fireEmployee);
			
			setButtonBackground(graphic,buy);
			setButtonBackground(graphic,sell);
			setButtonBackground(graphic,changeOutput);
			setButtonBackground(graphic,repair);

			setButtonBackground(graphic,produceProduct);
			
			setButtonBackground(graphic,bankrupt);
			setButtonBackground(graphic,nextDay);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void CSS(Button button) {
		amount += 1;
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold; -fx-text-fill: white");
	}
	
	private void CSSSubSelect(Button button) {
		subSelect += 1;
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold; -fx-text-fill: white");
	}
	
	private void CSSNoAddAmount(Button button) {
		button.setPrefSize(200, 20);
		button.setStyle("-fx-font-size:15px;-fx-font-weight: bold; -fx-text-fill: white");
	}
	
	public void start(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas, Canvas gameCanvas, Company company, NextCycleStarted nextCycleStarted, StackPane gamePane, LevelHolder level, Player player, GameManager game, SoundeffectManager sfx, UIMenuManager UI) {
		CSS(resources);
		CSS(employ);
		CSS(nextCycle);
		CSS(startButton);
		CSS(employeeManager);
		CSS(equipment);
		CSSNoAddAmount(produce);
		CSSNoAddAmount(nextDay);
		CSSSubSelect(goBack);
		CSSSubSelect(bankrupt);
		
		action(vBox, visual, warningCanvas, gameCanvas, company, nextCycleStarted, gamePane, level, player, game, sfx, UI);
	}
	
	public void action(VBox vBox, VisualElementsHolder visual, Canvas warningCanvas, Canvas gameCanvas, Company company, NextCycleStarted nextCycleStarted, StackPane gamePane, LevelHolder level, Player player, GameManager game, SoundeffectManager sfx, UIMenuManager UI) {
		WriteCSVFiles writer = new WriteCSVFiles();
		ReadCSVFiles reader = new ReadCSVFiles();
		
		startButton.setOnAction(event -> {
			sfx.playButton(UI.isSfxOn());
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
			case "Normal": moneyStart = 10000.00; break;
			case "Hard": moneyStart = 5000.00; break;
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

			level.getWalls().clear();
			level.loadLevel(company.getCompanyType(), player);
			
			Label money = new Label();
			visual.CSSLabel(money);
			money.setText(String.valueOf(company.getMoneyOfCompany()));
			visual.setMoneyOfCompany(money);
			
			startUpMain(vBox,visual,company);
			startUpCompany(vBox,visual,warningCanvas);
			System.out.println(companyNameSet);
			System.out.println(company.toString());
			game.updateState("InUIState");
		});
		
		resources.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
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
			sfx.playButton(UI.isSfxOn());
			if(!isProduce) {
				changeBuySell(vBox,changeTextResEqu,visual);
				if(changeTextResEqu) {
					changeTextResEqu = false;
				}else{
					changeTextResEqu = true;
				}
				visual.updateResourceEquipment(reader, changeTextResEqu, company,false);
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
			sfx.playButton(UI.isSfxOn());
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			
			produceUI(vBox,visual);
			isProduce = true;
			visual.insertIntoProduct(company);
			String currentCase = "";
			
			switch(changePro-1) {
			case 1: currentCase = "Produceable:"; break;
			case 2: currentCase = "In Production:"; break;
			case 3: currentCase = "On stock:"; break;
			}
			visual.productText(currentCase,company);
		});
		
		employeeManager.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			
			HBox hBox = new HBox();
			hBox.getChildren().addAll(assignTo, visual.getAssignToMachine());
			HBox fireRepair = new HBox();
			fireRepair.getChildren().addAll(fireEmployee,repair);

			vBox.getChildren().addAll(visual.getAssignEmployeeTo(),visual.getAssignEmployed());
			vBox.getChildren().addAll(visual.getAssignedEmployee(),hBox);
			vBox.getChildren().addAll(visual.getFireEmployee(),fireRepair);
			vBox.getChildren().addAll(visual.getHiredEmployees(),visual.getEmployedStats());
			
			CSSNoAddAmount(assignTo);
			CSSSubSelect(fireEmployee);
			CSSSubSelect(repair);
			visual.subSelectEmployed(company);
			visual.employedTextArea();
		});
		
		employ.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			
			CSSSubSelect(employSelectedEmployee);
			visual.subSelectUnemployed();
			
			unemployedEmployees(vBox,visual,gameCanvas);
		});
		
		equipment.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
			vBox.getChildren().clear();
			vBox.getChildren().addAll(goBack);
			
			resourceEquipmentUI(false,vBox,visual,changeTextResEqu);
			if(changeTextResEqu) {
				visual.insertEquipment("MachineNotBought.csv",company,visual.getSelectEquipment(),false);
			}else{
				visual.insertEquipment("MachineBought.csv",company,visual.getSelectEquipment(),false);
			}
		});
		
		employSelectedEmployee.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
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
			sfx.playButton(UI.isSfxOn());
			if(visual.getAssignEmployed().getValue() == null) {
				errorMessage.errorMessageComboBox(visual.getAssignEmployed(), vBox);
				return;
			}
			
			if(employeeAlreadyWorking(reader,company,visual.getAssignEmployed().getValue())) {
				errorMessage.errorMessageComboBox(visual.getAssignEmployed(), vBox);
				return;
			}
			
			writer.setEmployeeMachine(visual.getAssignEmployed().getValue(), visual.getAssignToMachine().getValue());
			writer.deleteProduction(visual.getAssignEmployed().getValue(), company, reader);
			
			visual.getAssignEmployed().setValue(null);
			visual.insertEquipment("MachineBought.csv", company, visual.getAssignToMachine(),true);
			visual.getAssignToMachine().getItems().add("none");
			visual.getAssignToMachine().setValue("none");
			visual.employedTextArea();
		});
		
		repair.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
			if(visual.getAssignEmployed().getValue() == null) {
				errorMessage.errorMessageComboBox(visual.getAssignEmployed(), vBox);
				return;
			}
			if(visual.getAssignToMachine().getValue() == "none") {
				return;
			}
			
			ArrayList<Machine> machines = reader.readMachines("MachineBought.csv",company);
			int baseSpend = 1000;
			int spentRepair = 0;
			for(Machine m : machines) {
				if(m.getCondition()>0) {
					if(m.getName().equals(visual.getAssignToMachine().getValue()) && m.getCondition() < 10) {
						spentRepair = (baseSpend * m.getAmount()) / m.getCondition();
					}
				}else {
					baseSpend += 250;
					baseSpend *= m.getAmount();
				}
			}
			for(Machine m : machines) {
				if(m.getName().equals(visual.getAssignToMachine().getValue())) {
					int newCompanyMoney = (int) (company.getMoneyOfCompany() - spentRepair);
					System.out.println("Test Money: " + newCompanyMoney);
					System.out.println("Test Company: " + company.getMoneyOfCompany());
					if(0 > newCompanyMoney) {
						return;
					}
				}
			}
			
			int newCompanyMoney = (int) (company.getMoneyOfCompany() - spentRepair);
			company.setMoneyOfCompany(newCompanyMoney);
			writer.repairMachine(visual.getAssignToMachine().getValue(), reader, company);
			writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
			
			machines = reader.readMachines("MachineBroken.csv",company);
			for(Machine m : machines) {
				if(m.getName().equals(visual.getAssignToMachine().getValue())) {
					writer.machineBrokenSold(reader, m.getName(), m.getAmount(), company);
				}
			}
		});
		
		fireEmployee.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
			if(visual.getAssignEmployed().getValue() == null) {
				errorMessage.errorMessageComboBox(visual.getAssignEmployed(), vBox);
				return;
			}
			
			ArrayList<Employee> employeeToPay = reader.employedEmployees();
			for(Employee e : employeeToPay) {
				if(e.getName().equals(visual.getAssignEmployed().getValue())) {
					double money = company.getMoneyOfCompany() - e.getCost();
					if(0 > money) {
						errorMessage.errorMessageComboBox(visual.getAssignEmployed(), vBox);
						return;
					}
					company.setMoneyOfCompany(money);
				}
			}
			
			writer.manageEmployeeInFiles(visual.getAssignEmployed().getValue(), "toUnemployed");
			writer.deleteProduction(visual.getAssignEmployed().getValue(), company, reader);
			
			writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
			
			visual.getAssignEmployed().setValue(null);
			visual.employedTextArea();
			visual.unemployedTextArea();
			visual.insertUnemployed();
			visual.insertEmployed();
		});
		
		goBack.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
			vBox.getChildren().clear();
			visual.clearTextFields();
			
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
			sfx.playButton(UI.isSfxOn());
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
			sfx.playLevelCash(UI.isSfxOn());
			
			if(isResource) {
				ArrayList<Resource> resources = reader.readResource("ResourcesOnSell.csv", company);
				for(Resource r : resources) {
					System.out.println("Test: " + r.toString());
					if(r.getName().equals(visual.getSelectResource().getValue())) {
						int moneyToSpend = buyAmount * r.getCost();
						System.out.println("Test Money: " + moneyToSpend);
						System.out.println("Test Company: " + company.getMoneyOfCompany());
						if(company.getMoneyOfCompany() <= moneyToSpend) {
							errorMessage.errorMessageText(visual.getAmountBuySell(), vBox);
							return;
						}
					}
				}
				
				writer.buySellResources(visual.getSelectResource().getValue(), "Bought", company, buyAmount);
				visual.getSelectResource().setValue(null);
			}else {
				ArrayList<Machine> machines = reader.readMachines("MachineNotBought.csv", company);
				for(Machine m : machines) {
					if(m.getName().equals(visual.getSelectEquipment().getValue())) {
						int moneyToSpend = buyAmount * m.getCost();
						if(company.getMoneyOfCompany() <= moneyToSpend) {
							errorMessage.errorMessageText(visual.getAmountBuySell(), vBox);
							return;
						}
					}
				}
				
				writer.buySellMachines(visual.getSelectEquipment().getValue(), "Bought", company, buyAmount);
				visual.getSelectEquipment().setValue(null);
			}

			visual.updateResourceEquipment(reader,changeTextResEqu,company,false);
			writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
		});
		
		sell.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
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
				boolean removeMachine = false;
				int machineRemoveAmount = 0;
				
				ArrayList<String[]> machineAmount = reader.machinesTopDownGet("MachinesPlaced.csv");
				ArrayList<Machine> machines = reader.readMachines("MachineBought.csv", company);
				for(String[] machine : machineAmount) {
					if(machine[0].equals(visual.getSelectEquipment().getValue())) {
						for(Machine m : machines) {
							if(m.getName().equals(machine[0])) {
								int amount = Integer.parseInt(machine[1]);
								machineRemoveAmount = m.getAmount() - amount - sellAmount;
								if(machineRemoveAmount < 0) {
									machineRemoveAmount *= -1;
									removeMachine = true;
								}
								System.out.println("Machine found: " + machine[0] + " Will be removed?: " + removeMachine);
								System.out.println("Amount Placed: " + amount + " Amount Sold: " + sellAmount + " Rest: " + (sellAmount-amount));
							}
						}
					}
				}

				ArrayList<Machine> machineBroken = reader.readMachines("MachineBroken.csv",company);
				boolean removeMachineBroken = false;
				int machineRemoveAmountBroken = 0;
				for(Machine machine : machineBroken) {
					if(machine.getName().equals(visual.getSelectEquipment().getValue())) {
						for(Machine m : machines) {
							if(m.getName().equals(machine.getName())) {
								int amount = machine.getAmount();
								machineRemoveAmountBroken = m.getAmount() - amount - sellAmount;
								if(machineRemoveAmountBroken < 0) {
									machineRemoveAmountBroken *= -1;
									removeMachineBroken = true;
								}
								System.out.println("Machine found: " + machine.getName() + " Will be removed?: " + removeMachine);
								System.out.println("Amount Placed: " + amount + " Amount Sold: " + sellAmount + " Rest: " + (sellAmount-amount));
							}
						}
					}
				}
				
				if(removeMachineBroken) {
					System.out.println("Machine had to be sold broken; amount: "+machineRemoveAmountBroken);
					writer.machineBrokenSold(reader, visual.getSelectEquipment().getValue(), machineRemoveAmountBroken, company);
				}
				
				if(removeMachine) {
					writer.removeMachineCords(visual.getSelectEquipment().getValue(), reader, company, machineRemoveAmount);
				}
				
				writer.buySellMachines(visual.getSelectEquipment().getValue(), "Sold", company, sellAmount);
				visual.getSelectEquipment().setValue(null);
				
				if(removeMachine) {
					level.getMachines().clear();
					level.getInteract().clear();
					
					level.machinesLoad(reader.machinesPlaced());
					level.interactLoad(level.getMachines());
				}
			}

			sfx.playLevelCash(UI.isSfxOn());
			visual.updateResourceEquipment(reader,changeTextResEqu,company,false);
			writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
		});
		produceProduct.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
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
			if(writer.resourcesNotAvailable(visual.getSelectProduct().getValue(), visual.getAvailableEmployees().getValue(), Integer.parseInt(visual.getAmountToProduce().getText()), company, reader)){
				errorMessage.errorMessageButton(produceProduct, vBox);
				errorOccured = true;
			}
			if(writer.notEnoughResources(visual.getSelectProduct().getValue(), visual.getAvailableEmployees().getValue(), Integer.parseInt(visual.getAmountToProduce().getText()), company, reader)){
				errorMessage.errorMessageButton(produceProduct, vBox);
				errorOccured = true;
			}
			if(errorOccured) {
				return;
			}
			
			int produceAmount = Integer.parseInt(visual.getAmountToProduce().getText());
			ArrayList<Product> products = reader.readProducts("ProductData.csv", company);
			for(Product p : products) {
				if(p.getName().equals(visual.getSelectProduct().getValue())) {
					int moneyToSpend = produceAmount * p.getCost();
					if(company.getMoneyOfCompany() <= moneyToSpend) {
						errorMessage.errorMessageText(visual.getAmountToProduce(), vBox);
						return;
					}
				}
			}
			
			errorMessage.errorMessageHandlerButton(produceProduct, vBox);
			
			writer.startProduction(visual.getSelectProduct().getValue(), visual.getAvailableEmployees().getValue(), Integer.parseInt(visual.getAmountToProduce().getText()), company, reader);
			String currentCase = "";
			
			switch(changePro-1) {
			case 1: currentCase = "Produceable:"; break;
			case 2: currentCase = "In Production:"; break;
			case 3: currentCase = "On stock:"; break;
			}
			visual.productText(currentCase,company);
			visual.insertAvailableEmployees(company, visual.getSelectProduct().getValue());
			visual.updateResourceEquipment(reader, changeTextResEqu, company,false);
		});
		nextCycle.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
			nextCycleStarted.nextDay(visual.getSelectCycleAmount().getValue(),reader,company);
			writer.companyDataSave(company.getName(), company.getMoneyOfCompany(), company.getReputation(), company.getCompanyType());
			visual.updateResourceEquipment(reader, changeTextResEqu, company, false);

			vBox.getChildren().clear();
			
			Label money = new Label("Money made/lost: " + nextCycleStarted.getBalance());
			Label producedProducts = new Label("Products made: " + nextCycleStarted.getProducedProducts());
			Label soldProducts = new Label("Products sold: " + nextCycleStarted.getSoldProducts());
			Label date = new Label(visual.getSelectCycleAmount().getValue() + " report:");
			
			VBox vBoxDate = new VBox();
			vBoxDate.getChildren().add(date);
			vBoxDate.setAlignment(Pos.TOP_CENTER);
			vBox.setAlignment(Pos.CENTER);
			vBox.getChildren().clear();
			
			visual.CSSLabelNoAddAmountBig(date);
			if(nextCycleStarted.getBalance() < 0) {
				visual.CSSLabelNoAddAmountRed(money);
			}else {
				visual.CSSLabelNoAddAmountGreen(money);
			}
			visual.CSSLabelNoAddAmount(soldProducts);
			visual.CSSLabelNoAddAmount(producedProducts);
			
			gamePane.getChildren().clear();
			gamePane.getChildren().add(gameCanvas);
			gamePane.getChildren().add(vBoxDate);
			gamePane.getChildren().add(vBox);
			
			writer.machineBroken(reader, company);
			
			vBox.getChildren().addAll(money,producedProducts,soldProducts,nextDay);
			
			nextCycleStarted.setBalance(0);
			nextCycleStarted.setProducedProducts(0);
			nextCycleStarted.setSoldProducts(0);
			sfx.playLevelComplete(UI.isSfxOn());
		});
		nextDay.setOnAction(event->{
			sfx.playButton(UI.isSfxOn());
			vBox.getChildren().clear();
			gamePane.getChildren().clear();
			gamePane.getChildren().addAll(gameCanvas, warningCanvas, vBox);
			goBack.fire();
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
	
	public void changeTextAreaSize(Canvas gameCanvas,VisualElementsHolder visual, VBox vBox) {
		int setAboveCanvasEdge = 20;
		subHeight = (int) (gameCanvas.getHeight());
		
		visual.setTextAreaSize(subHeight, gameCanvas);
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
		Label moneyOver = new Label();
		moneyOver.setText(String.valueOf(company.getMoneyOfCompany()));
		visual.CSSLabelWhite(moneyOver);
		
		HBox hBox = new HBox();
		Label name = new Label("Name of Company: ");
		Label money = new Label(" | Money of the Company (€): ");
		Label companyType = new Label(" | Company Type: ");
		Label companySpecification = new Label(company.getCompanyType());
		visual.CSSLabelWhite(money);
		visual.CSSLabelWhite(name);
		visual.CSSLabelWhite(companyType);
		visual.CSSLabelWhite(companySpecification);
		visual.CSSLabelWhite(visual.getNameOfCompany());
		
		hBox.getChildren().addAll(name,visual.getNameOfCompany(),money,moneyOver,companyType,companySpecification);
		vBox.getChildren().set(0,hBox);
		hBox.setAlignment(Pos.TOP_CENTER);
		vBox.setAlignment(Pos.TOP_CENTER);
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
		booleanReset();
	}
	
	public boolean employeeAlreadyWorking(ReadCSVFiles reader, Company company, String employee) {
		ArrayList<Product> productsInProduction = reader.readProducts("InProduction.csv", company);
		for(Product p : productsInProduction) {
			if(p.getAsignedEmployee().equals(employee)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void booleanReset() {
		changeTextResEqu = false;
		isProduce = false;
		isResource = false;
	}

	public double getMoneyStart() {
		return moneyStart;
	}

	public int getSubHeight() {
		return subHeight;
	}

	public int getHeight() {
		System.out.println(height);
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}

	public Button getBankrupt() {
		return bankrupt;
	}
}