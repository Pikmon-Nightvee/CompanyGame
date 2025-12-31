package GameLogic;

public class NextCycleStarted {
	private long timePassed=0;
	
	public void nextDay(String time) {
		int days=0;
		switch(time) {
		case "Day": days=1;break;
		case "Week": days=5;break;
		case "Month": days=25;break;
		case "Year": days=300;break;
		}
		for(int i = 0; i < days; i++) {
			timePassed++;
			
			productsProduced();
			sellProduction();
			if(timePassed%5==0) {
				newResources();
			}
			if(timePassed%25==0) {
				payEmployeeWages();
			}
			if(timePassed%300==0) {
				newMachine();
			}
		}
	}
	//TODO: Production in Produce, Sell Production
	//TODO: pay Employee Wages
	//TODO: New Resources
	//TODO: New Machines
	//TODO: Black base to show what changed.
	private void productsProduced() {
		
	}
	private void sellProduction() {
		
	}
	private void payEmployeeWages() {
		
	}
	private void newResources() {
		
	}
	private void newMachine() {
		
	}
}