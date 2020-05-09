import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends JPanel {
    private int levelWidth;
    private int levelHeight;

    private String world = "levels/level1.txt";

    Player p1;
    private Camera cam1;
    private LevelLoader l1;
    private ArrayList<Obj> wallList;
    private ArrayList<Obj> stuffList;
    private ArrayList<Bullet> bulletList;

    public Game() {
        wallList = new ArrayList<Obj>();
        stuffList = new ArrayList<Obj>();
        bulletList = new ArrayList<Bullet>();
        p1 = new Player(0, 0, 16, 16, 2);
        cam1 = new Camera();
        Dimension levelDim = new Dimension(0, 0);
        l1 = new LevelLoader();
        try {
            levelDim = l1.loadLevel(world, wallList, stuffList, p1);
        } catch (IOException er) {}
        levelWidth = levelDim.width;
        levelHeight = levelDim.height;
    }

    public void run() {
        int fps = 60;
        int skip = 1000/fps;
        long sleepTime;
        long nextTick = System.currentTimeMillis();

        boolean can_shoot = true;
        int rof = 30;
        int rofCounter = 0;

        while (true) {

            p1.move(wallList, stuffList);
            Iterator itr = bulletList.iterator();
            while (itr.hasNext())
            {
                Bullet i = (Bullet)itr.next();
                i.move(wallList, cam1);
                if (i.to_delete) {
                    itr.remove();
                }
            }
            if (!can_shoot) {
                if (rofCounter >= rof) {
                    can_shoot = true;
                    rofCounter = 0;
                } else {
                    rofCounter++;
                }
            }
            if (p1.to_shoot && can_shoot) {
                can_shoot = false;
                if (p1.lastDirection) {
                    bulletList.add(new Bullet(p1.x + 16, p1.y + 8, 2, 1, "sprites/bullet.png", true));
                } else {
                    bulletList.add(new Bullet(p1.x - 2, p1.y + 8, 2, 1, "sprites/bullet.png", false));
                }
            }
            cam1.reposition(p1, levelWidth * 16, levelHeight * 16);
            repaint();

            nextTick += skip;
            sleepTime = nextTick - System.currentTimeMillis();
            if (sleepTime >= 0) {
                try {
                    Thread.sleep(sleepTime);
                } catch (Exception e) {}
            }
        }
    }

    public void paint(Graphics g) {
        ((Graphics2D)g).scale(4, 4);
        g.translate(cam1.camX, cam1.camY);
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, levelWidth * 16, levelHeight * 16);
        for (Obj i : stuffList) {
            g.drawImage(i.img, i.x, i.y, null);
        }
        g.drawImage(p1.img, p1.x, p1.y, null);
        for (Obj i : bulletList) {
            g.drawImage(i.img, i.x, i.y, null);
        }
        for (Obj i : wallList) {
            g.drawImage(i.img, i.x, i.y, null);
        }
    }
}
