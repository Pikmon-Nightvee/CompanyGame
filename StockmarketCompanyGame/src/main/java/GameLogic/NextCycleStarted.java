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
			sellProduction(reader);
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
	//TODO: Add a safety net to Machine, Products and Resources, so you can not overbuy and go bankrupt immediatly. (Today)
	//TODO: Production in Produce (02.01.2026)
	//TODO: Sell Production (02.01.2026)
	//TODO: Black base to show what changed. (02.01.2026 or the day after that)
	private void productsProduced(ReadCSVFiles reader) {

	}
	private void sellProduction(ReadCSVFiles reader ) {
		
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