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

public class Dog extends Thread {
    private static final int XSIZE = 10; // length of the egg
    private static final int YSIZE = 30; // height of the egg

    private JPanel panel;
    private InfoPanel infoPanel;

    private int x; // x coordinate to start drawing
    private int y; // y coordinate to start drawing

    private int dy; // amount of y pixels moved by snake

    private Random random; // used to generate random numbers snake movement

    Graphics2D g2;

    private Color backgroundColor;
    private Dimension dimension;
    private Color colour = Color.ORANGE;

    boolean isRunning;

    private Chicken chicken;

    Clip hitClip = null;

    public Dog(JPanel p, Chicken chicken, InfoPanel infoPanel) {
        panel = p;
        this.infoPanel = infoPanel;

        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;
        backgroundColor = panel.getBackground();

        this.chicken = chicken;

        dimension = panel.getSize();
        random = new Random();

        isRunning = false;

        x = random.nextInt(dimension.width - XSIZE);
        y = 0;

        dy = 5;

        loadClips();
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, XSIZE, YSIZE);
    }

    /**
     * Draws the dog on the panel
     */
    public void draw() {
        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;

        g2.setColor(colour);
        g2.fill(new Rectangle2D.Double(x, y, XSIZE, YSIZE));
    }

    /**
     * Erases the dog on the panel
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
        
        erase();

        y = y + dy;

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

        if (myRect.intersects(chickenRect) || (y > dimension.width)) {
            x = random.nextInt(dimension.width - XSIZE);
            y = 0;
        }
    }

    public void loadClips() {
        hitClip = getClip("DogHit.wav");
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

    public void stopClip() {
        hitClip.stop();
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
