import java.awt.Rectangle;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;
import java.awt.Graphics2D; 

public class MeleeWeapon {
    GamePanel gp;
    
    
    public BufferedImage[] swordUpSprites = new BufferedImage[3];
    public BufferedImage[] swordDownSprites = new BufferedImage[3];
    public BufferedImage[] swordLeftSprites = new BufferedImage[3];
    public BufferedImage[] swordRightSprites = new BufferedImage[3];
    
    public boolean isAttacking = false;
    public int attackAnimationCounter = 0;
    public int attackAnimationSpeed = 5; 
    public int currentSpriteIndex = 0;

    public MeleeWeapon(GamePanel gp) {
        this.gp = gp;
        loadSwordSprites();
    }

    private void loadSwordSprites() {
        try {
            
            swordUpSprites[0] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword1.png"));
            swordUpSprites[1] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));
            swordUpSprites[2] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));

            
            swordDownSprites[0] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword1.png"));
            swordDownSprites[1] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));
            swordDownSprites[2] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));

            
            swordLeftSprites[0] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword1.png"));
            swordLeftSprites[1] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));
            swordLeftSprites[2] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));

            
            swordRightSprites[0] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword1.png"));
            swordRightSprites[1] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));
            swordRightSprites[2] = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void drawSwordAnimation(Graphics2D g2, Player player) {
        if (!isAttacking) return;

        BufferedImage[] currentSprites;
        int spriteX = 0, spriteY = 0;

        
        switch (player.direction) {
            case "up":
                currentSprites = swordUpSprites;
                spriteX = player.worldX - gp.player.worldX + player.screenX;
                spriteY = player.worldY - gp.player.worldY + player.screenY - gp.tileSize;
                break;
            case "down":
                currentSprites = swordDownSprites;
                spriteX = player.worldX - gp.player.worldX + player.screenX;
                spriteY = player.worldY - gp.player.worldY + player.screenY + gp.tileSize;
                break;
            case "left":
                currentSprites = swordLeftSprites;
                spriteX = player.worldX - gp.player.worldX + player.screenX - gp.tileSize;
                spriteY = player.worldY - gp.player.worldY + player.screenY;
                break;
            case "right":
                currentSprites = swordRightSprites;
                spriteX = player.worldX - gp.player.worldX + player.screenX + gp.tileSize;
                spriteY = player.worldY - gp.player.worldY + player.screenY;
                break;
            default:
                return;
        }

        
        g2.drawImage(currentSprites[currentSpriteIndex], spriteX, spriteY, gp.tileSize, gp.tileSize, null);
    }

    public void attack(Player player, ArrayList<Zombie> zombies) {
    
        gp.audioManager.playAttackSound();
        
        if (!isAttacking) {
            isAttacking = true;
            attackAnimationCounter = 0;
            currentSpriteIndex = 0;
            
        }

       
        attackAnimationCounter++;
        if (attackAnimationCounter > attackAnimationSpeed) {
            currentSpriteIndex++;
            attackAnimationCounter = 0;

            
            if (currentSpriteIndex >= 3) {
                isAttacking = false;
                currentSpriteIndex = 0;
            }
        }

        int attackRange = 50;
        Rectangle weaponArea = new Rectangle();

        
        switch (player.direction) {
            case "up":
                weaponArea.setBounds(
                    player.worldX + player.solidArea.x - attackRange / 2,
                    player.worldY + player.solidArea.y - attackRange,
                    player.solidArea.width,
                    attackRange
                );
                break;
            case "down":
                weaponArea.setBounds(
                    player.worldX + player.solidArea.x - attackRange / 2,
                    player.worldY + player.solidArea.y + player.solidArea.height,
                    player.solidArea.width,
                    attackRange
                );
                break;
            case "left":
                weaponArea.setBounds(
                    player.worldX + player.solidArea.x - attackRange,
                    player.worldY + player.solidArea.y + player.solidArea.height / 2,
                    attackRange,
                    player.solidArea.height
                );
                break;
            case "right":
                weaponArea.setBounds(
                    player.worldX + player.solidArea.x + player.solidArea.width,
                    player.worldY + player.solidArea.y + player.solidArea.height / 2,
                    attackRange,
                    player.solidArea.height
                );
                break;
        }

        
        for (Zombie zombie : zombies) {
            if (weaponArea.intersects(zombie.solidArea)) {
                zombie.decreaseHealth(20);
                
                
                int knockbackDistance = 30;
                switch (player.direction) {
                    case "up":
                        zombie.worldY -= knockbackDistance;
                        break;
                    case "down":
                        zombie.worldY += knockbackDistance;
                        break;
                    case "left":
                        zombie.worldX -= knockbackDistance;
                        break;
                    case "right":
                        zombie.worldX += knockbackDistance;
                        break;
                }
            }
        }
    }
}