import javax.swing.*;
import java.awt.*;

public class Obj {
    int x;
    int y;
    int width;
    int height;
    Rectangle rect;
    Image img;

    public Obj(int x, int y, int width, int height, String imgPath) {

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.img = new ImageIcon(imgPath).getImage();
        rect = new Rectangle(x, y, width, height);
    }

    public void updateRect() {
        rect = new Rectangle(x, y, width, height);
    }

    public Image[] loadAnim(int frames, String path) {
        Image[] toReturn = new Image[frames];
        for (int i = 1; i <= frames; i++) {
            toReturn[i-1] = new ImageIcon(path + i + ".png").getImage();
        }
        return toReturn;
    }

}
