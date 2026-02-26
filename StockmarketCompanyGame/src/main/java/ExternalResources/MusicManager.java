package ExternalResources;

import java.net.URL;
import java.nio.file.Paths;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class MusicManager {
	private MediaPlayer elevator;
	private MediaPlayer lounge;
	private MediaPlayer musicRelax;

	private int musicPlaying = 0;
	private boolean notPlayed = true;

	/**
	 * Musik wird aus dem Classpath geladen.
	 *
	 * Lege deine Dateien so ab:
	 *   src/main/resources/Music/ElevatorJazz.mp3
	 *   src/main/resources/Music/HotelJazz.mp3
	 *   src/main/resources/Music/RelaxingMusic.mp3
	 */
	private final String folder = "Resources/Music/";

	private MediaPlayer loadPlayerFromResources(String resourcePath) {
		String url = Paths.get(resourcePath).toUri().toString();
		if (url == null) {
			System.err.println("[MusicManager] Resource not found: " + resourcePath);
			return null;
		}
		Media media = new Media(url);
		return new MediaPlayer(media);
	}

	public void loadMusic() {
		try {
			elevator = loadPlayerFromResources(folder + "ElevatorJazz.mp3");
			lounge = loadPlayerFromResources(folder + "HotelJazz.mp3");
			musicRelax = loadPlayerFromResources(folder + "RelaxingMusic.mp3");

			// Wenn etwas nicht geladen werden konnte, lieber früh raus – dann crasht später nichts.
			if (elevator == null || lounge == null || musicRelax == null) {
				System.err.println("[MusicManager] One or more music files could not be loaded. Background music disabled.");
				return;
			}

			elevator.setCycleCount(1);
			lounge.setCycleCount(1);
			musicRelax.setCycleCount(1);

			elevator.setOnEndOfMedia(() -> {
				int nextSound = (int) (Math.random() * 2) + 1;
				musicPlaying = nextSound;
				notPlayed = true;
			});

			lounge.setOnEndOfMedia(() -> {
				int nextSound = (int) (Math.random() * 2) + 1;
				if (nextSound == 1) {
					nextSound = 2;
				} else {
					nextSound = 0;
				}
				musicPlaying = nextSound;
				notPlayed = true;
			});

			musicRelax.setOnEndOfMedia(() -> {
				int nextSound = (int) (Math.random() * 2) + 1;
				if (nextSound == 1) {
					nextSound = 0;
				} else {
					nextSound = 1;
				}
				notPlayed = true;
				musicPlaying = nextSound;
			});

			musicPlaying = (int) (Math.random() * 3);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void chooseBackgroundMusic(boolean isMusic) {
		if (!isMusic) return;

		// Wenn loadMusic() nicht aufgerufen wurde oder Dateien nicht gefunden wurden:
		if (elevator == null || lounge == null || musicRelax == null) {
			return;
		}

		switch (musicPlaying) {
			case 0:
				if (notPlayed) {
					elevator.play();
					lounge.stop();
					musicRelax.stop();
					notPlayed = false;
				}
				break;
			case 1:
				if (notPlayed) {
					lounge.play();
					musicRelax.stop();
					elevator.stop();
					notPlayed = false;
				}
				break;
			case 2:
				if (notPlayed) {
					musicRelax.play();
					elevator.stop();
					lounge.stop();
					notPlayed = false;
				}
				break;
			default:
				break;
		}
	}

	public void musicOff(boolean isMusicOff) {
		isMusicOff = !isMusicOff;
		if (isMusicOff) {
			notPlayed = true;
			if (elevator != null) elevator.stop();
			if (lounge != null) lounge.stop();
			if (musicRelax != null) musicRelax.stop();
		}
	}
}
