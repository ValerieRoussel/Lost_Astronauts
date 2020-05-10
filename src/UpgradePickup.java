import javax.swing.*;
import java.awt.*;

public class UpgradePickup extends Obj {
    int num;

    public UpgradePickup(int x, int y, int width, int height, String imgPath, int num) {
        super(x, y, width, height, "sprites/upgrades/upSmall" + num + ".png");
        this.num = num;
    }

}
