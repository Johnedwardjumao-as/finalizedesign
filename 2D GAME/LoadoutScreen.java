import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoadoutScreen extends JFrame {
    private MainMenu mainMenu;
    private BufferedImage weapon1;
    private BufferedImage weapon2;

    public LoadoutScreen(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setTitle("Loadout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        // Load weapon images
        try {
            weapon1 = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword1.png"));
            weapon2 = ImageIO.read(getClass().getResourceAsStream("/src/swords/sword2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel loadoutPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());

                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                String title = "Loadout";
                int textWidth = g.getFontMetrics().stringWidth(title);
                g.drawString(title, getWidth() / 2 - textWidth / 2, 100);

                // Draw weapon images
                if (weapon1 != null) {
                    g.drawImage(weapon1, 200, 200, 100, 100, null);
                }
                if (weapon2 != null) {
                    g.drawImage(weapon2, 500, 200, 100, 100, null);
                }
            }
        };
        loadoutPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Equip Weapon 1 Button
        JButton equipWeapon1Button = new JButton("Equip");
        equipWeapon1Button.setPreferredSize(new Dimension(100, 50));
        equipWeapon1Button.addActionListener(e -> showUnavailableMessage());
        gbc.gridx = 0;
        gbc.gridy = 1;
        loadoutPanel.add(equipWeapon1Button, gbc);

        // Equip Weapon 2 Button
        JButton equipWeapon2Button = new JButton("Equip");
        equipWeapon2Button.setPreferredSize(new Dimension(100, 50));
        equipWeapon2Button.addActionListener(e -> showUnavailableMessage());
        gbc.gridx = 1;
        loadoutPanel.add(equipWeapon2Button, gbc);

        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setPreferredSize(new Dimension(200, 50));
        backButton.addActionListener(e -> returnToMainMenu());
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        loadoutPanel.add(backButton, gbc);

        add(loadoutPanel);
    }

    private void showUnavailableMessage() {
        JOptionPane.showMessageDialog(this,
                "Weapon not available",
                "Unavailable",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void returnToMainMenu() {
        mainMenu.setVisible(true);
        dispose();
    }
}
