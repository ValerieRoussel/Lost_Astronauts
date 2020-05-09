import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

public class UI extends JPanel {
    String path = "sprites/icons/";
    int healthPixels;

    private Image p1_connected = new ImageIcon(path + "P1_iconC.png").getImage();
    private Image p1_disconnected = new ImageIcon(path + "P1_iconDC.png").getImage();
    private Image p2_connected = new ImageIcon(path + "P2_iconC.png").getImage();
    private Image p2_disconnected = new ImageIcon(path + "P2_iconDC.png").getImage();
    private Image p1_switch = new ImageIcon(path + "P1_switch.png").getImage();
    private Image p2_switch = new ImageIcon(path + "P2_switch.png").getImage();
    private Image p1_main = new ImageIcon(path + "P1_main.png").getImage();
    private Image p2_main = new ImageIcon(path + "P2_main.png").getImage();

    Image p1_connection;
    //Image connectionStatus;
    Image p2_connection;
    //Image connectionPrompt;
    Image mainIcon;
    Image switchIcon;

    public UI() {
        p1_connection = p1_connected;
        p2_connection = p2_connected;
        switchIcon = p2_switch;
        mainIcon = p1_main;
        healthPixels = 14;
    }

    public void updateUI(Player currPlayer, Player p1, Player p2) {
        if (p1.connected) {
            p1_connection = p1_connected;
        } else {
            p1_connection = p1_disconnected;
        }

        if (p2.connected) {
            p2_connection = p2_connected;
        } else {
            p2_connection = p2_disconnected;
        }

        if (currPlayer.playerNum == 1) {
            mainIcon = p1_main;
            switchIcon = p2_switch;
        } else {
            mainIcon = p2_main;
            switchIcon = p1_switch;
        }
        float healthFloat = (float)(currPlayer.hp)/100;
        healthPixels = (int)Math.ceil(healthFloat * 14);
    }

    public void paint(Graphics g) {
        AffineTransform oldXForm = ((Graphics2D)g).getTransform();
        ((Graphics2D)g).scale(4, 4);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 200, 18);

        g.drawImage(mainIcon, 92, 1, null);
        g.setColor(Color.decode("#67d100"));
        g.fillRect(93, 14, healthPixels, 2);
        g.drawImage(switchIcon, 158, 1, null);

        ((Graphics2D) g).setTransform(oldXForm);
        ((Graphics2D)g).scale(3, 3);
        g.drawImage(p1_connection, 2, 2, null);
        g.drawImage(p2_connection, 46, 2, null);
    }
}
