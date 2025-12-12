package FileLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class WriteCSVFiles {
	public void manageEmployeeInFiles(String id, String path1, String path2) {
		
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