import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
public class GamePanel extends JPanel implements Runnable {
    
    int originalTileSize = 16; 
    int scale = 3;
    public int tileSize = originalTileSize * scale; 
    public int maxScreenCol = 16;
    public int maxScreenRow = 12;
    public int screenWidth = tileSize * maxScreenCol; 
    public int screenHeight = tileSize * maxScreenRow; 
    
    public final int maxWorldCol = 70;
    public final int maxWorldRow = 30;
    public final int worldWidth = tileSize * maxWorldCol;
    public final int worldHeight = tileSize * maxWorldRow;
    
    int FPS = 60;
    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    EnvironmentManager eManager = new EnvironmentManager(this);
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);
    public WaveManager waveManager;
    public PlayerStats playerStats;
    public MeleeWeapon meleeWeapon;
    public AudioManager audioManager;
    
    public boolean gameOver = false;
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
        
        audioManager = new AudioManager();
        audioManager.loadBackgroundMusic();
        audioManager.loadZombieSound(); 
        audioManager.loadAttackSound(); 
        audioManager.playBackgroundMusic();
              
        playerStats = new PlayerStats(this);
        waveManager = new WaveManager(this, player);
        meleeWeapon = new MeleeWeapon(this);
            
        eManager.setup();
    }
    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }
    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        long drawCount = 0;
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;
            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }
            if (timer >= 1000000000) {
                System.out.println("FPS:" + drawCount);
                drawCount = 0;
                timer = 0;
            }
        }
    }
    public void update() {
        if (!gameOver) {
            player.update();
            waveManager.update();

            if (playerStats.getHealth() <= 0) {
                gameOver = true;
            }
        }
    }
    public void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2 = (Graphics2D) g;
    
    if (!gameOver) {
        tileM.draw(g2);
        player.draw(g2);
        waveManager.draw(g2);
        
        eManager.draw(g2);
    }
    
    if (gameOver) {
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 72));
        String gameOverText = "YOU DIED";
        int textWidth = g2.getFontMetrics().stringWidth(gameOverText);
        g2.drawString(gameOverText,
                screenWidth / 2 - textWidth / 2,
                screenHeight / 2);
    }
    g2.dispose();
}
}