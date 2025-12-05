package FileLogic;

import java.io.File;

public class CreateImportantCSVFiles {
	/*4 Directories for:
	 *	Game State
	 *	Employees
	 *	Equipment
	 *	ResourceData
	 *	
	 *	Creates these dirs, aswell as the files. Needs to be extended.
	 */	
	public void createFilesEmployees(ReadCSVFiles readerCSV,WriteCSVFiles writeCSV) {
		try {			
			File file = new File("DataCSV/EmployeeData/UnemployedEmployees.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Unemployed created");
				writeCSV.resetData(readerCSV);
			}else {
				System.out.println("Important File: Unemployed exists");
			}
			
			file = new File("DataCSV/EmployeeData/EmployedEmployees.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Employed created");
				writeCSV.resetData(readerCSV);
			}else {
				System.out.println("Important File: Employed exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void createImportantGameStateFiles(ReadCSVFiles readerCSV,WriteCSVFiles writeCSV) {
		try {
			File file = new File("DataCSV/GameStartUp/GameAlreadyStarted.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Game State created");
				writeCSV.resetData(readerCSV);
			}else {
				System.out.println("Important File: Game State exists");
			}
			file = new File("DataCSV/GameStartUp/CompanyData.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Company Data created");
				writeCSV.resetData(readerCSV);
			}else {
				System.out.println("Important File: Company Data exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}