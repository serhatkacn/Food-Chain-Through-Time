package characters;
import game.GameState;
import helpers.Role;
public class ApexPredator extends Animal {
	public ApexPredator(Position position) {
		super(position, Role.APEX_PREDATOR);
	}
	@Override
	public void move(Position newPosition) {
		this.position=newPosition;
	}
	@Override
	public boolean isPlayerControlled() {
	    return false; // played by ai
	}
	@Override
	public boolean canUseSpecial() {
	    return true;
	}
	@Override
	public void useSpecial(GameState state) {
	    if (!canUseSpecial()) return;
	    position.setX(position.getX() + 2);
	    specialCooldown = 3;
	}
}
