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

    public Object playLevelComplete(boolean isPlayed) {
        stopSafely(levelComplete);
        if (isPlayed) playSafely(levelComplete);
        return null;
    }

    public Object playLevelCash(boolean isPlayed) {
        stopSafely(cash);
        if (isPlayed) playSafely(cash);
        return null;
    }

    public Object playRotation(boolean isPlayed) {
        stopSafely(rotation);
        if (isPlayed) playSafely(rotation);
        return null;
    }

    public Object playRepair(boolean isPlayed) {
        stopSafely(repair);
        if (isPlayed) playSafely(repair);
        return null;
    }

    public Object playPlacing(boolean isPlayed) {
        stopSafely(placing);
        if (isPlayed) playSafely(placing);
        return null;
    }

    public Object playPickUp(boolean isPlayed) {
        stopSafely(pickUp);
        if (isPlayed) playSafely(pickUp);
        return null;
    }

    public Object playSelect(boolean isPlayed) {
        stopSafely(select);
        if (isPlayed) playSafely(select);
        return null;
    }

    public Object playWalkingPlay(boolean isPlayed) {
        if (!isPlayed) return null;
        if (walking == null) return null;

        try {
            walking.setCycleCount(MediaPlayer.INDEFINITE);
            walking.play();
        } catch (Exception ignored) {
        }
        return null;
    }

    public Object playWalkingStop() {
        stopSafely(walking);
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
