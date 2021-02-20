import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.*;
import java.io.*;

public class Snake extends Thread {
    private static final int XSIZE = 30; // length of the egg
    private static final int YSIZE = 10; // height of the egg

    private JPanel panel;
    private InfoPanel infoPanel;

    private int x; // x coordinate to start drawing
    private int y; // y coordinate to start drawing

    private int dx; // amount of x pixels moved by snake

    private Random random; // used to generate random numbers snake movement

    Graphics2D g2;

    private Color backgroundColor;
    private Dimension dimension;
    private Color colour = Color.PINK;

    boolean isRunning;

    private Chicken chicken;

    Dog dog;

    Clip hitClip = null;

    public Snake(JPanel p, Chicken chicken, InfoPanel infoPanel) {
        panel = p;
        this.infoPanel = infoPanel;

        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;
        backgroundColor = panel.getBackground();

        this.chicken = chicken;

        dimension = panel.getSize();
        random = new Random();

        isRunning = false;

        x = 0;
        y = random.nextInt(dimension.height - YSIZE);

        dx = 5;

        loadClips();
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, XSIZE, YSIZE);
    }

    /**
     * Draws the snake
     */
    public void draw() {
        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;

        g2.setColor(colour);
        g2.fill(new Rectangle2D.Double(x, y, XSIZE, YSIZE));
    }

    /**
     * Erases the egg
     */
    public void erase() {
        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;

        g2.setColor(backgroundColor);
        g2.fill(new Rectangle2D.Double(x, y, XSIZE, YSIZE));
    }

    /**
     * Moves the egg when the chicken collects it
     */
    public void move() {
        if (!panel.isVisible())
            return;

        x = x + dx;

        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double chickenRect = chicken.getBoundingRectangle();

        if (myRect.intersects(chickenRect)) {
            playClip(1);
            infoPanel.decreaseLives();
            infoPanel.displayInfo();

            try {
                Thread.sleep(1000);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (myRect.intersects(chickenRect) || (x > dimension.width)) {
            x = 0;
            y = random.nextInt(dimension.height - YSIZE);
        }
    }

    public void loadClips() {
        hitClip = getClip("SnakeHit.wav");
    }

    public void stopClip() {
        hitClip.stop();
    }

    Clip getClip(String fileName) {
        AudioInputStream audioIn;
        Clip clip = null;

        try {
            File file = new File(fileName);
            audioIn = AudioSystem.getAudioInputStream(file.toURI().toURL());
            clip = AudioSystem.getClip();
            clip.open(audioIn);
        }
        catch (Exception e) {
            System.out.println("Error opening sound files: " + e);
        }

        return clip;
    }

    public void playClip(int i) {
        if ((i == 1) && (hitClip != null)) {
            hitClip.setFramePosition(0);
            hitClip.start();
        } 
    }

    @Override
    public void run() {
        try {
            draw();
            isRunning = true;
            while (isRunning) {
                erase();
                move();
                draw();
                sleep(15);
            }
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void endGame() {
        if (isRunning) {
            isRunning = false;
            erase();
        }
    }
}
