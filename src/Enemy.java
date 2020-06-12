import java.awt.*;
import java.util.ArrayList;

public class Enemy extends Obj {
    boolean curr_direction;
    boolean to_delete;
    int speed;
    int frameNum = 0;
    int hp;
    public int damageFrames = 0;

    private Image[] walkL;
    private Image[] walkR;

    public Enemy(int x, int y, int width, int height, String imgPath) {
        super(x, y, width, height, "");
        curr_direction = true;
        speed = 1;
        hp = 3;
        to_delete = false;

        walkL = loadAnim(4, "sprites/enemies/" + imgPath + "/walkL/" + imgPath + "_alienL");
        walkR = loadAnim(4, "sprites/enemies/" + imgPath + "/walkR/" + imgPath + "_alienR");
    }

    public void takeDamage(int damage) {
        if (damageFrames == 0) {
            hp -= damage;
            if (hp <= 0) {
                to_delete = true;
            } else {
                damageFrames = 8;
            }
        }
    }

    public void move(ArrayList<Obj> wallList) {
        if (damageFrames > 0) {
            damageFrames--;
        }
        if (frameNum == 15) {
            frameNum = 0;
        } else {
            frameNum++;
        }
        if (curr_direction) {
            this.img = walkR[frameNum / 4];
            x += frameNum % 2;
        } else {
            this.img = walkL[frameNum / 4];
            x -= frameNum % 2;
        }
        if (damageFrames > 0) {
            this.img = null;
        }
        xCollide(wallList);
    }

    private void xCollide(ArrayList<Obj> wallList) {
        updateRect();
        boolean againstWall = false;
        boolean willFall = true;
        for (Obj i : wallList) {
            if (i instanceof SwitchWall && !(((SwitchWall) i).on)) {
                continue;
            }
            if (rect.intersects(i.rect)) {
                againstWall = true;
                if (curr_direction) {
                    x = i.rect.x - width;
                } else {
                    x = i.rect.x + i.rect.width;
                }
            }
            if (!curr_direction && (new Rectangle(x-1, y + 1, 1, height)).intersects(i.rect)) {
                willFall = false;
            } else if (curr_direction && (new Rectangle(x + width-1, y + 1, 1, height)).intersects(i.rect)) {
                willFall = false;
            }
        }
        if (againstWall || willFall) {
            curr_direction = !curr_direction;
        }
    }



}
