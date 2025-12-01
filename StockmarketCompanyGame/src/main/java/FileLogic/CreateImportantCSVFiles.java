package FileLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class CreateImportantCSVFiles {
	/*3 CSV Files:
	 *	Game Started
	 *	Employed Employees
	 *	Not Employed Employees
	 */
	public void createFilesEmployees() {
		try {
			File file = new File("DataCSV/GameAlreadyStarted.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Game State created");
				try(BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(),true))){
					writer.write("false");
					writer.write("\n");
				}catch(Exception e) {
					e.printStackTrace();
				}
			}else {
				System.out.println("Important File: Game State exists");
			}
			
			file = new File("DataCSV/UnemployedEmployees.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Unemployed created");
			}else {
				System.out.println("Important File: Unemployed exists");
			}
			
			file = new File("DataCSV/EmployedEmployees.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Employed created");
			}else {
				System.out.println("Important File: Employed exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}