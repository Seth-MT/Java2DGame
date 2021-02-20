import java.awt.Color;  // used for colors
import javax.swing.*;   // used for GUI objects
import java.awt.*;      // used for certain AWT classes 

public class InfoPanel extends JPanel {
    
    private static final long serialVersionUID = 1L; // linter variable

    // variables
    private int score;
    private int lives;

    // labels
    private JLabel scoreL;
    private JLabel livesL;

    // textfields
    private JTextField scoreTF;
    private JTextField livesTF;

    public InfoPanel() {

        setBackground(Color.CYAN);

        // create labels
        scoreL = new JLabel("Total Score");
        livesL = new JLabel("# Lives Remaining");

        // create textfields
        scoreTF = new JTextField();
        livesTF = new JTextField();

        scoreTF.setEditable(false);
        livesTF.setEditable(false);

        // create layout manager
        GridLayout gridLayout = new GridLayout(2,2);
        setLayout(gridLayout);

        // add labels and textfields to panel
        add(scoreL);
        add(livesL);

        add(scoreTF);
        add(livesTF);

        // sets game information when the game starts / restarts
        reset();
    }

    /**
     * This methods sets the score to 0 when the game is started or restarted.
     */
    public void reset() {
        score = 0;
        lives = 3;
    }

    /**
     * This method increases the score by 1 when the condition to increase the score is met.
     */
    public void increaseScore() {
        score++;
    }

    /**
     * This method decreases the amount of lives by 1 when the chicken is hit
     */
    public void decreaseLives() {
        lives--;
    }

    /**
     * This method displays the score and lives in the panel.
     */
    public void displayInfo() {
        scoreTF.setText(score + "");
        livesTF.setText(lives + "");
    }

    /**
     * Get method (Accessor) to get the current score of the InfoPanel
     * @return int Returns the current score
     */
    public int getScore() {
        return score;
    }
    
    /**
     * Get method (Accessor) to get the current lives of the InfoPanel
     * @return int Returns the current amount of lives
     */
    public int getLives() {
        return lives;
    }
}
