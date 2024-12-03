import java.util.ArrayList;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
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
   
   public class Zombie extends Entity {
       GamePanel gp; 
       int health = 100;
       int maxHealth = 100;
       BufferedImage zombie1; 
       int speed = 1; 
       public long lastDamageTime = 0;
       private long lastSoundTime = 0;
       private static final long SOUND_DELAY = 5000;
   
       public Zombie(GamePanel gp) {
           this.gp = gp;
           solidArea = new Rectangle(0, 0, gp.tileSize, gp.tileSize);
   
           
           try {
               zombie1 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/zombie.png"));
           } catch (IOException e) {
               e.printStackTrace();
           }
       }
   
       public void drawHealthBar(Graphics2D g2, int screenX, int screenY) {
           if (gp == null) return; 
           
           
           g2.setColor(Color.RED);
           g2.fillRect(screenX, screenY - 10, gp.tileSize, 5);
           
          
           g2.setColor(Color.GREEN);
           int currentHealthWidth = (int)((double) health / maxHealth * gp.tileSize);
           g2.fillRect(screenX, screenY - 10, currentHealthWidth, 5);
       }
   
       public void draw(Graphics2D g2) {
           if (zombie1 == null) return; 
   
           int screenX = worldX - gp.player.worldX + gp.player.screenX;
           int screenY = worldY - gp.player.worldY + gp.player.screenY;
   
           if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
               worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
               worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
               worldY - gp.tileSize < gp.player.worldY + gp.player.screenY) {
   
               g2.drawImage(zombie1, screenX, screenY, gp.tileSize, gp.tileSize, null);
               drawHealthBar(g2, screenX, screenY);
           }
       }
   
       public void decreaseHealth(int damage) {
           health = Math.max(0, health - damage);
       }
   
       public void update() {
           if (gp.player.worldX > this.worldX) {
               worldX += speed;
           } else if (gp.player.worldX < this.worldX) {
               worldX -= speed;
           }
   
           if (gp.player.worldY > this.worldY) {
               worldY += speed;
           } else if (gp.player.worldY < this.worldY) {
               worldY -= speed;
           }
            
           long currentTime = System.currentTimeMillis();
           if (currentTime - lastSoundTime >= SOUND_DELAY) {
               gp.audioManager.playZombieSound();
               lastSoundTime = currentTime;
        }
           
           solidArea.x = worldX;
           solidArea.y = worldY;
       }
   }
   