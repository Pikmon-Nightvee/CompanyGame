package FileLogic;

import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import GameLogic.Company;
import GameLogic.Employee;
import GameLogic.Machine;
import GameLogic.Resource;

public class ReadCSVFiles {
	public Company gameAlreadyPlayedCompanyData() {
		File file = new File("DataCSV/GameStartUp/CompanyData.csv");
		Company company = new Company("",0.0,"");
		
		try(Scanner reader = new Scanner(file)){
			String dataLine = reader.nextLine();
			String[] data = dataLine.split(",");
			
			String name = data[0];
			double money = Double.parseDouble(data[1]);
			int reputation = Integer.parseInt(data[2]);
			String companyType = data[3];
			
			company.setName(name);
			company.setMoneyOfCompany(money);
			company.setReputation(reputation);
			company.setCompanyType(companyType);
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		System.out.println(company.toString());
		return company;
	}
	
	public boolean gameAlreadyStarted() {
		File file = new File("DataCSV/GameStartUp/GameAlreadyStarted.csv");
		boolean alreadyStarted = false;
		
		try(Scanner reader = new Scanner(file)){
			String dataLine = reader.nextLine();
			if(dataLine.equals("true")) {
				alreadyStarted = true;
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return alreadyStarted;
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
				String machine = data[5];
				
				Employee employeeToAdd = new Employee(name,accuracy,speed,reliability,cost,machine);
				employees.add(employeeToAdd);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return employees;
	}
	
	public ArrayList<Employee> unemployedEmployees() {
		File file = new File("DataCSV/EmployeeData/UnemployedEmployees.csv");
		ArrayList<Employee> employees = new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String dataLine = reader.nextLine();
				String[] data = dataLine.split(",");
				
				try {
					String name = data[0];
					int cost = Integer.parseInt(data[1]);
					int accuracy = Integer.parseInt(data[2]);
					int speed = Integer.parseInt(data[3]);
					int reliability = Integer.parseInt(data[4]);
					String machine = data[5];
					
					Employee employeeToAdd = new Employee(name,accuracy,speed,reliability,cost,machine);
					employees.add(employeeToAdd);
				}catch(Exception e) {
					System.out.println(e.getMessage());
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return employees;
	}
	
	public ArrayList<String> allEmployees() {
		File file = new File("DataCSV/EmployeeData/EmployeeManager.csv");
		ArrayList<String> unEmployed = new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String dataLine = reader.nextLine();
				unEmployed.add(dataLine);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return unEmployed;
	}
	
	public ArrayList<Machine> readMachines(String path,Company company){
		String filePath = "DataCSV/EquipmentData/"  + path;
		File file = new File(filePath);
		ArrayList<Machine> machines= new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String readLine = reader.nextLine();
				String[] dataInLine = readLine.split(",");
				
				String name = dataInLine[0];
				int amount = Integer.parseInt(dataInLine[1]);
				int cost = Integer.parseInt(dataInLine[2]);
				int reliability = Integer.parseInt(dataInLine[3]);
				System.out.println(dataInLine[4]);
				
				if(company.getCompanyType().equals(dataInLine[4]) || dataInLine[4].equals("All")) {
					Machine newMachine = new Machine(name,amount,cost,reliability);
					machines.add(newMachine);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		for(Machine m : machines) {
			System.out.println(m.toString());
		}
		
		return machines;
	}
	
	public ArrayList<Resource> readResource(String path,Company company){
		String filePath = "DataCSV/ResourceData/"  + path;
		File file = new File(filePath);
		ArrayList<Resource> resources= new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String readLine = reader.nextLine();
				String[] dataInLine = readLine.split(",");
				
				String name = dataInLine[0];
				int amount = Integer.parseInt(dataInLine[1]);
				int cost = Integer.parseInt(dataInLine[2]);
				System.out.println(dataInLine[3]);
				
				if(company.getCompanyType().equals(dataInLine[3]) || dataInLine[3].equals("All")) {
					Resource newResource = new Resource(name,amount,cost);
					resources.add(newResource);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		for(Resource r : resources) {
			System.out.println(r.toString());
		}
		
		return resources;
	}
	
	public ArrayList<String> readResourceAsString(String path){
		String filePath = "DataCSV/ResourceData/"  + path;
		File file = new File(filePath);
		ArrayList<String> resources= new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				resources.add(reader.nextLine());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return resources;
	}
	
	public ArrayList<String> readMachineAsString(String path){
		String filePath = "DataCSV/EquipmentData/"  + path;
		File file = new File(filePath);
		ArrayList<String> machines = new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				machines.add(reader.nextLine());
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return machines;
	}
}