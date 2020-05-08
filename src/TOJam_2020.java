import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class TOJam_2020 {

    //Camera shows 200X150 pixels in 800X600 resolution
    //Character and wall tiles are 16X16 pixels

    public static void main(String []args) {

        Game g = new Game();
        Dimension d1 = new Dimension(200, 150);
        g.setPreferredSize(d1);
        g.setMinimumSize(d1);
        g.setMaximumSize(d1);
        g.setVisible(true);

        Listener gw = new Listener(g);

        Dimension d2 = new Dimension(800, 599);
        gw.setPreferredSize(d2);
        gw.setMinimumSize(d2);
        gw.setMaximumSize(d2);

        gw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gw.setResizable(false);
        gw.setVisible(true);
        gw.add(g);

        g.run();
        gw.dispatchEvent(new WindowEvent(gw, WindowEvent.WINDOW_CLOSING));
    }

}
