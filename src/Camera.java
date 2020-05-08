import java.awt.*;

public class Camera {
    int camX;
    int camY;
    private int rectWidth = 30;
    private int rectHeight = 50;
    private Rectangle moveRect;

    public Camera() {
        moveRect = new Rectangle(0, 0, rectWidth, rectHeight);
    }

    public void reposition(Player p1, int levelWidth, int levelHeight) {
        if (p1.x < moveRect.x) {
            moveRect = new Rectangle(p1.x, moveRect.y, rectWidth, rectHeight);
        }
        if (p1.x + p1.width > moveRect.x + rectWidth) {
            moveRect = new Rectangle(p1.x + p1.width - rectWidth, moveRect.y, rectWidth, rectHeight);
        }
        if (p1.y < moveRect.y) {
            moveRect = new Rectangle(moveRect.x, p1.y, rectWidth, rectHeight);
        }
        if (p1.y + p1.height > moveRect.y + rectHeight) {
            moveRect = new Rectangle(moveRect.x, p1.y + p1.height - rectHeight, rectWidth, rectHeight);
        }

        camX = -moveRect.x + ((200-rectWidth)/2);
        camY = -moveRect.y + ((150-rectHeight)/2) - 20;

        if (camX > 0) {
            camX = 0;
        } else if (camX < -levelWidth + 196) {
            camX = -levelWidth + 196;
        }
        if (camY > 0) {
            camY = 0;
        } else if (camY < -levelHeight + 140) {
            camY = -levelHeight + 140;
        }

    }

}
