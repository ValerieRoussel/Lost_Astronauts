import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Listener extends JFrame implements KeyListener{
    Game g;
    boolean jump_released = true;
    boolean switch_released = true;
    boolean menu_released = true;

    public Listener(Game g) {
        this.g = g;
        this.setFocusTraversalKeysEnabled(false);
        this.addKeyListener(this);
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            g.currPlayer.to_left = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            g.currPlayer.to_right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            g.currPlayer.to_crouch = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            g.currPlayer.to_shoot = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (jump_released) {
                g.currPlayer.to_jump = true;
                jump_released = false;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            if (switch_released) {
                g.switchPlayer();
                switch_released = false;
            }
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            if (menu_released) {
                if (g.inMenu) {
                    g.inMenu = false;
                } else {
                    g.inMenu = true;
                }
                menu_released = false;
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            g.currPlayer.to_left = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            g.currPlayer.to_right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            g.currPlayer.to_crouch = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            g.currPlayer.to_shoot = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump_released = true;
            g.currPlayer.to_jump = false;
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            switch_released = true;
        } else if (e.getKeyCode() == KeyEvent.VK_F) {
            menu_released = true;
        }
    }
}
