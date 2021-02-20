import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;

public class Chicken {

    private static final int XSIZE = 15; // length of the chicken
    private static final int YSIZE = 15; // height of the chicken

    private int dx; // amount of x pixels moved in one keystroke
    private int dy; // amount of y pixels moved in one keystroke

    private int x; // x coordinate to start drawing
    private int y; // y coordinate to start drawing

    private JPanel panel;
    
    Graphics2D g2;
    private Color backgroundColor;

    public Chicken(JPanel p, int xPos, int yPos) {
        panel = p;
        backgroundColor = panel.getBackground();
        x = xPos;
        y = yPos;
        dx = 5;
        dy = 5;
    }
    
    /**
     * Draws the chicken on the panel
     */
    public void draw() {
        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;

        Rectangle chicken = new Rectangle(x, y, XSIZE, YSIZE);
        g2.setColor(Color.RED);
        g2.fill(chicken);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, XSIZE, YSIZE);
    }

    /**
     * Erases the chicken from the panel
     */
    public void erase() {
        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;

        g2.setColor(backgroundColor);
        g2.fill(new Rectangle(x, y, XSIZE, YSIZE));
    }

    /**
     * Moves the shape left and ensures x does not go outside the left side of the panel
     */
    public void moveLeft() {
        if (!panel.isVisible()) return;

        erase();

        if (x - dx > 0)
            x = x - dx;
    }

    /**
     * Moves the shape right and ensures x does not go outside the right side of the panel
     */
    public void moveRight() {
        Dimension dimension;

        if (!panel.isVisible()) return;

        erase();

        dimension = panel.getSize();

        if (x + dx + XSIZE < dimension.width)
            x = x + dx;
    }

    /**
     * Moves the shape up and ensures y does not go outside the top side of the panel
     */
    public void moveUp() {
        if (!panel.isVisible()) return;

        erase(); 

        if (y - dy > 0)
            y = y - dy;
    }

    /**
     * Moves the shape down and ensures y does not go outside the bottom side of the panel
     */
    public void moveDown() {
        Dimension dimension;

        if (!panel.isVisible()) return;

        erase();

        dimension = panel.getSize();

        if (y + dy + YSIZE < dimension.height)
            y = y + dy;
    }

    public void increaseSpeed() {
        dx = dx + 2;
        dy = dy + 2;
    }

    public void resetSpeed() {
        dx = 5;
        dy = 5;
    }
}
