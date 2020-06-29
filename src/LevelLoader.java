import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelLoader {

    public Dimension loadLevel(String levelPath, ArrayList<Obj> walls, ArrayList<Obj> stuff, ArrayList<Character> neighbors, Coord pLoc, int tileSet) throws IOException {
        //0 = nothing
        //w = walls
        //p = player

        //q = b/r switch
        //r = blue wall
        //s = red wall
        //t = y/p switch
        //u = yellow wall
        //v = pink wall

        //f = frog enemy
        //ABC = door to other room
        //123 = collectible upgrade

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
                if (inc.charAt(i) == 'w') {
                    if (prev != null && prev.charAt(i) != 'w' && prev.charAt(i) != 'q' && prev.charAt(i) != 't') {
                        if (tileSet == 0) {
                            Obj newWall = new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_floor.png");
                            newWall.rect = new Rectangle(newWall.rect.x, newWall.rect.y + 2, newWall.rect.width, newWall.rect.height - 2);
                            walls.add(newWall);
                        } else if (tileSet == 1) {
                            Obj newWall = new Obj(i * 16, j * 16, 16, 16, "sprites/walls/space_floor.png");
                            newWall.rect = new Rectangle(newWall.rect.x, newWall.rect.y + 2, newWall.rect.width, newWall.rect.height - 2);
                            walls.add(newWall);
                        } else if (tileSet == 2) {
                            Obj newWall = new Obj(i * 16, j * 16, 16, 16, "sprites/walls/cave_floor.png");
                            newWall.rect = new Rectangle(newWall.rect.x, newWall.rect.y + 2, newWall.rect.width, newWall.rect.height - 2);
                            walls.add(newWall);
                        }
                    } else {
                        if (tileSet == 0) {
                            walls.add(new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_wall.png"));
                        } else if (tileSet == 1) {
                            walls.add(new Obj(i * 16, j * 16, 16, 16, "sprites/walls/space_wall.png"));
                        } else if (tileSet == 2) {
                            walls.add(new Obj(i * 16, j * 16, 16, 16, "sprites/walls/cave_wall.png"));
                        }
                    }
                } else if (inc.charAt(i) == 'x') {
                    if (tileSet == 1) {
                        walls.add(new BrokenWall(i * 16, (j * 16) - 16, 16, 32, "sprites/walls/space_weak.png"));
                    } else {
                        walls.add(new BrokenWall(i * 16, (j * 16) - 16, 16, 32, "sprites/walls/cave_weak.png"));
                    }
                } else if (inc.charAt(i) == 'q') {
                    walls.add(new SwitchTrigger(i * 16, j * 16, 16, 16, "sprites/stuff/blue_trigger.png", "blue"));
                } else if (inc.charAt(i) == 'r') {
                    walls.add(new SwitchWall(i * 16, j * 16, 16, 16, "", "blue"));
                } else if (inc.charAt(i) == 's') {
                    walls.add(new SwitchWall(i * 16, j * 16, 16, 16, "", "red"));
                } else if (inc.charAt(i) == 't') {
                    walls.add(new SwitchTrigger(i * 16, j * 16, 16, 16, "sprites/stuff/yellow_trigger.png", "yellow"));
                } else if (inc.charAt(i) == 'u') {
                    walls.add(new SwitchWall(i * 16, j * 16, 16, 16, "", "yellow"));
                } else if (inc.charAt(i) == 'v') {
                    walls.add(new SwitchWall(i * 16, j * 16, 16, 16, "", "pink"));
                } else if (inc.charAt(i) == 'p') {
                    pLoc.x = i * 16;
                    pLoc.y = (j * 16) + 2;
                } else if (inc.charAt(i) == 'f') {
                    stuff.add(new Enemy((i * 16)+1, (j * 16)+4, 14, 14, "frog"));
                } else if (inc.charAt(i) == '@') {
                    stuff.add(new KillPit(i * 16, j * 16));
                } else if (Character.isUpperCase(inc.charAt(i))) {
                    neighbors.add(inc.charAt(i));
                    stuff.add(new Door(i * 16, j * 16, 16, 32, "", (prev != null && next != null), inc.charAt(i)));
                } else if (inc.charAt(i) != '0' && Character.isDigit(inc.charAt(i))) {
                    stuff.add(new UpgradePickup((i * 16) + 3, (j * 16) + 3, 10, 10, "", Character.getNumericValue(inc.charAt(i)) - 1));
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

}
