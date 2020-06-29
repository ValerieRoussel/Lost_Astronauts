import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Iterator;

public class Game extends JPanel implements MouseListener {
    private int levelWidth;
    private int levelHeight;
    boolean gameRunning = true;

    Player p1;
    Player p2;
    Player currPlayer;
    boolean inMenu;
    boolean pauseMenu;
    private Camera cam1;
    private TradeMenu tm;
    private UI ui;
    private SoundManager sm;
    private LevelLoader l1;
    private ArrayList<Obj> wallList;
    private ArrayList<Obj> stuffList;
    private ArrayList<Character> brokenWalls;
    private ArrayList<Bullet> bulletList;

    boolean[] upgradesCollected;
    boolean blueSwitch;
    boolean yellowSwitch;

    public Game(UI ui) {
        this.ui = ui;
        startGame();
        this.addMouseListener(this);
    }

    public void startGame() {
        brokenWalls = new ArrayList<Character>();
        bulletList = new ArrayList<Bullet>();
        p1 = new Player(0, 0, 16, 16, 1);
        p2 = new Player(0, 0, 16, 16, 2);
        cam1 = new Camera();
        tm = new TradeMenu();
        sm = new SoundManager();
        inMenu = false;
        pauseMenu = false;
        currPlayer = p1;
        l1 = new LevelLoader();

        upgradesCollected = new boolean[] {false, false, false, false, false, false};
        enterRoom(p1, 'A');
        enterRoom(p2, 'A');
        blueSwitch = false;
        yellowSwitch = false;
        ui.updateUI(currPlayer, p1, p2);
    }

    public void run() {
        int fps = 60;
        int skip = 1000/fps;
        long sleepTime;
        long nextTick = System.currentTimeMillis();

        boolean can_shoot = true;
        int rof = 30;
        int rofCounter = 0;

        while (gameRunning) {

            if (!inMenu && !pauseMenu) {
                if (currPlayer.oob) {
                    if (currPlayer.nextRoomCode != null) {
                        enterRoom(currPlayer, currPlayer.nextRoomCode);
                    } else {
                        currPlayer.setPosition(currPlayer.respawnX, currPlayer.respawnY);
                    }
                }
                if (blueSwitch != SwitchTrigger.blueSwitch) {
                    blueSwitch = SwitchTrigger.blueSwitch;
                    updateSwitchWalls();
                }
                if (yellowSwitch != SwitchTrigger.yellowSwitch) {
                    yellowSwitch = SwitchTrigger.yellowSwitch;
                    updateSwitchWalls();
                }
                currPlayer.move(wallList, stuffList, sm, upgradesCollected);

                Iterator itr = stuffList.iterator();
                while (itr.hasNext()) {
                    Obj i = (Obj) itr.next();
                    if (i instanceof Enemy) {
                        ((Enemy)i).move(wallList);
                        if (((Enemy)i).to_delete) {
                            itr.remove();
                        }
                    }
                }

                itr = bulletList.iterator();
                while (itr.hasNext()) {
                    Bullet i = (Bullet) itr.next();
                    i.move(wallList, stuffList, cam1, sm, brokenWalls, currPlayer.currRoom);
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
                if (currPlayer.to_shoot && can_shoot) {
                    can_shoot = false;
                    int yLoc = 8;
                    if (currPlayer.crouching) {
                        yLoc++;
                    }
                    if (!currPlayer.slidingR && (currPlayer.slidingL || currPlayer.lastDirection)) {
                        if (currPlayer.crouching && currPlayer.grenade) {
                            bulletList.add(new Grenade(currPlayer.x + 16, currPlayer.y + yLoc-1, 4, 3, "sprites/grenade.png", true));
                        } else {
                            bulletList.add(new Bullet(currPlayer.x + 16, currPlayer.y + yLoc, 2, 1, "sprites/bullet.png", true));
                        }
                    } else {
                        if (currPlayer.crouching && currPlayer.grenade) {
                            bulletList.add(new Grenade(currPlayer.x - 4, currPlayer.y + yLoc-1, 4, 3, "sprites/grenadeL.png", false));
                        } else {
                            bulletList.add(new Bullet(currPlayer.x - 2, currPlayer.y + yLoc, 2, 1, "sprites/bullet.png", false));
                        }
                    }
                    sm.playSound(sm.shoot);
                }
                cam1.reposition(currPlayer, levelWidth * 16, levelHeight * 16);
            }

            repaint();
            ui.updateUI(currPlayer, p1, p2);
            ui.repaint();

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
        g.setColor(currPlayer.currRoom.backDrop);
        g.fillRect(0, 0, levelWidth * 16, levelHeight * 16);
        for (Obj i : bulletList) {
            g.drawImage(i.img, i.x, i.y, null);
        }
        for (Obj i : wallList) {
            g.drawImage(i.img, i.x, i.y, null);
        }
        for (Obj i : stuffList) {
            if (i instanceof Enemy && ((Enemy)i).damageFrames > 0) {

            }
            g.drawImage(i.img, i.x, i.y, null);
        }
        g.drawImage(currPlayer.img, currPlayer.x, currPlayer.y, null);
        if (inMenu) {
            tm.drawMenu(g, cam1, p1, p2);
        } else if (pauseMenu) {
            tm.drawPauseMenu(g, cam1, upgradesCollected);
        }
    }

    public void switchRoom(Room newRoom){
        currPlayer.setPosition(newRoom.pStartPos.x, newRoom.pStartPos.y);
        wallList = newRoom.wallList;
        stuffList = newRoom.stuffList;
        bulletList.clear();
        updateSwitchWalls();
        currPlayer.connected = currPlayer.currRoom.connected;
        levelWidth = newRoom.levelDim.width;
        levelHeight = newRoom.levelDim.height;
    }

    public void enterRoom(Player p, char newRoomCode) {
        char lastCode = '?';
        if (p.currRoom != null) {
            lastCode = p.currRoom.code;
        }

        Iterator itr = p.roomList.iterator();
        while (itr.hasNext()) {
            Room i = (Room) itr.next();
            if (i.code != lastCode && i.code != newRoomCode) {
                itr.remove();
            } else if (i.code == newRoomCode) {
                p.currRoom = i;
            }
        }

        if (p.currRoom == null) {
            if (p == p1) {
                p.roomList.add(p.currRoom = new Room(false, newRoomCode, l1, brokenWalls));
            } else {
                p.roomList.add(p.currRoom = new Room(true, newRoomCode, l1, brokenWalls));
            }
        }

        for (char i : p.currRoom.neighbors) {
            if (i != lastCode)
                if (p == p1) {
                    p.roomList.add(removeDuplicates(new Room(false, i, l1, brokenWalls)));
                } else {
                    p.roomList.add(removeDuplicates(new Room(true, i, l1, brokenWalls)));
                }
        }

        for (Obj i : p.currRoom.stuffList) {
            if (i instanceof Door && ((Door) i).nextRoomCode == lastCode) {
                if (((Door) i).vertical) {
                    p.currRoom.pStartPos.x = i.x;
                    p.currRoom.pStartPos.y = i.y + 18;
                } else {
                    if (i.y == 0) {
                        p.currRoom.pStartPos.x = i.x + 8;
                        p.currRoom.pStartPos.y = i.y;
                    } else {
                        if (p.to_left) {
                            p.currRoom.pStartPos.x = i.x -16;
                        } else {
                            p.currRoom.pStartPos.x = i.x + 32;
                        }
                        p.currRoom.pStartPos.y = i.y - 16;
                    }
                }
                break;
            }
        }
        p.setRespawnPosition(p.currRoom.pStartPos.x, p.currRoom.pStartPos.y);
        p.connected = p.currRoom.connected;

        p.nextRoomCode = null;
        if (currPlayer == p) {
            switchRoom(currPlayer.currRoom);
        }
    }

    public Room removeDuplicates(Room newRoom) {
        Iterator itr = newRoom.stuffList.iterator();
        while (itr.hasNext()) {
            Obj i = (Obj) itr.next();
            if (i instanceof UpgradePickup && upgradesCollected[((UpgradePickup) i).num] == true) {
                itr.remove();
            }
        }
        return newRoom;
    }

    public void updateSwitchWalls() {
        for (Obj i : wallList) {
            if (i instanceof SwitchWall) {
                if (((SwitchWall) i).color == "blue" && blueSwitch != ((SwitchWall) i).on) {
                    ((SwitchWall) i).update();
                } else if (((SwitchWall) i).color == "red" && blueSwitch == ((SwitchWall) i).on) {
                    ((SwitchWall) i).update();
                } else if (((SwitchWall) i).color == "yellow" && yellowSwitch != ((SwitchWall) i).on) {
                    ((SwitchWall) i).update();
                } else if (((SwitchWall) i).color == "pink" && yellowSwitch == ((SwitchWall) i).on) {
                    ((SwitchWall) i).update();
                }
            }
        }
    }

    public void switchPlayer() {
        if (!inMenu && !pauseMenu) {
            currPlayer.switchOff();
            if (currPlayer == p1) {
                p1.currRoom.pStartPos.x = p1.x;
                p1.currRoom.pStartPos.y = p1.y;
                currPlayer = p2;
            } else {
                p2.currRoom.pStartPos.x = p2.x;
                p2.currRoom.pStartPos.y = p2.y;
                currPlayer = p1;
            }
            switchRoom(currPlayer.currRoom);
            sm.playSound(sm.swoosh);
        }
    }

    public void switchMenu() {
        if (!pauseMenu) {
            if (inMenu) {
                p1.resetUpgrades();
                p2.resetUpgrades();
                inMenu = false;
            } else if (p1.connected && p2.connected) {
                inMenu = true;
            }
        }
    }

    public void switchPause() {
        if (!inMenu) {
            pauseMenu = !pauseMenu;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        if (inMenu) {
            int mouseX = (int)Math.floor(e.getX() / 4);
            int mouseY = (int)Math.floor(e.getY() / 4);
            tm.trade(mouseX, mouseY, p1, p2, sm);
        } else if (pauseMenu) {
            int mouseX = (int)Math.floor(e.getX() / 4);
            int mouseY = (int)Math.floor(e.getY() / 4);
            if (mouseY > 12 + 82 && mouseY < 12 + 92) {
                if (mouseX > 64 + 6 && mouseX < 64 + 34) {
                    //restart
                    startGame();
                } else if (mouseX > 64 + 37 && mouseX < 64 + 65) {
                    //quit
                    gameRunning = false;
                }
            }

        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
