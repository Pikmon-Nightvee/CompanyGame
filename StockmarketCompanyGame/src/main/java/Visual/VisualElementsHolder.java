package Visual;

import java.util.ArrayList;

import ExternalResources.GraphicsManager;
import FileLogic.ReadCSVFiles;
import GameLogic.Company;
import GameLogic.Employee;
import GameLogic.Machine;
import GameLogic.Product;
import GameLogic.Resource;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class VisualElementsHolder {
	private int amount = 0;
	private int subAmount = 0;
	
	//Labels:
	private Label moneyOfCompany = new Label("");
	private Label nameOfCompany = new Label("");
	private Label enterName = new Label("Enter the companys name");
	private Label difficulty = new Label("Game difficulty:");
	private Label chooseCompanyType = new Label("Company Type:");
	private Label chooseCompanyWork = new Label("Company Specilty:");
	private Label employ = new Label("Hire");
	private Label employUnemployed = new Label("Select who to hire");
	private Label statsOfUnemployed = new Label("Unemployed stats:");
	
	private Label assignEmployeeTo = new Label("Select Employee to asign");
	private Label assignedEmployee = new Label("Asign employee to Machine");
	private Label hiredEmployees = new Label("Hired:");
	private Label fireEmployee = new Label("Fire/Repair Machine");
	
	private Label buyLabel = new Label("Buy:");
	private Label sellLabel = new Label("Sell:");
	private Label amountLabel = new Label("Put in amount");
	private Label equipmentSelect = new Label("Select equipment:");
	private Label resourceSelect = new Label("Select resource:");
	private Label changeTextArea = new Label("Change output:");

	private Label employeesAvailable = new Label("Available Employees");
	private Label selectProductLabel = new Label("Select Product:");
	private Label startProduction = new Label("Produce:");
	private Label productLabel = new Label("Producable Products:");

	private Label bankrupt = new Label("You have gone bankrupt!");
	
	//TextFields:
	private TextField insertName = new TextField();

	private TextField amountBuySell = new TextField();

	private TextField amountToProduce = new TextField();

	//TextAreas:
	private TextArea unemployedStats = new TextArea();
	private TextArea employedStats = new TextArea();
	
	private TextArea equipmentAll = new TextArea();
	private TextArea resourceAll = new TextArea();
	
	private TextArea productAll = new TextArea();
	
	//ComboBox:
	private ComboBox <String>selectDifficulty = new ComboBox<>();
	private ComboBox <String>selectCycleAmount = new ComboBox<>();
	private ComboBox <String>selectCompanyType = new ComboBox<>();
	private ComboBox <String>selectCompanySpecification = new ComboBox<>();
	private ComboBox <String>selectUnemployed = new ComboBox<>();

	private ComboBox <String>assignEmployed = new ComboBox<>();
	private ComboBox <String>assignToMachine = new ComboBox<>();

	private ComboBox <String>selectResource = new ComboBox<>();
	private ComboBox <String>selectEquipment = new ComboBox<>();

	private ComboBox <String>availableEmployees = new ComboBox<>();
	private ComboBox <String>selectProduct = new ComboBox<>();
	
	//CSV reader:
	private ReadCSVFiles readerCSV = new ReadCSVFiles();
	
	//CSS Styles and call up
	public void insertTypes() {
		selectCompanyType.getItems().add("GmbH");
		selectCompanyType.getItems().add("Einzel Unternehmen");
	}
	
	public void insertSpecifications() {
		selectCompanySpecification.getItems().add("Foodtruck");
		selectCompanySpecification.getItems().add("EDV-Manager");
		selectCompanySpecification.getItems().add("Craft Buisness");
	}
	
	public void insertDifficulty() {
		selectDifficulty.getItems().add("Easy");
		selectDifficulty.getItems().add("Normal");
		selectDifficulty.getItems().add("Hard");
	}
	
	public void insertCycleDates() {
		selectCycleAmount.getItems().add("Day");
		selectCycleAmount.getItems().add("Week");
		selectCycleAmount.getItems().add("Month");
		selectCycleAmount.getItems().add("Year");
	}
	
	public void insertIntoProduct(Company company) {
		selectProduct.getItems().clear();
		ArrayList<Product> products = readerCSV.readProducts("Produceable.csv",company);
		for(Product product : products) {
			selectProduct.getItems().add(product.getName());
		}
	}
	
	public void unemployedTextArea() {
		String text = "";
		StringBuilder builder = new StringBuilder();
		
		ArrayList<Employee> unemployed = new ArrayList<>();
		unemployed = readerCSV.unemployedEmployees();
		for(Employee unemployedEmployee : unemployed) {
			builder.append(unemployedEmployee.toString());
		}
		text = builder.toString();
		
		unemployedStats.setText(text);
	}
	
	public void employedTextArea() {
		String text = "";
		StringBuilder builder = new StringBuilder();
		
		ArrayList<Employee> employed = new ArrayList<>();
		employed = readerCSV.employedEmployees();
		for(Employee employedEmployee : employed) {
			builder.append(employedEmployee.toString());
		}
		text = builder.toString();
		
		employedStats.setText(text);
	}
	
	public void setTextAreaSize(int size, Canvas canvas) {
		unemployedStats.setPrefSize(canvas.getWidth(), size);
		employedStats.setPrefSize(canvas.getWidth(), size); 
		equipmentAll.setPrefSize(canvas.getWidth(), size); 
		resourceAll.setPrefSize(canvas.getWidth(), size); 
		productAll.setPrefSize(canvas.getWidth(), size); 
	}
	
	public void insertUnemployed() {
		selectUnemployed.getItems().clear();
		ArrayList<Employee> unemployed = new ArrayList<>();
		unemployed = readerCSV.unemployedEmployees();
		for(Employee unemployedEmployee : unemployed) {
			selectUnemployed.getItems().add(unemployedEmployee.getName());
		}
	}
	
	public void insertEmployed() {
		assignEmployed.getItems().clear();
		ArrayList<Employee> unemployed = new ArrayList<>();
		unemployed = readerCSV.employedEmployees();
		for(Employee employedEmployee : unemployed) {
			assignEmployed.getItems().add(employedEmployee.getName());
		}
	}

	public void insertAvailableEmployees(Company company,String selected) {
		availableEmployees.getItems().clear();
		ArrayList<Employee> employeesAvailable = readerCSV.employeeAbleToProduce(company, selected);
		for(Employee available : employeesAvailable) {
			availableEmployees.getItems().add(available.getName());
		}
	}
	
	public void insertResource(String resourceSelected,Company company) {
		selectResource.getItems().clear();
		ArrayList<Resource> resources = new ArrayList<>();
		resources = readerCSV.readResource(resourceSelected,company);
		for(Resource r : resources) {
			selectResource.getItems().add(r.getName());
		}
	}
	
	public void insertEquipment(String equipmentSelected,Company company, ComboBox comboBox, boolean isEmployeeManager) {
		comboBox.getItems().clear();
		ArrayList<Machine> equipments = new ArrayList<>();
		ArrayList<Employee> employees = readerCSV.employedEmployees();
		equipments = readerCSV.readMachines(equipmentSelected,company);
		
		boolean isMachineBought = isEmployeeManager;
		
		for(Machine m : equipments) {
			int amount = 0;
			if(isMachineBought) {
				for(Employee e : employees) {
					if(e.getMachine().equals(m.getName())) {
						amount++;
					}
				}
			}
			if(m.getAmount() > amount || !isMachineBought) {
				comboBox.getItems().add(m.getName());
			}
		}
	}
	
	private void updateEquipmentText(ReadCSVFiles reader, String path, String startText, Company company) {
		StringBuilder builder = new StringBuilder();
		builder.append(startText + "\n");
		ArrayList<Machine> machines = reader.readMachines(path,company);
		
		for(Machine m : machines) {
			builder.append(m.toString());
		}
		
		changeEquipmentText(builder.toString());
	}
	
	private void updateResourceText(ReadCSVFiles reader, String path, String startText, Company company) {
		StringBuilder builder = new StringBuilder();
		builder.append(startText + "\n");
		
		ArrayList<Resource> resources = reader.readResource(path,company);
		for(Resource r : resources) {
			builder.append(r.toString());
		}
		
		changeResourceText(builder.toString());
	}
	
	public void updateResourceEquipment(ReadCSVFiles reader,boolean isBought, Company company, boolean isEmployeeManager) {
		if(isBought) {
			String beginningText = "On sell:";
			System.out.println(beginningText);
			
			insertEquipment("MachineNotBought.csv",company,selectEquipment,isEmployeeManager);
			updateEquipmentText(reader,"MachineNotBought.csv",beginningText,company);

			insertResource("ResourcesOnSell.csv",company);
			updateResourceText(reader,"ResourcesOnSell.csv",beginningText,company);
		}else {
			String beginningText = "Bought:";
			System.out.println(beginningText);
			
			insertEquipment("MachineBought.csv",company,selectEquipment,isEmployeeManager);
			updateEquipmentText(reader,"MachineBought.csv",beginningText,company);
			
			insertResource("ResourcesBought.csv",company);
			updateResourceText(reader,"ResourcesBought.csv",beginningText,company);
		}
		amountBuySell.clear();
	}
	
	public void productText(String inputText,Company company) {
		switch(inputText) {
		case "Produceable:": produceProduct(inputText,company);break;
		case "In Production:": inProduction(inputText,company);break;
		case "On stock:": onStock(inputText,company);break;
		}
	}
	
	private void produceProduct(String inputText,Company company) {
		ArrayList<Product> products = readerCSV.readProducts("Produceable.csv", company);
		StringBuilder builder = new StringBuilder();
		builder.append(inputText + "\n");
		for(Product product : products) {
			builder.append(product.toStringProduceable() + "\n");
		}
		setProductAllText(builder.toString());
	}
	
	private void inProduction(String inputText,Company company) {
		ArrayList<Product> products = readerCSV.readProducts("InProduction.csv", company);
		StringBuilder builder = new StringBuilder();
		builder.append(inputText + "\n");
		for(Product product : products) {
			builder.append(product.toStringInProduction() + "\n");
		}
		setProductAllText(builder.toString());
	}
	
	private void onStock(String inputText,Company company) {
		ArrayList<Product> products = readerCSV.readProducts("OnStock.csv", company);
		StringBuilder builder = new StringBuilder();
		builder.append(inputText + "\n");
		for(Product product : products) {
			builder.append(product.toStringOnStock() + "\n");
		}
		setProductAllText(builder.toString());
	}
	
	private void setButtonBackground(GraphicsManager graphic, ComboBox combo) {
		try {
			combo.setOnMousePressed(event->{
				combo.setBackground(graphic.getComboPressed());
			});
			combo.setOnMouseMoved(event->{
				combo.setBackground(graphic.getComboHover());
			});
			combo.setOnMouseExited(event->{
				combo.setBackground(graphic.getComboUnpressed());
			});
			combo.setOnMouseReleased(event->{
				combo.setBackground(graphic.getComboUnpressed());
			});
			combo.setBackground(graphic.getComboUnpressed());
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void loadImage(GraphicsManager graphic) {
		setButtonBackground(graphic,selectDifficulty);
		setButtonBackground(graphic,selectCycleAmount);
		setButtonBackground(graphic,selectCompanyType);
		setButtonBackground(graphic,selectCompanySpecification);
		setButtonBackground(graphic,selectUnemployed);

		setButtonBackground(graphic,assignEmployed);
		setButtonBackground(graphic,assignToMachine);

		setButtonBackground(graphic,selectResource);
		setButtonBackground(graphic,selectEquipment);

		setButtonBackground(graphic,availableEmployees);
		setButtonBackground(graphic,selectProduct);
	}
	
	public void changeTextAreas() {
		CSSArea(unemployedStats);
		CSSArea(employedStats);

		CSSArea(equipmentAll);
		CSSArea(resourceAll);
		CSSArea(productAll);
	}

	private void CSSArea(TextArea textArea) {
		textArea.setStyle("-fx-font-weight: bold;");
	}
	private void CSSBox(ComboBox<String> comboBox) {
		amount += 1;
		comboBox.setPrefSize(200, 20);
		comboBox.setStyle("-fx-font-size:15px;-fx-font-weight: bold; -fx-text-fill: white");
	}
	public void CSSLabel(Label label) {
		amount += 1;
		label.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
	}
	private void CSSBoxNoAddAmount(ComboBox<String> comboBox) {
		comboBox.setPrefSize(200, 20);
		comboBox.setStyle("-fx-font-size:15px;-fx-font-weight: bold; -fx-text-fill: white");
	}
	public void CSSLabelNoAddAmount(Label label) {
		label.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
	}
	public void CSSLabelNoAddAmountRed(Label label) {
		label.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;;-fx-font-weight: bold;-fx-text-fill: red;");
	}
	public void CSSLabelNoAddAmountGreen(Label label) {
		label.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;;-fx-font-weight: bold;-fx-text-fill: green;");
	}
	public void CSSLabelNoAddAmountBig(Label label) {
		label.setStyle("-fx-font-size: 100px;-fx-font-weight: bold;");
	}
	private void CSSLabelNoAddAmountWhite(Label label) {
		label.setStyle("-fx-font-size: 75;-fx-font-weight: bold;-fx-text-fill: white;");
	}
	private void CSSTextField(TextField field) {
		field.setPrefSize(5, 1);
	}
	private void CSSTextArea(TextArea area) {
		area.setPrefSize(5, 1);
	}
	
	public void start(ReadCSVFiles reader, Company company) {
		insertUnemployed();
		insertEmployed();
		
		CSSBox(selectCycleAmount);
		CSSBoxNoAddAmount(selectDifficulty);
		CSSBoxNoAddAmount(selectCompanyType);
		CSSBoxNoAddAmount(selectCompanySpecification);
		
		CSSLabel(moneyOfCompany);
		CSSLabel(nameOfCompany);
		CSSLabelNoAddAmount(enterName);
		CSSLabelNoAddAmount(difficulty);
		CSSLabelNoAddAmount(chooseCompanyType);
		CSSLabelNoAddAmount(chooseCompanyWork);
		//Test
		CSSTextField(insertName);

		CSSTextArea(unemployedStats);
		CSSTextArea(employedStats);
		unemployedStats.setEditable(false);
		employedStats.setEditable(false);

		StringBuilder builder = new StringBuilder();
		builder.append("Bought:" + "\n");
		
		ArrayList<Machine> machines = reader.readMachines("MachineBought.csv",company);
		for(Machine m : machines) {
			builder.append(m.toString());
		}
		equipmentAll.setText(builder.toString());
		equipmentAll.setEditable(false);

		builder = new StringBuilder();
		builder.append("Bought:" + "\n");
		
		ArrayList<Resource> resources = reader.readResource("ResourcesBought.csv",company);
		for(Resource r : resources) {
			builder.append(r.toString());
		}
		resourceAll.setText(builder.toString());
		resourceAll.setEditable(false);
		
		productText("Produceable:",company);
		productAll.setEditable(false);
		
		unemployedTextArea();
		employedTextArea();
		
		CSSLabelNoAddAmountWhite(bankrupt);
	}
	
	public void subSelectUnemployed() {
		subAmount = 0;
		CSSBoxSubSelect(selectUnemployed);
		
		CSSLabelSubSelect(employUnemployed);
		CSSLabelSubSelect(statsOfUnemployed);
		CSSLabelSubSelect(employ);
		
		//For TextArea:
		subAmount += 1;
	}
	
	public void subSelectResourceEquipment() {
		CSSBoxSubSelect(selectResource);
		CSSBoxSubSelect(selectEquipment);

		CSSLabelSubSelect(resourceSelect);
		CSSLabelSubSelect(equipmentSelect);
		
		subAmount = 1;
		CSSLabelSubSelect(buyLabel);
		CSSLabelSubSelect(sellLabel);
		CSSLabelSubSelect(changeTextArea);
		CSSLabelSubSelect(amountLabel);
		
		//For TextArea:
		subAmount += 1;
		System.out.println(subAmount);
	} 

	public void subSelectProduce() {
		subAmount = 0;
		CSSBoxSubSelect(availableEmployees);
		CSSBoxSubSelect(selectProduct);
		
		CSSLabelSubSelect(employeesAvailable);
		CSSLabelSubSelect(selectProductLabel);
		CSSLabelSubSelect(startProduction);
		CSSLabelSubSelect(productLabel);
		CSSLabelSubSelect(amountLabel);
		
		//For TextArea:
		subAmount += 1;
		System.out.println(subAmount);
	}
	
	public void subSelectEmployed(Company company) {
		subAmount = 0;
		CSSBoxSubSelect(assignEmployed);
		CSSBoxSubSelect(assignToMachine);
		insertEquipment("MachineBought.csv",company,assignToMachine,true);
		assignToMachine.getItems().add("none");
		assignToMachine.setValue("none");
		
		CSSLabelSubSelect(assignEmployeeTo);
		CSSLabelSubSelect(assignedEmployee);
		CSSLabelSubSelect(hiredEmployees);
		CSSLabelSubSelect(fireEmployee);
		
		//For TextArea:
		subAmount += 1;
		System.out.println(subAmount);
	}
	
	private void CSSBoxSubSelect(ComboBox<String> comboBox) {
		subAmount += 1;
		comboBox.setPrefSize(200, 20);
		comboBox.setStyle("-fx-font-size:15px;-fx-font-weight: bold; -fx-text-fill: white");
	}
	public void CSSLabelSubSelect(Label label) {
		subAmount += 1;
		label.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
	}
	
	public void clearTextFields() {
		insertName.clear();
		amountBuySell.clear();
		amountToProduce.clear();
	}
	
	//Getters and Setters
	public Label getDifficulty() {
		return difficulty;
	}

	public Label getEnterName() {
		return enterName;
	}

	public Label getNameOfCompany() {
		return nameOfCompany;
	}

	public Label getMoneyOfCompany() {
		return moneyOfCompany;
	}

	public TextField getInsertName() {
		return insertName;
	}

	public ComboBox<String> getSelectDifficulty() {
		return selectDifficulty;
	}

	public ComboBox<String> getSelectCycleAmount() {
		return selectCycleAmount;
	}
	public Label getChooseCompanyType() {
		return chooseCompanyType;
	}

	public Label getChooseCompanyWork() {
		return chooseCompanyWork;
	}

	public ComboBox<String> getSelectCompanyType() {
		return selectCompanyType;
	}

	public ComboBox<String> getSelectCompanySpecification() {
		return selectCompanySpecification;
	}

	public void setNameOfCompany(String nameOfCompany) {
		this.nameOfCompany.setText(nameOfCompany);
	}

	public void setMoneyOfCompany(Label moneyOfCompany) {
		this.moneyOfCompany = moneyOfCompany;
	}

	public int getAmount() {
		return amount;
	}

	public Label getEmployUnemployed() {
		return employUnemployed;
	}

	public Label getStatsOfUnemployed() {
		return statsOfUnemployed;
	}

	public TextArea getUnemployedStats() {
		return unemployedStats;
	}

	public ComboBox<String> getSelectUnemployed() {
		return selectUnemployed;
	}

	public Label getEmploy() {
		return employ;
	}

	public int getSubAmount() {
		return subAmount;
	}

	public void setSubAmount(int subAmount) {
		this.subAmount = subAmount;
	}

	public Label getAssignEmployeeTo() {
		return assignEmployeeTo;
	}

	public Label getAssignedEmployee() {
		return assignedEmployee;
	}

	public Label getHiredEmployees() {
		return hiredEmployees;
	}

	public TextArea getEmployedStats() {
		return employedStats;
	}

	public Label getBuyLabel() {
		return buyLabel;
	}

	public Label getSellLabel() {
		return sellLabel;
	}

	public Label getEquipmentSelect() {
		return equipmentSelect;
	}

	public Label getResourceSelect() {
		return resourceSelect;
	}

	public Label getChangeTextArea() {
		return changeTextArea;
	}

	public ComboBox<String> getSelectResource() {
		return selectResource;
	}

	public ComboBox<String> getSelectEquipment() {
		return selectEquipment;
	}

	public ComboBox<String> getAssignEmployed() {
		return assignEmployed;
	}

	public Label getFireEmployee() {
		return fireEmployee;
	}

	public TextField getAmountBuySell() {
		return amountBuySell;
	}

	public TextArea getEquipmentAll() {
		return equipmentAll;
	}

	public TextArea getResourceAll() {
		return resourceAll;
	}

	public Label getAmountLabel() {
		return amountLabel;
	}

	public void changeEquipmentText(String text) {
		equipmentAll.setText(text);
	}

	public void changeResourceText(String text) {
		resourceAll.setText(text);
	}

	public Label getEmployeesAvailable() {
		return employeesAvailable;
	}

	public Label getSelectProductLabel() {
		return selectProductLabel;
	}

	public Label getProductLabel() {
		return productLabel;
	}

	public Label getStartProduction() {
		return startProduction;
	}

	public Label getBankrupt() {
		return bankrupt;
	}

	public TextField getAmountToProduce() {
		return amountToProduce;
	}

	public TextArea getProductAll() {
		return productAll;
	}

	public void setProductAllText(String text) {
		productAll.setText(text);
	}

	public ComboBox<String> getAvailableEmployees() {
		return availableEmployees;
	}

	public ComboBox<String> getSelectProduct() {
		return selectProduct;
	}

	public ComboBox<String> getAssignToMachine() {
		return assignToMachine;
	}
}
