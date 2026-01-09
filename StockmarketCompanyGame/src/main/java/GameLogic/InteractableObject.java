package GameLogic;

public class InteractableObject extends Item{
	private boolean interacted = false;

	public InteractableObject(double x, double y, double width, double height, boolean interacted) {
		super(x, y, width, height);
		this.interacted = interacted;
	}

	public boolean isInteracted() {
		return interacted;
	}
}