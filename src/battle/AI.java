package battle;

import java.util.ArrayList;

public class AI {
	protected Board board;
	protected boolean first;

	public AI(Board board, boolean self) {
		this.board = board;
		this.first = self;
	}

	public void updateBoard(boolean one, boolean increase, Direction d) {
		if (increase) {
			board.increaseSnake(one, d, false);
		} else {
			board.moveSnake(one, d, false);
		}
	}
	
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
