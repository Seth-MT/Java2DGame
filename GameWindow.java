import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GameWindow extends JFrame implements KeyListener, ActionListener {

    private static final long serialVersionUID = 1L; // linter variable

    private JLabel gameL;
    private JLabel statusBarL;
    
    private JButton startGameB;
    private JButton endGameB;
    private JButton exitGameB;

    InfoPanel infoPanel;
    GamePanel gamePanel;
    JPanel buttonPanel;
    JPanel mainPanel;

    private Container c;

    public GameWindow() {
        setTitle("Chicken Runner");
        setSize(320, 500);

        gameL = new JLabel("Collect the Eggs!");
        statusBarL = new JLabel();

        startGameB = new JButton("Start Game");
        endGameB = new JButton("End Game");
        exitGameB = new JButton("Exit");

        startGameB.addActionListener(this);
        endGameB.addActionListener(this);
        exitGameB.addActionListener(this);

        infoPanel = new InfoPanel();
        
        gamePanel = new GamePanel(infoPanel);
        gamePanel.createGameEntities();
        gamePanel.setPreferredSize(new Dimension(300, 300));

        GridLayout gridLayout;
        
        buttonPanel = new JPanel();
        gridLayout = new GridLayout(1, 3);
        buttonPanel.setLayout(gridLayout);

        buttonPanel.add(startGameB);
        buttonPanel.add(endGameB);
        buttonPanel.add(exitGameB);

        mainPanel = new JPanel();
        mainPanel.add(infoPanel);
        mainPanel.add(gameL);
        mainPanel.add(gamePanel);
        mainPanel.add(buttonPanel);
        mainPanel.add(statusBarL);
        
        c = getContentPane();
        c.add(mainPanel);

        mainPanel.addKeyListener(this);

        mainPanel.setFocusable(true);
        mainPanel.requestFocus();

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setVisible(true);
    }

    public void keyPressed(KeyEvent e) {
        if (gamePanel.chicken == null)
            return;

        int direction = 0;
        
        int keyCode = e.getKeyCode();

        if (keyCode == KeyEvent.VK_LEFT) {
            direction = 1;
        }
        else if (keyCode == KeyEvent.VK_UP) {
            direction = 2;
        }
        else if (keyCode == KeyEvent.VK_RIGHT) {
            direction = 3;
        }
        else if (keyCode == KeyEvent.VK_DOWN) {
            direction = 4;
        }

        gamePanel.updateGameEntities(direction);
        gamePanel.drawGameEntities();

        if (infoPanel.getLives() == 0) {
            statusBarL.setText("Game Over!");
        }
    }

    public void keyReleased(KeyEvent e) {

    }

    public void keyTyped(KeyEvent e) {

    }

    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        if (command.equals(startGameB.getText())) {
            gamePanel.resetChicken();
            infoPanel.reset();
            gamePanel.startGame();
            statusBarL.setText("Game Started!");
        }
        else if (command.equals(endGameB.getText())) {
            gamePanel.endGame();
            statusBarL.setText("Game Over!");
        }
        else if (command.equals(exitGameB.getText())) {
            System.exit(0);
        }

        mainPanel.requestFocus();
    }
}