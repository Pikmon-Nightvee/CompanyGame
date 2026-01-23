package ExternalResources;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
	private MediaPlayer elevator;
	private MediaPlayer lounge;
	private MediaPlayer musicRelax;
	
	private int musicPlaying = 0;
	private boolean notPlayed = true;
	
	public void loadMusic() {
		try {
			String folder = "/Music/";
	        Media sound = new Media(getClass().getResource(folder+"ElevatorJazz.mp3").toExternalForm());
	        elevator = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"HotelJazz.mp3").toExternalForm());
	        lounge = new MediaPlayer(sound);

	        sound = new Media(getClass().getResource(folder+"RelaxingMusic.mp3").toExternalForm());
	        musicRelax = new MediaPlayer(sound);
	        
	        elevator.setCycleCount(1);
	        lounge.setCycleCount(1);
	        musicRelax.setCycleCount(1);
	        
	        elevator.setOnEndOfMedia(() -> {
				int nextSound = (int)(Math.random()*2)+1;
				musicPlaying = nextSound;
				notPlayed = true;
			});
	        
	        lounge.setOnEndOfMedia(() -> {
				int nextSound = (int)(Math.random()*2)+1;
				if(nextSound == 1) {
					nextSound = 2;
				}else {
					nextSound = 0;
				}
				musicPlaying = nextSound;
				notPlayed = true;
			});
	        
	        musicRelax.setOnEndOfMedia(() -> {
				int nextSound = (int)(Math.random()*2)+1;
				if(nextSound == 1) {
					nextSound = 0;
				}else {
					nextSound = 1;
				}
				notPlayed = true;
				musicPlaying = nextSound;
			});
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void chooseBackgroundMusic(boolean isMusic) {
		if(isMusic) {
			//System.out.println("Music playable: " + isMusic + " status: " + musicPlaying + " Music started: " + notPlayed);
			switch(musicPlaying) {
			case 0: 
				if(notPlayed) {
					elevator.play(); lounge.stop(); musicRelax.stop();
					notPlayed = false;
				}
				break;
			case 1: 
				if(notPlayed) {
					lounge.play(); musicRelax.stop(); elevator.stop();
					notPlayed = false;
				}
				break;
			case 2: 
				if(notPlayed) {
					musicRelax.play(); elevator.stop(); lounge.stop();
					notPlayed = false;
				}
				break;
			}
		}
	}
	
	public void musicOff(boolean isMusicOff) {
		isMusicOff = !isMusicOff;
		if(isMusicOff) {
			notPlayed = true;
			elevator.stop();
			lounge.stop();
			musicRelax.stop();
		}
	}
}