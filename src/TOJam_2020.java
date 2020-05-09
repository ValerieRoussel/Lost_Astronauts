import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;

public class TOJam_2020 {

    //Camera shows 200X150 pixels in 800X600 resolution
    //Character and wall tiles are 16X16 pixels

    public static void main(String []args) {

        JSplitPane splitPaneV = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPaneV.setDividerSize(0);

        UI ui = new UI();
        splitPaneV.setLeftComponent(ui);
        Dimension d0 = new Dimension(800, 72);
        ui.setPreferredSize(d0);
        ui.setMinimumSize(d0);
        ui.setMaximumSize(d0);
        ui.setVisible(true);

        Game g = new Game(ui);
        splitPaneV.setRightComponent(g);
        Dimension d1 = new Dimension(800, 528);
        g.setPreferredSize(d1);
        g.setMinimumSize(d1);
        g.setMaximumSize(d1);
        g.setVisible(true);

        Listener gw = new Listener(g);
        Dimension d2 = new Dimension(818, 601);
        gw.setPreferredSize(d2);
        gw.setMinimumSize(d2);
        gw.setMaximumSize(d2);

        gw.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gw.setResizable(false);
        gw.setVisible(true);
        gw.add(splitPaneV);

        g.run();
        gw.dispatchEvent(new WindowEvent(gw, WindowEvent.WINDOW_CLOSING));
    }

}
