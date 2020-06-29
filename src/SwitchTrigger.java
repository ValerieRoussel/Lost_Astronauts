import javax.swing.*;
import java.awt.*;

public class SwitchTrigger extends Obj {
    String color;

    static boolean blueSwitch = false;
    static boolean yellowSwitch = false;

    public SwitchTrigger(int x, int y, int width, int height, String imgPath, String color) {
        super(x, y, width, height, imgPath);
        this.color = color;
    }

    public void activate() {
        if (color == "blue") {
            blueSwitch = !blueSwitch;
        } else if (color == "yellow") {
            yellowSwitch = !yellowSwitch;
        }
    }


}
