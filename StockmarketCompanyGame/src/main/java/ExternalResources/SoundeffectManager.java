package ExternalResources;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundeffectManager {

	private MediaPlayer button;
	private MediaPlayer gameOver;
	private MediaPlayer levelComplete;
	private MediaPlayer cash;

	private MediaPlayer rotation;
	private MediaPlayer repair;
	private MediaPlayer walking;
	private MediaPlayer placing;
	private MediaPlayer select;
	private MediaPlayer pickUp;
	
	public void loadSfx() {
		try {
			String folder = "/SoundEffects/";
	        Media sound = new Media(getClass().getResource(folder+"buttonClick.mp3").toExternalForm());
	        button = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"gameover.mp3").toExternalForm());
	        gameOver = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"levelComplete.mp3").toExternalForm());
	        levelComplete = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"cashRegistre.mp3").toExternalForm());
	        cash = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"Rotation.mp3").toExternalForm());
	        rotation = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"MachineFix.mp3").toExternalForm());
	        repair = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"Walking.mp3").toExternalForm());
	        walking = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"MachinePlace.mp3").toExternalForm());
	        placing = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"select.mp3").toExternalForm());
	        select = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"Pickup.mp3").toExternalForm());
	        pickUp = new MediaPlayer(sound);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public MediaPlayer getButton() {
		return button;
	}

	public Object playButton(boolean isPlayed) {
		button.stop();
		if(isPlayed)button.play();
		return null;
	}

	public Object playGameOver(boolean isPlayed) {
		gameOver.stop();
		if(isPlayed)gameOver.play();
		return null;
	}

	public Object playLevelComplete(boolean isPlayed) {
		levelComplete.stop();
		if(isPlayed)levelComplete.play();
		return null;
	}

	public Object playLevelCash(boolean isPlayed) {
		cash.stop();
		if(isPlayed)cash.play();
		return null;
	}

	public Object playRotation(boolean isPlayed) {
		rotation.stop();
		if(isPlayed)rotation.play();
		return null;
	}

	public Object playRepair(boolean isPlayed) {
		repair.stop();
		if(isPlayed)repair.play();
		return null;
	}

	public Object playPlacing(boolean isPlayed) {
		placing.stop();
		if(isPlayed)placing.play();
		return null;
	}

	public Object playPickUp(boolean isPlayed) {
		pickUp.stop();
		if(isPlayed)pickUp.play();
		return null;
	}

	public Object playSelect(boolean isPlayed) {
		select.stop();
		if(isPlayed)select.play();
		return null;
	}

	public Object playWalkingPlay(boolean isPlayed) {
		if(isPlayed) {
			walking.setCycleCount(-1);
			walking.play();
		}
		return null;
	}

	public Object playWalkingStop() {
		walking.stop();
		return null;
	}

	public MediaPlayer getGameOver() {
		return gameOver;
	}

	public MediaPlayer getLevelComplete() {
		return levelComplete;
	}

	public MediaPlayer getCash() {
		return cash;
	}

	public MediaPlayer getRotation() {
		return rotation;
	}

	public MediaPlayer getRepair() {
		return repair;
	}

	public MediaPlayer getWalking() {
		return walking;
	}

	public MediaPlayer getPlacing() {
		return placing;
	}

	public MediaPlayer getSelect() {
		return select;
	}

	public MediaPlayer getPickUp() {
		return pickUp;
	}
}