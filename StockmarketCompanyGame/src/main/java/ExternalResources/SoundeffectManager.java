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
	private MediaPlayer error;
	String folder = "/SoundEffects/";
	
	public void loadSfx() {
		try {
			String filePath = folder+"buttonClick.mp3";
	        Media sound = new Media(getClass().getResource(filePath).toExternalForm());
	        button = new MediaPlayer(sound);

			filePath = folder+"gameover.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        gameOver = new MediaPlayer(sound);

			filePath = folder+"levelComplete.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        levelComplete = new MediaPlayer(sound);

			filePath = folder+"cashRegistre.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        cash = new MediaPlayer(sound);

			filePath = folder+"Rotation.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        rotation = new MediaPlayer(sound);

			filePath = folder+"MachineFix.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        repair = new MediaPlayer(sound);

			filePath = folder+"Walking.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        walking = new MediaPlayer(sound);

			filePath = folder+"MachinePlace.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        placing = new MediaPlayer(sound);

			filePath = folder+"select.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        select = new MediaPlayer(sound);

			filePath = folder+"Pickup.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        pickUp = new MediaPlayer(sound);

			filePath = folder+"Error.mp3";
	        sound = new Media(getClass().getResource(filePath).toExternalForm());
	        error = new MediaPlayer(sound);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public MediaPlayer getButton() {
		return button;
	}

	public Object playError(boolean isPlayed) {
		error.stop();
		if(isPlayed)error.play();
		return null;
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