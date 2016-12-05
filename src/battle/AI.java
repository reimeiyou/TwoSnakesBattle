package battle;

import java.util.ArrayList;


public class AI {
	private Board board;
	private boolean first;

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

	public Direction nextStep(int depth) {
		SearchResult res = search(board, depth, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
		updateBoard(first, false, res.dir);
		return res.dir;
	}

	private ArrayList<Direction> possibleSteps(boolean one, Board board) {
		ArrayList<Direction> dirs = new ArrayList<Direction>();
		for (Direction d : Direction.values()) {
			if (board.increaseSnake(one, d, true)) {
				dirs.add(d);
			}
		}
		return dirs;
	}

	private int evaluation(Board bd) {
		return 0;
	}

	private SearchResult search(Board bd, int depth, boolean maxplayer, int alpha,
			int beta) {
		int value = 0;
		Direction res = Direction.Left;
		if (depth == 0) {
			return new SearchResult(res, evaluation(bd));
		}
		ArrayList<Direction> dirs = null;
		if (maxplayer) {
			dirs = possibleSteps(first, bd);
			value = Integer.MIN_VALUE;
			for (Direction d : dirs) {
				Board tmp = new Board(bd);
				tmp.moveSnake(first, d, false);
				int retval = search(tmp, depth - 1, false, alpha, beta).value;
				if(retval > value){
					res = d;
					value = retval;
				}
				alpha = Integer.max(alpha, value);
				if (beta <= alpha)
					break;
			}
		} else {
			dirs = possibleSteps(!first, bd);
			value = Integer.MAX_VALUE;
			for (Direction d : dirs) {
				Board tmp = new Board(bd);
				tmp.moveSnake(!first, d, false);
				int retval = search(tmp, depth - 1, true, alpha, beta).value;
				if(retval < value){
					res = d;
					value = retval;
				}
				beta = Integer.min(beta, value);
				if (beta <= alpha)
					break;
			}
		}
		return new SearchResult(res, value);
	}
}

class SearchResult{
	public Direction dir;
	public int value;
	public SearchResult(Direction d, int val){
		dir = d;
		value = val;
	}
}