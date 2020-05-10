import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

public class Player extends Obj {
    private boolean alive;
    private boolean grounded;
    private boolean slidingL;
    private boolean slidingR;
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
    boolean crouching;
    boolean to_left;
    boolean to_right;
    boolean to_jump;
    boolean to_crouch;
    boolean to_shoot;

    int playerNum;
    int hp;
    boolean connected;
    ArrayList<Upgrade> inventory;

    Room currRoom;
    boolean oob;
    Character nextRoomCode;
    ArrayList<Room> roomList;

    private boolean wallJump;

    private Image idleL;
    private Image idleR;
    private Image[] walkL;
    private Image[] walkR;
    private Image jr;
    private Image jru;
    private Image jrd;
    private Image jl;
    private Image jlu;
    private Image jld;
    private Image crouchL;
    private Image crouchR;

    public Player(int x, int y, int width, int height, int playerNum) {
        super(x, y, width, height, "sprites/p" + playerNum + "/P" + playerNum + "_idleRight.png");
        this.grounded = true;
        this.crouching = false;
        this.alive = true;
        lastDirection = true;
        to_left = false;
        to_right = false;
        to_jump = false;
        to_crouch = false;
        this.speed = 2;
        this.fallSpeed = 3;
        wallJump = false;

        currRoom = null;
        nextRoomCode = null;
        oob = false;
        this.playerNum = playerNum;
        hp = (3 - playerNum) * 50;
        connected = true;
        inventory = new ArrayList<Upgrade>();
        roomList = new ArrayList<Room>();

        idleL = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_idleLeft.png").getImage();
        idleR = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_idleRight.png").getImage();
        walkL = loadAnim(8, "sprites/p" + playerNum + "/walkLeft/P" + playerNum + "_walkLeft");
        walkR = loadAnim(8, "sprites/p" + playerNum + "/walkRight/P" + playerNum + "_walkRight");
        jr = new ImageIcon("sprites/p" + playerNum + "/jump/P" + playerNum + "_jr.png").getImage();
        jru = new ImageIcon("sprites/p" + playerNum + "/jump/P" + playerNum + "_jru.png").getImage();
        jrd = new ImageIcon("sprites/p" + playerNum + "/jump/P" + playerNum + "_jrd.png").getImage();
        jl = new ImageIcon("sprites/p" + playerNum + "/jump/P" + playerNum + "_jl.png").getImage();
        jlu = new ImageIcon("sprites/p" + playerNum + "/jump/P" + playerNum + "_jlu.png").getImage();
        jld = new ImageIcon("sprites/p" + playerNum + "/jump/P" + playerNum + "_jld.png").getImage();
        crouchL = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_crouchLeft.png").getImage();
        crouchR = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_crouchRight.png").getImage();

        resetUpgrades();
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

    public void move(ArrayList<Obj> wallList, ArrayList<Obj> stuffList, SoundManager sm) {
        stuffCollide(stuffList);
        if (alive) {
            frameNum++;
            calcXChange();
            x += xChange;
            slidingL = false;
            slidingR = false;
            crouching = false;
            xCollide(wallList);

            if (to_jump && grounded) {
                sm.playSound(sm.jump);
                dy = -4;
            } else if (to_crouch && grounded) {
                crouching = true;
                if (!lastDirection) {
                    img = crouchL;
                } else {
                    img = crouchR;
                }
            } else if (to_jump && slidingL) {
                sm.playSound(sm.jump);
                slidingL = false;
                dy = -4;
                dx = speed;
            } else if (to_jump && slidingR) {
                sm.playSound(sm.jump);
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
                img = getVerticalSprite();
            }
            y += dy;
            grounded = false;
            yCollide(wallList);

            if (frameNum == 60) {
                frameNum = 0;
            }
            checkOOB();
        } else {
            if (frameNum >= 30) {
                x = respawnX;
                y = respawnY;
                alive = true;
            }
            frameNum++;
        }
    }

    private void checkOOB() {
        oob = (x < -16 || y < -16 || x > currRoom.levelDim.width * 16 || y > currRoom.levelDim.height * 16);
    }

    private Image getVerticalSprite() {
        /*if (slidingL) {
            return sL;
        } else if (slidingR) {
            return sR;
        }*/
        if (dy < 0) {
            if (!lastDirection) {
                return jlu;
            } else {
                return jru;
            }
        } else if (dy > 0) {
            if (!lastDirection) {
                return jld;
            } else {
                return jrd;
            }
        } else {
            if (!lastDirection) {
                return jl;
            } else {
                return jr;
            }
        }
    }

    public void resetUpgrades() {
        wallJump = false;
        for (int i = 0; i < inventory.size(); i++) {
            int num = inventory.get(i).upgradeNum;
            if (num == 0) {
                wallJump = true;
            }
        }
    }

    public void switchOff() {
        to_left = false;
        to_right = false;
        to_crouch = false;
        to_shoot = false;
        to_jump = false;
    }

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
            for (Obj i : stuffList) {
                if (rect.intersects(i.rect)) {
                    if (i instanceof Door) {
                        nextRoomCode = ((Door) i).nextRoomCode;
                    }
                }
            }
        }
    }

   private void xCollide(ArrayList<Obj> wallList) {
        updateRect();
        for (Obj i : wallList) {
            if (i instanceof SwitchWall && !(((SwitchWall) i).on)) {
                continue;
            }
            if (rect.intersects(i.rect)) {
                if (xChange > 0) {
                    dx = 0;
                    x = i.rect.x - width;
                }
                if (xChange < 0) {
                    dx = 0;
                    x = i.rect.x + i.width;
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
            if (i instanceof SwitchWall && !(((SwitchWall) i).on)) {
                continue;
            }
            if (rect.intersects(i.rect)) {
                if (dy > 0) {
                    dy = 0;
                    y = i.rect.y - height;
                }
                if (dy < 0) {
                    dy = 0;
                    y = i.rect.y + i.rect.height;
                }
            }
            if (!grounded && (new Rectangle(x, y + 1, width, height)).intersects(i.rect)) {
                grounded = true;
                jumpFrame = 0;
            }
        }
    }

   private void calcXChange() {
       if (to_left && !to_right && !crouching) {
           if (dx > speed * (-1) && frameNum % 3 == 0) {
               dx -= 0.25;
               lastDirection = false;
           }
       } else if (!to_left && to_right && !crouching) {
           if (dx < speed && frameNum % 3 == 0) {
               dx += 0.25;
               lastDirection = true;
           }
       } else {
           if (dx > 0 && frameNum % 2 == 0) {
               dx -= 0.25;
           } else if (dx < 0 && frameNum % 2 == 0) {
               dx += 0.25;
           }
           if (lastDirection && to_left && !to_right && crouching) {
               lastDirection = false;
           } else if (!lastDirection && to_right && !to_left && crouching) {
               lastDirection = true;
           }
       }

       if (to_left && !to_right) {
           if (grounded) {
               if (animFrame == 23) {
                   animFrame = 0;
               } else {
                   animFrame++;
               }
               img = walkL[animFrame / 3];
           }
           xChange = (int)Math.ceil(dx);
           if (frameNum % 4 < ((dx - Math.ceil(dx)) * -4)) {
               xChange--;
           }
       } else if (!to_left && to_right) {
           if (grounded) {
               if (animFrame == 23) {
                   animFrame = 0;
               } else {
                   animFrame++;
               }
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
           xChange = (int)Math.round(dx);
           animFrame = 0;
       }
   }

}
