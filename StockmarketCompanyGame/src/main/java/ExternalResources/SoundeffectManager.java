package ExternalResources;

import java.net.URL;

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

    /**
     * IMPORTANT:
     * Put your audio files into src/main/resources/SoundEffects/
     * e.g. src/main/resources/SoundEffects/buttonClick.mp3
     *
     * Then load them via classpath with getResource, not via file paths.
     */
    private static final String FOLDER = "/SoundEffects/";

    public void loadSfx() {
        // Load all sfx from resources (classpath)
        button = loadPlayer(FOLDER + "buttonClick.mp3");
        gameOver = loadPlayer(FOLDER + "gameover.mp3");
        levelComplete = loadPlayer(FOLDER + "levelComplete.mp3");
        cash = loadPlayer(FOLDER + "cashRegistre.mp3");

        rotation = loadPlayer(FOLDER + "Rotation.mp3");
        repair = loadPlayer(FOLDER + "MachineFix.mp3");
        walking = loadPlayer(FOLDER + "Walking.mp3");
        placing = loadPlayer(FOLDER + "MachinePlace.mp3");

        select = loadPlayer(FOLDER + "select.mp3");
        pickUp = loadPlayer(FOLDER + "Pickup.mp3");
        error = loadPlayer(FOLDER + "Error.mp3");
    }

    private MediaPlayer loadPlayer(String resourcePath) {
        try {
            URL url = getClass().getResource(resourcePath);
            if (url == null) {
                System.err.println("[SoundeffectManager] Resource not found: " + resourcePath);
                return null;
            }
            Media media = new Media(url.toExternalForm());
            return new MediaPlayer(media);
        } catch (Exception e) {
            System.err.println("[SoundeffectManager] Failed to load: " + resourcePath);
            e.printStackTrace();
            return null;
        }
    }

    private void stopSafely(MediaPlayer p) {
        if (p == null) return;
        try {
            p.stop();
        } catch (Exception ignored) {
            // MediaPlayer can throw if not ready; ignore to avoid crashing the game loop
        }
    }

    private void playSafely(MediaPlayer p) {
        if (p == null) return;
        try {
            p.play();
        } catch (Exception ignored) {
        }
    }

    public MediaPlayer getButton() {
        return button;
    }

    public Object playError(boolean isPlayed) {
        stopSafely(error);
        if (isPlayed) playSafely(error);
        return null;
    }

    public Object playButton(boolean isPlayed) {
        stopSafely(button);
        if (isPlayed) playSafely(button);
        return null;
    }

    public Object playGameOver(boolean isPlayed) {
        stopSafely(gameOver);
        if (isPlayed) playSafely(gameOver);
        return null;
    }

<<<<<<< HEAD
    public Object playLevelComplete(boolean isPlayed) {
        stopSafely(levelComplete);
        if (isPlayed) playSafely(levelComplete);
        return null;
    }
=======
	public void playError(boolean isPlayed) {
		error.stop();
		if(isPlayed)error.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public Object playLevelCash(boolean isPlayed) {
        stopSafely(cash);
        if (isPlayed) playSafely(cash);
        return null;
    }
=======
	public void playButton(boolean isPlayed) {
		button.stop();
		if(isPlayed)button.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public Object playRotation(boolean isPlayed) {
        stopSafely(rotation);
        if (isPlayed) playSafely(rotation);
        return null;
    }
=======
	public void playGameOver(boolean isPlayed) {
		gameOver.stop();
		if(isPlayed)gameOver.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public Object playRepair(boolean isPlayed) {
        stopSafely(repair);
        if (isPlayed) playSafely(repair);
        return null;
    }
=======
	public void playLevelComplete(boolean isPlayed) {
		levelComplete.stop();
		if(isPlayed)levelComplete.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public Object playPlacing(boolean isPlayed) {
        stopSafely(placing);
        if (isPlayed) playSafely(placing);
        return null;
    }
=======
	public void playLevelCash(boolean isPlayed) {
		cash.stop();
		if(isPlayed)cash.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public Object playPickUp(boolean isPlayed) {
        stopSafely(pickUp);
        if (isPlayed) playSafely(pickUp);
        return null;
    }
=======
	public void playRotation(boolean isPlayed) {
		rotation.stop();
		if(isPlayed)rotation.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public Object playSelect(boolean isPlayed) {
        stopSafely(select);
        if (isPlayed) playSafely(select);
        return null;
    }
=======
	public void playRepair(boolean isPlayed) {
		repair.stop();
		if(isPlayed)repair.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public Object playWalkingPlay(boolean isPlayed) {
        if (!isPlayed) return null;
        if (walking == null) return null;
=======
	public void playPlacing(boolean isPlayed) {
		placing.stop();
		if(isPlayed)placing.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
        try {
            walking.setCycleCount(MediaPlayer.INDEFINITE);
            walking.play();
        } catch (Exception ignored) {
        }
        return null;
    }
=======
	public void playPickUp(boolean isPlayed) {
		pickUp.stop();
		if(isPlayed)pickUp.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public Object playWalkingStop() {
        stopSafely(walking);
        return null;
    }
=======
	public void playSelect(boolean isPlayed) {
		select.stop();
		if(isPlayed)select.play();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public MediaPlayer getGameOver() {
        return gameOver;
    }
=======
	public void playWalkingPlay(boolean isPlayed) {
		if(isPlayed) {
			walking.setCycleCount(-1);
			walking.play();
		}
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

<<<<<<< HEAD
    public MediaPlayer getLevelComplete() {
        return levelComplete;
    }
=======
	public void playWalkingStop() {
		walking.stop();
	}
>>>>>>> branch 'main' of https://github.com/Pikmon-Nightvee/CompanyGame

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
