package ai;
import game.GameState;
import helpers.Role;
import java.util.ArrayList;
import java.util.List;
import characters.*;
import java.security.SecureRandom;
public class PreyAI {
	private SecureRandom random = new SecureRandom();
	/**
	 * maximize survival while opportunistically moving toward food.
	 * Prey movement is determined by evaluating all possible neighbouring positions
	 * and assigning a score to each position based on environmental risk and reward.
	 * @param prey
	 * @param state
	 * @return The selected next Position that provides the safest and most beneficial move for the Prey.
	 */
    public Position decideMove(Prey prey, GameState state) {
        Position current = prey.getPosition();
        List<Position> possibleMoves = new ArrayList<>();
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                Position next = new Position(current.getX() + dx, current.getY() + dy);
                if (state.getGrid().Inside(next)) {
                    possibleMoves.add(next);
                }
            }
        }
        Position bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        for (Position p : possibleMoves) {
            int minPredatorDist = Integer.MAX_VALUE;
            for (Animal a : state.getAnimals()) {
                if (a.getRole() == Role.PREDATOR || a.getRole() == Role.APEX_PREDATOR) {
                    int d = Math.abs(a.getPosition().getX() - p.getX()) + Math.abs(a.getPosition().getY() - p.getY());
                    minPredatorDist = Math.min(minPredatorDist, d);
                }
            }
            int minFoodDist = Integer.MAX_VALUE;
            for (Food f : state.getFoods()) {
                int d = Math.abs(f.getPosition().getX() - p.getX())+ Math.abs(f.getPosition().getY() - p.getY());
                minFoodDist = Math.min(minFoodDist, d);
            }
            int score = (minPredatorDist * 2) - minFoodDist;
            if (score > bestScore) {
                bestScore = score;
                bestMove = p;
            }
        }
        if (bestMove == null) {
            return possibleMoves.get(random.nextInt(possibleMoves.size()));
        }
        return bestMove;
    }
}