import java.util.Random;

public class Game {
	public static Board gameBoard;
	public static int round = 0;
	public static AI myai, othai;

	/**
	 * @param args
	 *            x, y, nObs
	 */
	public static void main(String[] args) {
		if (args.length != 4) {
			return;
		}
		int x = Integer.parseInt(args[1]), y = Integer.parseInt(args[2]), nObs = Integer
				.parseInt(args[3]);
		Random r = new Random();
		Coordinate[] obstacles = new Coordinate[nObs];
		boolean used[] = new boolean[x * y];
		for (int i = 0; i < nObs;) {
			int rn = r.nextInt(x * y);
			if (used[rn] || rn == 0 || rn == x * y - 1)
				continue;
			obstacles[i] = new Coordinate(rn / y, rn % y);
			used[rn] = true;
			i++;
		}
		gameBoard = new Board(x, y, obstacles);
		myai = new AI(gameBoard, true);
		othai = new User(gameBoard, false);
		while(true){
			
		}
	}

}
