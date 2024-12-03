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
    private boolean showWaveText = true;
    private long waveTextTimer = 0;
    private Clip waveSound;
    public int zombieCount = 0; 

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

        for (Zombie zombie : zombies) {
            zombie.update();

            Rectangle playerBounds = new Rectangle(
                player.worldX + player.solidArea.x - 50,
                player.worldY + player.solidArea.y - 50,
                player.solidArea.width + 60,
                player.solidArea.height + 60
            );

            Rectangle zombieBounds = new Rectangle(
                zombie.worldX + zombie.solidArea.x,
                zombie.worldY + zombie.solidArea.y,
                zombie.solidArea.width,
                zombie.solidArea.height
            );

            if (playerBounds.intersects(zombieBounds)) {
                if (currentTime - zombie.lastDamageTime >= 0) {
                    gp.playerStats.decreaseHealth(10);
                    zombie.lastDamageTime = currentTime;
                    System.out.println("Zombie collided with player and dealt damage!"); 
                }
            }
        }

        zombies.removeIf(zombie -> zombie.health <= 0);

        zombieCount = zombies.size();

        if (zombies.isEmpty()) {
            currentWave++;
            if (currentWave <= 5) {
                spawnWave();
            } else {
                System.out.println("All waves completed!");
            }
        }

        for (int i = 0; i < zombies.size(); i++) {
            for (int j = i + 1; j < zombies.size(); j++) {
                Zombie zombie1 = zombies.get(i);
                Zombie zombie2 = zombies.get(j);

                if (zombie1.solidArea.intersects(zombie2.solidArea)) {
                    if (zombie1.worldX < zombie2.worldX) {
                        zombie1.worldX -= zombie1.speed;
                        zombie2.worldX += zombie2.speed;
                    } else {
                        zombie1.worldX += zombie1.speed;
                        zombie2.worldX -= zombie2.speed;
                    }

                    if (zombie1.worldY < zombie2.worldY) {
                        zombie1.worldY -= zombie1.speed;
                        zombie2.worldY += zombie2.speed;
                    } else {
                        zombie1.worldY += zombie1.speed;
                        zombie2.worldY -= zombie2.speed;
                    }
                }
            }
        }
    }

    public void draw(Graphics2D g2) {
        for (Zombie zombie : zombies) {
            zombie.draw(g2);
        }

        if (showWaveText) {
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 48));
            String waveText = "Wave " + currentWave;
            int textWidth = g2.getFontMetrics().stringWidth(waveText);
            g2.drawString(waveText, 
                gp.screenWidth / 2 - textWidth / 2, 
                gp.screenHeight / 2);
        }

        
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Zombies: " + zombieCount, 
            gp.screenWidth / 2 - 50, 30);
    }
}
