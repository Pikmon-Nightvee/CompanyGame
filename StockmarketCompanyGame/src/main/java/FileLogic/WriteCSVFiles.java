package FileLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import GameLogic.Company;
import GameLogic.Employee;
import GameLogic.Machine;
import GameLogic.Resource;

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
	
	public void buySellResources(String id, String toWhere, Company company, int amount) {
		//TODO: Fill this function out, then this is done.
		ArrayList<Resource> resources = new ArrayList<>();
		ArrayList<Resource> resourceCheck = new ArrayList<>();
		ReadCSVFiles reader = new ReadCSVFiles();
		
		String path1 = "";
		String path2 = "";
		
		boolean sell = false;
		
		switch(toWhere){
		case "Sold": resources = reader.readResource("ResourcesBought.csv"); resourceCheck = reader.readResource("ResourcesOnSell.csv"); path1 = "ResourcesOnSell.csv"; path2 = "ResourcesBought.csv"; sell = true; break;
		case "Bought": resources = reader.readResource("ResourcesOnSell.csv"); resourceCheck = reader.readResource("ResourcesBought.csv"); path1 = "ResourcesBought.csv"; path2 = "ResourcesOnSell.csv"; break;
		}
		
		boolean nothingFound = true;
		Resource holder = null;
		System.out.println(id);
		
		for(Resource resourceCurrent : resources) {
			if(resourceCurrent.getName().contains(id)) {
				nothingFound = false;
				Resource resourceToInsert = new Resource(resourceCurrent.getName(),resourceCurrent.getAmount(),resourceCurrent.getCost());
				holder=resourceToInsert;
			}
		}
		
		if(nothingFound) {
			System.out.println("No Resource was found!");
			return;
		}
		File file = new File("DataCSV/ResourceData/" + path1);

		if(holder.getAmount() < amount) {
			amount = holder.getAmount();
		}
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			boolean resourceNotAlreadyThere = true;
			for(Resource toAdd : resourceCheck) {
				if(!toAdd.getName().equals(id)) {
					writer.write(toAdd.getName() + "," + toAdd.getAmount() + "," + toAdd.getCost());
					writer.write("\n");
				}else {
					int amountInsert = amount + toAdd.getAmount();
					writer.write(toAdd.getName() + "," + amountInsert + "," + toAdd.getCost());
					writer.write("\n");
					resourceNotAlreadyThere = false;
				}
			}
			if(resourceNotAlreadyThere) {
				writer.write(holder.getName() + "," + amount + "," + holder.getCost());
				writer.write("\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		file = new File("DataCSV/ResourceData/" + path2);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Resource toAdd : resources) {
				if(!toAdd.getName().equals(id)) {
					writer.write(toAdd.getName() + "," + toAdd.getAmount() + "," + toAdd.getCost());
					writer.write("\n");
				}else {
					if(toAdd.getAmount() > amount) {
						writer.write(toAdd.getName() + "," + (toAdd.getAmount()-amount) + "," + toAdd.getCost());
						writer.write("\n");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		double costToCompany = amount * holder.getCost();
		double money = company.getMoneyOfCompany()-costToCompany;
		if(sell) {
			money = company.getMoneyOfCompany()+costToCompany;
		}
		company.setMoneyOfCompany(money);
	}
	
	public void buySellMachines(String id, String toWhere, Company company, int amount) {
		//TODO: Fill this function out, then this is done.
		ArrayList<Machine> machines = new ArrayList<>();
		ArrayList<Machine> machineCheck = new ArrayList<>();
		ReadCSVFiles reader = new ReadCSVFiles();
		
		String path1 = "";
		String path2 = "";
		
		boolean sell = false;
		
		switch(toWhere){
		case "Sold": machines = reader.readMachines("MachineBought.csv"); machineCheck = reader.readMachines("MachineNotBought.csv"); path1 = "MachineNotBought.csv"; path2 = "MachineBought.csv"; sell = true; break;
		case "Bought": machines = reader.readMachines("MachineNotBought.csv"); machineCheck = reader.readMachines("MachineBought.csv"); path1 = "MachineBought.csv"; path2 = "MachineNotBought.csv"; break;
		}
		
		boolean nothingFound = true;
		Machine holder = null;
		System.out.println(id);
		
		for(Machine machineCurrent : machines) {
			if(machineCurrent.getName().contains(id)) {
				nothingFound = false;
				Machine machineToInsert = new Machine(machineCurrent.getName(),machineCurrent.getAmount(),machineCurrent.getCost(),machineCurrent.getCondition());
				holder=machineToInsert;
			}
		}
		
		if(nothingFound) {
			System.out.println("No Machine was found!");
			return;
		}
		File file = new File("DataCSV/EquipmentData/" + path1);

		if(holder.getAmount() < amount) {
			amount = holder.getAmount();
		}
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),true))){
			int amountInsert = amount;
			for(Machine machineCurrent : machineCheck) {
				if(machineCurrent.getName().contains(id)) {
					amount += machineCurrent.getAmount();
				}
			}
			writer.write(holder.getName() + "," + amountInsert + "," + holder.getCost() + "," + holder.getCondition());
			writer.write("\n");
		}catch(Exception e) {
			e.printStackTrace();
		}

		file = new File("DataCSV/EquipmentData/" + path2);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Machine toAdd : machines) {
				if(!toAdd.getName().equals(id)) {
					writer.write(toAdd.getName() + "," + toAdd.getAmount() + "," + toAdd.getCost() + "," + toAdd.getCondition());
					writer.write("\n");
				}else {
					if(toAdd.getAmount() > amount) {
						writer.write(toAdd.getName() + "," + (toAdd.getAmount()-amount) + "," + toAdd.getCost() + "," + toAdd.getCondition());
						writer.write("\n");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		double costToCompany = amount * holder.getCost();
		double money = company.getMoneyOfCompany()-costToCompany;
		if(sell) {
			money = company.getMoneyOfCompany()+costToCompany;
		}
		company.setMoneyOfCompany(money);
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
	
	public void companyDataSave(String name, double money, int reputation, String companyType) {
		try {
			File file = new File("DataCSV/GameStartUp/CompanyData.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
				writer.write(name + "," + money + "," + reputation + "," + companyType);
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
			
			file = new File("DataCSV/EquipmentData/MachineBought.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/EquipmentData/MachineNotBought.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				for(String toInsert : readCSV.readMachineAsString("MachinesData.csv")) {
					writer.write(toInsert);
					writer.write("\n");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/ResourceData/ResourcesBought.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/ResourceData/ResourcesOnSell.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				for(String toInsert : readCSV.readResourceAsString("ResourceData.csv")) {
					writer.write(toInsert);
					writer.write("\n");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}