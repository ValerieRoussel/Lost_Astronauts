import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.ArrayList;

public class UI extends JPanel {
    String path = "sprites/icons/";
    int healthPixels;
    ArrayList<Upgrade> inv = new ArrayList<Upgrade>();
    Coord[] upgradeSlots = {
            new Coord(4, 4),
            new Coord(18, 4),
            new Coord(32, 4),
            new Coord(4, 18),
            new Coord(18, 18),
            new Coord(32, 18)};

    private Image p1_connected = new ImageIcon(path + "P1_iconC.png").getImage();
    private Image p1_disconnected = new ImageIcon(path + "P1_iconDC.png").getImage();
    private Image p2_connected = new ImageIcon(path + "P2_iconC.png").getImage();
    private Image p2_disconnected = new ImageIcon(path + "P2_iconDC.png").getImage();
    private Image p1_switch = new ImageIcon(path + "P1_switch.png").getImage();
    private Image p2_switch = new ImageIcon(path + "P2_switch.png").getImage();
    private Image p1_main = new ImageIcon(path + "P1_main.png").getImage();
    private Image p2_main = new ImageIcon(path + "P2_main.png").getImage();
    private Image con = new ImageIcon(path + "connection.png").getImage();
    private Image discon = new ImageIcon(path + "noConnection.png").getImage();

    Image inventory = new ImageIcon(path + "inventory.png").getImage();

    Image p1_connection;
    Image connectionStatus;
    Image p2_connection;
    Image mainIcon;
    Image switchIcon;

    public UI() {
        p1_connection = p1_connected;
        p2_connection = p2_connected;
        connectionStatus = con;
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

        if (p1.connected && p2.connected) {
            connectionStatus = con;
        } else {
            connectionStatus = discon;
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
        inv = currPlayer.inventory;
    }

    public void paint(Graphics g) {
        AffineTransform oldXForm = ((Graphics2D)g).getTransform();
        ((Graphics2D)g).scale(4, 4);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 200, 18);

        g.drawImage(mainIcon, 92, 1, null);
        g.setColor(Color.decode("#67d100"));
        g.fillRect(93, 14, healthPixels, 2);

        ((Graphics2D) g).setTransform(oldXForm);
        ((Graphics2D)g).scale(3, 3);
        g.drawImage(p1_connection, 2, 4, null);
        g.drawImage(connectionStatus, 29, 4, null);
        g.drawImage(p2_connection, 51, 4, null);
        g.drawImage(switchIcon, 224, 4, null);

        ((Graphics2D) g).setTransform(oldXForm);
        ((Graphics2D)g).scale(2, 2);
        int invX = 126;
        int invY = 2;
        g.drawImage(inventory, invX, invY, null);
        for (int i = 0; i < inv.size() && i < upgradeSlots.length; i++) {
            g.drawImage(inv.get(i).smallIcon, invX + upgradeSlots[i].x, invY + upgradeSlots[i].y, null);
        }
    }
}
