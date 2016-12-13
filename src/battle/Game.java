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
		round = 1;
		initGUI();
		initAI();
	}
	
	public boolean shouldIncrease() {
		return (round <= 10 || round % 3 == 0) ? true : false;
	}
	
	public static void main(String[] args) {
		int x = Constants.HEIGHT, y = Constants.WIDTH, numObstacles = Constants.NUM_OF_OBSTACLES;
		if (args.length == 4) {
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			numObstacles = Integer.parseInt(args[3]);
		}
		Game game = new Game(x, y, numObstacles);
		Direction snake1Direction = null, snake2Direction = null;
		boolean snake1 = false, snake2 = false;
		
		while(true){
			while (game.isRunning()) {	
				snake1Direction = game.snake1AI.nextStep(snake2Direction, game.shouldIncrease());
				snake2Direction = game.snake2AI.nextStep(snake1Direction, game.shouldIncrease());

				if (game.isRunning()) {
					snake1 = game.board.moveSnake(true, snake1Direction, false);
					if (game.checkGame(snake1, true)) {
						snake1Direction = null;
						snake2Direction = null;
					}
				}
				if (game.isRunning()) {
					snake2 = game.board.moveSnake(false, snake2Direction, false);
					if (game.checkGame(snake2, false)) {
						snake1Direction = null;
						snake2Direction = null;
					}
				}
				try {
					Thread.sleep(Constants.WAIT_TIME);
				} catch (InterruptedException e) {
				}
			}
		}
	}
	
	public boolean checkGame(boolean moveResult, boolean isFirst) {
		if (!moveResult) {
			setRunning(false);
			JOptionPane.showMessageDialog(frame, String.format("Game ends. Snake %s loses!", isFirst ? "1" : "2"));
			return true;
		}
		return false;
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
		String snake1AIType = null, snake1AILevel = null, snake2AIType = null, snake2AILevel = null;
		
		while (snake1AIType == null || snake1AILevel == null || snake2AIType == null || snake2AILevel == null) {
			JOptionPane.showMessageDialog(frame, "Please choose an AI and its smartness for each snake. All of them should be specified.");
			snake1AIType = (String) JOptionPane.showInputDialog(frame, "Choose an AI that controls Snake 1", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AITypes, AITypes[0]);
			snake1AILevel = (String) JOptionPane.showInputDialog(frame, "Choose the smartness of the AI that controls Snake 1", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AILevels, AILevels[0]);
			snake2AIType = (String) JOptionPane.showInputDialog(frame, "Choose an AI that controls Snake 2", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AITypes, AITypes[0]);
			snake2AILevel = (String) JOptionPane.showInputDialog(frame, "Choose the smartness of the AI that controls Snake 2", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AILevels, AILevels[0]);
			System.out.println("1 AI Type " + snake1AIType + " 1 AI Level " + snake1AILevel + " 2 AI Type " + snake2AIType + " 2 AI Level " + snake2AILevel);
		}
		snake1AI = createAI(snake1AIType, snake1AILevel, board, true);
		snake2AI = createAI(snake2AIType, snake2AILevel, board, false);
	}
	
	public AI createAI(String AITYpe, String AILevel, Board board, boolean isFirst) {
		int depth = Integer.parseInt(AILevel);
		switch (AITYpe) {
			case Constants.ALPHA_BETA_AI:
				return new AlphaBetaAI(board, isFirst, depth);
			case Constants.RANDOM_AI:
				return new RandomAI(board, isFirst, depth);
			case Constants.THIRD_AI:
				return new AlphaBetaAI(board, isFirst, depth); // TODO: change this when the third AI is ready
			default:
				return new AlphaBetaAI(board, isFirst, depth);
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
	
	private void setRunning(boolean toRun) {
		running = toRun;
	}

}
