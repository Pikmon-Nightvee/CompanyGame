package GameLogic;

import java.util.ArrayList;

import FileLogic.WriteCSVFiles;

public class LevelHolder {
	//0 -> Luft platzierbar, 1 -> Wand, 2 -> Spawnpoint, 3 -> Luft nicht platzierbar, 4 -> Rad, 5 -> Headlight
	private ArrayList<Wall> walls = new ArrayList<>();
	private ArrayList<Wall> machines = new ArrayList<>();
	private ArrayList<Wall> wheels = new ArrayList<>();
	private ArrayList<Wall> headlights = new ArrayList<>();
	
	private ArrayList<InteractableObject> interact = new ArrayList<>();
	private ArrayList<InteractableObject> blinking = new ArrayList<>();
	private ArrayList<InteractableObject> toRemove = new ArrayList<>();
	
	private ArrayList<Wall> placeable = new ArrayList<>();
	
	private int[][] levelEDVManager = {
									{1,1,1,1,1,1,1},
									{1,0,0,0,0,0,1},
									{1,0,0,2,0,0,1},
									{1,0,0,0,0,0,1},
									{1,1,1,1,1,1,1}
												};
	
	private int[][] levelFoodtruck = {
									{3,1,1,4,1,1,1,1,1,4,1},
									{5,1,0,0,0,0,0,0,0,0,1},
									{1,0,0,0,0,2,0,0,0,0,1},
									{5,1,0,0,0,0,0,0,0,0,1},
									{3,1,1,4,1,1,1,1,1,4,1}
												};
	
	private int[][] levelCraftBusiness = {
									{1,1,1,1,1,1,1,1,1,},
									{1,2,0,1,0,0,0,0,1,},
									{1,0,0,1,0,0,0,0,1,},
									{1,1,0,1,0,0,0,0,1,},
									{1,0,0,0,0,0,0,0,1,},
									{1,0,0,0,0,0,0,0,1,},
									{1,1,1,1,1,1,1,1,1,}
												};
	
	public void loadLevel(String selected, Player player) {
		int[][] loadLevel = null;
		System.out.println(selected);
		switch(selected) {
		case "Foodtruck": loadLevel = levelFoodtruck; break;
		case "EDV-Manager": loadLevel = levelEDVManager; break;
		case "Craft Buisness": loadLevel = levelCraftBusiness; break;
		}
		int sizeWall = 100;
		int currentX = 0;
		int currentY = 0;
		if(loadLevel != null) {
			for(int i = 0; i < loadLevel.length; i++) {
				for(int j = 0; j < loadLevel[0].length; j++) {
					if(loadLevel[i][j] == 0) {
						int insertX = currentX * sizeWall;
						int insertY = currentY * sizeWall;
						Wall wall = new Wall(insertX,insertY,sizeWall,sizeWall);
						placeable.add(wall);
					}else if(loadLevel[i][j] == 1) {
						int insertX = currentX * sizeWall;
						int insertY = currentY * sizeWall;
						Wall wall = new Wall(insertX,insertY,sizeWall,sizeWall);
						walls.add(wall);
					}else if(loadLevel[i][j] == 2) {
						int insertX = currentX * sizeWall;
						int insertY = currentY * sizeWall;
						player.setX(insertX);
						player.setY(insertY);
						Wall wall = new Wall(insertX,insertY,sizeWall,sizeWall);
						placeable.add(wall);
					}else if(loadLevel[i][j] == 4) {
						int insertX = currentX * sizeWall;
						int insertY = currentY * sizeWall;
						Wall wall = new Wall(insertX,insertY,sizeWall,sizeWall);
						wheels.add(wall);
					}else if(loadLevel[i][j] == 5) {
						int insertX = currentX * sizeWall;
						int insertY = currentY * sizeWall;
						Wall wall = new Wall(insertX,insertY,sizeWall,sizeWall);
						headlights.add(wall);
					}
					currentX++;
				}
				currentY++;
				currentX = 0;
			}
		}
	}

	public void machinesLoad(ArrayList<Wall> toAdd) {
		machines.addAll(toAdd);
	}

	public void machineAdd(double xPos, double yPos, double width, double height){
		if(width > 0 && height > 0) {
			Wall toAdd = new Wall(xPos,yPos,width,height);
			machines.add(toAdd);
			
			int extra = 15;
			InteractableObject toAddIo = new InteractableObject(xPos-extra,yPos-extra,width+(extra*2),height+(extra*2),false,false);
			interact.add(toAddIo);
		}
	}
	public void interactLoad(ArrayList<Wall> walls){
		for(Wall w : walls) {
			int extra = 15;
			InteractableObject toAddIo = new InteractableObject(w.getX()-extra,w.getY()-extra,w.getWidth()+(extra*2),w.getHeight()+(extra*2),false,false);
			this.interact.add(toAddIo);
		}
	}
	public void removeMachine(ArrayList<Wall> walls, InteractableObject i) {
		ArrayList<Wall> toRemove = new ArrayList<>();
		for(Wall w : walls) {
			if(w.getX() == i.getX()+i.getExtra() && w.getY() == i.getY() + i.getExtra()) {
				toRemove.add(w);
				this.toRemove.add(i);
				
				boolean interacted = false;
				boolean broken = false;
				
				InteractableObject iNew = new InteractableObject(w.getX(),w.getY(),w.getWidth(),w.getHeight(),interacted,broken);
				this.toRemove.add(iNew);
				System.out.println("Remove Alarmlayer: "+iNew.toString());
			}
		}
		for(Wall w : toRemove) {
			walls.remove(w);
		}
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}

	public ArrayList<Wall> getMachines() {
		return machines;
	}

	public ArrayList<Wall> getPlaceable() {
		return placeable;
	}

	public ArrayList<InteractableObject> getInteract() {
		return interact;
	}

	public ArrayList<InteractableObject> getToRemove() {
		return toRemove;
	}

	public ArrayList<InteractableObject> getBlinking() {
		return blinking;
	}

	public void setBlinking(ArrayList<InteractableObject> blinking) {
		this.blinking = blinking;
	}

	public void addBlinking(InteractableObject blinking) {
		this.blinking.add(blinking);
	}

	public ArrayList<Wall> getWheels() {
		return wheels;
	}

	public ArrayList<Wall> getHeadlights() {
		return headlights;
	}
}
