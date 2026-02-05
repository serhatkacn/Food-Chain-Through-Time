package gui;
import game.GameController;
import game.GameState;
import helpers.Role;

import javax.swing.JPanel;

import characters.Animal;
import characters.Food;
import characters.Position;
import file.GameLogger;
import file.SaveLoadManager;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.swing.JButton;
public class GamePanel extends JPanel {
	private JButton finishButton;
    private GameState state;
    private GameController controller;
    private Image background;
    private Image preyImg;
    private Image predatorImg;
    private Image apexImg;
    private Image foodImg;
    private static final int CELL_SIZE = 40;
    /**
     * It creates a GameController to manage game logic,
     * loads era-specific images, and enables keyboard input. 
     * @param state
     */
    public GamePanel(GameState state) {
        this.state = state;
        this.controller = new GameController(state);
        loadEraImages();
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                Animal player = state.getPlayerAnimal();
                if (player == null || player.getRole() != Role.PREDATOR) return;
                Position cur = player.getPosition();
                Position next = null;
                if (e.getKeyCode() == KeyEvent.VK_SPACE || e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    if (player.canUseSpecial()) {
                        player.useSpecial(state);
                        controller.playRound(); 
                        repaint();
                    }
                    return;
                }
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> next = new Position(cur.getX(), cur.getY() - 1);
                    case KeyEvent.VK_DOWN -> next = new Position(cur.getX(), cur.getY() + 1);
                    case KeyEvent.VK_LEFT -> next = new Position(cur.getX() - 1, cur.getY());
                    case KeyEvent.VK_RIGHT -> next = new Position(cur.getX() + 1, cur.getY());
                    default -> { return; }
                }
                controller.movePlayer(player, next);
                controller.playRound();
                repaint();
                if (controller.isGameOver()) {
                	SaveLoadManager.save(state);
                	String winner = controller.determineGameWinner();
                    GameLogger.log("Game winner: " + winner);
                    JOptionPane.showMessageDialog(GamePanel.this,"GAME WINNER: " + winner,"Game Over",JOptionPane.INFORMATION_MESSAGE);
                    finishButton.setVisible(true);
                    removeKeyListener(this);
                }
            }
        });
        finishButton = new JButton("Finish");
        finishButton.setBounds(state.getGrid().getWidth() * CELL_SIZE / 2 - 50, state.getGrid().getHeight() * CELL_SIZE / 2,100,40);
        finishButton.setVisible(false);
        finishButton.addActionListener(e -> {
            System.exit(0);
        });
        setLayout(null);
        add(finishButton);

    }
    @Override
    /**
     * A key listener is added to detect player actions such as
     *  movement and special abilities. 
     */
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int w = state.getGrid().getWidth();
        int h = state.getGrid().getHeight();
        // Background
        g.drawImage(background, 0, 0, w * CELL_SIZE, h * CELL_SIZE, null);
        drawGrid(g);
        drawFood(g);
        drawAnimals(g);
        drawHUD(g);
        drawLegend(g);
    }
    private void drawGrid(Graphics g) {
        int w = state.getGrid().getWidth();
        int h = state.getGrid().getHeight();

        g.setColor(Color.BLACK);
        for (int i = 0; i <= w; i++)
            g.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, h * CELL_SIZE);

        for (int j = 0; j <= h; j++)
            g.drawLine(0, j * CELL_SIZE, w * CELL_SIZE, j * CELL_SIZE);
    }

    private void drawFood(Graphics g) {
        for (Food f : state.getFoods()) {
            int x = f.getPosition().getX() * CELL_SIZE;
            int y = f.getPosition().getY() * CELL_SIZE;
            g.drawImage(foodImg, x + 2, y + 2, CELL_SIZE - 4, CELL_SIZE - 4, null);
        }
    }
    private void drawAnimals(Graphics g) {
        for (Animal a : state.getAnimals()) {
            int x = a.getPosition().getX() * CELL_SIZE;
            int y = a.getPosition().getY() * CELL_SIZE;
            Image img = switch (a.getRole()) {
                case PREY -> preyImg;
                case PREDATOR -> predatorImg;
                case APEX_PREDATOR -> apexImg;
            };
            g.drawImage(img, x + 4, y + 4, CELL_SIZE - 8, CELL_SIZE - 8, null);
        }
    }
    /**
     * Displays game information below the grid, including the selected era, 
     * current round, maximum rounds, player name, and scores of all roles. 
     * @param g
     */
    private void drawHUD(Graphics g) {
        int startX = state.getGrid().getWidth() * CELL_SIZE + 20;
        int startY = 30;
        int lineHeight = 20;

        g.setColor(Color.BLACK);
        g.drawString("Era: " + state.getEra(), startX, startY);
        Animal player = state.getPlayerAnimal();
        if (player != null) {
            g.drawString( "Player: " + player.getUsername() + " | Score: " + player.getScore(), startX,startY + lineHeight);
        }

        g.drawString("Round: " + state.getCurrentRound() + " / " + state.getMaxRounds(),startX, startY + 2 * lineHeight);
        int y = startY + 3 * lineHeight;
        Animal prey = state.getAnimalByRole(Role.PREY);
        Animal predator = state.getAnimalByRole(Role.PREDATOR);
        Animal apex = state.getAnimalByRole(Role.APEX_PREDATOR);

        if (prey != null)
            g.drawString("Prey Score: " + prey.getScore(), startX, y);

        if (predator != null)
            g.drawString("Predator Score: " + predator.getScore(), startX, y + lineHeight);

        if (apex != null)
            g.drawString("Apex Score: " + apex.getScore(), startX, y + 2 * lineHeight);
    }
    /**
     * Draws a small legend on the side of the screen that shows 
     * which icon belongs to each role and food
     * @param g The Graphics g object is used to draw text and visuals on the screen. 
     * It allows the game to display information such as scores, rounds, and player details.
     */
    private void drawLegend(Graphics g) {
        int x = state.getGrid().getWidth() * CELL_SIZE + 20;
        int y = 180;
        int imgSize = 25;
        int spacing = 35;
        g.setColor(Color.BLACK);
        g.drawString("Characters", x, y - 20);
        g.drawImage(preyImg, x, y, imgSize, imgSize, null);
        g.drawString("Prey", x + 35, y + 18);
        g.drawImage(predatorImg, x, y + spacing, imgSize, imgSize, null);
        g.drawString("Predator (Player)", x + 35, y + spacing + 18);
        g.drawImage(apexImg, x, y + 2 * spacing, imgSize, imgSize, null);
        g.drawString("Apex Predator", x + 35, y + 2 * spacing + 18);
        g.drawImage(foodImg, x, y + 3 * spacing, imgSize, imgSize, null);
        g.drawString("Food", x + 35, y + 3 * spacing + 18);
    }
    private void loadEraImages() {
        preyImg = loadImage(state.getEra().getSpritePath("prey"));
        predatorImg = loadImage(state.getEra().getSpritePath("predator"));
        apexImg = loadImage(state.getEra().getSpritePath("apex"));
        foodImg = loadImage(state.getEra().getSpritePath("food"));
        background = loadImage(state.getEra().getBackgroundPath());
    }
    /**
     * Loads an image from the project resources using the given file path. 
     * If the image cannot be found, an error is thrown to prevent the game from running with missing assets.
     * @param path The resource path of the image file to be loaded from the project assets.
     * @return An Image object created from the specified resource file.
     */
    private Image loadImage(String path) {
        URL url = GamePanel.class.getResource(path);
        if (url == null) throw new RuntimeException("Missing resource: " + path);
        return new ImageIcon(url).getImage();
    }
    /**
     * Updates the game panel when a new game state is loaded. 
     * @param newState The new GameState object that replaces the current game state.
     */
    public void setState(GameState newState) {
        this.state = newState;
        this.controller = new GameController(newState);
        repaint();
        requestFocusInWindow();
    }
}

