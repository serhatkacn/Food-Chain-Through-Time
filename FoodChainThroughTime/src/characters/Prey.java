package characters;
import game.GameState;
import helpers.Role;
public class Prey extends Animal{
	public Prey(Position position) {
		super(position, Role.PREY);		
	}
	@Override
	public void move(Position newPosition) {
		this.position=newPosition;
	}	
	@Override
	public boolean isPlayerControlled() {
	    return false;
	}
	@Override
	public boolean canUseSpecial() {
	    return false;
	}
	@Override
	public void useSpecial(GameState state) {
		
	}
}
