import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LevelLoader {

    public Dimension loadLevel(String levelPath, ArrayList<Obj> walls, ArrayList<Obj> stuff, Player p1, Player p2) throws IOException {
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
                if (inc.charAt(i) == 'W' || inc.charAt(i) == 'C') {
                    if (prev != null && prev.charAt(i) != 'W' && prev.charAt(i) != 'C') {
                        Obj newWall = new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_floor.png");
                        newWall.rect = new Rectangle(newWall.rect.x, newWall.rect.y + 2, newWall.rect.width, newWall.rect.height - 2);
                        walls.add(newWall);
                    } else {
                        walls.add(new Obj(i * 16, j * 16, 16, 16, "sprites/walls/temp_wall.png"));
                    }
                } else if (inc.charAt(i) == '1') {
                    p1.setPosition(i * 16, (j * 16) + 2);
                } else if (inc.charAt(i) == '2') {
                    p2.setPosition(i * 16, (j * 16) + 2);
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
