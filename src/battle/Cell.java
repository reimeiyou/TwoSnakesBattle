package battle;

public class Cell {
	private int x;
	private int y;
	private boolean isSnake;
	private boolean isObstacle;
	
	public Cell(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isObstacle() { return isObstacle; }
	public void setObstacle(boolean isObstacle) { this.isObstacle = isObstacle; }
	
	public boolean isSnake() { return isSnake; }
	public void setSnake(boolean isSnake) { this.isSnake = isSnake; }
	
	public int getVerticalCoordinate() { return y; }
	public void setVerticalCoordinate(int y) { this.y = y; }
	
	public int getHorizontalCoordinate() { return x; }
	public void setHorizontalCoordinate(int x) { this.x = x; }
}
