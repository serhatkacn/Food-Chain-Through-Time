package game;
import java.util.ArrayList;
import java.util.List;
import characters.Animal;
import characters.Food;
import helpers.Era;
import helpers.Role;
public class GameState { //what is happening in the game 
	private List<Animal> animals;
	private Grid grid;
	private int currentRound;
	private int maxRounds;
	private List<Food> foods;
	private Era era;
	private Animal playerAnimal;
	private String username;
	/**
	 * 
	 * @param grid The game grid that defines the playable area.
	 * @param maxRounds The maximum number of rounds the game can run.
	 * @param era The selected era that determines visuals and game theme.
	 */
	public GameState(Grid grid, int maxRounds, Era era) {
        this.grid = grid;
        this.maxRounds = maxRounds;
        this.currentRound = 0;
        this.animals = new ArrayList<>();
        this.foods = new ArrayList<>();
        this.era = era;
    }
    public GameState(Grid grid, int maxRounds) {
        this(grid, maxRounds, Era.PRESENT);
    }
    public GameState(Grid grid) {
        this(grid, 100, Era.PRESENT);
    }
    public GameState(Grid grid, Era era) {
        this(grid, 100, era);
    }	
	public List<Animal> getAnimals() {
        return animals;
    }
	public Grid getGrid() {
		return grid;
	}
	public int getCurrentRound() {
		return currentRound;
	}
	public int getMaxRounds() {
		return maxRounds;
	}
	public void nextRound() {
		currentRound++;
	}
	public boolean isGameOver() {
		return currentRound>=maxRounds;
	}
	public void addAnimal(Animal animal) {
		animals.add(animal);
	}
	public void removeAnimal(Animal animal) {
		animals.remove(animal);
	}
	public void addFood(Food food) {
	    foods.add(food);
	}
    public List<Food> getFoods() {
        return foods;
    }
    public Era getEra() {
        return era;
    }   
    public void setPlayerAnimal(Animal a) {
        this.playerAnimal = a;
    }
    public Animal getPlayerAnimal() {
        return playerAnimal;
    }
    public void incrementRound() {
        currentRound++;
    }
    public Animal getAnimalByRole(Role role) {
        for (Animal a : animals) {
            if (a.getRole() == role) return a;
        }
        return null;
    }
    public String getUsername() {
    	return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setCurrentRound(int round) {
        this.currentRound = round;
    }
    //converts the current game state into a text format, including era, grid size, round number, 
    //animals, and food, so that the game can be saved to a file.
    public String serialize() {
        StringBuilder sb = new StringBuilder();
        sb.append("ERA ").append(era).append("\n");
        sb.append("USERNAME ").append(username).append("\n");
        sb.append("GRID ").append(grid.getWidth()).append(" ").append(grid.getHeight()).append("\n");
        sb.append("ROUND ").append(currentRound).append("\n");
        for (Animal a : animals) {
            sb.append("ANIMAL ")
              .append(a.getRole()).append(" ")
              .append(a.getPosition().getX()).append(" ")
              .append(a.getPosition().getY()).append(" ")
              .append(a.getScore())
              .append("\n");
        }
        for (Food f : foods) {
            sb.append("FOOD ")
              .append(f.getPosition().getX()).append(" ")
              .append(f.getPosition().getY()).append(" ")
              .append(f.getValue())
              .append("\n");
        }
        return sb.toString();
    }
    //generates a readable summary of the current round, including the era,
    //player name, and the scores of all animals.
    public String roundSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("PLay: ").append(currentRound).append("\n");
        sb.append("Era: ").append(era).append("\n");
        sb.append("Player: ").append(username).append("\n");
        for (Animal a : animals) {
        	sb.append(a.getRole())
        	  .append(" | Score: ")
        	  .append(a.getScore())
        	  .append("\n");
        }
        sb.append("------------------------\n");
        return sb.toString();
    }
}
