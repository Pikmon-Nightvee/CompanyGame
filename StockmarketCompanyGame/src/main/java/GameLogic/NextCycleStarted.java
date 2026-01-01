package GameLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import FileLogic.ReadCSVFiles;

public class NextCycleStarted {
	private long timePassed=0;
	
	public void nextDay(String time, ReadCSVFiles reader, Company company) {
		int days=0;
		switch(time) {
		case "Day": days=1;break;
		case "Week": days=5;break;
		case "Month": days=25;break;
		case "Year": days=300;break;
		}
		for(int i = 0; i < days; i++) {
			timePassed++;
			
			productsProduced(reader);
			sellProduction(reader,company);
			if(timePassed%5==0) {
				newResources(reader,company);
			}
			if(timePassed%25==0) {
				payEmployeeWages(reader,company);
			}
			if(timePassed%300==0) {
				newMachine(reader,company);
			}
		}
		setTimeStart();
	}
	//TODO: Production in Produce (02.01.2026)
	//TODO: Black base to show what changed. (02.01.2026 or the day after that)
	private void productsProduced(ReadCSVFiles reader) {

	}
	private void sellProduction(ReadCSVFiles reader, Company company) {
		if(!reader.readProductsAsString("OnStock.csv").isEmpty()) {
			ArrayList<Product> products = reader.readProducts("OnStock.csv", company);
			File file = new File("DataCSV/ProductsData/OnStock.csv");
			try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
				for(Product p : products) {
					System.out.println(p.toString());
					int soldRange = 3;
					
					switch(p.getQuality()) {
					case "Standard":soldRange=3;break;
					case "Gut":soldRange=2;break;
					case "Hoch":soldRange=1;break;
					}
					
					int wasSold = (int)(Math.random()*5);
					
					if(company.getReputation()<=25) {
						wasSold--;
					}else if(50<company.getReputation()&&company.getReputation()<=75) {
						wasSold++;
					}else {
						wasSold+=2;
					}
					
					int amountLeft = p.getAmount();
					
					if(wasSold > soldRange) {
						int amountSold = (int)(Math.random()*p.getAmount())+1;
						amountLeft -= amountSold;
						
						int moneyMade = amountSold * p.getCost();
						double moneyTotal = moneyMade + company.getMoneyOfCompany();
						
						p.setAmount(amountLeft);
						company.setMoneyOfCompany(moneyTotal);
					}
		
					if(amountLeft > 0) {
						writer.write(p.getName()+","+amountLeft+","+p.getCost()+","+p.getTimePerUnit()+","+p.getTime()+","+p.getQuality()+","+p.getMachineNeeded()+","+p.getAsignedEmployee()+","+p.getAsignedCompanyType());
						for(int i = 0; i < p.getResourcesNeeded().length; i++) {
							writer.write(","+p.getResourcesNeeded()[i]);
						}
						for(int i = 0; i < p.getResourcesAmount().length; i++) {
							writer.write(","+p.getResourcesAmount()[i]);
						}
						writer.write("\n");
					}
				}
				System.out.println("Done");
			}catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	private void payEmployeeWages(ReadCSVFiles reader, Company company) {
		System.out.println("Pay Salary");
		ArrayList<Employee> employees = reader.employedEmployees();
		for(Employee e : employees) {
			double money = company.getMoneyOfCompany() - e.getCost();
			company.setMoneyOfCompany(money);
		}
	}
	private void newResources(ReadCSVFiles reader, Company company) {
		System.out.println("New Resource");
		
		ArrayList<Resource> resourcesOG = reader.readResource("ResourceData.csv", company);
		ArrayList<Resource> resources = reader.readResource("ResourcesOnSell.csv", company);
		File file = new File("DataCSV/ResourceData/ResourcesOnSell.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
			for(Resource r : resourcesOG) {
				int resourceStockAdd = (int)(Math.random()*15)+1;
				for(Resource rOnSell : resources) {
					if(rOnSell.getName().equals(r.getName())) {
						resourceStockAdd += rOnSell.getAmount();
					}
				}
				writer.write(r.getName()+","+resourceStockAdd+","+r.getCost()+","+company.getCompanyType());
				writer.write("\n");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void newMachine(ReadCSVFiles reader, Company company) {
		System.out.println("New Machine");
		
		ArrayList<Machine> machinesOG = reader.readMachines("MachinesData.csv", company);
		ArrayList<Machine> machines = reader.readMachines("MachineNotBought.csv", company);
		File file = new File("DataCSV/EquipmentData/MachineNotBought.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){
			for(Machine m : machinesOG) {
				int machineStockAdd = (int)(Math.random()*5)+1;
				for(Machine mOnSell : machines) {
					if(mOnSell.getName().equals(m.getName())) {
						machineStockAdd += mOnSell.getAmount();
					}
				}
				writer.write(m.getName()+","+machineStockAdd+","+m.getCost()+","+m.getCondition()+","+company.getCompanyType());
				writer.write("\n");
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	private void setTimeStart() {
		File file = new File("DataCSV/GameStartUp/GameCycleData.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			writer.write((int)timePassed + "\n");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public long getTimePassed() {
		return timePassed;
	}
	public void setTimePassed(long timePassed) {
		this.timePassed = timePassed;
	}
	public void readTimeStart() {
		File file = new File("DataCSV/GameStartUp/GameCycleData.csv");
		try(Scanner reader = new Scanner(file)){
			String longInsert = reader.nextLine();
			if(!longInsert.isBlank())	timePassed = Long.parseLong(longInsert);
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
}