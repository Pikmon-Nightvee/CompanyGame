package GameLogic;

import java.util.ArrayList;

public class LevelHolder {
	//0 -> Luft, 1 -> Wand, 2 -> Spawnpoint
	private ArrayList<Wall> walls = new ArrayList<>();
	
	private int[][] levelEDVManager = {
									{1,1,1,1,1,1,1},
									{1,0,0,0,0,0,1},
									{1,0,0,2,0,0,1},
									{1,0,0,0,0,0,1},
									{1,1,1,1,1,1,1}
												};
	
	private int[][] levelFoodtruck = {
									{0,1,1,1,1,1,1,1,1,1,1},
									{1,1,0,0,0,0,0,0,0,0,1},
									{1,0,0,0,0,2,0,0,0,0,1},
									{1,1,0,0,0,0,0,0,0,0,1},
									{0,1,1,1,1,1,1,1,1,1,1}
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
					if(loadLevel[i][j] == 1) {
						int insertX = currentX * sizeWall;
						int insertY = currentY * sizeWall;
						Wall wall = new Wall(insertX,insertY,sizeWall,sizeWall);
						walls.add(wall);
					}else if(loadLevel[i][j] == 2) {
						int insertX = currentX * sizeWall;
						int insertY = currentY * sizeWall;
						player.setX(insertX);
						player.setY(insertY);
					}
					currentX++;
				}
				currentY++;
				currentX = 0;
			}
		}
	}

	public ArrayList<Wall> getWalls() {
		return walls;
	}
}
