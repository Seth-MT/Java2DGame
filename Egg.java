import java.awt.Dimension;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import javax.swing.JPanel;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.*;
import java.io.*;

public class Egg extends Thread {

    private static final int XSIZE = 20; // length of the egg
    private static final int YSIZE = 20; // height of the egg

    private JPanel panel;
    private InfoPanel infoPanel;

    private int x; // x coordinate to start drawing
    private int y; // y coordinate to start drawing

    private Random random; // used to generate random numbers for egg relocation

    Graphics2D g2;

    private Color backgroundColor;
    private Dimension dimension;
    private Color colour = Color.BLUE;

    boolean isRunning;

    private Chicken chicken;

    Clip hitClip = null;
    Clip backgroundClip = null;

    public Egg(JPanel p, Chicken chicken, InfoPanel infoPanel) {
        panel = p;
        this.infoPanel = infoPanel;

        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;
        backgroundColor = panel.getBackground();

        this.chicken = chicken;

        dimension = panel.getSize();
        random = new Random();

        x = random.nextInt(dimension.width - XSIZE);
        y = random.nextInt(dimension.height - YSIZE);

        isRunning = false;

        infoPanel.displayInfo();

        loadClips();
        playClip(2);
    }

    public Rectangle2D.Double getBoundingRectangle() {
        return new Rectangle2D.Double(x, y, XSIZE, YSIZE);
    }

    /**
     * Draws the egg on the panel
     */
    public void draw() {
        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;

        g2.setColor(colour);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));

        infoPanel.displayInfo();
    }

    /**
     * Erases the egg from the panel
     */
    public void erase() {
        Graphics g = panel.getGraphics();
        g2 = (Graphics2D)g;

        g2.setColor(backgroundColor);
        g2.fill(new Ellipse2D.Double(x, y, XSIZE, YSIZE));
    }

    /**
     * Moves the egg when the chicken collects it
     */
    public void move() {
        if (!panel.isVisible())
            return;

        Rectangle2D.Double myRect = getBoundingRectangle();
        Rectangle2D.Double chickenRect = chicken.getBoundingRectangle();

        if (myRect.intersects(chickenRect)) {
            playClip(1);
            infoPanel.increaseScore();
            infoPanel.displayInfo();

            if (infoPanel.getScore() % 5 == 0) {
                chicken.increaseSpeed();
            }

            try {
                sleep(300);
            }
            catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        if (myRect.intersects(chickenRect)) {
            x = random.nextInt(dimension.width - XSIZE);
            y = random.nextInt(dimension.height - YSIZE);
        }
    }

    public void loadClips() {
        hitClip = getClip("EggHit.wav");
        backgroundClip = getClip("Background.wav");
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
        else if ((i == 2) && (hitClip != null)) {
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stopClip() {
        hitClip.stop();
        backgroundClip.stop();
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
                sleep(20);
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
