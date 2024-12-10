import javax.imageio.ImageIO; 
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.image.BufferedImage; 
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.File;
import java.awt.Rectangle;
import javax.sound.sampled.*;
import java.util.ArrayList; 
import java.awt.Color;

public class WaveManager {
    private GamePanel gp;
    private Player player;
    public ArrayList<Zombie> zombies = new ArrayList<>();
    private int currentWave = 1;
    public boolean showWaveText = true;
    public long waveTextTimer = 0;
    private Clip waveSound;
    public int zombieCount = 0;
    public boolean allWavesCompleted = false;
    private long allWavesCompletedTimer = 0;

    public WaveManager(GamePanel gp, Player player) {
        this.gp = gp;
        this.player = player;
        loadWaveSound();
        spawnWave();
    }

    private void loadWaveSound() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                getClass().getResourceAsStream("/src/music/wave.wav"));
            waveSound = AudioSystem.getClip();
            waveSound.open(audioStream);
        } catch (Exception e) {
            System.err.println("Error loading wave sound: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void spawnWave() {
        int zombieCount = currentWave * 10;

        if (waveSound != null) {
            waveSound.setFramePosition(0);
            waveSound.start();
        }

        zombies.clear();
        for (int i = 0; i < zombieCount; i++) {
            Zombie zombie = new Zombie(gp);

            int mapWidth = gp.maxWorldCol * gp.tileSize;
            int mapHeight = gp.maxWorldRow * gp.tileSize;

            zombie.worldX = (int)(Math.random() * mapWidth);
            zombie.worldY = (int)(Math.random() * mapHeight);

            zombies.add(zombie);
        }

        showWaveText = true;
        waveTextTimer = System.currentTimeMillis();
    }

    public void update() {
        if (showWaveText && System.currentTimeMillis() - waveTextTimer > 2000) {
            showWaveText = false;
        }

        long currentTime = System.currentTimeMillis();

        for (Zombie zombie : new ArrayList<>(zombies)) {
            zombie.update();

            Rectangle playerBounds = new Rectangle(
                player.worldX + player.solidArea.x - 10,
                player.worldY + player.solidArea.y - 10,
                player.solidArea.width + 10,
                player.solidArea.height + 10
            );

            Rectangle zombieBounds = new Rectangle(
                zombie.worldX + zombie.solidArea.x,
                zombie.worldY + zombie.solidArea.y,
                zombie.solidArea.width,
                zombie.solidArea.height
            );

            if (playerBounds.intersects(zombieBounds)) {
                if (currentTime - zombie.lastDamageTime >= 0) {
                    gp.playerStats.decreaseHealth(0);
                    zombie.lastDamageTime = currentTime;
                }
            }
        }

        zombies.removeIf(zombie -> {
            if (zombie.health <= 0) {
                int bounty = currentWave == 2 ? 20 : 10;
                gp.playerStats.addMoney(bounty);
                return true;
            }
            return false;
        });

        zombieCount = zombies.size();

        if (zombies.isEmpty()) {
            if (currentWave < 5) {
                currentWave++;
                spawnWave();
            } else if (!allWavesCompleted) {
                allWavesCompleted = true;
                showWaveText = true;
                waveTextTimer = System.currentTimeMillis();
                allWavesCompletedTimer = System.currentTimeMillis();
            }
        }
    }
    
    public int getCurrentWave() {
    return currentWave;
}

    public void draw(Graphics2D g2) {
        for (Zombie zombie : zombies) {
            zombie.draw(g2);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Money: $" + gp.playerStats.getMoney(), gp.screenWidth - 200, gp.screenHeight - 30);
    }

}
