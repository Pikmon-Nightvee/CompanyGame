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
			if(timePassed%7==0) {
				payEmployeeWages();
			}
			if(timePassed%25==0) {
				newResources();
			}
		}
	}
	//TODO: Production in Produce, Sell Production
	//TODO:pay Employee Wages
	//TODO: New Resources
	private void productsProduced() {
		
	}
	private void sellProduction() {
		
	}
	private void payEmployeeWages() {
		
	}
	private void newResources() {
		
	}
}