package file;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;
import characters.*;
import game.GameState;
import game.Grid;
import helpers.Era;
import helpers.Role;
public class SaveLoadManager {
	/**
	 * Saves the current game state to a file so the game can be resumed later.
	 * @param state GameState object that contains all current game data
	 */
    public static void save(GameState state) {
    	//stores everything needed to resume the game later.
    	try (PrintWriter pw = new PrintWriter(new FileWriter("save.txt"))) {
            pw.println("ERA " + state.getEra());
            pw.println("MAX_ROUNDS " + state.getMaxRounds());
            pw.println("CURRENT_ROUND " + state.getCurrentRound());
            Grid g = state.getGrid();
            pw.println("GRID " + g.getWidth() + " " + g.getHeight());
            for (Animal a : state.getAnimals()) {
                pw.println("ANIMAL " + a.getRole() + " " + a.getPosition().getX() + " " +a.getPosition().getY() + " " + a.getScore() + " " + a.isPlayerControlled());
            }
            for (Food f : state.getFoods()) {
                pw.println("FOOD " + f.getPosition().getX() + " " + f.getPosition().getY() + " " +f.getValue());
            }
        } catch (IOException e) {
            System.out.println("Save failed.");
        }
    }
    /**
     * Loads a previously saved game state from a file
     * @param filename name of the file that contains saved game data.
     * @return a GameState object recreated from the saved file.
     */
    public static GameState load(String filename) {
    	//rebuilds the game exactly as it was when it was saved.
        try (Scanner sc = new Scanner(new File(filename))) {
            Era era = Era.PRESENT;
            Grid grid = null;
            int maxRounds = 0;
            int currentRound = 0;
            GameState state = null;
            while (sc.hasNext()) {
                String token = sc.next();
                switch (token) {
                    case "ERA" -> era = Era.valueOf(sc.next());
                    case "MAX_ROUNDS" -> maxRounds = sc.nextInt();
                    case "CURRENT_ROUND" -> currentRound = sc.nextInt();
                    case "GRID" -> {
                        int w = sc.nextInt();
                        int h = sc.nextInt();
                        grid = new Grid(w, h);
                        state = new GameState(grid, maxRounds, era);
                        state.setCurrentRound(currentRound);
                    }
                    case "ANIMAL" -> {
                        Role role = Role.valueOf(sc.next());
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        int score = sc.nextInt();
                        boolean isPlayer = sc.nextBoolean();
                        Animal a;
                        Position pos = new Position(x, y);
                        switch (role) {
                            case PREY -> a = new Prey(pos);
                            case PREDATOR -> a = new Predator(pos, "Player");
                            case APEX_PREDATOR -> a = new ApexPredator(pos);
                            default -> throw new IllegalStateException();
                        }
                        a.increaseScore(score);
                        state.addAnimal(a);

                        if (isPlayer) {
                            state.setPlayerAnimal(a);
                        }
                    }
                    case "FOOD" -> {
                        int x = sc.nextInt();
                        int y = sc.nextInt();
                        int value = sc.nextInt();
                        state.addFood(new Food(new Position(x, y), value));
                    }
                }
            }
            return state;
        } catch (Exception e) {
            System.out.println("Load failed.");
            return null;
        }
    }
}