import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelLoader {

    public Dimension loadLevel(String levelPath, ArrayList<Obj> walls, ArrayList<Obj> stuff, Player p1, Player p2) throws IOException {
        //N = out of bounds
        //0 = nothing
        //C = outer walls
        //W = inner walls
        //S = spikes
        //P = player
        //T = checkpoint
        //U = upgrade
        //F = goal

        File file = new File(levelPath);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String inc;
        String prev = null;
        String next;
        int j = 0;

        inc = reader.readLine();
        next = reader.readLine();
        int levelWidth = inc.length();
        while (inc != null) {

            for (int i = 0; i < levelWidth && i < inc.length(); i++) {
                if (inc.charAt(i) == 'W' || inc.charAt(i) == 'C') {
                    if (prev != null && prev.charAt(i) != 'W' && prev.charAt(i) != 'C') {
                        Obj newWall = new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_floor.png");
                        newWall.rect = new Rectangle(newWall.rect.x, newWall.rect.y + 2, newWall.rect.width, newWall.rect.height - 2);
                        walls.add(newWall);
                    } else {
                        walls.add(new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_wall.png"));
                    }
                } else if (inc.charAt(i) == '1') {
                    p1.setPosition(i * 16, j * 16);
                } else if (inc.charAt(i) == '2') {
                    p2.setPosition(i * 16, j * 16);
                }
            }

            j++;
            prev = inc;
            inc = next;
            next = reader.readLine();
        }
        int levelHeight = j;
        return new Dimension(levelWidth, levelHeight);
    }

    private String getWallImage(char top, char[] mid, char bot) {
        if (mid[1] == 'C') {
            if (bot == 'N') {
                if (top == 'C' && mid[0] == 'C') {
                    return "sprites/outerWalls/obr.png";
                } else if (top == 'C' && mid[2] == 'C') {
                    return "sprites/outerWalls/obl.png";
                } else if (top == 'W') {
                    return "sprites/outerWalls/nFloor.png";
                } else {
                    return "sprites/outerWalls/floor.png";
                }
            } else if (top == 'N') {
                if (bot == 'C' && mid[0] == 'C') {
                    return "sprites/outerWalls/otr.png";
                } else if (bot == 'C' && mid[2] == 'C') {
                    return "sprites/outerWalls/otl.png";
                } else if (bot == 'W') {
                    return "sprites/outerWalls/nCeiling.png";
                } else {
                    return "sprites/outerWalls/ceiling.png";
                }
            } else if (mid[0] == 'N') {
                if (mid[2] == 'W') {
                    return "sprites/outerWalls/nLeft.png";
                } else {
                    return "sprites/outerWalls/lWall.png";
                }
            } else if (mid[2] == 'N') {
                if (mid[0] == 'W') {
                    return "sprites/outerWalls/nRight.png";
                } else {
                    return "sprites/outerWalls/rWall.png";
                }
            } else {
                return "sprites/innerWalls/topinner.png";
            }
        } else if (mid[1] == 'W') {
            if (top == 'W' || top == 'C') {
                if (mid[0]== 'W' || mid[0] == 'C') {
                    if (mid[2]== 'W' || mid[2] == 'C') {
                        if (bot == 'W' || bot == 'C') {
                            return "sprites/innerWalls/topInner.png";
                        } else {
                            return "sprites/innerWalls/bottom.png";
                        }
                    } else if (bot == 'W' || bot == 'C') {
                        return "sprites/innerWalls/right.png";
                    } else {
                        return "sprites/innerWalls/brCorner.png";
                    }
                } else if (mid[2]== 'W' || mid[2] == 'C') {
                    if (bot == 'W' || bot == 'C') {
                        return "sprites/innerWalls/left.png";
                    } else {
                        return "sprites/innerWalls/blCorner.png";
                    }
                } else {
                    if (bot == 'W' || bot == 'C') {
                        return "sprites/innerWalls/Vert.png";
                    } else {
                        return "sprites/innerWalls/sDown.png";
                    }
                }
            } else if (mid[0]== 'W' || mid[0] == 'C') {
                if (mid[2]== 'W' || mid[2] == 'C') {
                    if (bot == 'W' || bot == 'C') {
                        return "sprites/innerWalls/top.png";
                    } else {
                        return "sprites/innerWalls/Hori.png";
                    }
                } else if (bot == 'W' || bot == 'C') {
                    return "sprites/innerWalls/trCorner.png";
                } else {
                    return "sprites/innerWalls/sRight.png";
                }
            } else if (mid[2]== 'W' || mid[2] == 'C') {
                if (bot == 'W' || bot == 'C') {
                    return "sprites/innerWalls/tlCorner.png";
                } else {
                    return "sprites/innerWalls/sLeft.png";
                }
            } else {
                if (bot == 'W' || bot == 'C') {
                    return "sprites/innerWalls/sTop.png";
                } else {
                    return "sprites/innerWalls/1.png";
                }
            }
        } else if (mid[1] == 'S') {
            if (bot == '0' && (top == 'C' || top == 'W')) {
                return "sprites/spikes/spikesDown.png";
            } else if (mid[0] == '0' && (mid[2] == 'C' || mid[2] == 'W')) {
                return "sprites/spikes/spikesLeft.png";
            } else if (mid[2] == '0' && (mid[0] == 'C' || mid[0] == 'W')) {
                return "sprites/spikes/spikesRight.png";
            } else {
                return "sprites/spikes/spikesUp.png";
            }
        }
        return "";
    }

}
