package characters;
import game.GameState;
import helpers.Role;
public abstract class Animal {
	protected Position position;
	protected Role role;
	protected int score;	
	protected String username;
	protected int specialCooldown = 0;
	/**
	 * 
	 * @param position starting position of the Predator on the grid
	 * @param role role of the animal
	 */
	public Animal(Position position,Role role) {
		this.position=position;
		this.role=role;
		this.score=0;
	}
	public Position getPosition() {
		return position;
	}
	public void setPosition(Position position) {
        this.position = position;
    }
	public Role getRole() {
        return role;
    }
	public int getScore() {
        return score;
    }
	public void increaseScore(int amount) {
        this.score += amount;
    }
	public String getUsername() {
	    return username;
	}
	public boolean canUseSpecial() {
	    return specialCooldown == 0;
	}
	public void tickCooldown() {
	    if (specialCooldown > 0) {
	        specialCooldown--;
	    }
	}
	/**
	 *  Moves the Animal to a new position on the grid
	 * @param newPosition position the animal will move to
	 */
	public abstract void move(Position newPosition);
	/**
	 * 
	 * @return true if is controlled by the player
	 */
	public abstract boolean isPlayerControlled();
	/**
	 * 
	 * @param state current game state used during the special action
	 */
	public abstract void useSpecial(GameState state);
}
