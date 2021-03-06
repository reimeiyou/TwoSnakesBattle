package battle;

import java.util.ArrayList;
import java.util.Random;

import util.Constants;

public class RandomAI extends AI {

	public RandomAI(Board board, boolean self, int depth) {
		super(board, self, depth);
	}
	
	public Direction nextStep(Direction d, boolean increase) {
		ArrayList<Direction> dirs = this.possibleSteps(first, board);
		if(dirs.isEmpty()){
			return Direction.Down;
		}
		Random rand = new Random();
		int ii = (rand.nextInt() % dirs.size() + dirs.size()) % dirs.size();
		return dirs.get(ii);
	}

	@Override
	public String getName() {
		return Constants.RANDOM_AI;
	}
}
