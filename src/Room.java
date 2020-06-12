import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Room {
    boolean world;
    //false = space station (Player 1)
    //true = planet (Player 2)

    boolean connected;
    Color backDrop;
    int tileSet;
    //0 = planet surface
    //1 = space station
    //2 = planet caves

    char code;
    Dimension levelDim;
    Coord pStartPos;
    ArrayList<Obj> wallList;
    ArrayList<Obj> stuffList;
    ArrayList<Character> neighbors;

    public Room(boolean world, char code, LevelLoader ll) {
        this.world = world;
        this.code = code;
        pStartPos = new Coord(0, 0);
        wallList = new ArrayList<Obj>();
        stuffList = new ArrayList<Obj>();
        neighbors = new ArrayList<Character>();

        if (!world) {
            backDrop = Color.decode("#8b94a7");
            tileSet = 1;
            if (code == 'C') {
                connected = true;
            } else {
                connected = false;
            }
        } else {
            if (code == 'D') {
                tileSet = 0;
                backDrop = Color.decode("#c0e5be");
                connected = true;
            } else {
                tileSet = 2;
                backDrop = Color.decode("#352317");
                connected = false;
            }
        }

        if (!world) {
            loadRoom("levels/world1/level" + code + ".txt", ll);
        } else {
            loadRoom("levels/world2/level" + code + ".txt", ll);
        }

    }

    public void loadRoom(String path, LevelLoader ll) {
        try {
            levelDim = ll.loadLevel(path, wallList, stuffList, neighbors, pStartPos, tileSet);
        } catch (IOException er) {}
    }


}
