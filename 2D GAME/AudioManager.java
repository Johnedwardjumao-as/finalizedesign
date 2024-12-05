import javax.sound.sampled.*;
import java.io.IOException;

public class AudioManager {
    private Clip backgroundMusic;
    private Clip zombieSound;
    private Clip attackSound;
    
    private float backgroundMusicVolume = -20.0f; 
    private float zombieSoundVolume = -20.0f;      
    private float attackSoundVolume = 0;
    private float mainMenuMusicVolume = -10.0f;
    
    public void loadMainMenuMusic() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResourceAsStream("/src/music/walkingdead.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(mainMenuMusicVolume);
            
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadBackgroundMusic() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResourceAsStream("/src/music/gameplay.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(backgroundMusicVolume);
            
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadZombieSound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResourceAsStream("/src/music/zombie.wav"));
            zombieSound = AudioSystem.getClip();
            zombieSound.open(audioStream);
            
            FloatControl gainControl = (FloatControl) zombieSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(zombieSoundVolume);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadAttackSound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResourceAsStream("/src/music/attack.wav"));
            attackSound = AudioSystem.getClip();
            attackSound.open(audioStream);
            
            FloatControl gainControl = (FloatControl) attackSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(attackSoundVolume);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.setFramePosition(0);
            backgroundMusic.start();
        }
    }

    public void playZombieSound() {
        if (zombieSound != null) {
            zombieSound.setFramePosition(0);
            zombieSound.start();
        }
    }

    public void playAttackSound() {
        if (attackSound != null) {
            attackSound.setFramePosition(0);
            attackSound.start();
        }
    }

    public void stopAll() {
        if (backgroundMusic != null) backgroundMusic.stop();
        if (zombieSound != null) zombieSound.stop();
        if (attackSound != null) attackSound.stop();
    }
}