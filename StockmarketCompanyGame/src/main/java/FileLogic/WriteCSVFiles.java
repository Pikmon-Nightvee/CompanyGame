package FileLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import GameLogic.Employee;

public class WriteCSVFiles {
	public void manageEmployeeInFiles(String id, String toWhere) {
		ArrayList<Employee> employees = new ArrayList<>();
		ReadCSVFiles reader = new ReadCSVFiles();
		
		String path1 = "";
		String path2 = "";
		
		switch(toWhere){
		case "toUnemployed":employees = reader.employedEmployees();path1 = "UnemployedEmployees.csv";path2 = "EmployedEmployees.csv";;break;
		case "toEmployed":employees = reader.unemployedEmployees();path1 = "EmployedEmployees.csv";path2 = "UnemployedEmployees.csv";break;
		}
		
		boolean nothingFound = true;
		ArrayList<Employee> holder = new ArrayList<>();
		System.out.println(id);
		
		for(Employee employeeCurrent : employees) {
			System.out.println(employeeCurrent.getName());
			if(employeeCurrent.getName().equals(id)) {
				nothingFound = false;
				Employee employeeToInsert = new Employee(employeeCurrent.getName(), employeeCurrent.getAccuracy(), employeeCurrent.getSpeed(), employeeCurrent.getReliability(), employeeCurrent.getCost());
				holder.add(employeeToInsert);
			}
		}
		
		if(nothingFound) {
			System.out.println("No employee was found!");
			return;
		}
		File file = new File("DataCSV/EmployeeData/" + path1);
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),true))){
			for(Employee toAdd : holder) {
				writer.write(toAdd.getName() + "," + toAdd.getCost() + "," + toAdd.getAccuracy() + "," + toAdd.getSpeed()+ "," +toAdd.getReliability());
				writer.write("\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		file = new File("DataCSV/EmployeeData/" + path2);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Employee toAdd : employees) {
				if(!toAdd.getName().equals(id)) {
					writer.write(toAdd.getName() + "," + toAdd.getCost() + "," + toAdd.getAccuracy() + "," + toAdd.getSpeed()+ "," +toAdd.getReliability());
					writer.write("\n");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void companyDataLastPlayed(String name, String money, String reputation) {
		
	}
	
	public void gameWasPlayed() {
		try {
			File file = new File("DataCSV/GameStartUp/GameAlreadyStarted.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
				writer.write("true");
				writer.write("\n");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void resetData(ReadCSVFiles readCSV) {
		try {
			//Game State Reset:
			File file = new File("DataCSV/GameStartUp/GameAlreadyStarted.csv");
			System.out.println("Important File: Game State created");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
				writer.write("false");
				writer.write("\n");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/GameStartUp/CompanyData.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			//Employee Reset
			file = new File("DataCSV/EmployeeData/UnemployedEmployees.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				for(String unemployed : readCSV.allEmployees()) {
					writer.write(unemployed);
					writer.write("\n");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/EmployeeData/EmployedEmployees.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}