package battle;


import util.Constants;


public class Game {
	public static Board gameBoard;
	public static int round = 0;
	public static AI snake1AI, snake2AI;

	/**
	 * @param args
	 *            x, y, nObs
	 */
	public static void main(String[] args) {
		int x = Constants.HEIGHT, y = Constants.WIDTH, numObstacles = Constants.NUM_OF_OBSTACLES;
		if (args.length == 4) {
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			numObstacles = Integer.parseInt(args[3]);
		}
		
		gameBoard = new Board(x, y, numObstacles);

//		snake1AI = createAI(snake1AIName, gameBoard, true);
//		snake2AI = createAI(snake2AIName, gameBoard, false);

		while(true){
			while (gameBoard.isRunning()) {
				// AI compute next direction for snake 1
				// AI compute next direction for snake 2
				// snake 1 move
				// snake 2 move
				gameBoard.moveSnake(true, Direction.Right, false);
				gameBoard.moveSnake(false, Direction.Up, false);
				try {
					Thread.sleep(Constants.WAIT_TIME);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
