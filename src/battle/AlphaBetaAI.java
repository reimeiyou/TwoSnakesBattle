package battle;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.ArrayDeque;

public class AlphaBetaAI extends AI {

	public AlphaBetaAI(Board board, boolean self, int depth) {
		super(board, self, depth);
	}

	public Direction nextStep(Direction d, boolean increase) {
		// if(d != null)
		// updateBoard(!first, increase, d);
		SearchResult res = search(board, depth, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
		// updateBoard(first, increase, res.dir);
		return res.dir;
	}

	/**
	 * Evaluation Function: the estimated value of a certain board #1. number of
	 * choices #2. reachable positions within steps #3. ???
	 */
	private int evaluation(Board bd) {
		int value1 = 0, value2 = 0;
		int hx1 = board.snake1.getHead().x, hy1 = board.snake2.getHead().y;
		int hx2 = board.snake2.getHead().x, hy2 = board.snake2.getHead().y;
		value1 = possibleMoves(new Coordinate(hx1, hy1), 3);
		value2 = possibleMoves(new Coordinate(hx2, hy2), 3);
		return value1 - value2;
	}

	private int possibleMoves(Coordinate c, int steps) {
		int counter = 0;
		ArrayDeque<Coordinate> queue = new ArrayDeque<Coordinate>();
		ArrayDeque<Integer> levels = new ArrayDeque<Integer>();
		HashSet<Coordinate> set = new HashSet<Coordinate>();
		queue.add(c);
		levels.add(0);
		set.add(c);
		while (!queue.isEmpty()) {
			Coordinate cur = queue.poll();
			int lvl = levels.poll();
			if (lvl >= steps)
				break;
			if (cur.x > 0 && board.board[cur.x - 1][cur.y] == CellType.Empty
					&& set.contains(new Coordinate(cur.x - 1, cur.y))) {
				counter++;
				queue.add(new Coordinate(cur.x - 1, cur.y));
				set.add(new Coordinate(cur.x - 1, cur.y));
				levels.add(lvl + 1);
			}
			if (cur.x < board.height - 1 && board.board[cur.x + 1][cur.y] == CellType.Empty
					&& set.contains(new Coordinate(cur.x + 1, cur.y))) {
				counter++;
				queue.add(new Coordinate(cur.x + 1, cur.y));
				set.add(new Coordinate(cur.x + 1, cur.y));
				levels.add(lvl + 1);
			}
			if (cur.y > 0 && board.board[cur.x][cur.y - 1] == CellType.Empty
					&& set.contains(new Coordinate(cur.x, cur.y - 1))) {
				counter++;
				queue.add(new Coordinate(cur.x, cur.y - 1));
				set.add(new Coordinate(cur.x, cur.y - 1));
				levels.add(lvl + 1);
			}
			if (cur.y > board.width - 1 && board.board[cur.x][cur.y + 1] == CellType.Empty
					&& set.contains(new Coordinate(cur.x, cur.y + 1))) {
				counter++;
				queue.add(new Coordinate(cur.x, cur.y + 1));
				set.add(new Coordinate(cur.x, cur.y + 1));
				levels.add(lvl + 1);
			}
		}
		return counter;
	}

	private SearchResult search(Board bd, int depth, boolean maxplayer, int alpha, int beta) {
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
				if (retval > value) {
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
				if (retval < value) {
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

class SearchResult {
	public Direction dir;
	public int value;

	public SearchResult(Direction d, int val) {
		dir = d;
		value = val;
	}
}