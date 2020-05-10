import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelLoader {

    public Dimension loadLevel(String levelPath, ArrayList<Obj> walls, ArrayList<Obj> stuff, ArrayList<Character> neighbors, Coord pLoc) throws IOException {
        //0 = nothing
        //w = walls
        //p = player

        //q = b/r switch
        //r = blue wall
        //s = red wall

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
                if (inc.charAt(i) == 'w') {
                    if (prev != null && prev.charAt(i) != 'w' && prev.charAt(i) != 'q') {
                        Obj newWall = new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_floor.png");
                        newWall.rect = new Rectangle(newWall.rect.x, newWall.rect.y + 2, newWall.rect.width, newWall.rect.height - 2);
                        walls.add(newWall);
                    } else {
                        walls.add(new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_wall.png"));
                    }
                } else if (inc.charAt(i) == 'q') {
                    walls.add(new SwitchTrigger(i * 16, j * 16, 16, 16, "sprites/stuff/blue_trigger.png", "blue"));
                } else if (inc.charAt(i) == 'r') {
                    walls.add(new SwitchWall(i * 16, j * 16, 16, 16, "", "blue"));
                } else if (inc.charAt(i) == 's') {
                    walls.add(new SwitchWall(i * 16, j * 16, 16, 16, "", "red"));
                } else if (inc.charAt(i) == 'p') {
                    pLoc.x = i * 16;
                    pLoc.y = (j * 16) + 2;
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
