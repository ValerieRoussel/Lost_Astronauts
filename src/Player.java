import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Player extends Obj {
    private boolean alive;
    private boolean grounded;
    private boolean slidingL;
    private boolean slidingR;
    private boolean wallJump;
    private double speed;
    private int fallSpeed;
    private double dx;
    private double dy;
    private int frameNum = 0;
    private int jumpFrame = 0;
    private int animFrame = 0;

    private int xChange = 0;
    private int respawnX;
    private int respawnY;

    boolean lastDirection;
    boolean to_left;
    boolean to_right;
    boolean to_jump;
    boolean to_shoot;

    private Image idleL;
    private Image idleR;
    private Image[] walkL;
    private Image[] walkR;

    public Player(int x, int y, int width, int height, int playerNum) {
        super(x, y, width, height, "sprites/p" + playerNum + "/P" + playerNum + "_idleRight.png");
        this.grounded = true;
        this.alive = true;
        lastDirection = true;
        to_left = false;
        to_right = false;
        to_jump = false;
        this.speed = 2;
        this.fallSpeed = 3;
        wallJump = false;

        idleL = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_idleLeft.png").getImage();
        idleR = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_idleRight.png").getImage();
        walkL = loadAnim(8, "sprites/p" + playerNum + "/walkLeft/P" + playerNum + "_walkLeft");
        walkR = loadAnim(8, "sprites/p" + playerNum + "/walkRight/P" + playerNum + "_walkRight");

    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        dx = 0;
        dy = 0;
        setRespawnPosition(x, y);
    }

    public void setRespawnPosition(int x, int y) {
        this.respawnX = x;
        this.respawnY = y;
    }

    public void move(ArrayList<Obj> wallList, ArrayList<Obj> stuffList) {
        stuffCollide(stuffList);
        if (alive) {
            frameNum++;
            calcXChange();
            x += xChange;
            slidingL = false;
            slidingR = false;
            xCollide(wallList);

            if (to_jump && grounded) {
                dy = -4;
            } else if (to_jump && slidingL) {
                slidingL = false;
                dy = -4;
                dx = speed;
            } else if (to_jump && slidingR) {
                slidingR = false;
                dy = -4;
                dx = -speed;
            } else if (!grounded && dy < fallSpeed && jumpFrame % 5 == 1) {
                jumpFrame++;
                dy++;
            } else if (!grounded) {
                jumpFrame++;
            }
            if ((slidingL || slidingR) && dy > fallSpeed / 2) {
                dy = fallSpeed / 2;
            }
            to_jump = false;
            if (!grounded) {
                //img = getVerticalSprite();
            }
            y += dy;
            grounded = false;
            yCollide(wallList);

            if (frameNum == 60) {
                frameNum = 0;
            }
        } else {
            if (frameNum >= 30) {
                x = respawnX;
                y = respawnY;
                alive = true;
            }
            frameNum++;
        }
    }

    /*private Image getVerticalSprite() {
        if (slidingL) {
            return sL;
        } else if (slidingR) {
            return sR;
        }
        if (dy < 0) {
            if (dx < 0 || (to_left && !to_right)) {
                return jumpUpLeft;
            } else {
                return jumpUpRight;
            }
        } else if (dy > 0) {
            if (dx < 0 || (to_left && !to_right)) {
                return jumpDownLeft;
            } else {
                return jumpDownRight;
            }
        } else {
            if (dx < 0 || (to_left && !to_right)) {
                return jumpLeft;
            } else {
                return jumpRight;
            }
        }
    }*/

    private void die() {
        dx = 0;
        dy = 0;
        frameNum = 0;
        //img = dead;
        alive = false;
    }

    private void stuffCollide(ArrayList<Obj> stuffList) {
        if (alive) {
            updateRect();
            Rectangle hurtRect = new Rectangle(x + 4, y + 4, 4, 4);
            for (Obj i : stuffList) {
                if (hurtRect.intersects(i.rect)) {
                    //TODO collide with objects
                }
            }
        }
    }

   private void xCollide(ArrayList<Obj> wallList) {
        updateRect();
        for (Obj i : wallList) {
            if (rect.intersects(i.rect)) {
                if (xChange > 0) {
                    dx = 0;
                    x = i.x - width;
                }
                if (xChange < 0) {
                    dx = 0;
                    x = i.x + i.width;
                }
            }
            if (wallJump && !grounded && (new Rectangle(x-1, y, width, height)).intersects(i.rect)) {
                slidingL = true;
            } else if (wallJump && !grounded && (new Rectangle(x+1, y, width, height)).intersects(i.rect)) {
                slidingR = true;
            }
        }
    }

    private void yCollide(ArrayList<Obj> wallList) {
        updateRect();
        for (Obj i : wallList) {
            if (rect.intersects(i.rect)) {
                if (dy > 0) {
                    dy = 0;
                    y = i.y - height;
                }
                if (dy < 0) {
                    dy = 0;
                    y = i.y + i.height;
                }
            }
            if (!grounded && (new Rectangle(x, y + 1, width, height)).intersects(i.rect)) {
                grounded = true;
                jumpFrame = 0;
            }
        }
    }

   private void calcXChange() {
       if (to_left && !to_right) {
           if (dx > speed * (-1) && frameNum % 3 == 0) {
               dx -= 0.25;
           }
       } else if (!to_left && to_right) {
           if (dx < speed && frameNum % 3 == 0) {
               dx += 0.25;
           }
       } else {
           if (dx > 0 && frameNum % 2 == 0) {
               dx -= 0.25;
           } else if (dx < 0 && frameNum % 2 == 0) {
               dx += 0.25;
           }
       }

       if (dx < 0 || (to_left && !to_right)) {
           if (grounded) {
               if (animFrame == 23) {
                   animFrame = 0;
               } else {
                   animFrame++;
               }
               img = walkL[animFrame / 3];
               lastDirection = false;
           }
           xChange = (int)Math.ceil(dx);
           if (frameNum % 4 < ((dx - Math.ceil(dx)) * -4)) {
               xChange--;
           }
       } else if (dx > 0  || (!to_left && to_right)) {
           if (grounded) {
               if (animFrame == 23) {
                   animFrame = 0;
               } else {
                   animFrame++;
               }
               lastDirection = true;
               img = walkR[animFrame / 3];
           }
           xChange = (int)Math.floor(dx);
           if (frameNum % 4 < ((dx - Math.floor(dx)) * 4)) {
               xChange++;
           }
       } else {
           if (lastDirection) {
               img = idleR;
           } else {
               img = idleL;
           }
           xChange = 0;
           animFrame = 0;
       }
   }

}
