import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class LoadoutScreen extends JFrame {
    private MainMenu mainMenu;
    private BufferedImage weapon1;
    private BufferedImage weapon2;
    private BufferedImage background;

    public LoadoutScreen(MainMenu mainMenu) {
        this.mainMenu = mainMenu;

        setTitle("Loadout");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            weapon1 = ImageIO.read(getClass().getResourceAsStream("/src/swords/loadout1.png"));
            weapon2 = ImageIO.read(getClass().getResourceAsStream("/src/swords/loadout2.png"));
            background = ImageIO.read(getClass().getResourceAsStream("/src/mc sprites/loadout.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel loadoutPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                
                if (background != null) {
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
                
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial", Font.BOLD, 48));
                String title = "Loadout";
                int textWidth = g.getFontMetrics().stringWidth(title);
                g.drawString(title, getWidth() / 2 - textWidth / 2, 100);
            }
        };
        loadoutPanel.setLayout(null);

        JLabel weapon1Label = new JLabel(new ImageIcon(weapon1.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        weapon1Label.setBounds(200, 200, 100, 100);
        loadoutPanel.add(weapon1Label);

        JLabel equipWeapon1Label = new JLabel("Equip", JLabel.CENTER);
        equipWeapon1Label.setForeground(Color.GREEN);
        equipWeapon1Label.setFont(new Font("Arial", Font.BOLD, 18));
        equipWeapon1Label.setBounds(200, 310, 100, 30);
        equipWeapon1Label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        equipWeapon1Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showUnavailableMessage();
            }
        });
        loadoutPanel.add(equipWeapon1Label);

        JLabel weapon2Label = new JLabel(new ImageIcon(weapon2.getScaledInstance(100, 100, Image.SCALE_SMOOTH)));
        weapon2Label.setBounds(500, 200, 100, 100);
        loadoutPanel.add(weapon2Label);

        JLabel equipWeapon2Label = new JLabel("Equip", JLabel.CENTER);
        equipWeapon2Label.setForeground(Color.GREEN);
        equipWeapon2Label.setFont(new Font("Arial", Font.BOLD, 18));
        equipWeapon2Label.setBounds(500, 310, 100, 30);
        equipWeapon2Label.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        equipWeapon2Label.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showUnavailableMessage();
            }
        });
        loadoutPanel.add(equipWeapon2Label);

        JLabel backLabel = new JLabel("Back", JLabel.CENTER);
        backLabel.setForeground(Color.GREEN);
        backLabel.setFont(new Font("Arial", Font.BOLD, 24));
        backLabel.setBounds(300, 400, 200, 50);
        backLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                returnToMainMenu();
            }
        });
        loadoutPanel.add(backLabel);

        add(loadoutPanel);
    }

    private void showUnavailableMessage() {
        JDialog dialog = new JDialog(this, "Unavailable", true);
        dialog.setUndecorated(true);
        dialog.setSize(300, 100);
        dialog.setLocationRelativeTo(this);

        JPanel panel = new JPanel();
        panel.setBackground(Color.BLACK);
        panel.setBorder(BorderFactory.createLineBorder(Color.WHITE, 2));
        panel.setLayout(new BorderLayout());

        JLabel message = new JLabel("Weapon not available.", JLabel.CENTER);
        message.setForeground(Color.WHITE);
        message.setFont(new Font("Arial", Font.BOLD, 16));
        message.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        message.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                dialog.dispose();
            }
        });

        panel.add(message, BorderLayout.CENTER);
        dialog.add(panel);
        dialog.setVisible(true);
    }

    private void returnToMainMenu() {
        mainMenu.setVisible(true);
        dispose();
    }
}
