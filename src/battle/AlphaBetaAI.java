package battle;

import java.util.ArrayList;


public class AlphaBetaAI extends AI {

	public AlphaBetaAI(Board board, boolean self, int depth) {
		super(board, self, depth);
	}

	public Direction nextStep(Direction d, boolean increase) {
//		if(d != null)
//			updateBoard(!first, increase, d);
		SearchResult res = search(board, depth, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
//		updateBoard(first, increase, res.dir);
		return res.dir;
	}

	/**
	 * Evaluation Function:
	 *     the estimated value of a certain board 
	 * */
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