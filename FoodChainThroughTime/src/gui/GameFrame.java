package gui;
import javax.swing.JFrame;
import game.GameController;
import game.GameState;
public class GameFrame extends JFrame {
	private GameState state;
	private GamePanel panel;
	private GameController controller;
	/**
	 * represents the main application window of the game.
	 * It initializes the game interface based on the 
	 * selected grid size, sets the window title using the chosen era.
	 * It also handles basic window configuration and allows 
	 * the game state to be updated when a new state is loaded. 
	 * @param state The current game state used to initialize the game window and its components.
	 */
	public GameFrame(GameState state) {
        this.state = state;
        this.controller = new GameController(state);
        int cellSize = 60;
        int gridWidthPx = state.getGrid().getWidth() * cellSize;
        int gridHeightPx = state.getGrid().getHeight() * cellSize;
        setTitle("Food Chain Through Time - " + state.getEra());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        panel = new GamePanel(state);
        panel.setPreferredSize(new java.awt.Dimension(gridWidthPx + 200, gridHeightPx + 230));
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
        panel.requestFocusInWindow();
    }
	/**
	 * Updates the game window to reflect a newly loaded game 
	 * state by resetting the controller and panel.
	 * @param newState The newly loaded GameState object that replaces the current game state.
	 */
    public void setGameState(GameState newState) {
        this.state = newState;
        this.controller = new GameController(newState);
        panel.setState(newState);
        repaint();
    }
}
