package battle;

import java.util.ArrayList;
import util.Constants;
import java.util.HashSet;
import java.util.ArrayDeque;

public class AlphaBetaAI extends AI {

	public AlphaBetaAI(Board board, boolean self, int depth) {
		super(board, self, 3 * depth);
	}

	public Direction nextStep(Direction d, boolean increase) {
		// if(d != null)
		// updateBoard(!first, increase, d);
		SearchResult res = search(board, depth, true, Integer.MIN_VALUE,
				Integer.MAX_VALUE);
		// updateBoard(first, increase, res.dir);
		return res.dir;
	}

	/**
	 * Evaluation Function: the estimated value of a certain board #1. number of
	 * choices #2. reachable positions within steps #3. ???
	 */
	private int evaluation(Board bd, boolean firstplayer) {
		int value1 = 0, value2 = 0;
		value1 = possibleMoves(bd.snake1.getHead(), 3);
		value2 = possibleMoves(bd.snake2.getHead(), 3);
		// System.out.println("value1 " + value1 + " value2 " + value2);
		// board.print();
		if (firstplayer)
			return (value1 - value2) * 10 + positionValue(bd.snake1.getHead());
		return (value2 - value1) * 10 + positionValue(bd.snake2.getHead());
	}

	private int positionValue(Coordinate c) {
		return 2 * (Integer.min(board.height - c.x, c.x) + Integer.min(
				board.width - c.y, c.y));
	}

	private int possibleMoves(Coordinate c, int steps) {
		// System.out.println("---------");
		// board.print();
		// System.out.println("Head x: " + c.x + ", y: " + c.y);
		int counter = 0;
		ArrayDeque<Coordinate> queue = new ArrayDeque<Coordinate>();
		ArrayDeque<Integer> levels = new ArrayDeque<Integer>();
		HashSet<String> set = new HashSet<String>();
		queue.add(c);
		levels.add(0);
		set.add(c.toString());
		while (!queue.isEmpty()) {
			Coordinate cur = queue.pollFirst();
			int lvl = levels.pollFirst();
			if (lvl > steps)
				continue;
			counter++;
			// System.out.println(cur.x + " " + cur.y);
			if (cur.x > 0
					&& board.board[cur.x - 1][cur.y] == CellType.Empty
					&& !set.contains(new Coordinate(cur.x - 1, cur.y)
							.toString())) {
				queue.add(new Coordinate(cur.x - 1, cur.y));
				set.add(new Coordinate(cur.x - 1, cur.y).toString());
				levels.addLast(lvl + 1);
			}
			if (cur.x < board.height - 1
					&& board.board[cur.x + 1][cur.y] == CellType.Empty
					&& !set.contains(new Coordinate(cur.x + 1, cur.y)
							.toString())) {
				queue.add(new Coordinate(cur.x + 1, cur.y));
				set.add(new Coordinate(cur.x + 1, cur.y).toString());
				levels.addLast(lvl + 1);
			}
			if (cur.y > 0
					&& board.board[cur.x][cur.y - 1] == CellType.Empty
					&& !set.contains(new Coordinate(cur.x, cur.y - 1)
							.toString())) {
				queue.add(new Coordinate(cur.x, cur.y - 1));
				set.add(new Coordinate(cur.x, cur.y - 1).toString());
				levels.addLast(lvl + 1);
			}
			if (cur.y < board.width - 1
					&& board.board[cur.x][cur.y + 1] == CellType.Empty
					&& !set.contains(new Coordinate(cur.x, cur.y + 1)
							.toString())) {
				queue.add(new Coordinate(cur.x, cur.y + 1));
				set.add(new Coordinate(cur.x, cur.y + 1).toString());
				levels.addLast(lvl + 1);
			}
		}
		return counter;
	}

	private SearchResult search(Board bd, int depth, boolean maxplayer,
			int alpha, int beta) {
		System.out.println("Depth " + depth + " " + alpha + " " + beta);
		int value = 0;
		Direction res = Direction.Left;
		if (depth == 0) {
			value = evaluation(bd, first);
			// System.out.println("First snake " + first + " depth " + depth +
			// " value " + value);
			return new SearchResult(res, value);
		}
		ArrayList<Direction> dirs = null;
		if (maxplayer) {
			dirs = possibleSteps(first, bd);
			value = Integer.MIN_VALUE;
			for (Direction d : dirs) {
				Board tmp = new Board(bd);
				// TODO round information
//				System.out.println(first + "------MAX-----" + d.name()
//						+ "---DP---" + depth);
				// tmp.print();
				tmp.increaseSnake(first, d, false);
				// tmp.print();
				int retval = search(tmp, depth - 1, false, alpha, beta).value;
				if (retval > value) {
					res = d;
					value = retval;
				}
				alpha = Integer.max(alpha, value);
				// System.out.println("Depth " + depth + " " + alpha + " " +
				// beta);
				if (beta <= alpha)
					break;
			}
		} else {
			dirs = possibleSteps(!first, bd);
			value = Integer.MAX_VALUE;
			for (Direction d : dirs) {
				Board tmp = new Board(bd);
				// TODO round information
//				System.out.println(!first + "-----MIN-----" + d.name()
//						+ "---DP---" + depth);
				// tmp.print();
				tmp.increaseSnake(!first, d, false);
				// tmp.print();
				int retval = search(tmp, depth - 1, true, alpha, beta).value;
				if (retval < value) {
					res = d;
					value = retval;
				}
				beta = Integer.min(beta, value);
				// System.out.println("Depth " + depth + " " + alpha + " " +
				// beta);
				if (beta <= alpha)
					break;
			}
		}
		// System.out.println("First snake " + first + " depth " + depth +
		// " value " + value);
		return new SearchResult(res, value);
	}

	@Override
	public String getName() {
		return Constants.ALPHA_BETA_AI;
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