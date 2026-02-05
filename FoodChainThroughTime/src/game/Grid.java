package game;
import characters.Position;
public class Grid {
	private int width;
	private int height;
	/**
	 * 
	 * @param width number of columns
	 * @param height number of rows
	 */
	public Grid(int width,int height) {
		this.width=width;
		this.height=height;
	}
	public int getWidth() {
		return width;
	}
	public int getHeight() {
		return height;
	}
	/**
	 * 
	 * @param position
	 * @return True if the position is inside the grid boundaries, false otherwise.
	 */
	public boolean Inside(Position position) {
		int x=position.getX();
		int y=position.getY();
		return x>=0 && x < width && y >= 0 && y < height;
	}
}
