import java.awt.Color;
import javax.swing.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.*;
import java.io.*;

public class GamePanel extends JPanel {
    
    private static final long serialVersionUID = 1L; // linter variable

    Egg egg;
    Dog dog;
    Snake snake;
    Chicken chicken;

    InfoPanel infoPanel;

    Clip loseClip = null;

    public GamePanel(InfoPanel infoPanel) {
        setBackground(Color.GREEN);

        this.infoPanel = infoPanel;

        loadClips();
    }

    public void createGameEntities() {
        chicken = new Chicken(this, 145, 150);
    }

    public void startGame() {
        egg = new Egg(this, chicken, infoPanel);
        dog = new Dog(this, chicken, infoPanel);
        snake = new Snake(this, chicken, infoPanel);

        egg.start();
        dog.start();
        snake.start();
    }
    
    public void endGame() {
        egg.endGame();
        egg.stopClip();
        snake.endGame();
        snake.stopClip();
        dog.endGame();
        dog.stopClip();

        playClip(1);
    }

    public void updateGameEntities(int direction) {
        if (chicken == null)
            return;

        if (direction == 1) {
            chicken.erase();
            chicken.moveLeft();
        }
        else if (direction == 2) {
            chicken.erase();
            chicken.moveUp();
        }
        else if (direction == 3) {
            chicken.erase();
            chicken.moveRight();
        }
        else if (direction == 4) {
            chicken.erase();
            chicken.moveDown();
        }

        if (infoPanel.getLives() == 0) {
            endGame();
        }
    }

    public void drawGameEntities() {
        if (chicken != null) {
            chicken.draw();
        }
    }

    public void resetChicken() {
        chicken.resetSpeed();
    }

    public void loadClips() {
        loseClip = getClip("LoseSound.wav");
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
        if ((i == 1) && (loseClip != null)) {
            loseClip.setFramePosition(0);
            loseClip.start();
        } 
    }
}
