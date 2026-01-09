package GameLogic;

public class ColissionHandler {
	public boolean AABB(double x1, double y1, double width1, double height1, double x2, double y2, double width2, double height2) {
		return (x1 + width1 > x2 && x2 + width2 > x1 && y1 + height1 > y2 && y2 + height2 > y1);
	}
	public void pushBack(Player item1, Item item2) {
		boolean overlaps = AABB(item1.getX(),item1.getY(),item1.getWidth(),item1.getHeight(),item2.getX(),item2.getY(),item2.getWidth(),item2.getHeight());
		if(overlaps) {
			double pushSpeed = item1.getSpeed();
			double xLeft = Math.abs(item1.getX() + item1.getWidth() - item2.getX());
			double xRight = Math.abs(item2.getX() + item2.getWidth() - item1.getX());
			double yUp = Math.abs(item1.getY() + item1.getHeight() - item2.getY());
			double yDown = Math.abs(item2.getY() + item2.getHeight() - item1.getY());
			
			double overlapX = Math.min(xRight, xLeft);
			double overlapY = Math.min(yUp, yDown);
			
			if(overlapX < overlapY) {
				if(xLeft < xRight) {
					double xPushBack = item1.getX() - pushSpeed;
					item1.setX(xPushBack);
				}else {
					double xPushBack = item1.getX() + pushSpeed;
					item1.setX(xPushBack);
				}
			}else {
				if(yUp < yDown) {
					double yPushBack = item1.getY() - pushSpeed;
					item1.setY(yPushBack);
				}else {
					double yPushBack = item1.getY() + pushSpeed;
					item1.setY(yPushBack);
				}
			}
		}
	}
}