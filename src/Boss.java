import javax.sound.sampled.SourceDataLine;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Boss extends Obj {

    public int startHp;
    public int hp;
    private String path = "sprites/enemies/boss/";

    private int dx;
    private int dy;
    private boolean grounded;
    private boolean jumpDir = false;
    private boolean bigLoc = true;
    private int loc = 0;
    private int frameNum;
    private int jumpFrame = 0;
    private boolean bigJumping = false;
    private boolean repeat = true;
    private boolean charging = false;
    private boolean crouching = false;
    public Rectangle eyeBox;
    private int damageFrames;
    public boolean dead;

    private Image currBody;
    private Image currEyelid;
    private Image currEye;

    private Image body;
    private Image crouch;

    private Image eyelid;
    private Image[] wink;

    private Image[] eyeL;
    private Image[] eyeR;

    public Boss(int x, int y) {
        super(x, y, 48, 48, "");
        startHp = 27;
        hp = startHp;

        grounded = true;
        dead = false;
        frameNum = 40;
        damageFrames = 0;

        body = new ImageIcon(path + "body/neutral.png").getImage();
        crouch = new ImageIcon(path + "body/crouch.png").getImage();

        eyelid = new ImageIcon(path + "eyelid/main.png").getImage();
        wink = loadAnim(6, path + "eyelid/wink");

        eyeL = loadAnim(3, path + "eye/left");
        eyeR = loadAnim(3, path + "eye/right");

        currBody = body;
        currEyelid = eyelid;
        currEye = eyeL[0];
    }

    public void move(ArrayList<Obj> wallList, Player p, SoundManager sm) {
        updateEye(p);
        if (grounded) {
            frameNum--;
            if (frameNum <= 0) {
                if ((!bigLoc && loc == 1) || (bigLoc && loc == -1)) {
                    if (repeat) {
                        smallJump(sm);
                    } else {
                        bigJump(sm);
                    }
                } else {
                    smallJump(sm);
                }
            }
        } else {
            jumpFrame++;
            if (bigJumping) {
                if (jumpFrame % 6 == 1) {
                    dy++;
                }
            } else {
                if (jumpFrame % 4 == 1) {
                    dy++;
                }
            }
            yCollide(wallList, sm);
        }
        x += dx;
        y += dy;
        currEyelid = eyelid;
        if (damageFrames > 0) {
            currEyelid = wink[(18-damageFrames)/3];
            damageFrames--;
        }
    }

    private void yCollide(ArrayList<Obj> wallList, SoundManager sm) {
        updateRect();
        for (Obj i : wallList) {
            if ((new Rectangle(x, y + 1, width, height)).intersects(i.rect)) {
                if (dy > 0) {
                    grounded = true;
                    dx = 0;
                    dy = 0;
                    y = i.rect.y - height;
                    jumpFrame = 0;
                    frameNum = 30;
                    bigJumping = false;
                    sm.playSound(sm.land);
                }
            }
        }
    }

    private void bigJump(SoundManager sm){
        if (!charging) {
            frameNum = 40;
        } else {
            grounded = false;
            bigJumping = true;
            sm.playSound(sm.deepjump);
            if (bigLoc) {
                dx = -2;
            } else {
                dx = 2;
            }
            dy -= 5;
            bigLoc = !bigLoc;
            repeat = true;
        }
        charging = !charging;
    }

    private void smallJump(SoundManager sm){
        if (!crouching) {
            frameNum = 20;
        } else {
            if (repeat && (!bigLoc && loc == 1) || (bigLoc && loc == -1)) {
                repeat = false;
            }
            grounded = false;
            sm.playSound(sm.deepjump);
            if (jumpDir) {
                dx = -1;
                loc--;
            } else {
                dx = 1;
                loc++;
            }
            if (loc != 0) {
                jumpDir = !jumpDir;
            }
            dy = -2;
        }
        crouching = !crouching;
    }

    public void updateEye(Player p) {
        if (!dead) {
            int eyeDamage = (startHp - hp)/9;
            if (p.x < x+(width/2)) {
                currEye = eyeL[eyeDamage];
            } else {
                currEye = eyeR[eyeDamage];
            }
        }
    }

    public void takeDamage(int damage) {
        if (damageFrames == 0) {
            hp -= damage;
            if (hp <= 0) {
                dead = true;
            } else {
                damageFrames = 18;
            }
        }
    }

    public void draw(Graphics g) {
        int yOffset = 0;
        if (crouching || charging) {
            yOffset = 4;
            currBody = crouch;
        } else {
            currBody = body;
        }
        eyeBox = new Rectangle(x+17, y+yOffset, 14, 16);
        g.drawImage(currBody, x, y, null);
        g.drawImage(currEye, x+13, y+1 + yOffset, null);
        g.drawImage(currEyelid, x+13, y+1 + yOffset, null);
    }

}
