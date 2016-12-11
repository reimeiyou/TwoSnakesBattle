package battle;

import java.util.ArrayList;

public abstract class AI {
	protected Board board;
	protected boolean first;
	int depth;

	public AI(Board board, boolean self, int depth) {
		this.board = board;
		this.first = self;
		this.depth = depth;
		
	}

	public void updateBoard(boolean one, boolean increase, Direction d) {
		if (increase) {
			board.increaseSnake(one, d, false);
		} else {
			board.moveSnake(one, d, false);
		}
	}
	
	public abstract Direction nextStep(Direction d, boolean increase);
	
	protected ArrayList<Direction> possibleSteps(boolean one, Board board) {
		ArrayList<Direction> dirs = new ArrayList<Direction>();
		for (Direction d : Direction.values()) {
			if (board.increaseSnake(one, d, true)) {
				dirs.add(d);
			}
		}
		return dirs;
	}
}
