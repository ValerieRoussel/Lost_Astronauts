import javax.swing.*;
import java.awt.*;

public class SwitchWall extends Obj {
    boolean on;
    String color;

    private Image onImage;
    private Image offImage;

    public SwitchWall(int x, int y, int width, int height, String imgPath, String color) {
        super(x, y, width, height, imgPath);

        onImage = new ImageIcon("sprites/stuff/" + color + "_solid.png").getImage();
        offImage = new ImageIcon("sprites/stuff/" + color + "_off.png").getImage();

        this.color = color;
        this.on = true;
        this.img = onImage;
    }

    public void update() {
        if (on) {
            img = offImage;
        } else {
            img = onImage;
        }
        on = !on;
    }

}
