import java.awt.Rectangle;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.JPanel;



public class CollisionChecker {
   
   GamePanel gp;
   
   public CollisionChecker(GamePanel gp)  {
      this.gp = gp;
   }
   
   public void checkTile(Entity entity)   {
      
      
      int entityLeftWorldX = Math.max(0, entity.worldX + entity.solidArea.x);
      int entityRightWorldX = Math.min(gp.worldWidth, entity.worldX + entity.solidArea.x + entity.solidArea.width);
      int entityTopWorldY = Math.max(0, entity.worldY + entity.solidArea.y);
      int entityBottomWorldY = Math.min(gp.worldHeight, entity.worldY + entity.solidArea.y + entity.solidArea.height);
      
      
      int entityLeftCol = Math.min(Math.max(entityLeftWorldX/gp.tileSize, 0), gp.maxWorldCol - 1);
      int entityRightCol = Math.min(Math.max(entityRightWorldX/gp.tileSize, 0), gp.maxWorldCol - 1);
      int entityTopRow = Math.min(Math.max(entityTopWorldY/gp.tileSize, 0), gp.maxWorldRow - 1);
      int entityBottomRow = Math.min(Math.max(entityBottomWorldY/gp.tileSize, 0), gp.maxWorldRow - 1);
      
      int tileNum1, tileNum2;
      
      switch(entity.direction)   {
      case "up":
          entityTopRow = Math.min(Math.max((entityTopWorldY - entity.speed)/gp.tileSize, 0), gp.maxWorldRow - 1);
          tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
          tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
          if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)  {
            entity.collisionOn = true;
          }
          break;
      case "down":
          entityBottomRow = Math.min(Math.max((entityBottomWorldY + entity.speed)/gp.tileSize, 0), gp.maxWorldRow - 1);
          tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
          tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
          if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)  {
            entity.collisionOn = true;
          }
          break;
      case "left":
          entityLeftCol = Math.min(Math.max((entityLeftWorldX - entity.speed)/gp.tileSize, 0), gp.maxWorldCol - 1);
          tileNum1 = gp.tileM.mapTileNum[entityLeftCol][entityTopRow];
          tileNum2 = gp.tileM.mapTileNum[entityLeftCol][entityBottomRow];
          if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)  {
            entity.collisionOn = true;
          }
          break;
      case "right":
          entityRightCol = Math.min(Math.max((entityRightWorldX + entity.speed)/gp.tileSize, 0), gp.maxWorldCol - 1);
          tileNum1 = gp.tileM.mapTileNum[entityRightCol][entityTopRow];
          tileNum2 = gp.tileM.mapTileNum[entityRightCol][entityBottomRow];
          if(gp.tileM.tile[tileNum1].collision == true || gp.tileM.tile[tileNum2].collision == true)  {
            entity.collisionOn = true;
          }
          break;
      }
   }
}