package characters;

import game.GameState;
import helpers.Role;
public class Predator extends Animal {
	/**
	 * 
	 * @param position the starting position of the Predator on the grid
	 * @param username the name of the player controlling this Predator
	 */
	public Predator(Position position, String username) {
	    super(position, Role.PREDATOR);
	    this.username = username;
	}
	@Override
	public void move(Position newPosition) {
		this.position=newPosition;
	}
	@Override
	public boolean isPlayerControlled() {
	    return true; // played by the player
	}
	@Override
	public boolean canUseSpecial() {
	    return true;
	}
	@Override
	/**
	 * Performs a dash move that advances the Predator two cells forward
	 * and applies a cooldown to limit repeated use.
	 */
	public void useSpecial(GameState state) {
		if (!canUseSpecial()) return;
	    position.setX(position.getX() + 2);
	    specialCooldown = 2;
	}
}
