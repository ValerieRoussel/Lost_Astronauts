import javax.swing.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Listener extends JFrame implements KeyListener{
    Game g;
    boolean jump_released = true;

    public Listener(Game g) {
        this.g = g;
        this.setFocusTraversalKeysEnabled(false);
        this.addKeyListener(this);
    }

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            g.p1.to_left = true;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            g.p1.to_right = true;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            g.p1.to_crouch = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            g.p1.to_shoot = true;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (jump_released) {
                g.p1.to_jump = true;
                jump_released = false;
            }
        }
    }
    
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_A) {
            g.p1.to_left = false;
        } else if (e.getKeyCode() == KeyEvent.VK_D) {
            g.p1.to_right = false;
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            g.p1.to_crouch = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            g.p1.to_shoot = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump_released = true;
            g.p1.to_jump = false;
        }
    }
}
