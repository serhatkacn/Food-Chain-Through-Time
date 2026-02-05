package ai;
import characters.Animal;
import characters.ApexPredator;
import characters.Position;
import game.GameState;
import helpers.Role;
public class ApexPredatorAI {
	public Position decideMove(ApexPredator apex, GameState state) {
		Position c = apex.getPosition();//current
        Animal closestPrey = null;
        int minDistance = Integer.MAX_VALUE;//the max value that a variable can get in Java, a final value
        for (Animal a : state.getAnimals()) {
        	if (a.getRole() == Role.PREY) {
        		int dx = a.getPosition().getX() - c.getX();
                int dy = a.getPosition().getY() - c.getY();
                int distance = Math.abs(dx) + Math.abs(dy);
                if (distance < minDistance) {
                	minDistance = distance;
                    closestPrey = a;
                }
        	}
        }
        if (closestPrey == null) {
            return c;
        }
        //closestPrey.getPosition().getX()= this is the place apexpredator wants to go
        int stepX = Integer.compare(closestPrey.getPosition().getX(),c.getX());//if the first value is greater than the second one than it's +1, if its equal it's 0
        int stepY = Integer.compare( closestPrey.getPosition().getY(),c.getY()); 
        Position next = new Position(c.getX() + stepX,c.getY() + stepY );
        if (state.getGrid().Inside(next)) {
            return next;
        }
        return c;
	}		
}
