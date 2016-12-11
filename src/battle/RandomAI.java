package battle;

import java.util.ArrayList;
import java.util.Random;

public class RandomAI extends AI {

	public RandomAI(Board board, boolean self) {
		super(board, self);
	}
	
	public Direction nextStep(Direction d, boolean increase) {
		this.updateBoard(!first, increase, d);
		ArrayList<Direction> dirs = this.possibleSteps(first, board);
		if(dirs.isEmpty()){
			return Direction.Down;
		}
		Random rand = new Random();
		int ii = rand.nextInt() % dirs.size();
		return dirs.get(ii);
	}
}
