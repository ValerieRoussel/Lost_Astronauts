import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;

public class Room {
    boolean world;
    //false = space station (Player 1)
    //true = planet (Player 2)

    boolean connected;
    Color backDrop;

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
            loadRoom("levels/world1/level" + code + ".txt", ll);
        } else {
            loadRoom("levels/world2/level" + code + ".txt", ll);
        }

        connected = true;
        backDrop = Color.decode("#d1a259");

    }

    public void loadRoom(String path, LevelLoader ll) {
        try {
            levelDim = ll.loadLevel(path, wallList, stuffList, neighbors, pStartPos);
        } catch (IOException er) {}
    }


}
