import java.util.ArrayList;

public class Bullet extends Obj {
    private int bulletSpeed = 4;
    protected boolean dir;

    public boolean to_delete;

    public Bullet(int x, int y, int width, int height, String path, boolean dir) {
        super(x, y, width, height, path);
        this.dir = dir;
        to_delete = false;
    }

    public void move(ArrayList<Obj> walls, ArrayList<Obj> stuff, Camera cam, SoundManager sm, ArrayList<Character> brokenWalls, Room currRoom) {
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
            if (rect.intersects(i.rect)) {
                to_delete = true;
                if (i instanceof SwitchTrigger) {
                    ((SwitchTrigger) i).activate();
                    sm.playSound(sm.click);
                }
                return;
            }
        }
        for (Obj i : stuff) {
            if (i instanceof Enemy && rect.intersects(i.rect)) {
                to_delete = true;
                ((Enemy)i).takeDamage(1);
                sm.playSound(sm.splat);
                return;
            } else if (i instanceof Boss && rect.intersects(((Boss) i).eyeBox)) {
                to_delete = true;
                ((Boss)i).takeDamage(1);
                sm.playSound(sm.splat);
                return;
            }
        }
    }

}
