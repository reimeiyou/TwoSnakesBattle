package battle;

import oldSnake.mainframe;
import util.Constants;

public class Game {
	static int round;

	public static void main(String[] args) throws InterruptedException {
		Board board = new Board();
		// TODO
		// ask if AI vs AI or AI vs person
		Snake snake1 = new Snake(board, 1);
		Snake snake2 = new Snake(board, 2);
		while (true) {
			// change condition to mainframe.running && !mainframe.pausing
			// after implementing pause and 
			while (true) {
				//snake1 search
				//snake2 search
				
				// snake1 move
				// snake2 move
				
				Thread.sleep(Constants.WAIT_TIME);
			}
		}

	}

}
