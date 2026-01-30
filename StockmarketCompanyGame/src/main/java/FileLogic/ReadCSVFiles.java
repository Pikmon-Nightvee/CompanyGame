package FileLogic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import GameLogic.Camera;
import GameLogic.Company;
import GameLogic.Employee;
import GameLogic.InteractableObject;
import GameLogic.LevelHolder;
import GameLogic.Machine;
import GameLogic.MachinePlaceObject;
import GameLogic.Player;
import GameLogic.Product;
import GameLogic.Resource;
import GameLogic.Wall;

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
				
				if(company.getCompanyType().equals(dataInLine[4]) || dataInLine[4].equals("All")) {
					Machine newMachine = new Machine(name,amount,cost,reliability);
					machines.add(newMachine);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
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
	
	public ArrayList<Product> readProducts(String path, Company company){
		String filePath = "DataCSV/ProductsData/"  + path;
		File file = new File(filePath);
		ArrayList<Product> products = new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String data = reader.nextLine();
				String[] dataInLine = data.split(",");
				
				String name = dataInLine[0];
				int amount = Integer.parseInt(dataInLine[1]);
				int cost = Integer.parseInt(dataInLine[2]);
				int timePerUnit = Integer.parseInt(dataInLine[3]);
				int time = Integer.parseInt(dataInLine[4]);
				String quality = dataInLine[5];
				String machineNeeded = dataInLine[6];
				String asignedEmployee = dataInLine[7];
				String companyType = dataInLine[8];
					
				if(company.getCompanyType().equals(companyType)) {
					int i = 9;
					StringBuilder builder = new StringBuilder();
					
					for(int j = i; j < dataInLine.length; j++) {
						if(!dataInLine[j].contains("1") && !dataInLine[j].contains("2") && !dataInLine[j].contains("3") && !dataInLine[j].contains("4") && !dataInLine[j].contains("5") && !dataInLine[j].contains("6") && !dataInLine[j].contains("7") && !dataInLine[j].contains("8") && !dataInLine[j].contains("9") && !dataInLine[j].contains("0")) {
							builder.append(dataInLine[j]);
							builder.append(",");
							i++;
						}
					}
					System.out.println(builder.toString());
						
					String toSplit = builder.toString();
					String[] resources = toSplit.split(",");
	
					System.out.println(i);
					StringBuilder builderTwo = new StringBuilder();
					for(int j = i; j < dataInLine.length; j++) {
						builderTwo.append(dataInLine[j]);
						if(j + 1 < dataInLine.length) {
							builderTwo.append(",");
						}
					}
	
					toSplit = builderTwo.toString();
					String[] resourceAmountHolder = toSplit.split(",");
					int[] resourceAmount = new int[resourceAmountHolder.length];
					for(int j = 0; j < resourceAmount.length; j++) {
						resourceAmount[j] = Integer.parseInt(resourceAmountHolder[j]);
						System.out.println(j);
					}
						
					Product toAdd = new Product(name,amount,cost,timePerUnit,time,quality,machineNeeded,asignedEmployee,companyType,resources,resourceAmount);
					products.add(toAdd);
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return products;
	}
	
	public ArrayList<String> readProductsAsString(String path){
		String filePath = "DataCSV/ProductsData/"  + path;
		File file = new File(filePath);
		ArrayList<String> products = new ArrayList<>();
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String data = reader.nextLine();
				products.add(data);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return products;
	}
	
	public ArrayList<Employee> employeeAbleToProduce(Company company, String selected){
		ArrayList<Employee> employees = employedEmployees();
		ArrayList<Product> products = readProducts("Produceable.csv",company);
		ArrayList<Employee> employeesToAdd = new ArrayList<>();
		for(Employee employee : employees) {
			for(Product product : products) {
				if(product.getName().equals(selected)) {
					if(notAlreadyAsigned(company,employee.getName())) {
						if(product.getMachineNeeded().equals(employee.getMachine())) {
							employeesToAdd.add(employee);
						}
					}
				}
			}
		}
		
		return employeesToAdd;
	}
	
	public boolean notAlreadyAsigned(Company company,String employeeName) {
		boolean returnValue = true;
		ArrayList<Product> products = readProducts("InProduction.csv",company);
		for(Product p : products) {
			System.out.println(employeeName);
			System.out.println(p.getAsignedEmployee());
			if(p.getAsignedEmployee().equals(employeeName)) {
				returnValue = false;
			}
		}
		return returnValue;
	}
	
	public ArrayList<MachinePlaceObject> machinesPlaced(){
		ArrayList<MachinePlaceObject> machines = new ArrayList<>();
		File file = new File("DataCSV/CoordinateData/MachineCoordinates.csv");
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String currentLine = reader.nextLine();
				String[] data = currentLine.split(",");
				double xCoordinate = Double.parseDouble(data[0]);
				double yCoordinate = Double.parseDouble(data[1]);
				double width = Double.parseDouble(data[2]);
				double height = Double.parseDouble(data[3]);
				String machine = data[4];
				
				MachinePlaceObject toAdd = new MachinePlaceObject(xCoordinate,yCoordinate,width,height,machine);
				machines.add(toAdd);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return machines;
	}
	
	public ArrayList<String[]> machinesTopDownGet(String dataPath){
		ArrayList<String[]> all = new ArrayList<>();
		String location = "DataCSV/CoordinateData/" + dataPath;
		File file = new File(location);
		
		try(Scanner reader = new Scanner(file)){
			while(reader.hasNext()) {
				String currentLine = reader.nextLine();
				String[] data = currentLine.split(",");
				all.add(data);
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return all;
	}
	
	public ArrayList<InteractableObject> activateBlink(Company company, LevelHolder level){
		ArrayList<InteractableObject> toAdd = new ArrayList<>();
		
		ArrayList<Machine> broken = readMachines("MachineBroken.csv",company);
		ArrayList<String[]> machines = machinesTopDownGet("MachineCoordinates.csv");
		
		if(!broken.isEmpty()) {
			for(Machine m : broken) {
				int amount = 0;
				for(String[] mString : machines) {
					if(mString[4].equals(m.getName())) {
						if(amount < m.getAmount()) {
							double xCoordinate = Double.parseDouble(mString[0]);
							double yCoordinate = Double.parseDouble(mString[1]);
							double width = Double.parseDouble(mString[2]);
							double height = Double.parseDouble(mString[3]);
							InteractableObject i = new InteractableObject(xCoordinate,yCoordinate,width,height,false,true);
							toAdd.add(i);
						}
						amount++;
					}
				}
			}
		}
		
		return toAdd;
	}
	
	public boolean isAdded() {
		String location = "DataCSV/CoordinateData/Playercords.csv";
		File file = new File(location);
		
		try(Scanner reader = new Scanner(file)){
			if(!reader.hasNext()) {
				return false;
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		return true;
	}
	
	public void setPlayerCord(Player player) {
		String location = "DataCSV/CoordinateData/Playercords.csv";
		File file = new File(location);
		
		try(Scanner reader = new Scanner(file)){
			String line = reader.nextLine();
			String[] data = line.split(",");
			double x = Double.parseDouble(data[0]);
			double y = Double.parseDouble(data[1]);
			
			
			player.setX(x);
			player.setY(y);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}