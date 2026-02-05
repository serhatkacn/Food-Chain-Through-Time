package characters;

public class Food {
	private Position position;
    private int value;
    /**
     * 
     * @param position position of the food on the grid
     * @param value score value gained when the food is eaten
     */   		
    public Food(Position position, int value) {
        this.position = position;
        this.value = value;
    }
    public Position getPosition() {
        return position;
    }
    public int getValue() {
        return value;
    }
}
