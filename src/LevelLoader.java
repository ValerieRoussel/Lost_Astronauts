import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelLoader {

    public Dimension loadLevel(String levelPath, ArrayList<Obj> walls, ArrayList<Obj> stuff, ArrayList<Character> neighbors, Coord pLoc) throws IOException {
        //0 = nothing
        //C = outer walls
        //W = inner walls
        //1 = player 1
        //2 = player 2
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
                    if (prev != null && prev.charAt(i) != 'w') {
                        Obj newWall = new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_floor.png");
                        newWall.rect = new Rectangle(newWall.rect.x, newWall.rect.y + 2, newWall.rect.width, newWall.rect.height - 2);
                        walls.add(newWall);
                    } else {
                        walls.add(new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_wall.png"));
                    }
                } else if (inc.charAt(i) == 'p') {
                    pLoc.x = i * 16;
                    pLoc.y = (j * 16) + 2;
                } else if (Character.isUpperCase(inc.charAt(i))) {
                    neighbors.add(inc.charAt(i));
                    stuff.add(new Door(i * 16, j * 16, 16, 32, "", (prev != null && next != null), inc.charAt(i)));
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
