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
				newResources();
			}
			if(timePassed%25==0) {
				payEmployeeWages(reader,company);
			}
			if(timePassed%300==0) {
				newMachine();
			}
		}
		setTimeStart();
	}
	//TODO: Production in Produce, Sell Production (Tomorrow)
	//TODO: New Resources (Today)
	//TODO: New Machines (Today)
	//TODO: Black base to show what changed. (Tomorrow or the day after that)
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
	private void newResources() {
		System.out.println("New Resource");
	}
	private void newMachine() {
		System.out.println("New Machine");
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