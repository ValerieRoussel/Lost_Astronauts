import javax.swing.*;
import java.awt.*;

public class TradeMenu {
    String path = "sprites/upgrades/";
    Image p1_frame = new ImageIcon(path + "P1_tradeFrame.png").getImage();
    Image p2_frame = new ImageIcon(path + "P2_tradeFrame.png").getImage();
    Image instruct = new ImageIcon(path + "instruct.png").getImage();
    Image pause = new ImageIcon("sprites/icons/PauseMenu.png").getImage();

    Coord[] upgradeSlots = {
            new Coord(4, 4),
            new Coord(34, 4),
            new Coord(4, 35),
            new Coord(34, 35),
            new Coord(4, 66),
            new Coord(34, 66),};

    public TradeMenu() {}

    public void drawMenu(Graphics g, Camera cam, Player p1, Player p2) {
        int p1_frameX = -cam.camX + 16;
        int p2_frameX = -cam.camX + 120;
        int frameY = -cam.camY + 12;

        g.setColor(new Color(0f, 0f,0f, 0.8f));
        g.fillRect(-cam.camX, -cam.camY, 200, 132);
        g.drawImage(p1_frame, p1_frameX, frameY, null);
        g.drawImage(p2_frame, p2_frameX, frameY, null);
        g.drawImage(instruct, p1_frameX + 68, frameY + 28, null);

        for (int i = 0; i < p1.inventory.size() && i < upgradeSlots.length; i++) {
            g.drawImage(p1.inventory.get(i).largeIcon, p1_frameX + upgradeSlots[i].x, frameY + upgradeSlots[i].y, null);
        }
        for (int i = 0; i < p2.inventory.size() && i < upgradeSlots.length; i++) {
            g.drawImage(p2.inventory.get(i).largeIcon, p2_frameX + upgradeSlots[i].x, frameY + upgradeSlots[i].y, null);
        }
    }

    public void drawPauseMenu(Graphics g, Camera cam, boolean[] upgrades) {
        int frameX = -cam.camX + 64;
        int frameY = -cam.camY + 12;

        g.setColor(new Color(0f, 0f,0f, 0.8f));
        g.fillRect(-cam.camX, -cam.camY, 200, 132);
        g.drawImage(pause, frameX, frameY, null);
        g.setColor(new Color(0f, 0f,0f, 1.0f));
        if (!upgrades[0]) {
            g.fillRect(frameX + 7, frameY + 47, 58, 10);
        }
        if (!upgrades[1]) {
            g.fillRect(frameX + 7, frameY + 57, 58, 10);
        }
        if (!upgrades[2]) {
            g.fillRect(frameX + 7, frameY + 67, 58, 10);
        }
    }

    public void trade(int x, int y, Player p1, Player p2, SoundManager sm) {
        Player giver = p1;
        Player taker = p2;
        if (x > 100) {
            x -= 104;
            giver = p2;
            taker = p1;
        }
        int index = 6;
        if (x >= 20 && x < 46) {
            if (y >= 16 && y < 42) {
                index = 0;
            } else if (y >= 47 && y < 73) {
                index = 2;
            } else if (y >= 78 && y < 104) {
                index = 4;
            }
        } else if (x >= 50 && x < 76) {
            if (y >= 16 && y < 42) {
                index = 1;
            } else if (y >= 47 && y < 73) {
                index = 3;
            } else if (y >= 78 && y < 104) {
                index = 5;
            }
        }
        if (giver.inventory.size() > index) {
            Upgrade item = giver.inventory.get(index);
            giver.inventory.remove(item);
            taker.inventory.add(item);
            sm.playSound(sm.click2);
        }

    }

}
