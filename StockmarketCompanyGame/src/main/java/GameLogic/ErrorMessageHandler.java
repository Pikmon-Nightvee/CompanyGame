package GameLogic;

import java.util.ArrayList;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ErrorMessageHandler {
	private ArrayList<Boolean> errorRecieved = new ArrayList<>();
	//Adds the error Label for a ComboBox
	public void errorMessageComboBox(ComboBox<String> comboBox, VBox vBox) {
		if(alreadyExistsCombo(vBox, comboBox)) {
			Label error = new Label("Error");
			int index = 0;
			
			if(vBox.getChildren().contains(error)) {
				index = vBox.getChildren().indexOf(error);
				vBox.getChildren().remove(index);
			}
			
			index = vBox.getChildren().indexOf(comboBox);
			System.out.println("index: " + index);
	
			error.setStyle("-fx-text-fill: red;-fx-font-weight: bold;");
			comboBox.setStyle("-fx-border-color: red; -fx-border-width: 2px;-fx-font-weight: bold;");
			
			vBox.getChildren().set(index, comboBox);
			vBox.getChildren().add(index, error);
			
			errorRecieved.add(true);
		}
	}
	//Deletes the Error Label for a ComboBox.
	public void errorMessageHandlerComboBox(ComboBox<String> comboBox, VBox vBox) {
		if(check()) {
			int index = vBox.getChildren().indexOf(comboBox) - 1;
			Label textBox = (Label)vBox.getChildren().get(index);
			System.out.println(textBox);		
			if(textBox.getText().contains("Error")) {
				errorRecieved.remove(0);
				vBox.getChildren().remove(index);
				comboBox.setStyle("-fx-font-size:15px;-fx-font-weight: bold;");	
			}
		}
	}
	//Checks if there is a true in the List.
	private boolean check() {
		boolean errorTrue = false;
		
		for(boolean error : errorRecieved ) {
			if(error) {
				errorTrue = error;
				break;
			}
		}
		
		return errorTrue;
	}
	//Checks, if the Label above is, is the Error label and if yes, it does not create another one.
	private boolean alreadyExistsCombo(VBox vBox, ComboBox<String> comboBox) {
		boolean doesNotExist = true;
		
		try {
			int index = vBox.getChildren().indexOf(comboBox) - 1;
			Label textBox = (Label)vBox.getChildren().get(index);
			System.out.println(textBox);		
			if(textBox.getText().contains("Error")) {
					doesNotExist = false;
			}
		}catch(Exception e){
			System.out.println("Cannot Cast Button to Label!");
		}
		
		return doesNotExist;
	}
	//Does the same, now only for a TextField.
	private boolean alreadyExistsText(VBox vBox, TextField text) {
		boolean doesNotExist = true;
		
		try {
			int index = vBox.getChildren().indexOf(text) - 1;
			Label textBox = (Label)vBox.getChildren().get(index);
			System.out.println(textBox);		
			if(textBox.getText().contains("Error")) {
					doesNotExist = false;
			}
		}catch(Exception e){
			System.out.println("Cannot Cast Button to Label!");
		}
		System.out.println("existance Status: " + doesNotExist);
		return doesNotExist;
	}
	
	public void errorMessageText(TextField text, VBox vBox) {
		if(alreadyExistsText(vBox, text)) {
			Label error = new Label("Error");
			int index = 0;
			
			if(vBox.getChildren().contains(error)) {
				index = vBox.getChildren().indexOf(error);
				vBox.getChildren().remove(index);
			}
			
			index = vBox.getChildren().indexOf(text);
			System.out.println("index: " + index);
	
			error.setStyle("-fx-text-fill: red;");
			text.setStyle("-fx-border-color: red; -fx-border-width: 2px;");
			vBox.getChildren().add(index, error);
	
			errorRecieved.add(true);
		}
	}
	
	public void errorMessageHandlerText(TextField text, VBox vBox) {
		if(check()) {
			int index = vBox.getChildren().indexOf(text) - 1;
			Label textBox = (Label)vBox.getChildren().get(index);
			System.out.println(textBox);		
			if(textBox.getText().contains("Error")) {
				errorRecieved.remove(0);
				vBox.getChildren().remove(index);
				text.setStyle("-fx-font-size:15px;");	
			}
		}
	}
	//Clears the entire list of true and false booleans.
	public void clearList() {
		errorRecieved.clear();
	}
}