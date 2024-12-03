

public class PlayerStats {
    private int health = 100;
    private int maxHealth = 100;
    private GamePanel gp;

    public PlayerStats(GamePanel gp) {
        this.gp = gp;
    }

    public int getHealth() {
        return health;
    }

    public void decreaseHealth(int amount) {
        health = Math.max(0, health - amount);
        System.out.println("Player hit! Health reduced to: " + health);
        
        if (health <= 0) {
            System.out.println("Game Over - Player died!");
            gp.gameOver = true;
        }
    }

    public void increaseHealth(int amount) {
        health = Math.min(health + amount, maxHealth);
        System.out.println("Health increased. Current health: " + health);
    }
}