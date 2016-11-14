package battle;

import java.util.LinkedList;

public class Snake {
	int length;
	LinkedList<Cell> snakeBody;
	Board board;
	
	public Snake(Board board, int count) {
		length = 1;
		this.board = board;
		snakeBody = new LinkedList<>();
		// TODO add initial position given count
	}
	
	public void grow(int round) {
		
	}
	
	public Direction search() {
		// TODO
		return null;
	}
	
	
	public void move(Direction direction) {
		Cell head = snakeBody.getFirst();
		switch (direction) {
			case Up:
				// TODO
				break;
			case Down:
				// TODO
				break;
			case Left:
				// TODO
				break;
			case Right:
				// TODO
				break;
		}
	}
	/**
	 * Given the current position of the head and the intended move direction,
	 * return false if the snake will hit the wall, or hit the body either of itself
	 * or of the other snake, or return true if the snake can live this round
	 * @param head, direction
	 */
	public boolean isValidDirection(int x, int y, Board board) {
		// TODO
		return false;
	}
}
