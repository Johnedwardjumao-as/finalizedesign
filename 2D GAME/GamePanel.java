import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.KeyStroke;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

public class GamePanel extends JPanel implements Runnable, MouseListener {

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
    private boolean mainMenuTriggered = false;
    
    private boolean isPaused = false;
    private boolean isExitHovered = false;

    public GamePanel(int initialMoney) { 
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.addMouseListener(this);
        this.setFocusable(true);

        audioManager = new AudioManager();
        audioManager.loadBackgroundMusic();
        audioManager.loadZombieSound();
        audioManager.loadAttackSound();
        audioManager.playBackgroundMusic();
        
        playerStats = new PlayerStats(this, initialMoney);
        waveManager = new WaveManager(this, player);
        meleeWeapon = new MeleeWeapon(this);
        eManager.setup();
        setupPauseListener();
    }

    private void setupPauseListener() {
        getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "pause");
        getActionMap().put("pause", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePause();
            }
        });
    }

    private void togglePause() {
        isPaused = !isPaused;
        
        if (isPaused) {
            audioManager.backgroundMusic.stop();
        } else {
            audioManager.playBackgroundMusic();
        }
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
        if (!isPaused && !gameOver) {
            player.update();
            waveManager.update();

            if (waveManager.allWavesCompleted && !mainMenuTriggered) {
                if (System.currentTimeMillis() - waveManager.waveTextTimer > 5000) {
                    returnToMainMenu();
                    mainMenuTriggered = true;
                }
            }

            if (playerStats.getHealth() <= 0) {
                audioManager.backgroundMusic.stop();
                gameOver = true;
                
                new Timer().schedule(new TimerTask()  {
                public void run()   {
                  SwingUtilities.invokeLater(() -> returnToMainMenu());
                }
                },5000);
                
            }
        }
    }

    private void returnToMainMenu() {
        MainMenu mainMenu = new MainMenu(playerStats.getMoney());
        mainMenu.setVisible(true);
        SwingUtilities.getWindowAncestor(this).dispose();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if (!gameOver && !waveManager.allWavesCompleted) {
            tileM.draw(g2);
            player.draw(g2);
            waveManager.draw(g2);
            eManager.draw(g2);
        }

        if (!waveManager.allWavesCompleted && waveManager.showWaveText) {
            long elapsedTime = System.currentTimeMillis() - waveManager.waveTextTimer;
            float fadeFactor = Math.max(0.0f, 1.0f - (elapsedTime / 3000.0f));

            g2.setColor(new Color(255, 0, 0, (int)(fadeFactor * 255)));
            g2.setFont(new Font("Arial", Font.BOLD, 70));
            String waveText = "Wave " + waveManager.getCurrentWave();

            int textWidth = g2.getFontMetrics().stringWidth(waveText);
            int textHeight = g2.getFontMetrics().getHeight();
            int x = screenWidth / 2 - textWidth / 2;
            int y = screenHeight / 2 - textHeight / 2;

            g2.drawString(waveText, x, y);
        }

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.drawString("Money: $" + playerStats.getMoney(),
                screenWidth - 200, screenHeight - 30);

        if (waveManager.allWavesCompleted) {
          audioManager.backgroundMusic.stop();
          g2.setColor(Color.GREEN);
      
          g2.setFont(new Font("Arial", Font.BOLD, 60));
          String completedText = "ALL WAVES COMPLETE";
          int textWidth = g2.getFontMetrics().stringWidth(completedText);
          int primaryTextY = screenHeight / 2; 
          g2.drawString(completedText,
                  screenWidth / 2 - textWidth / 2,
                  primaryTextY);
  
          g2.setFont(new Font("Arial", Font.PLAIN, 20));
          String secondaryText = "Going back to menu in 5s";
          int secondaryTextWidth = g2.getFontMetrics().stringWidth(secondaryText);
          int secondaryTextY = primaryTextY + 40; 
          g2.drawString(secondaryText,
                  screenWidth / 2 - secondaryTextWidth / 2,
                  secondaryTextY);
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

        // Draw pause overlay and menu
        if (isPaused) {
            g2.setColor(new Color(0, 0, 0, 150)); // Semi-transparent black
            g2.fillRect(0, 0, screenWidth, screenHeight);

            // Pause text
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Arial", Font.BOLD, 72));
            String pauseText = "PAUSED";
            int textWidth = g2.getFontMetrics().stringWidth(pauseText);
            g2.drawString(pauseText, screenWidth/2 - textWidth/2, screenHeight/2);

            // Pause instructions
            g2.setFont(new Font("Arial", Font.BOLD, 17));
            String resumeText = "Press ESC to Resume";
            int resumeWidth = g2.getFontMetrics().stringWidth(resumeText);
            g2.drawString(resumeText, screenWidth/2 - resumeWidth/2, screenHeight/2 + 100);

            // Exit to Main Menu option
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            String exitText = "Exit to Main Menu";
            int exitWidth = g2.getFontMetrics().stringWidth(exitText);
            
            // Highlight exit option if hovered
            if (isExitHovered) {
                g2.setColor(Color.CYAN);
            } else {
                g2.setColor(Color.GREEN);
            }
            
            g2.drawString(exitText, screenWidth/2 - exitWidth/2, screenHeight/2 + 200);
        }

        g2.dispose();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (isPaused) {
            int mouseX = e.getX();
            int mouseY = e.getY();

            int exitTextWidth = getFontMetrics(new Font("Arial", Font.BOLD, 36)).stringWidth("Exit to Main Menu");
            int exitTextX = screenWidth/2 - exitTextWidth/2;
            int exitTextY = screenHeight/2 + 200;

            if (mouseX >= exitTextX && mouseX <= exitTextX + exitTextWidth 
                && mouseY >= exitTextY - 36 && mouseY <= exitTextY) {
                returnToMainMenu();
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {}

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}

    public void mouseMoved(MouseEvent e) {
        if (isPaused) {
            int mouseX = e.getX();
            int mouseY = e.getY();
            
            int exitTextWidth = getFontMetrics(new Font("Arial", Font.BOLD, 36)).stringWidth("Exit to Main Menu");
            int exitTextX = screenWidth/2 - exitTextWidth/2;
            int exitTextY = screenHeight/2 + 200;

            boolean wasHovered = isExitHovered;
            isExitHovered = (mouseX >= exitTextX && mouseX <= exitTextX + exitTextWidth 
                             && mouseY >= exitTextY - 36 && mouseY <= exitTextY);

            if (wasHovered != isExitHovered) {
                repaint();
            }
        }
    }
}