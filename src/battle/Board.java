package battle;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class Board extends JPanel{
	private static final long serialVersionUID = 1L;
	Snake snake1, snake2;
	CellType[][] board;
	private JLabel[][] labels;
	int height, width;
	private int numObstacles;

	/**
	 * @param height
	 *            the length on x coordinate
	 * @param width
	 *            the length on y coordinate
	 * @param obstacles
	 *            the forbidden position
	 */
	public Board(int height, int width, int numObstacles) {
		super(new GridLayout(height, width));
		setPreferredSize(new Dimension(600, 400));		
		this.height = height;
		this.width = width;
		this.numObstacles = numObstacles;
		initCellSnakesObstacles();
		initLabels();
		colorCellSnakesObstacles();
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
	
	void initCellSnakesObstacles() {
		initCell();
		initSnakes();
		initObstacles();
	}
	
	private void initCell() {
		board = new CellType[height][width];		
		for(CellType[] item : board){
			Arrays.fill(item, CellType.Empty);
		}
	}
	
	private void initSnakes() {
		snake1 = new Snake(new Coordinate(0, 0));
		snake2 = new Snake(new Coordinate(height - 1, width - 1));
		board[0][0] = CellType.Snake1;
		board[height - 1][width - 1] = CellType.Snake2;
	}
	
	private void initObstacles() {
		int count = 0, totalCoord = height * width;
		HashSet<Integer> occupied = new HashSet<Integer>();
		occupied.add(0);
		occupied.add(totalCoord - 1);
		Random random = new Random();
		
		while (count < numObstacles) {
			int coordinate = random.nextInt(totalCoord);
			if (!occupied.contains(coordinate)) {
				occupied.add(coordinate);
				board[coordinate / width][coordinate % width] = CellType.Obstacle;
				count++;
			}
		}
	}

	private void initLabels() {
		labels = new JLabel[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				labels[i][j] = new JLabel();
				labels[i][j].setOpaque(true);
				labels[i][j].setVisible(true);
				this.add(labels[i][j]);
			}
		}
	}
	
	void colorCellSnakesObstacles() {		
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				switch (board[i][j]) {
					case Snake1:
						paintSnake1(i, j);
						break;
					case Snake2:
						paintSnake2(i, j);
						break;
					case Obstacle:
						paintObstacle(i, j);
						break;
					default:
						paintDefault(i, j);
						break;
					}
				labels[i][j].setVisible(true);
			}
		}	
	}
	
	private void paintSnake1(int x, int y) {
		labels[x][y].setBackground(new Color(255,99,71)); // tomato red
	}
	
	private void paintSnake2(int x, int y) {
		labels[x][y].setBackground(new Color(65,105,225)); // royal blue
	}
	
	private void paintObstacle(int x, int y) {
		labels[x][y].setBackground(new Color(0, 0, 0)); // black
	}
	
	private void paintDefault(int x, int y) {
		labels[x][y].setBackground(new Color(220, 220, 220)); // grey
	}

	/**
	 * 1. Move snakes according to designated direction 2. Update the
	 * information on board
	 * 
	 * @param one
	 *            The first snake?
	 * @param direction
	 *            Move to that direction
	 */
	public boolean increaseSnake(boolean one, Direction direction, boolean test) {
		int x = board.length, y = board[0].length, headX = 0, headY = 0;
		CellType headCellType = CellType.Empty;
		if (one) {
			headX = snake1.getHead().x;
			headY = snake1.getHead().y;
			headCellType = CellType.Snake1;
		} else {
			headX = snake2.getHead().x;
			headY = snake2.getHead().y;
			headCellType = CellType.Snake2;
		}
		switch (direction) {
			case Up:
				if (headX <= 0 || board[headX - 1][headY] != CellType.Empty) {
					return false;
				}
				if(test) return true;
				headX--;
				break;
			case Down:
				if (headX >= x - 1 || board[headX + 1][headY] != CellType.Empty) {
					return false;
				}
				if(test) return true;
				headX++;
				break;
			case Left:
				if (headY <= 0 || board[headX][headY - 1] != CellType.Empty) {
					return false;
				}
				if(test) return true;
				headY--;
				break;
			case Right:
				if (headY >= y - 1 || board[headX][headY + 1] != CellType.Empty) {
					return false;
				}
				if(test) return true;
				headY++;
				break;
			default:
				return false;
		}
		board[headX][headY] = headCellType;
		if (one) {
			snake1.increment(direction);
			paintSnake1(headX, headY);
		} else {
			snake2.increment(direction);
			paintSnake2(headX, headY);
		}
		return true;
	}

	public boolean moveSnake(boolean one, Direction direction, boolean test) {
		if (!increaseSnake(one, direction, test)) {
			return false;
		}
		if(test) return true;
		int tailX = 0, tailY = 0;
		if (one) {
			tailX = snake1.getTail().x;
			tailY = snake1.getTail().y;
			snake1.removeTail();
		} else {
			tailX = snake2.getTail().x;
			tailY = snake2.getTail().y;
			snake2.removeTail();
		}
		board[tailX][tailY] = CellType.Empty;
		paintDefault(tailX, tailY);
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
		int x = 5, y = 6, numObstacles = 5;
		Board bd = new Board(x, y, numObstacles);
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

	public void increment(Direction direction) {
		Coordinate head = getHead();
		int headx = head.x, heady = head.y;
		switch (direction) {
			case Up:
				headx--;
				break;
			case Down:
				headx++;
				break;
			case Left:
				heady--;
				break;
			case Right:
				heady++;
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

enum CellType {
	Empty, Snake1, Snake2, Obstacle;
}

enum Direction {
	Left, Right, Up, Down;
}