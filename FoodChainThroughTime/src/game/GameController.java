package game;
import java.util.ArrayList;
import java.util.List;
import characters.Animal;
import characters.ApexPredator;
import characters.Food;
import characters.Position;
import characters.Predator;
import characters.Prey;
import file.GameLogger;
import helpers.Role;
import java.security.SecureRandom;
public class GameController {
	private GameState gameState;
	private SecureRandom random = new SecureRandom();
	private boolean playerMovedThisTurn = false;
	private int movesThisRound = 0;
	/**
	 * 
	 * @param gameState The GameState object that stores all current game data.
	 */
	public GameController(GameState gameState) {
		this.gameState = gameState;
	}
	/**
	 * Moves the player to the next position if it is inside the grid
	 * and checks for possible collisions after the move.
	 * @param player The player-controlled animal
	 * @param next The next position the player wants to move to.
	 */
	public void movePlayer(Animal player, Position next) {
	    if (!isInsideGrid(next)) return;
	    player.setPosition(next);
	    checkCollisions();
	}
	/**
	 * Executes one full game round by updating cooldowns,
	 * moving AI animals, checking collisions, increasing the round number,
	 * and logging the round summary.
	 */
	public void playRound() {
	    for (Animal a : gameState.getAnimals()) {
	        a.tickCooldown();
	    }
	    moveAIAnimals();
	    checkCollisions();
	    gameState.incrementRound();
	    logRoundSummary();
	}
	// Moves all AI-controlled animals based on their roles and behaviors
	private void moveAIAnimals() {
	    Animal player = gameState.getPlayerAnimal();
	    for (Animal a : gameState.getAnimals()) {
	        if (a == player) continue;
	        Position current = a.getPosition();
	        Position next = current;
	        if (a.getRole() == Role.APEX_PREDATOR) {
	            if (gameState.getCurrentRound() % 2 == 0) {
	                next = apexSmartChase(current);
	            }
	        }
	        else if (a.getRole() == Role.PREY) {
	            next = preySmartMove(current);
	        }
	        if (isInsideGrid(next)) {
	            a.setPosition(next);
	            movesThisRound++;
	        }
	    }
	}
	/**
	 * 
	 * Calculates a movement direction for the Prey to move away from the nearest food.
	 * @param preyPosition The current position of the Prey.
	 * @return A new position that increases distance from food.
	 */
	private Position fleeFood(Position preyPosition) {
		Food closest = null;
		int mindistance = Integer.MAX_VALUE;
		for (Food f : gameState.getFoods()) {
			int d = Math.abs(f.getPosition().getX() - preyPosition.getX())+ Math.abs(f.getPosition().getY() - preyPosition.getY());
			if (d < mindistance) {
				mindistance = d;
				closest = f;
			}
		}
		if (closest == null)
			return preyPosition;
		int dx = Integer.compare(preyPosition.getX(), closest.getPosition().getX());
		int dy = Integer.compare(preyPosition.getY(), closest.getPosition().getY());
		return new Position(preyPosition.getX() + dx, preyPosition.getY() + dy);
	}
	//Logs the current round number and the scores of all animals to the game log file.
	private void logRoundSummary() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Round ").append(gameState.getCurrentRound()).append("\n");
	    for (Animal a : gameState.getAnimals()) {
	        sb.append(a.getRole())
	          .append(" Score: ")
	          .append(a.getScore())
	          .append("\n");
	    }
	    sb.append("--------------------");
	    GameLogger.log(sb.toString());
	}
	/**
	 * Calculates a movement step that moves one position closer to a target.
	 * @param from current position
	 * @param target the target position to chase
	 * @return The next position toward the target.
	 */
	private Position chasePlayer(Position from, Position target) {
		int dx = Integer.compare(target.getX(), from.getX());
		int dy = Integer.compare(target.getY(), from.getY());
		return new Position(from.getX() + dx, from.getY() + dy);
	}
	// Detects interactions between animals and food, updates scores,
	// and respawns animals or food when necessary.
	private void checkCollisions() {
		// Animal vs Food
		for (Animal a : gameState.getAnimals()) {
			for (Food f : gameState.getFoods()) {
				if (samePosition(a.getPosition(), f.getPosition())) {
					// Prey eats food
					if (a.getRole() == Role.PREY) {
						a.increaseScore(3);
						respawnFood(f);
					}
				}
			}
		}
		//️Animal vs Animal
		for (Animal a1 : gameState.getAnimals()) {
			for (Animal a2 : gameState.getAnimals()) {
				if (a1 == a2)
					continue;
				if (!samePosition(a1.getPosition(), a2.getPosition()))
					continue;
				// Predator eats Prey
				if (a1.getRole() == Role.PREDATOR && a2.getRole() == Role.PREY) {
					a1.increaseScore(3); // Predator +3
					a2.increaseScore(-1); // Prey -1
					respawnAnimal(a2);
				}
				// Apex eats Prey or Predator
				else if (a1.getRole() == Role.APEX_PREDATOR&& (a2.getRole() == Role.PREY || a2.getRole() == Role.PREDATOR)) {
					a1.increaseScore(1); // Apex +1
					a2.increaseScore(-1); // Victim -1
					respawnAnimal(a2);
				}
			}
		}
	}
	/**
	 * Respawns food at a random valid position on the grid.
	 * @param food food object that is going to be placed again
	 */
	private void respawnFood(Food food) {
		int x = random.nextInt(gameState.getGrid().getWidth());
	    int y = random.nextInt(gameState.getGrid().getHeight());
	    food.getPosition().setX(x);
	    food.getPosition().setY(y);
	}
	/**
	 * Checks whether two positions are the same.
	 * @param p1 first position
	 * @param p2 second position
	 * @return  True if both positions are equal.
	 */
	private boolean samePosition(Position p1, Position p2) {
		return p1.getX() == p2.getX() && p1.getY() == p2.getY();
	}
	/**
	 * Checks whether a position is inside the grid boundaries.
	 * @param p the position to check.
	 * @return true if the position is valid.
	 */
	private boolean isInsideGrid(Position p) {
		return p.getX() >= 0 && p.getY() >= 0 && p.getX() < gameState.getGrid().getWidth()
				&& p.getY() < gameState.getGrid().getHeight();
	}
	// Returns true if the maximum number of rounds has been reached
	public boolean isGameOver() {
		return gameState.getCurrentRound() >= gameState.getMaxRounds();
	}
	
	/**
	 * Compares the player score with AI scores and determines the winner.
	 * @return A string describing the winner.
	 */
	public String determineWinner() {
		Animal player = gameState.getPlayerAnimal();
		int playerScore = player.getScore();
		int bestAI = 0;
		for (Animal a : gameState.getAnimals()) {
			if (!a.isPlayerControlled()) {
				bestAI = Math.max(bestAI, a.getScore());
			}
		}
		if (playerScore > bestAI)
			return "PLAYER WINS!";
		else if (playerScore < bestAI)
			return "AI WINS!";
		else
			return "NOBODY WON!";
	}
	/**
	 * Determines the safest and most beneficial move for the Prey
	 * by balancing predator distance and food proximity.
	 * @param preyPosition The current position of the Prey.
	 * @return The best calculated next position.
	 */
	private Position preySmartMove(Position preyPosition) {
	    int bestScore = Integer.MIN_VALUE;
	    Position bestMove = preyPosition;
	    for (int dx = -1; dx <= 1; dx++) {
	        for (int dy = -1; dy <= 1; dy++) {
	            Position candidate = new Position( preyPosition.getX() + dx,preyPosition.getY() + dy);
	            if (!isInsideGrid(candidate)) continue;
	            int minPredatorDist = Integer.MAX_VALUE;
	            for (Animal a : gameState.getAnimals()) {
	                if (a.getRole() == Role.PREDATOR || a.getRole() == Role.APEX_PREDATOR) {
	                    int d = Math.abs(a.getPosition().getX() - candidate.getX())
	                          + Math.abs(a.getPosition().getY() - candidate.getY());
	                    minPredatorDist = Math.min(minPredatorDist, d);
	                }
	            }
	            int minFoodDist = Integer.MAX_VALUE;
	            for (Food f : gameState.getFoods()) {
	                int d = Math.abs(f.getPosition().getX() - candidate.getX())
	                      + Math.abs(f.getPosition().getY() - candidate.getY());
	                minFoodDist = Math.min(minFoodDist, d);
	            }
	            int score;
	            if (minPredatorDist <= 1) {
	                score = -1000;
	            }
	            else if (minPredatorDist >= 4) {
	                score = 1000 - minFoodDist;
	            }
	            else {
	                score = (minPredatorDist * 3) - minFoodDist;
	            }
	            if (score > bestScore) {
	                bestScore = score;
	                bestMove = candidate;
	            }
	        }
	    }
	    return bestMove;
	}
	/**
	 * Moves the Apex Predator toward the closest Prey or Predator.
	 * @param apexPos the current position of the Apex Predator.
	 * @return the next position toward the closest target.
	 */
	private Position apexSmartChase(Position apexPos) {
		Animal target = null;
		int minDistance = Integer.MAX_VALUE;
		for (Animal a : gameState.getAnimals()) {
			if (a.getRole() == Role.APEX_PREDATOR)
				continue;
			if (a.getRole() == Role.PREY || a.getRole() == Role.PREDATOR) {
				int distance = Math.abs(a.getPosition().getX() - apexPos.getX())+ Math.abs(a.getPosition().getY() - apexPos.getY());
				if (distance < minDistance) {
					minDistance = distance;
					target = a;
				}
			}
		}
		if (target == null)
			return apexPos;
		int dx = Integer.compare(target.getPosition().getX(), apexPos.getX());
		int dy = Integer.compare(target.getPosition().getY(), apexPos.getY());
		return new Position(apexPos.getX() + dx, apexPos.getY() + dy);
	}
	/**
	 * Determines the final winner of the game based on scores,
	 * including handling tie situations.
	 * @return A string describing the final result.
	 */
	public String determineGameWinner() {
		int maxScore = Integer.MIN_VALUE;
    for (Animal a : gameState.getAnimals()) {
	        maxScore = Math.max(maxScore, a.getScore());
	    }
	    List<String> winnerNames = new ArrayList<>();
	    for (Animal a : gameState.getAnimals()) {
	        if (a.getScore() == maxScore) {
	            winnerNames.add(a.getRole().toString());
	        }
	    }
	    if (winnerNames.size() == 1) {
	        return winnerNames.get(0) + " WINS!";
	    }
	    return "TIE! Winners: " + String.join(", ", winnerNames);
	}
	// Generates and returns a random valid position inside the grid.
	private Position randomPosition() {
		int x = random.nextInt(gameState.getGrid().getWidth());
	    int y = random.nextInt(gameState.getGrid().getHeight());
	    return new Position(x, y);
	}
	// Places an animal at a random empty position after it is eaten.
	private void respawnAnimal(Animal a) {
		Position p;
		do {
			p = randomPosition();
		} while (!isEmpty(p));
		a.setPosition(p);
	}
	//Checks whether a given position is free of other animals.
	private boolean isEmpty(Position p) {
		for (Animal a : gameState.getAnimals()) {
			if (samePosition(a.getPosition(), p))
				return false;
		}
		return true;
	}
	// Resets the game by clearing animals and spawning new ones for a new round.
	private void resetAfterRound() {
		gameState.getAnimals().clear();
		Predator newPlayer = new Predator(randomPosition(), gameState.getUsername());
		gameState.addAnimal(newPlayer);
		gameState.setPlayerAnimal(newPlayer);
		Prey prey = new Prey(randomPosition());
		gameState.addAnimal(prey);
		ApexPredator apex = new ApexPredator(randomPosition());
		gameState.addAnimal(apex);
	}
}


