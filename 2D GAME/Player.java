import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.imageio.ImageIO; 
import java.awt.image.BufferedImage; 
import java.io.IOException;
import java.awt.Rectangle;

public class Player extends Entity {

   GamePanel gp;
   KeyHandler keyH;
   
   public final int screenX;
   public final int screenY;

   public Player(GamePanel gp, KeyHandler keyH) {
      this.gp = gp;
      this.keyH = keyH;
      
      screenX = gp.screenWidth/2 - (gp.tileSize/2);
      screenY = gp.screenHeight/2 - (gp.tileSize/2);
      
      solidArea  = new Rectangle();
      solidArea.x = 8;
      solidArea.y = 16;
      solidArea.width = 32;
      solidArea.height = 32;
      
      setDefaultValues(); 
      getPlayerImage();
   }

   public void setDefaultValues() {
      worldX = gp.tileSize * 5; 
      worldY = gp.tileSize * 15; 
      speed = 4; 
      direction = "down";
   }
   
   public void getPlayerImage() {
      try {
         up1 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/up1.png"));
         up2 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/up2.png"));
         down1 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/down1.png"));
         down2 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/down2.png"));
         left1 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/left1.png"));
         left2 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/left2.png"));
         right1 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/right1.png"));
         right2 = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/right2.png"));
      } catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void update() {
      
      if(keyH.upPressed || keyH.downPressed || keyH.leftPressed || keyH.rightPressed) {
         if (keyH.upPressed) {
            direction = "up";
         } else if (keyH.downPressed) {
            direction = "down";
         } else if (keyH.leftPressed) {
            direction = "left";
         } else if (keyH.rightPressed) {
            direction = "right";
         }
         
         collisionOn = false;
         gp.cChecker.checkTile(this);
         
         if (!collisionOn) {
            switch(direction) {
               case "up": worldY -= speed; break;
               case "down": worldY += speed; break;
               case "left": worldX -= speed; break;
               case "right": worldX += speed; break;
            }
         }
         
         
         spriteCounter++;
         if(spriteCounter > 12) {
            spriteNum = (spriteNum == 1) ? 2 : 1;
            spriteCounter = 0;
         }
      }
      
      
      if (keyH.attackPressed) {
         gp.meleeWeapon.attack(this, gp.waveManager.zombies);
      }
   }

   public void draw(Graphics2D g2) {
        BufferedImage image = null;
        
        switch(direction) {
            case "up":
                image = (spriteNum == 1) ? up1 : up2;
                break;
            case "down":
                image = (spriteNum == 1) ? down1 : down2;
                break;
            case "left":
                image = (spriteNum == 1) ? left1 : left2;
                break;
            case "right":
                image = (spriteNum == 1) ? right1 : right2;
                break;
        }
        
        g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
        
        
        g2.setColor(Color.RED);
        g2.fillRect(screenX, screenY - 10, gp.tileSize, 5);
        
        g2.setColor(Color.GREEN);
        int currentHealthWidth = (int)((double)gp.playerStats.getHealth() / 100 * gp.tileSize);
        g2.fillRect(screenX, screenY - 10, currentHealthWidth, 5);
        
        
        gp.meleeWeapon.drawSwordAnimation(g2, this);
    }
}