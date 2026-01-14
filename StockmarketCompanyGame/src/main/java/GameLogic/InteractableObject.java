package GameLogic;

public class InteractableObject extends Item{
	private boolean interacted = false;
	private boolean inbounds = false;
	
	public InteractableObject(double x, double y, double width, double height, boolean interacted, boolean inbounds) {
		super(x, y, width, height);
		this.interacted = interacted;
		this.inbounds = inbounds;
	}

	public boolean isInteracted() {
		return interacted;
	}

	public boolean isInbounds() {
		return inbounds;
	}

	public void setInteracted(boolean interacted) {
		this.interacted = interacted;
	}

	public void setInbounds(boolean inbounds) {
		this.inbounds = inbounds;
	}
}