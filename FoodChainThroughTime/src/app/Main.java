
package app;
import javax.swing.JOptionPane;
import java.io.File;
import game.GameState;
import game.Grid;
import gui.GameFrame;
import helpers.Era;
import file.FoodChainLoader;
import file.GameLogger;
public class Main {
	/**
	 * collects the game settings from the user, initializes the game state, 
	 * logs the game start, and launches the main game window.
	 * @param args
	 */
    public static void main(String[] args) {
    	 String username = JOptionPane.showInputDialog("Enter username:");
         if (username == null || username.isBlank()) {
             username = "Player";
         }
         Era era = (Era) JOptionPane.showInputDialog(null, "Select Era", "Era Selection",JOptionPane.QUESTION_MESSAGE,null,Era.values(),Era.PRESENT);
         if (era == null) {
        	 era = Era.PRESENT;
         }
         String[] gridOptions = {"10x10", "15x15", "20x20"};
         String gridChoice = (String) JOptionPane.showInputDialog( null,"Select Grid Size", "Grid Size", JOptionPane.QUESTION_MESSAGE, null, gridOptions, gridOptions[0]);
         int gridSize = switch (gridChoice) {
         case "15x15" -> 15;
         case "20x20" -> 20;
         default -> 10;
     };
     int maxRounds = 10;
     while (true) {
         try {
             String input = JOptionPane.showInputDialog("Enter number of rounds (min 10):");
             maxRounds = Integer.parseInt(input);
             if (maxRounds >= 10) break;
         } catch (Exception ignored) {}
     }
     Grid grid = new Grid(gridSize, gridSize);
     GameState state = new GameState(grid, maxRounds, era);
     state.setUsername(username);
     GameLogger.log("Game started");
     GameLogger.log("Player: " + username);
     GameLogger.log("Era: " + era);
     FoodChainLoader.loadEra(era, state, username);
     GameFrame frame = new GameFrame(state);
     frame.setVisible(true);
     }
 }
    	