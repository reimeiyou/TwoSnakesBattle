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
	boolean running, paused;
	int round;
	int totalEvals;
	int finishedEval;
	
	public Game(int x, int y, int numObstacles) {
		board = new Board(x, y, numObstacles);
		round = 1;
		initGUI();
		initAI();
	}
	
	public boolean shouldIncrease() {
		return (round <= 10 || round % 3 == 0) ? true : false;
	}
	
	public int getNumOfTotalEvals() {
		return Integer.parseInt(JOptionPane.showInputDialog("Please type in the number of games you want to evaluate. Integers only"));
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
	
	public void finishedEvalIncrement() {
		finishedEval++;
	}
	
	public static void main(String[] args) {
		int x = Constants.HEIGHT, y = Constants.WIDTH, numObstacles = Constants.NUM_OF_OBSTACLES;
		if (args.length == 4) {
			x = Integer.parseInt(args[1]);
			y = Integer.parseInt(args[2]);
			numObstacles = Integer.parseInt(args[3]);
		}
		Game game = new Game(x, y, numObstacles);
		Direction snake1PrevDirection = null, snake2PrevDirection = null;
		Direction snake1NextDirection, snake2NextDirection;
		boolean snake1Moved = false, snake2Moved = false;
		long startTime = 0l;
		
		while(true){
			while (game.finishedEval < game.totalEvals) {
				while (game.isRunning() && !game.isPaused()) {	
					boolean increase = game.shouldIncrease();
					
					startTime = System.nanoTime();
					snake1NextDirection = game.snake1AI.nextStep(snake2PrevDirection, increase);
					game.AI1Stats.addResponseTime(System.nanoTime() - startTime);
					game.AI1Stats.search();
					
					startTime = System.nanoTime();
					snake2NextDirection = game.snake2AI.nextStep(snake1PrevDirection, increase);
					game.AI2Stats.addResponseTime(System.nanoTime() - startTime);
					game.AI2Stats.search();
					
					// simulate simultaneously move
					snake1Moved = increase ? 
							game.board.increaseSnake(true, snake1NextDirection, true) : 
							game.board.moveSnake(true, snake1NextDirection, true);
					snake2Moved = increase ? 
							game.board.increaseSnake(false, snake2NextDirection, true) : 
							game.board.moveSnake(false, snake2NextDirection, true);	
					
					if (snake1Moved && snake2Moved) {
						if (increase) {
							game.board.increaseSnake(true, snake1NextDirection, false);
							game.board.increaseSnake(false, snake2NextDirection, false);
						} else {
							game.board.moveSnake(true, snake1NextDirection, false);
							game.board.moveSnake(false, snake2NextDirection, false);	
						}
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
						game.finishedEvalIncrement();
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
				
				if (game.finishedEval > 0 && game.finishedEval < game.totalEvals) {
					game.setRunning(true);
				}
				
				if (game.finishedEval == game.totalEvals) {
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
		this.totalEvals = getNumOfTotalEvals();

		String[] AITypes = {Constants.ALPHA_BETA_AI, Constants.RANDOM_AI, Constants.BASIC_AI};
		String snake1AIType = null, snake1AILevel = null, snake2AIType = null, snake2AILevel = null;
		
		while (snake1AIType == null || snake1AILevel == null || snake2AIType == null || snake2AILevel == null) {
			JOptionPane.showMessageDialog(frame, "Please choose an AI and its smartness for each snake. All of them should be specified.");
			snake1AIType = (String) JOptionPane.showInputDialog(frame, "Choose an AI that controls Snake 1", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AITypes, AITypes[0]);
			snake1AILevel = getAILevel(snake1AIType);
			snake2AIType = (String) JOptionPane.showInputDialog(frame, "Choose an AI that controls Snake 2", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AITypes, AITypes[0]);
			snake2AILevel = getAILevel(snake2AIType);
			System.out.println("1 AI Type " + snake1AIType + " 1 AI Level " + snake1AILevel + " 2 AI Type " + snake2AIType + " 2 AI Level " + snake2AILevel);
		}
		snake1AI = createAI(snake1AIType, snake1AILevel, board, true);
		AI1Stats = new AIStats(snake1AILevel, totalEvals);
		snake2AI = createAI(snake2AIType, snake2AILevel, board, false);
		AI2Stats = new AIStats(snake2AILevel, totalEvals);
	}
	
	public String getAILevel(String AIType) {
		if (AIType.equals(Constants.ALPHA_BETA_AI)) {
			String[] AILevels = {Constants.LEVEL_1, Constants.LEVEL_2, Constants.LEVEL_3};
			return (String) JOptionPane.showInputDialog(frame, "Choose the smartness of the AI you just choosed", 
					"Choosing AI", JOptionPane.QUESTION_MESSAGE, null, AILevels, AILevels[0]);
		}
		return "0";
	}
	
	public AI createAI(String AITYpe, String AILevel, Board board, boolean isFirst) {
		int depth = Integer.parseInt(AILevel);
		switch (AITYpe) {
			case Constants.ALPHA_BETA_AI:
				return new AlphaBetaAI(board, isFirst, depth);
			case Constants.RANDOM_AI:
				return new RandomAI(board, isFirst, depth);
			case Constants.BASIC_AI:
				return new BasicAI(board, isFirst, depth);
			default:
				return new BasicAI(board, isFirst, depth);
		}
	}
	
	
	private void initButtons() {
		buttons = new JPanel();
		
		JButton start = new JButton("Start");
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!running) {
					running = true;
					paused = false;
					finishedEval = 0;
					AI1Stats.resetWinLostTieTimeSearchCount();
					AI2Stats.resetWinLostTieTimeSearchCount();
				}
			}
		});
		
		JButton pause = new JButton("Pause");
		pause.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
					running = false;
					paused = true;
			}
		});
		
		JButton reset = new JButton("Reset");
		reset.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				running = false;
				paused = false;
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
	
	public boolean isPaused() {
		return paused;
	}
	
	private void setRunning(boolean toRun) {
		running = toRun;
	}

}
