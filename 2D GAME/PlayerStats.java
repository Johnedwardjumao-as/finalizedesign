public class PlayerStats {
    private int health = 100;
    private int maxHealth = 100;
    private int money = 0;
    private GamePanel gp;

    public PlayerStats(GamePanel gp, int initialMoney) { // Updated constructor
        this.gp = gp;
        this.money = initialMoney; // Initialize money
    }

    public int getHealth() {
        return health;
    }

    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
        System.out.println("Money earned: $" + amount + ". Total: $" + money);
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
