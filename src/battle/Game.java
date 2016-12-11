package battle;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.Constants;

public class Game {
	JFrame frame;
	Board board;
	JPanel buttons;
	AI snake1AI, snake2AI;
	boolean running;
	int round;
	
	public Game(int x, int y, int numObstacles) {
		board = new Board(x, y, numObstacles);
		initGUI();
		initAI();
	}
	
	public static void main(String[] args) {
		int x = Constants.HEIGHT, y = Constants.WIDTH, numObstacles = Constants.NUM_OF_OBSTACLES;
		if (args.length == 4) {
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			numObstacles = Integer.parseInt(args[3]);
		}
		Game game = new Game(x, y, numObstacles);
		
		while(true){
			while (game.isRunning()) {
				// AI compute next direction for snake 1
				// AI compute next direction for snake 2
				// snake 1 move
				// snake 2 move
				
				// TODO check game over
				game.board.moveSnake(true, Direction.Right, false);
				game.board.moveSnake(false, Direction.Up, false);
				try {
					Thread.sleep(Constants.WAIT_TIME);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	private void initGUI() {
		frame = new JFrame("Snake Battle");
		frame.setLayout(new BorderLayout());
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(300, 200, 1200, 800);
		
		initButtons();	
		frame.add(board, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
	}
	
	public void initAI() {
		String[] AITypes = {Constants.ALPHA_BETA_AI, Constants.RANDOM_AI, Constants.THIRD_AI};
		String[] AILevels = {Constants.LEVEL_1, Constants.LEVEL_2, Constants.LEVEL_3};
		
		String snake1AIType = (String) JOptionPane.showInputDialog(frame, "Choose an AI that controls Snake 1", 
				"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AITypes, AITypes[0]);
		String snake1AILevel = (String) JOptionPane.showInputDialog(frame, "Choose the smartness of the AI that controls Snake 1", 
				"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AILevels, AILevels[0]);
		String snake2AIType = (String) JOptionPane.showInputDialog(frame, "Choose an AI that controls Snake 2", 
				"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AITypes, AITypes[0]);
		String snake2AILevel = (String) JOptionPane.showInputDialog(frame, "Choose the smartness of the AI that controls Snake 2", 
				"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AILevels, AILevels[0]);
		snake1AI = createAI(snake1AIType, snake1AILevel, board, true);
		snake2AI = createAI(snake2AIType, snake2AILevel, board, false);
	}
	
	public AI createAI(String AITYpe, String AILeve, Board board, boolean isFirst) {
		switch (AITYpe) {
		// TODO add AI level
			case Constants.ALPHA_BETA_AI:
				return new AlphaBetaAI(board, isFirst);
			case Constants.RANDOM_AI:
				return new RandomAI(board, isFirst);
			case Constants.THIRD_AI:
				return new AlphaBetaAI(board, isFirst); // TODO: change this when the third AI is ready
			default:
				return new AlphaBetaAI(board, isFirst);
		}
	}
	
	private void initButtons() {
		buttons = new JPanel();
		
		JButton start = new JButton("Start");
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!isRunning()) {
					setRunning(true);
					System.out.println("Start is pressed. Is game running? " + isRunning());
				}
			}
		});
		
		JButton pause = new JButton("Pause");
		pause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (isRunning()) {
					setRunning(false);
					System.out.println("Pause is pressed. Is game running? " + isRunning());
				}
			}
		});
		
		JButton reset = new JButton("Reset");
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Reset is pressed");
				setRunning(false);
				initAI();
				board.initCellSnakesObstacles();
				board.colorCellSnakesObstacles();
			}
		});
		
		buttons.add(start);
		buttons.add(pause);
		buttons.add(reset);
		frame.add(buttons, BorderLayout.SOUTH);
	}
	
	public boolean isRunning() {
		return running;
	}
	
	public void setRunning(boolean toRun) {
		running = toRun;
	}

}
