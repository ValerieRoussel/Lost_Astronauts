import javax.swing.*;
import java.awt.*;

public class Upgrade {
    String path = "sprites/upgrades/up";
    Image largeIcon;
    Image smallIcon;

    int upgradeNum;
    //0 - wall jump
    //1 -
    //and so on

    public Upgrade(int num) {
        upgradeNum = num;
        largeIcon = new ImageIcon(path + "Large" + num + ".png").getImage();
        smallIcon = new ImageIcon(path + "Small" + num + ".png").getImage();
    }

}
