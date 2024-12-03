import java.awt.Graphics2D; 

public class EnvironmentManager  {
   GamePanel gp;
   Lighting lighting;
   
   public EnvironmentManager(GamePanel gp)   {
      this.gp = gp;
   }
   public void setup()  {
      lighting = new Lighting(gp, 550);
   }
   public void draw(Graphics2D g2)  {
      lighting.setZombieCount(gp.waveManager.zombieCount);
      lighting.draw(g2);
   }
}