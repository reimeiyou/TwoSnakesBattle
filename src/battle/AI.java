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
	
	public abstract String getName();
}
