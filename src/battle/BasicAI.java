package battle;

import java.util.ArrayList;

import util.Constants;

public class BasicAI extends AI {
	public BasicAI(Board board, boolean self, int depth) {
		super(board, self, depth);
	}

	public Direction nextStep(Direction d, boolean increase) {
//		if (d != null)
//			this.updateBoard(!first, increase, d);
		ArrayList<Direction> dirs = this.possibleSteps(first, board);
		if (dirs.isEmpty()) {
			return Direction.Down;
		}
		int maxv = 0;
		Direction maxd = null;
		for (Direction direction : dirs) {
			int obsDis = computeObsDis(direction);
			if(obsDis > maxv){
				maxv = obsDis;
				maxd = direction;
			}
		}
		return maxd;
	}

	private int computeObsDis(Direction d) {
		int headx = 0, heady = 0, dis = 0;
		if (this.first) {
			headx = board.snake1.getHead().x;
			heady = board.snake1.getHead().y;
		} else {
			headx = board.snake2.getHead().x;
			heady = board.snake2.getHead().y;
		}
		while (true) {
			switch (d) {
			case Up:
				headx--;
				break;
			case Down:
				headx++;
				break;
			case Right:
				heady++;
				break;
			case Left:
				heady--;
				break;
			default:
				break;
			}
			if (headx >= board.height || headx < 0 || heady < 0 || heady >= board.width
					|| board.board[headx][heady] != CellType.Empty) {
				break;
			}
			dis++;
		}
		return dis;
	}

	@Override
	public String getName() {
		return Constants.BASIC_AI;
	}
}
