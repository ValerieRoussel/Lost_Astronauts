import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Game extends JPanel {
    private int levelWidth;
    private int levelHeight;

    private String world = "levels/level1.txt";

    Player p1;
    private Camera cam1;
    private LevelLoader l1;
    private ArrayList<Obj> wallList;
    private ArrayList<Obj> stuffList;

    public Game() {
        wallList = new ArrayList<Obj>();
        stuffList = new ArrayList<Obj>();
        p1 = new Player(0, 0, 16, 16, 1);
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

        while (true) {

            p1.move(wallList, stuffList);
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
        for (Obj i : wallList) {
            g.drawImage(i.img, i.x, i.y, null);
        }
    }
}
