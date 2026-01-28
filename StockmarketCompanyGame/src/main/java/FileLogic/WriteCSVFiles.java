package FileLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import GameLogic.Camera;
import GameLogic.Company;
import GameLogic.Employee;
import GameLogic.InteractableObject;
import GameLogic.Machine;
import GameLogic.Player;
import GameLogic.Product;
import GameLogic.Resource;
import GameLogic.Wall;

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
				Employee employeeToInsert = new Employee(employeeCurrent.getName(), employeeCurrent.getAccuracy(), employeeCurrent.getSpeed(), employeeCurrent.getReliability(), employeeCurrent.getCost(), "none");
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
				writer.write(toAdd.getName() + "," + toAdd.getCost() + "," + toAdd.getAccuracy() + "," + toAdd.getSpeed()+ "," +toAdd.getReliability() + "," + toAdd.getMachine());
				writer.write("\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		file = new File("DataCSV/EmployeeData/" + path2);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Employee toAdd : employees) {
				if(!toAdd.getName().equals(id)) {
					writer.write(toAdd.getName() + "," + toAdd.getCost() + "," + toAdd.getAccuracy() + "," + toAdd.getSpeed()+ "," +toAdd.getReliability() + "," + toAdd.getMachine());
					writer.write("\n");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void setEmployeeMachine(String id, String machine) {
		ArrayList<Employee> employees = new ArrayList<>();
		ReadCSVFiles reader = new ReadCSVFiles();
		employees = reader.employedEmployees();		
		
		System.out.println(id);
		
		File file = new File("DataCSV/EmployeeData/EmployedEmployees.csv");
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Employee check : employees) {
				if(!id.equals(check.getName())) {
					writer.write(check.getName() + "," + check.getCost() + "," + check.getAccuracy() + "," + check.getSpeed()+ "," + check.getReliability() + "," + check.getMachine());
					writer.write("\n");
				}else {
					writer.write(id + "," + check.getCost() + "," + check.getAccuracy() + "," + check.getSpeed()+ "," + check.getReliability() + "," + machine);
					writer.write("\n");
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void buySellResources(String id, String toWhere, Company company, int amount) {
		ArrayList<Resource> resources = new ArrayList<>();
		ArrayList<Resource> resourceCheck = new ArrayList<>();
		ReadCSVFiles reader = new ReadCSVFiles();
		
		String path1 = "";
		String path2 = "";
		
		boolean sell = false;
		
		switch(toWhere){
		case "Sold": resources = reader.readResource("ResourcesBought.csv",company); resourceCheck = reader.readResource("ResourcesOnSell.csv",company); path1 = "ResourcesOnSell.csv"; path2 = "ResourcesBought.csv"; sell = true; break;
		case "Bought": resources = reader.readResource("ResourcesOnSell.csv",company); resourceCheck = reader.readResource("ResourcesBought.csv",company); path1 = "ResourcesBought.csv"; path2 = "ResourcesOnSell.csv"; break;
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
					writer.write(toAdd.getName() + "," + toAdd.getAmount() + "," + toAdd.getCost() + "," + company.getCompanyType());
					writer.write("\n");
				}else {
					int amountInsert = amount + toAdd.getAmount();
					writer.write(toAdd.getName() + "," + amountInsert + "," + toAdd.getCost() + "," + company.getCompanyType());
					writer.write("\n");
					resourceNotAlreadyThere = false;
				}
			}
			if(resourceNotAlreadyThere) {
				writer.write(holder.getName() + "," + amount + "," + holder.getCost() + "," + company.getCompanyType());
				writer.write("\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		file = new File("DataCSV/ResourceData/" + path2);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Resource toAdd : resources) {
				if(!toAdd.getName().equals(id)) {
					writer.write(toAdd.getName() + "," + toAdd.getAmount() + "," + toAdd.getCost() + "," + company.getCompanyType());
					writer.write("\n");
				}else {
					if(toAdd.getAmount() > amount) {
						writer.write(toAdd.getName() + "," + (toAdd.getAmount()-amount) + "," + toAdd.getCost() + "," + company.getCompanyType());
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
		ArrayList<Machine> machines = new ArrayList<>();
		ArrayList<Machine> machineCheck = new ArrayList<>();
		ReadCSVFiles reader = new ReadCSVFiles();
		
		String path1 = "";
		String path2 = "";
		
		boolean sell = false;
		
		switch(toWhere){
		case "Sold": machines = reader.readMachines("MachineBought.csv",company); machineCheck = reader.readMachines("MachineNotBought.csv",company); path1 = "MachineNotBought.csv"; path2 = "MachineBought.csv"; sell = true; break;
		case "Bought": machines = reader.readMachines("MachineNotBought.csv",company); machineCheck = reader.readMachines("MachineBought.csv",company); path1 = "MachineBought.csv"; path2 = "MachineNotBought.csv"; break;
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
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			boolean resourceNotAlreadyThere = true;
			for(Machine toAdd : machineCheck) {
				if(!toAdd.getName().equals(id)) {
					writer.write(toAdd.getName() + "," + toAdd.getAmount() + "," + toAdd.getCost() + "," + toAdd.getCondition() + "," + company.getCompanyType());
					writer.write("\n");
				}else {
					int amountInsert = amount + toAdd.getAmount();
					writer.write(toAdd.getName() + "," + amountInsert + "," + toAdd.getCost() + "," + toAdd.getCondition() + "," + company.getCompanyType());
					writer.write("\n");
					resourceNotAlreadyThere = false;
				}
			}
			if(resourceNotAlreadyThere) {
				writer.write(holder.getName() + "," + amount + "," + holder.getCost() + "," + holder.getCondition() + "," + company.getCompanyType());
				writer.write("\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}

		file = new File("DataCSV/EquipmentData/" + path2);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Machine toAdd : machines) {
				if(!toAdd.getName().equals(id)) {
					writer.write(toAdd.getName() + "," + toAdd.getAmount() + "," + toAdd.getCost() + "," + toAdd.getCondition() + "," + company.getCompanyType());
					writer.write("\n");
				}else {
					if(toAdd.getAmount() > amount) {
						writer.write(toAdd.getName() + "," + (toAdd.getAmount()-amount) + "," + toAdd.getCost() + "," + toAdd.getCondition() + "," + company.getCompanyType());
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
		
		if(toWhere.equals("Sold")) {
			employeeNoMachine(id,amount,company,reader,machines);
		}
	}
	public void repairMachine(String id, ReadCSVFiles reader, Company company) {
		ArrayList<Machine> machines = reader.readMachines("MachineBought.csv", company);
		File file = new File("DataCSV/EquipmentData/MachineBought.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			for(Machine m : machines) {
				if(m.getName().equals(id)) {
					writer.write(m.getName() + "," + m.getAmount() + "," + m.getCost() + "," + 10 + "," + company.getCompanyType());
					writer.write("\n");
				}else {
					writer.write(m.getName() + "," + m.getAmount() + "," + m.getCost() + "," + m.getCondition() + "," + company.getCompanyType());
					writer.write("\n");
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private void employeeNoMachine(String machineName, int amountSold, Company company, ReadCSVFiles reader, ArrayList<Machine> machines) {
		ArrayList<String> employeesName = new ArrayList<>();
		ArrayList<Employee> employees = reader.employedEmployees();

		File file = new File("DataCSV/EmployeeData/EmployedEmployees.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			int amountTotalM = 0;
			for(Machine m : machines) {
				if(m.getName().equals(machineName)) {
					amountTotalM = m.getAmount();
				}
			}
			int amountTotalE = 0;
			for(Employee e : employees) {
				if(e.getMachine().equals(machineName)) {
					amountTotalE++;
				}
			}

			int amountEmployeeSell = amountTotalM - amountSold - amountTotalE;
			System.out.println("AmountEmployeeSell: " + amountEmployeeSell + " amountTotalMachines: " + amountTotalM + " amountTotalSold: " + amountSold + " amountTotalEmplyoees: " + amountTotalE);
			boolean sellEmployeeMachines = false;
			if(amountEmployeeSell < 0) {
				amountEmployeeSell *= -1;
				sellEmployeeMachines = true;
			}
			
			if(sellEmployeeMachines) {
				int amount = 0;
				for(Employee e : employees) {
					String machine = e.getMachine();
					if(e.getMachine().equals(machineName)) {
						if(amount < amountEmployeeSell) {
							employeesName.add(e.getName());
							machine = "none";
							amount++;
						}
					}
					System.out.println(e.toString());
					writer.write(e.getName() + "," + e.getCost() + "," + e.getAccuracy() + "," + e.getSpeed()+ "," + e.getReliability() + "," + machine);
					writer.write("\n");
				}
			}else {
				for(Employee e : employees) {
					writer.write(e.getName() + "," + e.getCost() + "," + e.getAccuracy() + "," + e.getSpeed()+ "," + e.getReliability() + "," + e.getMachine());
					writer.write("\n");
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		for(String e : employeesName) {
			deleteProduction(e,company,reader);
		}
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
	
	public void startProduction(String selectedProduct, String asignedEmployee, int selectedAmount, Company company, ReadCSVFiles reader) {
		ArrayList<Product> products = reader.readProducts("Produceable.csv", company);
		
		ArrayList<Product> product = new ArrayList<>();
		for(Product p : products) {
			if(p.getName().equals(selectedProduct)) {
				int cost = p.getCost() * selectedAmount;
				int time = p.getTimePerUnit() * selectedAmount;
				Product toAdd = new Product(p.getName(),selectedAmount,cost,p.getTimePerUnit(),time,p.getQuality(),p.getMachineNeeded(),asignedEmployee,p.getAsignedCompanyType(),p.getResourcesNeeded(),p.getResourcesAmount());
				System.out.println(toAdd.toString());
				product.add(toAdd);
			}
		}
		
		File file = new File("DataCSV/ResourceData/ResourcesBought.csv");
		ArrayList<Resource> resources = reader.readResource("ResourcesBought.csv", company);
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Product p : product) {
				for(Resource r : resources) {
					for(int i = 0; i < p.getResourcesAmount().length; i++) {
						if(p.getResourcesNeeded()[i].equals(r.getName())) {
							if(!(r.getAmount() - (p.getResourcesAmount()[i]*selectedAmount) >= 0)) {
								int amountCounted = -1;
								for(int j = 0; j <= r.getAmount(); j += p.getResourcesAmount()[i]) {
									amountCounted++;
								}
								selectedAmount = amountCounted;
							}
						}
					}
					for(int i = 0; i < p.getResourcesAmount().length; i++) {
						if(p.getResourcesNeeded()[i].equals(r.getName())) {
							for(Product ps : products) {
								if(ps.getName().equals(selectedProduct)) {
									p.setCost(ps.getCost()*selectedAmount);
									p.setTime(ps.getTimePerUnit()*selectedAmount);
								}
							}
							int amountToAdd = r.getAmount()-(p.getResourcesAmount()[i]*selectedAmount);
							if(amountToAdd > 0) {
								writer.write(r.getName()+","+amountToAdd+","+r.getCost()+","+company.getCompanyType());
								writer.write("\n");
							}
						}
					}
					
					StringBuilder builder = new StringBuilder();
					for(int i = 0; i < p.getResourcesAmount().length; i++) {
						builder.append(p.getResourcesNeeded()[i]);
					}
					if(!builder.toString().contains(r.getName())) {
						writer.write(r.getName()+","+r.getAmount()+","+r.getCost()+","+company.getCompanyType());
						writer.write("\n");
					}
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		file = new File("DataCSV/ProductsData/InProduction.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),true))){
			for(Product p : product) {
				writer.write(p.getName()+","+selectedAmount+","+p.getCost()+","+p.getTimePerUnit()+","+p.getTime()+","+p.getQuality()+","+p.getMachineNeeded()+","+asignedEmployee+","+p.getAsignedCompanyType());
				for(int i = 0; i < p.getResourcesNeeded().length; i++) {
					writer.write(","+p.getResourcesNeeded()[i]);
				}
				for(int i = 0; i < p.getResourcesAmount().length; i++) {
					writer.write(","+p.getResourcesAmount()[i]);
				}
				writer.write("\n");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void deleteProduction(String employeeName, Company company, ReadCSVFiles reader) {
		String path = "InProduction.csv";
		String pathR = "ResourcesBought.csv";
		
		ArrayList<Product> products = reader.readProducts(path, company);
		ArrayList<Product> product = new ArrayList<>();
		ArrayList<Resource> resources = reader.readResource(pathR, company);
		ArrayList<Resource> resourcesAll = reader.readResource("ResourceData.csv", company);
		boolean productDeleted = false;
		
		File file = new File("DataCSV/ProductsData/"+path);
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
			for(Product p : products) {
				if(!p.getAsignedEmployee().equals(employeeName)) {
					writer.write(p.getName()+","+p.getAmount()+","+p.getCost()+","+p.getTimePerUnit()+","+p.getTime()+","+p.getQuality()+","+p.getMachineNeeded()+","+p.getAsignedEmployee()+","+p.getAsignedCompanyType());
					for(int i = 0; i < p.getResourcesNeeded().length; i++) {
						writer.write(","+p.getResourcesNeeded()[i]);
					}
					for(int i = 0; i < p.getResourcesAmount().length; i++) {
						writer.write(","+p.getResourcesAmount()[i]);
					}
					writer.write("\n");
				}else {
					product.add(p);
					productDeleted = true;
					System.out.println("Removed: " + p.toString());
				}
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		if(productDeleted) {
			file = new File("DataCSV/ResourceData/"+pathR);
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),false))){
				for(Product p : product) {
					for(int i = 0; i < p.getResourcesNeeded().length;i++) {
						boolean resourceNotAlreadyThere = true;
						for(Resource r : resources) {
							int amount = r.getAmount();
							if(r.getName().equals(p.getResourcesNeeded()[i])) {
								System.out.println("Test:"+p.getResourcesNeeded()[i]);
								int amountInsert = amount + p.getResourcesAmount()[i]*p.getAmount();
								writer.write(r.getName() + "," + amountInsert + "," + r.getCost() + "," + company.getCompanyType());
								writer.write("\n");
								resourceNotAlreadyThere = false;
							}
						}
						if(resourceNotAlreadyThere) {
							for(Resource rA : resourcesAll) {
								if(rA.getName().equals(p.getResourcesNeeded()[i])) {
									writer.write(rA.getName() + "," + (p.getResourcesAmount()[i]*p.getAmount()) + "," + rA.getCost() + "," + company.getCompanyType());
									writer.write("\n");
								}
							}
						}
					}
					for(Resource r : resources) {
						StringBuilder builder = new StringBuilder();
						for(int i = 0; i < p.getResourcesNeeded().length;i++) {
							builder.append(p.getResourcesNeeded()[i]);
						}
						if(!builder.toString().contains(r.getName())) {
							writer.write(r.getName()+","+r.getAmount()+","+r.getCost()+","+company.getCompanyType());
							writer.write("\n");
						}
					}
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public boolean resourcesNotAvailable(String selectedProduct, String asignedEmployee, int selectedAmount, Company company, ReadCSVFiles reader) {		
		ArrayList<Product> products = reader.readProducts("Produceable.csv", company);
		
		ArrayList<Product> product = new ArrayList<>();
		for(Product p : products) {
			if(p.getName().equals(selectedProduct)) {
				int cost = p.getCost() * selectedAmount;
				int time = p.getTimePerUnit() * selectedAmount;
				Product toAdd = new Product(p.getName(),selectedAmount,cost,p.getTimePerUnit(),time,p.getQuality(),p.getMachineNeeded(),asignedEmployee,p.getAsignedCompanyType(),p.getResourcesNeeded(),p.getResourcesAmount());
				System.out.println(toAdd.toString());
				product.add(toAdd);
			}
		}
		
		ArrayList<Resource> resources = reader.readResource("ResourcesBought.csv", company);
		
		boolean missingResource = false;
		for(Product p : product) {
			for(int i = 0; i < p.getResourcesNeeded().length; i++) {
				boolean resourceNotThere = true;
				for(Resource r : resources) {
					if(r.getName().equals(p.getResourcesNeeded()[i])) {
						resourceNotThere = false;
					}
				}
				if(resourceNotThere) {
					missingResource = true;
				}
			}
		}
		
		if(resources.isEmpty() || missingResource) {
			return true;
		}
		
		return false;
	}
	
	public boolean notEnoughResources(String selectedProduct, String asignedEmployee, int selectedAmount, Company company, ReadCSVFiles reader) {
		ArrayList<Product> products = reader.readProducts("Produceable.csv", company);
		
		ArrayList<Product> product = new ArrayList<>();
		for(Product p : products) {
			if(p.getName().equals(selectedProduct)) {
				int cost = p.getCost() * selectedAmount;
				int time = p.getTimePerUnit() * selectedAmount;
				Product toAdd = new Product(p.getName(),selectedAmount,cost,p.getTimePerUnit(),time,p.getQuality(),p.getMachineNeeded(),asignedEmployee,p.getAsignedCompanyType(),p.getResourcesNeeded(),p.getResourcesAmount());
				System.out.println(toAdd.toString());
				product.add(toAdd);
			}
		}

		ArrayList<Resource> resources = reader.readResource("ResourcesBought.csv", company);
		for(Product p : product) {
			for(int i = 0; i < p.getResourcesAmount().length;i++) {
				for(Resource r : resources) {
					if(r.getName().equals(p.getResourcesNeeded()[i])) {
						if(p.getResourcesAmount()[i] > r.getAmount()) {
							System.out.println("Failed");
							return true;
						}
					}
				}
			}
		}
		return false;
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
			
			file = new File("DataCSV/EquipmentData/MachineBroken.csv");
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
			
			file = new File("DataCSV/ProductsData/OnStock.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/ProductsData/InProduction.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/ProductsData/Produceable.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				for(String toInsert : readCSV.readProductsAsString("ProductData.csv")) {
					writer.write(toInsert);
					writer.write("\n");
				}
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/CoordinateData/MachineCoordinates.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/CoordinateData/MachinesPlaced.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
			
			file = new File("DataCSV/CoordinateData/PlayerCords.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				writer.write("");
			}catch(Exception e) {
				e.printStackTrace();
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void savePlayerCords(Player player, Camera camera) {
		double x = player.getX();
		double y = player.getY();
		
		File file = new File("DataCSV/CoordinateData/PlayerCords.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			writer.write(x+","+y);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void coordinatesMachineSafe(Wall w, double x, double y, String machine) {
		File file = new File("DataCSV/CoordinateData/MachineCoordinates.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,true))){
			writer.write(x+","+y+","+w.getWidth()+","+w.getHeight()+","+machine+"\n");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void machineRemoved(ArrayList<Wall> walls, ReadCSVFiles reader) {
		ArrayList<String[]> machineOG = reader.machinesTopDownGet("MachineCoordinates.csv");
		
		File file = new File("DataCSV/CoordinateData/MachineCoordinates.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			for(Wall w : walls) {
				for(String[] s : machineOG) {
					double x = Double.parseDouble(s[0]);
					double y = Double.parseDouble(s[1]);
					if(w.getX() == x && w.getY() == y) {
						writer.write(w.getX()+","+w.getY()+","+w.getWidth()+","+w.getHeight()+","+s[4]+"\n");
					}
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void machineNamePlaceRemove(ReadCSVFiles reader, ArrayList<InteractableObject> arrayList, int extra) {
		ArrayList<String[]> machinePlacedName = reader.machinesTopDownGet("MachinesPlaced.csv");
		ArrayList<String[]> machinePlacedCords = reader.machinesTopDownGet("MachineCoordinates.csv");
		boolean notReduced = true;
		
		System.out.println("Reduce machine" + " size Amount: " + machinePlacedName.size() + " machine Cords Size: " + machinePlacedCords.size());
		
		File file = new File("DataCSV/CoordinateData/MachinesPlaced.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			for(String[] s : machinePlacedName) {
				int amount = Integer.parseInt(s[1]);
				System.out.println("Start Amount: " + amount);
				
				if(notReduced) {
					for(String[] cords : machinePlacedCords) {						
						double x = Double.parseDouble(cords[0]);
						double y = Double.parseDouble(cords[1]);
						
						if(cords[4].equals(s[0])) {
							for(InteractableObject i : arrayList) {
								if(i.getX()+extra == x && i.getY()+extra == y) {
									amount -= 1;
									notReduced = false;
								}
							}
						}
					}
				}
				if(amount > 0) {
					System.out.println("Reduced Amount: " + amount);
					writer.write(s[0]+","+amount);
					writer.write("\n");
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public String machineNamePlacedAdd(int scrollAmount, Company company, ReadCSVFiles reader) {
		String machine = "";
		switch(company.getCompanyType()) {
		case "Foodtruck":
			switch(scrollAmount) {
			case 0:machine = "Ofen";break;
			case 1:machine = "Herd";break;
			}
			break;
		case "Craft Buisness":
			switch(scrollAmount) {
			case 0:machine = "Fräsmaschine";break;
			case 1:machine = "Drehmaschine";break;
			case 2:machine = "Bohrmaschine";break;
			case 3:machine = "Kreissäge";break;
			}
			break;
		case "EDV-Manager":
			switch(scrollAmount) {
			case 0:machine = "PC";break;
			}
			break;
		}
		ArrayList<String[]> machinePlacedName = reader.machinesTopDownGet("MachinesPlaced.csv");
		
		File file = new File("DataCSV/CoordinateData/MachinesPlaced.csv");
		if(isNameContained(machinePlacedName,machine)) {
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				if(!machinePlacedName.isEmpty()) {
					for(String[] s : machinePlacedName) {
						int amount = Integer.parseInt(s[1]);
						if(s[0].equals(machine)) {
							amount += 1;
						}	
						writer.write(s[0]+","+amount);
						writer.write("\n");
					}
				}
			}catch(IOException e) {
				e.printStackTrace();
			}
		}else {
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,true))){
				writer.write(machine+",1");
				writer.write("\n");
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
		return machine;
	}
	
	public void removeMachineCords(String machineName, ReadCSVFiles reader, Company company, int amountNeeded) {
		ArrayList<String[]> machineNamePlaced = reader.machinesTopDownGet("MachinesPlaced.csv");
		ArrayList<String[]> machinePlacedCords = reader.machinesTopDownGet("MachineCoordinates.csv");
		File file = new File("DataCSV/CoordinateData/MachineCoordinates.csv");
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			int amount = 1;
			for(String[] machine : machinePlacedCords) {
				if(!machine[4].equals(machineName) || amount > amountNeeded) {
					writer.write(machine[0]+","+machine[1]+","+machine[2]+","+machine[3]+","+machine[4]+"\n");
				}else {
					amount++;
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		file = new File("DataCSV/CoordinateData/MachinesPlaced.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			for(String[] machine : machineNamePlaced) {
				int amount = Integer.parseInt(machine[1]);
				if(machine[0].equals(machineName)) {
					amount -= amountNeeded;
				}
				if(amount > 0) {
					writer.write(machine[0] + "," + amount + "\n");
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		System.out.println("Machine name: " + machineName);
	}
	
	private boolean isNameContained(ArrayList<String[]> machinesName, String machineName) {
		boolean check = false;
		for(String[] nameCheck : machinesName) {
			if(nameCheck[0].equals(machineName)) {
				check = true;
			}
		}
		return check;
	}
	
	public void machineBroken(ReadCSVFiles reader,Company company) {
		ArrayList<Machine> machines = reader.readMachines("MachineBought.csv", company);
		ArrayList<Machine> alreadyBroken = reader.readMachines("MachineBroken.csv", company);
		
		File file = new File("DataCSV/EquipmentData/MachineBroken.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			for(Machine m : machines) {
				if(m.getCondition() <= 0) {
					for(Machine mB : alreadyBroken) {
						if(mB.getName().equals(m.getName())) {
							writer.write(m.getName()+","+m.getAmount()+","+m.getCost()+","+m.getCondition()+","+company.getCompanyType());
							writer.write("\n");
						}else {
							writer.write(m.getName()+","+m.getAmount()+","+m.getCost()+","+m.getCondition()+","+company.getCompanyType());
							writer.write("\n");
						}
					}
					if(alreadyBroken.isEmpty()) {
						writer.write(m.getName()+","+m.getAmount()+","+m.getCost()+","+m.getCondition()+","+company.getCompanyType());
						writer.write("\n");
					}
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void machineBrokenSold(ReadCSVFiles reader, String id, int sold, Company company) {
		ArrayList<Machine> machines = reader.readMachines("MachineBroken.csv", company);
		File file = new File("DataCSV/EquipmentData/MachineBroken.csv");
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			for(Machine m : machines) {
				int amount = m.getAmount();
				if(m.getName().equals(id)){
					amount -= sold;
				}
				if(amount > 0) {
					System.out.println(m.toString());
					writer.write(m.getName()+","+amount+","+m.getCost()+","+m.getCondition()+","+company.getCompanyType());
					writer.write("\n");
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void machineBrokenRepair(ReadCSVFiles reader, String id, Company company) {
		ArrayList<Machine> machinesBroken = reader.readMachines("MachineBroken.csv", company);
		ArrayList<Machine> machines = reader.readMachines("MachineBought.csv", company);
		File file = new File("DataCSV/EquipmentData/MachineBought.csv");
		
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			for(Machine m : machines) {
				if(!machinesBroken.isEmpty()) {
					boolean nameNotChosen = true;
					for(Machine mB : machinesBroken) {
						if(mB.getName().equals(m.getName())) {
							nameNotChosen = false;
							int conditionMath = mB.getAmount()+1;
							int conditionInsert = m.getCondition();
							if(mB.getName().equals(id)) {
								System.out.println("Brokenamount:"+conditionMath);
								System.out.println("conditioninsert:"+conditionInsert);
								conditionInsert = (int)(10 / conditionMath);
								conditionInsert += m.getCondition();
								System.out.println("Condition Repair:"+conditionInsert);
								if(conditionInsert > 10) {
									conditionInsert = 10;
								}
								if(conditionInsert <= 0) {
									conditionInsert = 1;
								}
								System.out.println("Condition Repair:"+conditionInsert);
							}
							
							writer.write(m.getName()+","+m.getAmount()+","+m.getCost()+","+conditionInsert+","+company.getCompanyType());
							writer.write("\n");
						}
					}
					System.out.println(nameNotChosen);
					if(nameNotChosen) {
						writer.write(m.getName()+","+m.getAmount()+","+m.getCost()+","+m.getCondition()+","+company.getCompanyType());
						writer.write("\n");
					}
				}else {
					writer.write(m.getName()+","+m.getAmount()+","+m.getCost()+","+"10"+","+company.getCompanyType());
					writer.write("\n");
				}
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}