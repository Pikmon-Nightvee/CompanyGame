package FileLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import GameLogic.Company;
import GameLogic.Employee;

public class ReadCSVFiles {
	public Company gameAlreadyPlayedCompanyData() {
		Company company = new Company("",0.0);
		return company;
	}
	
	public boolean gameAlreadyStarted() {
		return false;
	}
	
	public ArrayList<Employee> employedEmployees() {
		File file = new File("DataCSV/EmployeeData/EmployedEmployees.csv");
		ArrayList<Employee> employees = new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String dataLine = reader.nextLine();
				String[] data = dataLine.split(",");
				
				String name = data[0];
				int cost = Integer.parseInt(data[1]);
				int accuracy = Integer.parseInt(data[2]);
				int speed = Integer.parseInt(data[3]);
				int reliability = Integer.parseInt(data[4]);
				
				Employee employeeToAdd = new Employee(name,accuracy,speed,reliability,cost);
				employees.add(employeeToAdd);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		for(Employee e : employees) {
			System.out.println(e.toString());
		}
		
		return employees;
	}
}