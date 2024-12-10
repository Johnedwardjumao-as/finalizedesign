import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import javax.imageio.ImageIO;

public class MainMenu extends JFrame {
    private int screenWidth = 800;
    private int screenHeight = 600;
    private String[] menuOptions = {"PLAY", "LOADOUT", "EXIT"};
    private int selectedOption = -1;
    private Image backgroundImage;
    private AudioManager audioManager;
    private int playerMoney = 0;

    public MainMenu(int previousMoney) {
        this();
        this.playerMoney = previousMoney;
    }

    public MainMenu() {
        setTitle("The Last Escape");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(screenWidth, screenHeight);
        setResizable(false);
        setLocationRelativeTo(null);

        try {
            backgroundImage = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/main.png"));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Background image not found.");
        }

        audioManager = new AudioManager();
        audioManager.loadMainMenuMusic();
        audioManager.playBackgroundMusic();

        JPanel menuPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                drawBackground(g2);
                drawMainMenu(g2);
            }
        };

        menuPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                updateHover(e.getX(), e.getY());
            }
        });

        add(menuPanel);
    }

    private void drawBackground(Graphics2D g2) {
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        } else {
            g2.setColor(Color.BLACK);
            g2.fillRect(0, 0, screenWidth, screenHeight);
        }
    }

    private void drawMainMenu(Graphics2D g2) {
        g2.setFont(new Font("Arial", Font.BOLD, 80));
        String title = "THE LAST ESCAPE";
        int titleWidth = g2.getFontMetrics().stringWidth(title);

        g2.setColor(Color.DARK_GRAY);
        g2.drawString(title, screenWidth / 2 - titleWidth / 2 + 5, screenHeight / 4 + 5);

        g2.setColor(new Color(50, 205, 50));
        g2.drawString(title, screenWidth / 2 - titleWidth / 2, screenHeight / 4);

        g2.setFont(new Font("Arial", Font.BOLD, 48));
        int startY = screenHeight / 2 + 50;
        for (int i = 0; i < menuOptions.length; i++) {
            drawMenuOption(g2, menuOptions[i], startY + i * 100, i == selectedOption);
        }

        g2.setFont(new Font("Arial", Font.BOLD, 24));
        g2.setColor(Color.WHITE);
        g2.drawString("Money: $" + playerMoney, screenWidth - 200, screenHeight - 50);
    }

    private void drawMenuOption(Graphics2D g2, String text, int y, boolean isSelected) {
        int textWidth = g2.getFontMetrics().stringWidth(text);

        if (isSelected) {
            g2.setColor(new Color(50, 50, 50, 180));
            g2.fillRoundRect(screenWidth / 2 - textWidth / 2 - 20, y - 40, textWidth + 40, 60, 30, 30);
            g2.setColor(Color.CYAN);
        } else {
            g2.setColor(Color.GREEN);
        }

        g2.drawString(text, screenWidth / 2 - textWidth / 2, y);
    }

    private void handleMouseClick(int x, int y) {
    int startY = screenHeight / 2 + 50;
    for (int i = 0; i < menuOptions.length; i++) {
        int optionY = startY + i * 100;
        int textWidth = getFontMetrics(new Font("Arial", Font.BOLD, 48)).stringWidth(menuOptions[i]);
        int textHeight = getFontMetrics(new Font("Arial", Font.BOLD, 48)).getHeight();

        // Dynamically calculate bounds
        int optionXStart = screenWidth / 2 - textWidth / 2 - 20;
        int optionXEnd = screenWidth / 2 + textWidth / 2 + 20;
        int optionYStart = optionY - 40;
        int optionYEnd = optionY + textHeight / 2;

        if (x > optionXStart && x < optionXEnd && y > optionYStart && y < optionYEnd) {
            switch (menuOptions[i]) {
                case "PLAY" -> startGame();
                case "LOADOUT" -> openLoadout();
                case "EXIT" -> System.exit(0);
            }
        }
    }
}


    private void updateHover(int x, int y) {
        int startY = screenHeight / 2 + 50;
        int newSelectedOption = -1;

        for (int i = 0; i < menuOptions.length; i++) {
            int optionY = startY + i * 100;
            if (x > screenWidth / 2 - 150 && x < screenWidth / 2 + 150 && y > optionY - 40 && y < optionY + 20) {
                newSelectedOption = i;
            }
        }

        if (newSelectedOption != selectedOption) {
            selectedOption = newSelectedOption;
            repaint();
        }
    }

    private void startGame() {
    audioManager.stopAll();

    JFrame gameWindow = new JFrame("The Last Escape");
    gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    gameWindow.setResizable(false);

    GamePanel gamePanel = new GamePanel(playerMoney);
    gameWindow.add(gamePanel);
    gameWindow.pack();
    gameWindow.setLocationRelativeTo(null);
    gameWindow.setVisible(true);

    gamePanel.startGameThread();
    dispose();
}


    private void openLoadout() {
       LoadoutScreen loadoutScreen = new LoadoutScreen(this,playerMoney);
       loadoutScreen.setVisible(true);
       dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu().setVisible(true));
    }
}
   