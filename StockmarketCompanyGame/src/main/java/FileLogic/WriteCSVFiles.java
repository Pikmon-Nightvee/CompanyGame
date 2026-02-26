package FileLogic;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import GameLogic.Camera;
import GameLogic.Company;
import GameLogic.Employee;
import GameLogic.InteractableObject;
import GameLogic.Machine;
import GameLogic.MachinePlaceObject;
import GameLogic.Player;
import GameLogic.Product;
import GameLogic.Resource;

public class WriteCSVFiles {

	// Resolves a CSV file path whether the project uses a DataCSV/ folder or flat folders.
	private File resolveFile(String... candidates) {
		for (String c : candidates) {
			if (c == null || c.isBlank()) continue;
			File f = new File(c);
			if (f.exists()) return f;
		}
		return new File(candidates != null && candidates.length > 0 ? candidates[0] : "");
	}

	private void ensureParentDir(File f) {
		File p = f.getParentFile();
		if (p != null && !p.exists()) p.mkdirs();
	}

	private static final String PENDING_MONEY_DELTA_PATH = "DataCSV/GameStartUp/PendingMoneyDelta.csv";
    private static final String PENDING_EVENT_NOTES_PATH = "DataCSV/GameStartUp/PendingEventNotes.txt";
	private static final int DEFAULT_MARKET_COST_INCREASE = 5;
	private static final int RESOURCE_MACHINE_MARKET_COST_DELTA = -5;


	// =========================
	// Resource / Product / Machine delta helpers (used by Events, Production, etc.)
	// =========================

	/**
	 * Adds/removes a resource amount in ResourcesBought.csv for the given company.
	 *
	 * @param resourceName name exactly as in CSV (e.g. "Wood")
	 * @param delta positive = add, negative = remove
	 * @return true if applied, false if not enough resources (when delta is negative)
	 */
	public boolean changeResourceAmount(Company company, String resourceName, int delta) {
		return changeResourceAmount(company, resourceName, delta, new ReadCSVFiles());
	}

	public boolean changeResourceAmount(Company company, String resourceName, int delta, ReadCSVFiles reader) {
		if (company == null || resourceName == null || resourceName.isBlank() || delta == 0) return true;

		ArrayList<Resource> current = reader.readResource("ResourcesBought.csv", company);
		int have = 0;
		Resource found = null;
		for (Resource r : current) {
			if (r.getName().equals(resourceName)) { found = r; have = r.getAmount(); break; }
		}

		if (delta < 0 && have < -delta) {
			return false; // not enough
		}

		int newAmount = have + delta;

		// Determine cost if we have to create a new row (delta > 0 and resource not present)
		int cost = (found != null) ? found.getCost() : 0;
		if (found == null && delta > 0) {
			ArrayList<Resource> all = reader.readResource("ResourceData.csv", company);
			for (Resource r : all) {
				if (r.getName().equals(resourceName)) { cost = r.getCost(); break; }
			}
		}

		File file = resolveFile("DataCSV/ResourceData/ResourcesBought.csv", "ResourceData/ResourcesBought.csv", "ResourcesBought.csv");
		ensureParentDir(file);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(), false))) {
			boolean wrote = false;
			for (Resource r : current) {
				if (!r.getName().equals(resourceName)) {
					writer.write(r.getName() + "," + r.getAmount() + "," + r.getCost() + "," + company.getCompanyType());
					writer.write("\n");
				} else {
					if (newAmount > 0) {
						writer.write(resourceName + "," + newAmount + "," + cost + "," + company.getCompanyType());
						writer.write("\n");
					}
					wrote = true;
				}
			}
			if (!wrote && newAmount > 0) {
				writer.write(resourceName + "," + newAmount + "," + cost + "," + company.getCompanyType());
				writer.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Adds/removes product stock in ProductsData/OnStock.csv.
	 * If adding a product that doesn't exist yet, we use Produceable.csv as template (Standard quality by default).
	 *
	 * @param quality if null/blank => "Standard"
	 * @return true if applied, false if not enough stock for removal
	 */
	public boolean changeProductStock(Company company, String productName, String quality, int delta) {
		return changeProductStock(company, productName, quality, delta, new ReadCSVFiles());
	}

	public boolean changeProductStock(Company company, String productName, String quality, int delta, ReadCSVFiles reader) {
		if (company == null || productName == null || productName.isBlank() || delta == 0) return true;
		if (quality == null || quality.isBlank()) quality = "Standard";

		ArrayList<Product> stock = reader.readProducts("OnStock.csv", company);
		Product found = null;
		for (Product p : stock) {
			if (p.getName().equals(productName) && p.getQuality().equals(quality)) { found = p; break; }
		}

		int have = (found != null) ? found.getAmount() : 0;
		if (delta < 0 && have < -delta) return false;

		int newAmount = have + delta;

		// Template if we need to create
		Product template = found;
		if (template == null && delta > 0) {
			ArrayList<Product> produceable = reader.readProducts("Produceable.csv", company);
			for (Product p : produceable) {
				if (p.getName().equals(productName)) { template = p; break; }
			}
		}

		File file = resolveFile("DataCSV/ProductsData/OnStock.csv", "ProductsData/OnStock.csv", "OnStock.csv");
		ensureParentDir(file);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(), false))) {
			boolean wrote = false;
			for (Product p : stock) {
				if (!(p.getName().equals(productName) && p.getQuality().equals(quality))) {
					// keep as-is
					writer.write(p.getName()+","+p.getAmount()+","+p.getCost()+","+p.getTimePerUnit()+","+p.getTime()+","+p.getQuality()+","+p.getMachineNeeded()+","+p.getAsignedEmployee()+","+p.getAsignedCompanyType());
					for(int i=0;i<p.getResourcesNeeded().length;i++) writer.write(","+p.getResourcesNeeded()[i]);
					for(int i=0;i<p.getResourcesAmount().length;i++) writer.write(","+p.getResourcesAmount()[i]);
					writer.write("\n");
				} else {
					if (newAmount > 0) {
						writer.write(productName+","+newAmount+","+p.getCost()+","+p.getTimePerUnit()+","+p.getTime()+","+quality+","+p.getMachineNeeded()+","+p.getAsignedEmployee()+","+p.getAsignedCompanyType());
						for(int i=0;i<p.getResourcesNeeded().length;i++) writer.write(","+p.getResourcesNeeded()[i]);
						for(int i=0;i<p.getResourcesAmount().length;i++) writer.write(","+p.getResourcesAmount()[i]);
						writer.write("\n");
					}
					wrote = true;
				}
			}
			if (!wrote && newAmount > 0 && template != null) {
				writer.write(productName+","+newAmount+","+template.getCost()+","+template.getTimePerUnit()+","+template.getTime()+","+quality+","+template.getMachineNeeded()+",none,"+company.getCompanyType());
				for(int i=0;i<template.getResourcesNeeded().length;i++) writer.write(","+template.getResourcesNeeded()[i]);
				for(int i=0;i<template.getResourcesAmount().length;i++) writer.write(","+template.getResourcesAmount()[i]);
				writer.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Adds/removes owned machines in EquipmentData/MachineBought.csv.
	 * If adding a new machine, we use MachineNotBought.csv as template (cost/condition).
	 *
	 * @return true if applied, false if not enough machines for removal
	 */
	public boolean changeMachineOwned(Company company, String machineName, int delta) {
		return changeMachineOwned(company, machineName, delta, new ReadCSVFiles());
	}

	public boolean changeMachineOwned(Company company, String machineName, int delta, ReadCSVFiles reader) {
		if (company == null || machineName == null || machineName.isBlank() || delta == 0) return true;

		ArrayList<Machine> owned = reader.readMachines("MachineBought.csv", company);
		Machine found = null;
		for (Machine m : owned) {
			if (m.getName().equals(machineName)) { found = m; break; }
		}

		int have = (found != null) ? found.getAmount() : 0;
		if (delta < 0 && have < -delta) return false;
		int newAmount = have + delta;

		Machine template = found;
		if (template == null && delta > 0) {
			ArrayList<Machine> market = reader.readMachines("MachineNotBought.csv", company);
			for (Machine m : market) {
				if (m.getName().equals(machineName)) { template = m; break; }
			}
		}

		File file = resolveFile("DataCSV/EquipmentData/MachineBought.csv", "EquipmentData/MachineBought.csv", "MachineBought.csv");
		ensureParentDir(file);

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(), false))) {
			boolean wrote = false;
			for (Machine m : owned) {
				if (!m.getName().equals(machineName)) {
					writer.write(m.getName() + "," + m.getAmount() + "," + m.getCost() + "," + m.getCondition() + "," + company.getCompanyType());
					writer.write("\n");
				} else {
					if (newAmount > 0) {
						writer.write(machineName + "," + newAmount + "," + m.getCost() + "," + m.getCondition() + "," + company.getCompanyType());
						writer.write("\n");
					}
					wrote = true;
				}
			}
			if (!wrote && newAmount > 0 && template != null) {
				writer.write(machineName + "," + newAmount + "," + template.getCost() + "," + template.getCondition() + "," + company.getCompanyType());
				writer.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * Convenience for Events: applies multiple deltas with a simple text format.
	 *
	 * Supported formats (very tolerant):
	 *  - "Wood:-10;Iron:+5"
	 *  - "Wood=-10;Iron=5"
	 *  - "Wood,-10;Iron,5"
	 *
	 * Type prefix optional:
	 *  - "R:Wood:-10" (resource)
	 *  - "P:Wooden Board:2" (product, Standard quality)
	 *  - "M:Circular Saw:1" (machine)
	 *
	 * Without prefix we default to Resource.
	 *
	 * @return true if all changes were applied, false if something failed because of missing stock.
	 */
	public boolean applyEventDeltas(Company company, String spec, ReadCSVFiles reader) {
		if (spec == null || spec.isBlank()) return true;
		String[] entries = spec.split(";");
		for (String raw : entries) {
			String s = raw.trim();
			if (s.isEmpty()) continue;

			String type = "R";
			if (s.length() > 2 && s.charAt(1) == ':') {
				type = ("" + s.charAt(0)).toUpperCase();
				s = s.substring(2).trim();
			}

			// Normalize separators to ':'
			s = s.replace('=', ':').replace(',', ':');
			String[] parts = s.split(":");
			if (parts.length < 2) continue;

			String name = parts[0].trim();
			int delta;
			try { delta = Integer.parseInt(parts[1].trim()); } catch (Exception ex) { continue; }

			switch (type) {
				case "P":
					if (!changeProductStock(company, name, "Standard", delta, reader)) return false;
					break;
				case "M":
					if (!changeMachineOwned(company, name, delta, reader)) return false;
					break;
				default:
					if (!changeResourceAmount(company, name, delta, reader)) return false;
					break;
			}
		}
		return true;
	}


	/**
	 * Helper: rewrite a CSV file and increase the "cost" column for a given item.
	 * This persists price changes immediately for the current run.
	 */
	private void increaseCostInFile(String relativePath,
								 String itemName,
								 String companyTypeFilter,
								 int costIndex,
								 int delta) {
		File file = resolveFile(relativePath,
					 relativePath.replaceFirst("^DataCSV/",""),
					 "DataCSV/" + relativePath,
					 relativePath.startsWith("DataCSV/") ? relativePath.substring("DataCSV/".length()) : relativePath);
		if (!file.exists()) return;
		List<String> lines = new ArrayList<>();
		try (java.util.Scanner sc = new java.util.Scanner(file)) {
			while (sc.hasNextLine()) {
				lines.add(sc.nextLine());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(), false))) {
			for (String line : lines) {
				if (line == null || line.isBlank()) continue;
				String[] parts = line.split(",");
				if (parts.length <= costIndex) {
					writer.write(line);
					writer.write("\n");
					continue;
				}

				String name = parts[0];
				String companyType = parts[parts.length - 1];
				boolean matchesCompany = (companyTypeFilter == null || companyTypeFilter.isBlank())
						|| companyTypeFilter.equals(companyType);

				if (matchesCompany && name.equals(itemName)) {
					try {
						int oldCost = Integer.parseInt(parts[costIndex]);
						int newCost = Math.max(0, oldCost + delta);
						parts[costIndex] = String.valueOf(newCost);
					} catch (Exception ignore) {
						// keep line as-is if parsing fails
					}
				}

				StringBuilder b = new StringBuilder();
				for (int i = 0; i < parts.length; i++) {
					b.append(parts[i]);
					if (i + 1 < parts.length) b.append(",");
				}
				writer.write(b.toString());
				writer.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void increaseResourceMarketCost(String resourceName, Company company, int delta) {
		if (company == null || resourceName == null) return;
		String type = company.getCompanyType();
		increaseCostInFile("DataCSV/ResourceData/ResourcesOnSell.csv", resourceName, type, 2, delta);
		increaseCostInFile("DataCSV/ResourceData/ResourcesBought.csv", resourceName, type, 2, delta);
	}

	private void increaseMachineMarketCost(String machineName, Company company, int delta) {
		if (company == null || machineName == null) return;
		String type = company.getCompanyType();
		increaseCostInFile("DataCSV/EquipmentData/MachineNotBought.csv", machineName, type, 2, delta);
		increaseCostInFile("DataCSV/EquipmentData/MachineBought.csv", machineName, type, 2, delta);
		increaseCostInFile("DataCSV/EquipmentData/MachineBroken.csv", machineName, type, 2, delta);
	}

	private void increaseProductMarketCost(String productName, Company company, int delta) {
		if (company == null || productName == null) return;
		String type = company.getCompanyType();
		// product CSV: cost is column 2, company type is column 8
		increaseCostInFile("DataCSV/ProductsData/Produceable.csv", productName, type, 2, delta);
		increaseCostInFile("DataCSV/ProductsData/OnStock.csv", productName, type, 2, delta);
		increaseCostInFile("DataCSV/ProductsData/InProduction.csv", productName, type, 2, delta);
	}

	// =========================
	// Event market-effect helpers (used by EventManager)
	// =========================
	/**
	 * Applies a cost delta to ALL resources of the company's type.
	 * This changes the cost column in both ResourcesBought.csv and ResourcesOnSell.csv.
	 */
	public void changeAllResourceMarketCosts(Company company, int delta, ReadCSVFiles reader) {
		if (company == null || delta == 0) return;
		ReadCSVFiles r = (reader != null) ? reader : new ReadCSVFiles();

		// Use ResourceData.csv as the "master list" for resources of this company type
		ArrayList<Resource> resources = r.readResource("ResourceData.csv", company);
		if (resources == null || resources.isEmpty()) {
			resources = r.readResource("ResourcesBought.csv", company);
		}
		if (resources == null) return;

		for (Resource res : resources) {
			if (res == null) continue;
			increaseResourceMarketCost(res.getName(), company, delta);
		}
	}

	/**
	 * Applies a cost delta to ALL products of the company's type.
	 * This changes the cost column in Produceable.csv, OnStock.csv, and InProduction.csv.
	 */
	public void changeAllProductMarketCosts(Company company, int delta, ReadCSVFiles reader) {
		if (company == null || delta == 0) return;
		ReadCSVFiles r = (reader != null) ? reader : new ReadCSVFiles();

		// Use Produceable.csv as the master list
		ArrayList<Product> products = r.readProducts("Produceable.csv", company);
		if (products == null) return;

		// Avoid duplicates by name
		HashMap<String, Boolean> seen = new HashMap<>();
		for (Product p : products) {
			if (p == null || p.getName() == null) continue;
			String name = p.getName();
			if (seen.containsKey(name)) continue;
			seen.put(name, true);
			increaseProductMarketCost(name, company, delta);
		}
	}

	/**
	 * Applies a cost delta to ALL machines of the company's type.
	 * This changes the cost column in MachineNotBought.csv, MachineBought.csv, and MachineBroken.csv.
	 */
	public void changeAllMachineMarketCosts(Company company, int delta, ReadCSVFiles reader) {
		if (company == null || delta == 0) return;
		ReadCSVFiles r = (reader != null) ? reader : new ReadCSVFiles();

		// Use MachinesData.csv as the master list
		ArrayList<Machine> machines = r.readMachines("MachinesData.csv", company);
		if (machines == null || machines.isEmpty()) {
			machines = r.readMachines("MachineNotBought.csv", company);
		}
		if (machines == null) return;

		HashMap<String, Boolean> seen = new HashMap<>();
		for (Machine m : machines) {
			if (m == null || m.getName() == null) continue;
			String name = m.getName();
			if (seen.containsKey(name)) continue;
			seen.put(name, true);
			increaseMachineMarketCost(name, company, delta);
		}
	}

	// --- Reliability / durability adjustments (column index 3 in equipment CSVs) ---

	private void changeIntInFileClamped(String filePath, String itemName, String companyTypeFilter,
	                                  int valueIndex, int delta, int minValue, int maxValue) {
		File file = resolveFile(filePath, filePath.replace("DataCSV/", ""), new File(filePath).getName());
		String inputPath = file.getPath();
		ensureParentDir(file);

		List<String> lines = new ArrayList<>();
		try (java.util.Scanner sc = new java.util.Scanner(file)) {
			while (sc.hasNextLine()) lines.add(sc.nextLine());
		} catch (Exception e) {
			// If it doesn't exist yet, nothing to adjust
			return;
		}

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputPath, false))) {
			for (String line : lines) {
				if (line == null || line.isBlank()) continue;

				String[] parts = line.split(",");
				if (parts.length <= valueIndex) {
					writer.write(line);
					writer.write("\n");
					continue;
				}

				String name = parts[0];
				String companyType = parts[parts.length - 1];
				boolean matchesCompany = (companyTypeFilter == null || companyTypeFilter.isBlank())
						|| companyTypeFilter.equals(companyType);

				if (matchesCompany && name.equals(itemName)) {
					try {
						int oldVal = Integer.parseInt(parts[valueIndex]);
						int newVal = oldVal + delta;
						if (newVal < minValue) newVal = minValue;
						if (newVal > maxValue) newVal = maxValue;
						parts[valueIndex] = String.valueOf(newVal);
					} catch (Exception ignore) {
						// keep line as-is
					}
				}

				StringBuilder b = new StringBuilder();
				for (int i = 0; i < parts.length; i++) {
					b.append(parts[i]);
					if (i + 1 < parts.length) b.append(",");
				}
				writer.write(b.toString());
				writer.write("\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void changeMachineReliability(String machineName, Company company, int delta) {
		if (company == null || machineName == null) return;
		String type = company.getCompanyType();

		// reliability is column index 3
		changeIntInFileClamped("DataCSV/EquipmentData/MachineBought.csv", machineName, type, 3, delta, 0, 100);
		changeIntInFileClamped("DataCSV/EquipmentData/MachineBroken.csv", machineName, type, 3, delta, 0, 100);
	}

	/**
	 * Applies a reliability/durability delta to ALL machines of the company's type.
	 * (Machines in MachineBought + MachineBroken get their reliability column adjusted, clamped 0..100.)
	 */
	public void changeAllMachineReliability(Company company, int delta, ReadCSVFiles reader) {
		if (company == null || delta == 0) return;
		ReadCSVFiles r = (reader != null) ? reader : new ReadCSVFiles();

		ArrayList<Machine> machines = r.readMachines("MachinesData.csv", company);
		if (machines == null || machines.isEmpty()) {
			machines = r.readMachines("MachineBought.csv", company);
		}
		if (machines == null) return;

		HashMap<String, Boolean> seen = new HashMap<>();
		for (Machine m : machines) {
			if (m == null || m.getName() == null) continue;
			String name = m.getName();
			if (seen.containsKey(name)) continue;
			seen.put(name, true);
			changeMachineReliability(name, company, delta);
		}
	}



	/**
	 * Adds a runtime-only money delta that should still show up in the next-cycle report.
	 * We keep it in a CSV file so the UI/report can read it easily.
	 *
	 * File format: one line with a single number (double).
	 */
	public void addPendingMoneyDelta(double delta) {
		ReadCSVFiles reader = new ReadCSVFiles();
		double current = reader.readPendingMoneyDelta();
		double next = current + delta;
		File file = resolveFile(PENDING_MONEY_DELTA_PATH, "GameStartUp/PendingMoneyDelta.csv", "PendingMoneyDelta.csv");
		ensureParentDir(file);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(), false))) {
			writer.write(String.valueOf(next));
			writer.write("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Resets the pending delta to 0 (call on startup and after the report consumed it). */
	public void resetPendingMoneyDelta() {
		File file = resolveFile(PENDING_MONEY_DELTA_PATH, "GameStartUp/PendingMoneyDelta.csv", "PendingMoneyDelta.csv");
		ensureParentDir(file);
		try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(), false))) {
			writer.write("0");
			writer.write("\n");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

    /** Appends a short note that will be shown on the next cycle report screen. */
    public void addPendingEventNote(String note) {
        if (note == null) return;
        String n = note.trim();
        if (n.isEmpty()) return;

        File file = resolveFile(PENDING_EVENT_NOTES_PATH, "GameStartUp/PendingEventNotes.txt", "PendingEventNotes.txt");
        try {
            file.getParentFile().mkdirs();
        } catch (Exception ignore) {}

        String existing = new ReadCSVFiles().readPendingEventNotes();
        String combined;
        if (existing == null || existing.trim().isEmpty()) {
            combined = n;
        } else {
            combined = existing.trim() + " | " + n;
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            bw.write(combined);
        } catch (Exception e) {
            System.out.println("Could not write PendingEventNotes: " + e.getMessage());
        }
    }

    /** Clears the pending event notes (called after showing the cycle report). */
    public void resetPendingEventNotes() {
        File file = resolveFile(PENDING_EVENT_NOTES_PATH, "GameStartUp/PendingEventNotes.txt", "PendingEventNotes.txt");
        try {
            file.getParentFile().mkdirs();
        } catch (Exception ignore) {}
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
            bw.write("");
        } catch (Exception e) {
            System.out.println("Could not reset PendingEventNotes: " + e.getMessage());
        }
    }

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
		File file = resolveFile("DataCSV/ResourceData/" + path1, "ResourceData/" + path1, path1);
		ensureParentDir(file);

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

		file = resolveFile("DataCSV/ResourceData/" + path2, "ResourceData/" + path2, path2);
		ensureParentDir(file);
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

		// Track for next-cycle report
		addPendingMoneyDelta(sell ? costToCompany : -costToCompany);

		// Persist price change (old cost - 5) after buying
		if (!sell) {
			increaseResourceMarketCost(id, company, RESOURCE_MACHINE_MARKET_COST_DELTA);
		}
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
		File file = resolveFile("DataCSV/EquipmentData/" + path1, "EquipmentData/" + path1, path1);
		ensureParentDir(file);

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

		file = resolveFile("DataCSV/EquipmentData/" + path2, "EquipmentData/" + path2, path2);
		ensureParentDir(file);
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

		// Track for next-cycle report
		addPendingMoneyDelta(sell ? costToCompany : -costToCompany);

		// Persist price change (old cost - 5) after buying
		if (!sell) {
			increaseMachineMarketCost(id, company, RESOURCE_MACHINE_MARKET_COST_DELTA);
		}
		
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
		int totalCost = 0;
		for(Product p : products) {
			if(p.getName().equals(selectedProduct)) {
				int cost = p.getCost() * selectedAmount;
				int time = p.getTimePerUnit() * selectedAmount;
				Product toAdd = new Product(p.getName(),selectedAmount,cost,p.getTimePerUnit(),time,p.getQuality(),p.getMachineNeeded(),asignedEmployee,p.getAsignedCompanyType(),p.getResourcesNeeded(),p.getResourcesAmount());
				System.out.println(toAdd.toString());
				product.add(toAdd);
				totalCost += cost;
			}
		}

		// Pay production cost immediately (so UI money changes instantly) and track for next-cycle report
		if (totalCost != 0) {
			company.setMoneyOfCompany(company.getMoneyOfCompany() - totalCost);
			addPendingMoneyDelta(-totalCost);
			// Persist product cost increase (old cost + 5) for this run
			increaseProductMarketCost(selectedProduct, company, DEFAULT_MARKET_COST_INCREASE);
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
	
	public void coordinatesMachineSafe(MachinePlaceObject w, double x, double y, String machine) {
		File file = new File("DataCSV/CoordinateData/MachineCoordinates.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,true))){
			writer.write(x+","+y+","+w.getWidth()+","+w.getHeight()+","+machine+"\n");
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void machineRemoved(ArrayList<MachinePlaceObject> machines, ReadCSVFiles reader) {
		ArrayList<String[]> machineOG = reader.machinesTopDownGet("MachineCoordinates.csv");
		
		File file = new File("DataCSV/CoordinateData/MachineCoordinates.csv");
		try(BufferedWriter writer = new BufferedWriter(new FileWriter(file,false))){
			for(MachinePlaceObject m : machines) {
				for(String[] s : machineOG) {
					double x = Double.parseDouble(s[0]);
					double y = Double.parseDouble(s[1]);
					if(m.getX() == x && m.getY() == y) {
						writer.write(m.getX()+","+m.getY()+","+m.getWidth()+","+m.getHeight()+","+s[4]+"\n");
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
			case 0:machine = "Oven";break;
			case 1:machine = "Stove";break;
			}
			break;
		case "Craft Buisness":
			switch(scrollAmount) {
			case 0:machine = "Milling Machine";break;
			case 1:machine = "Lathe";break;
			case 2:machine = "Drill Press";break;
			case 3:machine = "Circular Saw";break;
			}
			break;
		case "IT Manager":
			switch(scrollAmount) {
			case 0:machine = "PC";break;
			case 1:machine = "Soldering Iron";break;
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


// ======================= EVENT SYSTEM (CSV) =======================
// These helper methods are used by Visual.EventManager to store cooldown/time state.

/**
 * Writes the current cooldown state to:
 * DataCSV/EventData/EventCooldown.csv
 *
 * Each row should be:
 * id,nextAllowedDay,monthIndex,countThisMonth
 */
/**
 * Writes the current cooldown state to:
 * DataCSV/EventData/EventCooldown.csv
 *
 * Each row should be:
 * EventID,cooldownRemaining,monthCount,monthWindowStartDay
 */
public void writeEventCooldownState(List<String[]> rows) {
    writeRawCsv("DataCSV/EventData/EventCooldown.csv", rows);
}

/** Overload for existing calls that pass ArrayList. */
public void writeEventCooldownState(ArrayList<String[]> rows) {
    writeEventCooldownState((List<String[]>) rows);
}

/** Overload so EventManager can pass a Map (id -> row). */
public void writeEventCooldownState(Map<String, String[]> state) {
    if (state == null) return;
    ArrayList<String[]> rows = new ArrayList<>(state.values());
    writeEventCooldownState(rows);
}
/**
 * Adds "days" to the event day counter stored in:
 * DataCSV/EventData/GameTime.csv
 *
 * This is called from the cycle button (day/week/month/year).
 */
public void addEventDays(int delta) {
    addEventDays(delta, new ReadCSVFiles());
}

public void addEventDays(int delta, ReadCSVFiles reader) {
    int day = reader.readEventDay();
    day += delta;

    ArrayList<String[]> rows = new ArrayList<>();
    rows.add(new String[] { String.valueOf(day) });
    writeRawCsv("DataCSV/EventData/GameTime.csv", rows);
}

/**
 * Small generic CSV writer used by the event system.
 * Writes rows comma-separated.
 */
private void writeRawCsv(String path, List<String[]> rows) {
    try {
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file.getPath(), false))) {
            for (String[] r : rows) {
                if (r == null) continue;
                writer.write(String.join(",", r));
                writer.write("\n");
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}
}
