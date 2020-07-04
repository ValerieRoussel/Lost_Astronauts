import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Player extends Obj {
    private boolean alive;
    private boolean grounded;
    boolean slidingL;
    boolean slidingR;
    private double speed;
    private int fallSpeed;
    private double dx;
    private double dy;
    private int frameNum = 0;
    private int jumpFrame = 0;
    private int animFrame = 0;

    private int xChange = 0;
    int respawnX;
    int respawnY;

    boolean lastDirection;
    boolean crouching;
    boolean to_left;
    boolean to_right;
    boolean to_jump;
    boolean to_crouch;
    boolean to_shoot;

    int playerNum;
    int hp;
    int damageFrames = 0;
    boolean connected;
    ArrayList<Upgrade> inventory;

    Room currRoom;
    boolean oob;
    Character nextRoomCode;
    ArrayList<Room> roomList;

    private boolean wallJump;
    private boolean highJump;
    boolean grenade;

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
    private Image sl;
    private Image sr;
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
        highJump = false;
        grenade = false;

        currRoom = null;
        nextRoomCode = null;
        oob = false;
        this.playerNum = playerNum;
        hp = 100;
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
        sl = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_sl.png").getImage();
        sr = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_sr.png").getImage();
        crouchL = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_crouchLeft.png").getImage();
        crouchR = new ImageIcon("sprites/p" + playerNum + "/P" + playerNum + "_crouchRight.png").getImage();

        resetUpgrades();
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        dx = 0;
        dy = 0;
    }

    public void setRespawnPosition(int x, int y) {
        this.respawnX = x;
        this.respawnY = y;
    }

    public void takeDamage(int damage) {
        if (damageFrames == 0) {
            hp -= damage;
            if (hp <= 0) {
                die();
            } else {
                damageFrames = 48;
            }
        }
    }

    public void move(ArrayList<Obj> wallList, ArrayList<Obj> stuffList, SoundManager sm, boolean[] collected) {
        stuffCollide(stuffList, sm, collected);
        frameNum++;
        if (alive) {
            calcXChange(sm);
            x += xChange;
            slidingL = false;
            slidingR = false;
            xCollide(wallList);
            crouching = false;

            if (to_crouch && grounded) {
                crouching = true;
                if (!lastDirection) {
                    img = crouchL;
                } else {
                    img = crouchR;
                }
            }

            if (to_jump && grounded) {
                if (crouching && highJump) {
                    dy = -6;
                } else {
                    dy = -4;
                }
                sm.playSound(sm.jump);
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
            yCollide(wallList, sm);

            if (frameNum == 60) {
                frameNum = 0;
            }
            checkOOB();

            if (damageFrames > 0) {
                damageFrames--;
                if (damageFrames % 16 > 7) {
                    img = null;
                }
            }

        } else {
            if (frameNum >= 30) {
                x = respawnX;
                y = respawnY;
                hp = 100;
                alive = true;
            }
        }
    }

    private void checkOOB() {
        oob = (x < -16 || y < -16 || x > currRoom.levelDim.width * 16 || y > currRoom.levelDim.height * 16);
    }

    private Image getVerticalSprite() {
        if (slidingL) {
            return sl;
        } else if (slidingR) {
            return sr;
        }
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
        highJump = false;
        grenade = false;
        for (int i = 0; i < inventory.size(); i++) {
            int num = inventory.get(i).upgradeNum;
            if (num == 0) {
                wallJump = true;
            } else if (num == 1) {
                highJump = true;
            } else if (num == 2) {
                grenade = true;
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

    private void stuffCollide(ArrayList<Obj> stuffList, SoundManager sm, boolean[] collected) {
        if (alive) {
            updateRect();
            for (Obj i : stuffList) {
                if (rect.intersects(i.rect)) {
                    if (i instanceof Door) {
                        nextRoomCode = ((Door) i).nextRoomCode;
                        return;
                    } else if (i instanceof KillPit) {
                        nextRoomCode = null;
                        return;
                    } else if (i instanceof UpgradePickup) {
                        inventory.add(new Upgrade(((UpgradePickup) i).num));
                        stuffList.remove(i);
                        collected[((UpgradePickup) i).num] = true;
                        resetUpgrades();
                        sm.playSound(sm.collect);
                        return;
                    } else if (i instanceof Enemy || i instanceof Boss) {
                        Rectangle lRect = new Rectangle(x, y, 4, height);
                        Rectangle rRect = new Rectangle(x + width - 5, y, 4, height);
                        if (i.rect.intersects(lRect) && !i.rect.intersects(rRect)) {
                            dx = speed;
                        } else if (i.rect.intersects(rRect) && !i.rect.intersects(lRect)) {
                            dx = -speed;
                        }
                        takeDamage(10);
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

    private void yCollide(ArrayList<Obj> wallList, SoundManager sm) {
        updateRect();
        for (Obj i : wallList) {
            if (i instanceof SwitchWall && !(((SwitchWall) i).on)) {
                continue;
            }
            if (rect.intersects(i.rect)) {
                if (dy > 0) {
                    if (dy == fallSpeed) {
                        sm.playSound(sm.land);
                    }
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

   private void calcXChange(SoundManager sm) {
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
           if (grounded && !crouching) {
               if (animFrame == 23) {
                   animFrame = 0;
               } else {
                   animFrame++;
               }
               img = walkL[animFrame / 3];
               if (animFrame == 0) {
                   sm.playSound(sm.step1);
               } else if (animFrame == 12) {
                   sm.playSound(sm.step2);
               }
           }
           xChange = (int)Math.ceil(dx);
           if (frameNum % 4 < ((dx - Math.ceil(dx)) * -4)) {
               xChange--;
           }
       } else if (!to_left && to_right) {
           if (grounded && !crouching) {
               if (animFrame == 23) {
                   animFrame = 0;
               } else {
                   animFrame++;
               }
               img = walkR[animFrame / 3];
               if (animFrame == 0) {
                    sm.playSound(sm.step1);
               } else if (animFrame == 12) {
                   sm.playSound(sm.step2);
               }
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
