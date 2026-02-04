package GameLogic;

public class InteractableObject extends Item{
	private boolean interacted = false;
	private boolean broken = false;
	private int extra = 15;
	
	public InteractableObject(double x, double y, double width, double height, boolean interacted, boolean broken) {
		super(x, y, width, height);
		this.interacted = interacted;
		this.broken = broken;
	}
	
	private double time = 0;
	public double blinking() {
		double opacity = Math.sin(time)-0.1;
		time += 0.05;
		if(opacity < -0.5) {
			time = 0;
		}
		return opacity;
	}

	public boolean isInteracted() {
		return interacted;
	}

	public boolean isBroken() {
		return broken;
	}

	public void setInteracted(boolean interacted) {
		this.interacted = interacted;
	}

	public void setBroken(boolean broken) {
		this.broken = broken;
	}

	public int getExtra() {
		return extra;
	}

	@Override
	public String toString() {
		return super.toString() + "InteractableObject [interacted=" + interacted + ", broken=" + broken + "]";
	}
}