package Visual;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class VisualElementsHolder {
	private int amount = 0;
	
	//Labels:
	private Label moneyOfCompany = new Label("");
	private Label nameOfCompany = new Label("");
	private Label enterName = new Label("Enter the companys name");
	private Label difficulty = new Label("Game difficulty:");
	private Label chooseCompanyType = new Label("Company Type:");
	private Label chooseCompanyWork = new Label("Company Specilty:");
	
	//TextAreas:
	private TextField insertName = new TextField();
	
	//ComboBox:
	private ComboBox <String>selectDifficulty = new ComboBox<>();
	private ComboBox <String>selectCycleAmount = new ComboBox<>();
	private ComboBox <String>selectCompanyType = new ComboBox<>();
	private ComboBox <String>selectCompanySpecification = new ComboBox<>();
	
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
		selectCycleAmount.getItems().add("Month");
		selectCycleAmount.getItems().add("Year");
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
	private void CSSTextArea(TextField field) {
		field.setPrefSize(5, 1);
	}
	
	public void start() {
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
		
		CSSTextArea(insertName);
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
}
