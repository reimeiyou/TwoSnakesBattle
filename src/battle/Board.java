import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;

public class Board {
	Snake snake1, snake2;

	private Cell[][] board;

	/**
	 * @param x
	 *            the length on x coordinate
	 * @param y
	 *            the length on y coordinate
	 * @param obstacles
	 *            the forbidden position
	 */
	public Board(int x, int y, Coordinate[] obstacles) {
		board = new Cell[x][y];
		for(Cell[] item : board){
			Arrays.fill(item, Cell.Empty);
		}
		snake1 = new Snake(new Coordinate(0, 0));
		snake2 = new Snake(new Coordinate(x - 1, y - 1));
		board[0][0] = Cell.Snake1;
		board[x - 1][y - 1] = Cell.Snake2;
		for (Coordinate item : obstacles) {
			board[item.x][item.y] = Cell.Obstacle;
		}
	}
	
	public Board(Board bd){
		int x = bd.board.length, y = bd.board[0].length;
		for(int i = 0; i < x; i++){
			for(int j = 0; j < y; j++){
				board[i][j] = bd.board[i][j];
			}
		}
		snake1 = new Snake(bd.snake1);
		snake2 = new Snake(bd.snake2);
	}

	/**
	 * 1. Move snakes according to designated direction 2. Update the
	 * information on board
	 * 
	 * @param one
	 *            The first snake?
	 * @param d
	 *            Move to that direction
	 */
	public boolean increaseSnake(boolean one, Direction d, boolean test) {
		int x = board.length, y = board[0].length, hx = 0, hy = 0;
		Cell which = Cell.Empty;
		if (one) {
			hx = snake1.getHead().x;
			hy = snake1.getHead().y;
			which = Cell.Snake1;
		} else {
			hx = snake2.getHead().x;
			hy = snake2.getHead().y;
			which = Cell.Snake2;
		}
		switch (d) {
		case Up:
			if (hy >= y - 1 || board[hx][hy + 1] != Cell.Empty) {
				return false;
			}
			if(test) return true;
			board[hx][hy + 1] = which;
			break;
		case Down:
			if (hy <= 0 || board[hx][hy - 1] != Cell.Empty) {
				return false;
			}
			if(test) return true;
			board[hx][hy - 1] = which;
			break;
		case Left:
			if (hx <= 0 || board[hx - 1][hy] != Cell.Empty) {
				return false;
			}
			if(test) return true;
			board[hx - 1][hy] = which;
			break;
		case Right:
			if (hx >= x - 1 || board[hx + 1][hy] != Cell.Empty) {
				return false;
			}
			if(test) return true;
			board[hx + 1][hy] = which;
			break;
		default:
			return false;
		}
		if (one) {
			snake1.increment(d);
		} else {
			snake2.increment(d);
		}
		return true;
	}

	public boolean moveSnake(boolean one, Direction d, boolean test) {
		if (!increaseSnake(one, d, test)) {
			return false;
		}
		if(test) return true;
		int tx = 0, ty = 0;
		if (one) {
			tx = snake1.getTail().x;
			ty = snake1.getTail().y;
			snake1.removeTail();
		} else {
			tx = snake2.getTail().x;
			ty = snake2.getTail().y;
			snake2.removeTail();
		}
		board[tx][ty] = Cell.Empty;
		return true;
	}

	public void print() {
		for (int j = board[0].length - 1; j >= 0; j--) {
			for (int i = 0; i < board.length; i++) {
				System.out.print(board[i][j].ordinal() + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	public static void main(String[] args) {
		Coordinate[] obs = new Coordinate[5];
		int x = 5, y = 6, nextx = 0, nexty = 0;
		Random r = new Random();
		boolean[][] b = new boolean[x][y];
		b[0][0] = true;
		b[x - 1][y - 1] = true;
		for (int i = 0; i < obs.length;) {
			nextx = r.nextInt(x);
			nexty = r.nextInt(y);
			if (b[nextx][nexty])
				continue;
			b[nextx][nexty] = true;
			obs[i] = new Coordinate(nextx, nexty);
			i++;
		}
		Board bd = new Board(x, y, obs);
		bd.print();
		bd.increaseSnake(false, Direction.Right, false);
		bd.increaseSnake(false, Direction.Right, false);
		bd.increaseSnake(false, Direction.Up, false);
		bd.increaseSnake(false, Direction.Left, false);
		bd.print();
	}
}

class Snake {
	private LinkedList<Coordinate> body;

	public Snake(Coordinate c) {
		body = new LinkedList<Coordinate>();
		body.add(c);
	}
	
	public Snake(Snake s){
		this.body = new LinkedList<Coordinate>(s.body);
	}

	public Coordinate getHead() {
		return body.peekFirst();
	}

	public Coordinate getTail() {
		return body.peekLast();
	}

	public void increment(Direction d) {
		Coordinate h = getHead();
		int headx = h.x, heady = h.y;
		switch (d) {
		case Up:
			heady++;
			break;
		case Down:
			heady--;
			break;
		case Left:
			headx--;
			break;
		case Right:
			headx++;
			break;
		}
		body.addFirst(new Coordinate(headx, heady));
	}

	public void removeTail() {
		body.removeLast();
	}
}

class Coordinate {
	int x, y;

	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}
}

enum Cell {
	Empty, Snake1, Snake2, Obstacle;
}

enum Direction {
	Left, Right, Up, Down;
}