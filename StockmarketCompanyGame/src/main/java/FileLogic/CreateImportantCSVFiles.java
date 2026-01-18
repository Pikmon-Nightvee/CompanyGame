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
	 *	Could perhaps be extended to a more introcate File System, currently abondondend.
	 */	
	public boolean createFilesEmployees() {
		boolean error = false;
		try {			
			File file = new File("DataCSV/EmployeeData/UnemployedEmployees.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Unemployed created");
				error = true;
			}else {
				System.out.println("Important File: Unemployed exists");
			}
			
			file = new File("DataCSV/EmployeeData/EmployedEmployees.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Employed created");
				error = true;
			}else {
				System.out.println("Important File: Employed exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return error;
	}
	
	public boolean createImportantGameStateFiles() {
		boolean error = false;
		try {
			File file = new File("DataCSV/GameStartUp/GameAlreadyStarted.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Game State created");
				error = true;
			}else {
				System.out.println("Important File: Game State exists");
			}
			file = new File("DataCSV/GameStartUp/CompanyData.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Company Data created");
				error = true;
			}else {
				System.out.println("Important File: Company Data exists");
			}
			file = new File("DataCSV/GameStartUp/GameCycleData.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Game Cycle created");
				error = true;
			}else {
				System.out.println("Important File: Game Cycle exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return error;
	}
	
	public boolean createFilesMachine() {
		boolean error = false;
		try {
			File file = new File("DataCSV/EquipmentData/MachineBought.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: MachineBought created");
				error = true;
			}else {
				System.out.println("Important File: MachineBought exists");
			}
			file = new File("DataCSV/EquipmentData/MachineNotBought.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: MachineNotBought created");
				error = true;
			}else {
				System.out.println("Important File: MachineNotBought exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return error;
	}
	
	public boolean createFilesResource() {
		boolean error = false;
		try {
			File file = new File("DataCSV/ResourceData/ResourcesBought.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: ResourcesBought created");
				error = true;
			}else {
				System.out.println("Important File: ResourcesBought exists");
			}
			file = new File("DataCSV/ResourceData/ResourcesOnSell.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: ResourcesOnSell created");
				error = true;
			}else {
				System.out.println("Important File: ResourcesOnSell exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return error;
	}
	
	public boolean createFilesCoordinates() {
		boolean error = false;
		try {
			File file = new File("DataCSV/CoordinateData/MachineCoordinates.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: MachineCoordinates created");
				error = true;
			}else {
				System.out.println("Important File: MachineCoordinates exists");
			}
			
			file = new File("DataCSV/CoordinateData/MachinesPlaced.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: MachinesPlaced created");
				error = true;
			}else {
				System.out.println("Important File: MachinesPlaced exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return error;
	}
	
	public boolean createFilesProduct() {
		boolean error = false;
		try {
			File file = new File("DataCSV/ProductsData/InProduction.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: InProduction created");
				error = true;
			}else {
				System.out.println("Important File: InProduction exists");
			}
			file = new File("DataCSV/ProductsData/OnStock.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: OnStock created");
				error = true;
			}else {
				System.out.println("Important File: OnStock exists");
			}
			file = new File("DataCSV/ProductsData/Produceable.csv");
			if(file.createNewFile()) {
				System.out.println("Important File: Produceable created");
				error = true;
			}else {
				System.out.println("Important File: Produceable exists");
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return error;
	}
	
	public boolean createDirectories() {
	    boolean created = false;

	    try {
	        File dir = new File("DataCSV/EmployeeData");

	        if (dir.mkdir()) { // creates only the last directory
	            System.out.println("Important Directory: EmployeeData created");
	            created = true;
	        } else if (dir.exists()) {
	            System.out.println("Important Directory: EmployeeData exists");
	        } else {
	            System.out.println("Important Directory: EmployeeData could not be created");
	        }
	        
	        dir = new File("DataCSV/EquipmentData");

	        if (dir.mkdir()) { // creates only the last directory
	            System.out.println("Important Directory: EquipmentData created");
	            created = true;
	        } else if (dir.exists()) {
	            System.out.println("Important Directory: EquipmentData exists");
	        } else {
	            System.out.println("Important Directory: EquipmentData could not be created");
	        }
	        
	        dir = new File("DataCSV/GameStartUp");

	        if (dir.mkdir()) { // creates only the last directory
	            System.out.println("Important Directory: GameStartUp created");
	            created = true;
	        } else if (dir.exists()) {
	            System.out.println("Important Directory: GameStartUp exists");
	        } else {
	            System.out.println("Important Directory: GameStartUp could not be created");
	        }
	        
	        dir = new File("DataCSV/ProductsData");

	        if (dir.mkdir()) { // creates only the last directory
	            System.out.println("Important Directory: ProductsData created");
	            created = true;
	        } else if (dir.exists()) {
	            System.out.println("Important Directory: ProductsData exists");
	        } else {
	            System.out.println("Important Directory: ProductsData could not be created");
	        }
	        
	        dir = new File("DataCSV/ResourceData");

	        if (dir.mkdir()) { // creates only the last directory
	            System.out.println("Important Directory: ResourceData created");
	            created = true;
	        } else if (dir.exists()) {
	            System.out.println("Important Directory: ResourceData exists");
	        } else {
	            System.out.println("Important Directory: ResourceData could not be created");
	        }
	        
	        dir = new File("DataCSV/CoordinateData");

	        if (dir.mkdir()) { // creates only the last directory
	            System.out.println("Important Directory: CoordinateData created");
	            created = true;
	        } else if (dir.exists()) {
	            System.out.println("Important Directory: CoordinateData exists");
	        } else {
	            System.out.println("Important Directory: CoordinateData could not be created");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return created;
	}

}