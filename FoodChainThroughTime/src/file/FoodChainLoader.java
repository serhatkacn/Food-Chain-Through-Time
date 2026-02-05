package file;
import game.GameState;
import helpers.Era;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Random;
import characters.*;
public class FoodChainLoader {
	/**
	 * Loads the initial game objects for the selected era from a text file
	 * and adds them to the game state
	 * @param era selected era that determines which data file is loaded
	 * @param state current GameState to which animals and food are added
	 * @param username assigned to the player-controlled Predator
	 */
    public static void loadEra(Era era, GameState state, String username) {
        String filePath = "/data/" + era.name().toLowerCase() + ".txt";
        try {
            InputStream is = FoodChainLoader.class.getResourceAsStream(filePath);
            if (is == null) {
                throw new RuntimeException("Missing era file: " + filePath);
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                if (line.startsWith("Food Chain")) {
                    state.addAnimal(new ApexPredator(randomPos(state)));
                    Predator player = new Predator(randomPos(state), username);
                    state.addAnimal(player);
                    state.setPlayerAnimal(player);
                    state.addAnimal(new Prey(randomPos(state)));
                    state.addFood(new Food(randomPos(state), 3));
                    break;
                }
            }
            reader.close();
        } catch (Exception e) {
            throw new RuntimeException("Failed to load era data: " + era, e);
        }
    }
    /**
     * Generates a random position inside the game grid
     * @param state current GameState used to access grid dimensions.
     * @return a randomly generated Position inside the grid boundaries.
     */
    private static Position randomPos(GameState state) {
        Random r = new Random();
        return new Position(
                r.nextInt(state.getGrid().getWidth()),
                r.nextInt(state.getGrid().getHeight())
        );
    }
}

