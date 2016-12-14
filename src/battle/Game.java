package battle;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import util.AIStats;
import util.Constants;

public class Game {
	JFrame frame;
	Board board;
	JPanel buttons;
	AI snake1AI, snake2AI;
	AIStats AI1Stats, AI2Stats;
	boolean running;
	int round;
	int totalEvals;
	int finishedEval;
	
	public Game(int x, int y, int numObstacles, int totalEvals) {
		board = new Board(x, y, numObstacles);
		this.totalEvals = totalEvals;
//		finishedEval = 0;
		round = 1;
		initGUI();
		initAI();
	}
	
	public boolean shouldIncrease() {
		return (round <= 10 || round % 3 == 0) ? true : false;
	}
	
	/**
	 * rount += 1
	 */
	public void incrementRound() {
		round++;
	}
	
	public int getRound() {
		return round;
	}
	
	public void resetRound() {
		round = 0;
	}
	
	public void finishedGameIncrement() {
		finishedEval++;
	}
	
	public static void main(String[] args) {
		int x = Constants.HEIGHT, y = Constants.WIDTH, numObstacles = Constants.NUM_OF_OBSTACLES;
		if (args.length == 4) {
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			numObstacles = Integer.parseInt(args[3]);
		}
		int totalEvals = 5;
		Game game = new Game(x, y, numObstacles, totalEvals);
		Direction snake1PrevDirection = null, snake2PrevDirection = null;
		Direction snake1NextDirection, snake2NextDirection;
		boolean snake1Moved = false, snake2Moved = false;
		long startTime = 0l;
		game.AI1Stats.setTotalGames(totalEvals);
		game.AI2Stats.setTotalGames(totalEvals);
		
		while(true){
			while (game.finishedEval < totalEvals) {
				while (game.isRunning()) {	
					boolean increase = game.shouldIncrease();
					System.out.println(String.format(
							"In round %s, snake 1's length is %s, snake 2's length is %s. Increase? %s", 
							game.getRound(), game.board.snake1.getLength(),
							game.board.snake2.getLength(), increase ? "true" : "false"));
					
					startTime = System.nanoTime();
					snake1NextDirection = game.snake1AI.nextStep(snake2PrevDirection, increase);
					game.AI1Stats.addResponseTime(System.nanoTime() - startTime);
					game.AI1Stats.search();
					
					startTime = System.nanoTime();
					snake2NextDirection = game.snake2AI.nextStep(snake1PrevDirection, increase);
					System.out.println("Snake 1's next step " + snake1NextDirection.toString() + " Snake 2's next step " + snake2NextDirection.toString());
					game.AI2Stats.addResponseTime(System.nanoTime() - startTime);
					game.AI2Stats.search();
					
					snake1Moved = increase ? 
							game.board.increaseSnake(true, snake1NextDirection, false) : 
							game.board.moveSnake(true, snake1NextDirection, false);
					snake2Moved = increase ? 
							game.board.increaseSnake(false, snake2NextDirection, false) : 
							game.board.moveSnake(false, snake2NextDirection, false);	
					
					if (snake1Moved && snake2Moved) {
						snake1PrevDirection = snake1NextDirection;
						snake2PrevDirection = snake2NextDirection;
						game.incrementRound();
					} else {
						if (!snake1Moved && !snake2Moved) {
							game.showTieDialog();
							game.AI1Stats.tie();
							game.AI2Stats.tie();
						} else {
							if (!snake1Moved) {
								game.showLoseDialog(true);
								game.AI1Stats.lose();
								game.AI2Stats.win();
							} else {
								game.showLoseDialog(false);
								game.AI1Stats.win();
								game.AI2Stats.lose();
							}
						}
						game.finishedGameIncrement();
						System.out.println("=============== Finished game: " + game.finishedEval + " ==============");
						// reset game and board
						snake1PrevDirection = null;
						snake2PrevDirection = null;
						game.setRunning(false);
						game.resetRound();
						game.board.initCellSnakesObstacles();
						game.board.colorCellSnakesObstacles();
					}
					
					try {
						Thread.sleep(Constants.WAIT_TIME);
					} catch (InterruptedException e) {
					}
				}
				
				if (game.finishedEval > 0 && game.finishedEval < totalEvals) {
					game.setRunning(true);
				}
				
				if (game.finishedEval == totalEvals) {
					System.out.println(game.snake1AI.getName() + " " + game.AI1Stats.report());
					System.out.println(game.snake2AI.getName() + " " + game.AI2Stats.report());
				}
			}
		}
	}
	
	public void showTieDialog() {
		JOptionPane.showMessageDialog(frame, "Game ends and it was a tie.");
	}
	
	public void showLoseDialog(boolean isFirst) {
		JOptionPane.showMessageDialog(frame, String.format("Game ends. Snake %s loses!", isFirst ? "1" : "2"));
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
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AITypes, AITypes[1]);
			snake1AILevel = (String) JOptionPane.showInputDialog(frame, "Choose the smartness of the AI that controls Snake 1", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AILevels, AILevels[0]);
			snake2AIType = (String) JOptionPane.showInputDialog(frame, "Choose an AI that controls Snake 2", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AITypes, AITypes[1]);
			snake2AILevel = (String) JOptionPane.showInputDialog(frame, "Choose the smartness of the AI that controls Snake 2", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AILevels, AILevels[0]);
			System.out.println("1 AI Type " + snake1AIType + " 1 AI Level " + snake1AILevel + " 2 AI Type " + snake2AIType + " 2 AI Level " + snake2AILevel);
		}
		snake1AI = createAI(snake1AIType, snake1AILevel, board, true);
		AI1Stats = new AIStats(snake1AILevel);
		snake2AI = createAI(snake2AIType, snake2AILevel, board, false);
		AI2Stats = new AIStats(snake2AILevel);
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
					finishedEval = 0;
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
