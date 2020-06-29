import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Grenade extends Bullet{

    private int bulletSpeed = 4;
    public boolean exploded;
    private int countdown = 30;

    private Image explosion = new ImageIcon("sprites/explosion.png").getImage();

    public Grenade(int x, int y, int width, int height, String path, boolean dir) {
        super(x, y, width, height, path, dir);
        exploded = false;
    }

    public void explode() {
        bulletSpeed = 0;
        exploded = true;
        x -= 6;
        y -= 6;
        width = 16;
        height = 16;
        updateRect();
        img = explosion;
    }

    public void move(ArrayList<Obj> walls, ArrayList<Obj> stuff, Camera cam, SoundManager sm, ArrayList<Character> brokenWalls, Room currRoom) {
        if (exploded) {
            countdown--;
            if (countdown <= 0) {
                to_delete = true;
            }
            return;
        }
        if (dir) {
            this.x += bulletSpeed;
        } else {
            this.x -= bulletSpeed;
        }
        updateRect();
        if (this.x < -cam.camX - 10 || this.x > -cam.camX + 210) {
            to_delete = true;
            return;
        }
        for (Obj i : walls) {
            if (rect.intersects(i.rect) && !exploded) {
                if (i instanceof SwitchTrigger) {
                    ((SwitchTrigger) i).activate();
                    sm.playSound(sm.click);
                } else if (i instanceof BrokenWall) {
                    brokenWalls.add(currRoom.code);
                    walls.remove(i);
                }
                explode();
                return;
            }
        }
        for (Obj i : stuff) {
            if (i instanceof Enemy && rect.intersects(i.rect)) {
                ((Enemy)i).takeDamage(3);
                explode();
                return;
            }
        }
    }

}
