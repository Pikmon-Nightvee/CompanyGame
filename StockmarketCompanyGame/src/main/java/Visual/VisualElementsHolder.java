package Visual;

import java.util.ArrayList;

import FileLogic.ReadCSVFiles;
import GameLogic.Employee;
import javafx.scene.canvas.Canvas;
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
	private Label assignedEmployee = new Label("Asign employee to");
	private Label hiredEmployees = new Label("Hired:");
	private Label fireEmployee = new Label("Fire");
	
	//TextFields:
	private TextField insertName = new TextField();

	//TextAreas:
	private TextArea unemployedStats = new TextArea();
	private TextArea employedStats = new TextArea();
	
	//ComboBox:
	private ComboBox <String>selectDifficulty = new ComboBox<>();
	private ComboBox <String>selectCycleAmount = new ComboBox<>();
	private ComboBox <String>selectCompanyType = new ComboBox<>();
	private ComboBox <String>selectCompanySpecification = new ComboBox<>();
	private ComboBox <String>selectUnemployed = new ComboBox<>();

	private ComboBox <String>assignEmployed = new ComboBox<>();
	
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
		
		ArrayList<Employee> unemployed = new ArrayList<>();
		unemployed = readerCSV.employedEmployees();
		for(Employee employedEmployee : unemployed) {
			builder.append(employedEmployee.toString());
		}
		text = builder.toString();
		
		employedStats.setText(text);
	}
	
	public void setTextAreaSize(String select,int size, Canvas canvas) {
		switch(select){
		case "Unemployed": unemployedStats.setPrefSize(canvas.getWidth(), size); System.out.println(size);break;
		case"Employed":employedStats.setPrefSize(canvas.getWidth(), size); System.out.println(size);break;
		}
	}
	
	private void insertUnemployed() {
		ArrayList<Employee> unemployed = new ArrayList<>();
		unemployed = readerCSV.unemployedEmployees();
		for(Employee unemployedEmployee : unemployed) {
			selectUnemployed.getItems().add(unemployedEmployee.getName());
		}
	}
	
	private void insertEmployed() {
		ArrayList<Employee> unemployed = new ArrayList<>();
		unemployed = readerCSV.employedEmployees();
		for(Employee employedEmployee : unemployed) {
			assignEmployed.getItems().add(employedEmployee.getName());
		}
	}
	
	private void CSSBox(ComboBox<String> comboBox) {
		amount += 1;
		comboBox.setPrefSize(200, 20);
		comboBox.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	public void CSSLabel(Label label) {
		amount += 1;
		label.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
	}
	private void CSSBoxNoAddAmount(ComboBox<String> comboBox) {
		comboBox.setPrefSize(200, 20);
		comboBox.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	public void CSSLabelNoAddAmount(Label label) {
		label.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
	}
	private void CSSTextField(TextField field) {
		field.setPrefSize(5, 1);
	}
	private void CSSTextArea(TextArea area) {
		area.setPrefSize(5, 1);
	}
	
	public void start() {
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
		
		CSSTextField(insertName);

		CSSTextArea(unemployedStats);
		CSSTextArea(employedStats);
		unemployedStats.setEditable(false);
		employedStats.setEditable(false);
		unemployedTextArea();
		employedTextArea();
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
	
	public void subSelectEmployed() {
		subAmount = 0;
		CSSBoxSubSelect(assignEmployed);
		
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
		comboBox.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");
	}
	public void CSSLabelSubSelect(Label label) {
		subAmount += 1;
		label.setStyle("-fx-font-size: 20px;-fx-font-weight: bold;");
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

	public ComboBox<String> getAssignEmployed() {
		return assignEmployed;
	}

	public Label getFireEmployee() {
		return fireEmployee;
	}
}
