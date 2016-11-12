package homework9;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.JFrame;

public class mainframe {
	static int snakeLength;
	static final int framelength = 25, framewidth = 20;
	static int waittime = 300;
	static int fx, fy;
	static int headx, heady;
	static int tailx, taily;
	static int toward;// 1-up,2-down,3-right,4-left
	static int state[][];// the state of each label
	public static boolean running;
	public static boolean pausing;
	static JFrame fr;
	static Panel photo, sts;
	static Random rd;
	static Button btn1, btn2, btn3;
	static Label lbl[][];

	static {
		snakeLength = 8;
		state = new int[framewidth][framelength];
		lbl = new Label[framewidth][framelength];
		fr = new JFrame("GluttonousSnake");
		photo = new Panel();
		sts = new Panel();
		btn1 = new Button("Start");
		btn2 = new Button("Pause");
		btn3 = new Button("Reset");
		photo.setLayout(new GridLayout(framewidth, framelength));
		photo.setSize(1600, 1000);
		for (int i = 0; i < framewidth; i++) {
			for (int j = 0; j < framelength; j++) {
				lbl[i][j] = new Label();
				lbl[i][j].setVisible(false);
				photo.add(lbl[i][j]);
			}

		}

		rd = new Random();
		fr.setLayout(new BorderLayout());
		btn1.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!running) {
					running = true;
					pausing = false;
				}
			}

		});
		btn2.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				if (!pausing) {
					pausing = true;
				} else {
					pausing = false;
				}
			}

		});
		btn3.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				init();
			}

		});
		btn1.addKeyListener(new KeyAdapter() {
			@Override
			public synchronized void keyPressed(KeyEvent e) {
				int mychar = e.getKeyCode();
				// System.out.println("what a fuck!!   " + mychar);
				if (running && !pausing)
					if (mychar == KeyEvent.VK_UP) {
						if (toward != 1 && toward != 2) {
							toward = 1;
							state[heady][headx] = 1;
						}

					} else if (mychar == KeyEvent.VK_DOWN) {
						if (toward != 1 && toward != 2) {
							toward = 2;
							state[heady][headx] = 2;
						}

					} else if (mychar == KeyEvent.VK_RIGHT) {
						if (toward != 3 && toward != 4) {
							toward = 3;
							state[heady][headx] = 3;
						}
					} else if (mychar == KeyEvent.VK_LEFT) {
						if (toward != 3 && toward != 4) {
							toward = 4;
							state[heady][headx] = 4;
						}

					}
			}
		});

		sts.add(btn1);
//		sts.add(btn2);
		sts.add(btn3);

		fr.add(photo, BorderLayout.CENTER);
		fr.add(sts, BorderLayout.SOUTH);
		fr.pack();
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		fr.setVisible(true);
	}

	public static void init() {
		running = false;
		pausing = false;
		toward = 3;

		for (int i = 0; i < framewidth; i++) {
			for (int j = 0; j < framelength; j++) {
				lbl[i][j].setBackground(Color.BLACK);
				lbl[i][j].setVisible(false);

				state[i][j] = 0;
			}

		}

		int starty = rd.nextInt(framewidth);

		for (int i = 0; i < 8; i++) {
			lbl[starty][i].setVisible(true);
			state[starty][i] = 3;
		}
		headx = 7;
		heady = starty;
		tailx = 0;
		taily = starty;

		fx = rd.nextInt(framelength);
		fy = rd.nextInt(framewidth);
		lbl[fy][fx].setBackground(Color.RED);
		lbl[fy][fx].setVisible(true);

	}

	public synchronized void move() {
		if (toward == 1) {
			heady--;
			if (heady == -1) {
				hitwall();
				return;
			} else if (state[heady][headx] != 0) {
				hithimself();
				return;
			}
			state[heady][headx] = 1;
			if (headx == fx && heady == fy) {
				setfood();
			} else
				nexttail();
		} else if (toward == 2) {
			heady++;
			if (heady == framewidth) {
				hitwall();
				return;
			} else if (state[heady][headx] != 0) {
				hithimself();
				return;
			}
			state[heady][headx] = 2;
			if (headx == fx && heady == fy) {
				setfood();
			} else
				nexttail();
		} else if (toward == 3) {
			headx++;
			if (headx == framelength) {
				hitwall();
				return;
			} else if (state[heady][headx] != 0) {
				hithimself();
				return;
			}
			state[heady][headx] = 3;
			if (headx == fx && heady == fy) {
				setfood();
			} else
				nexttail();
		} else {
			headx--;
			if (headx == -1) {
				hitwall();
				return;
			} else if (state[heady][headx] != 0) {
				hithimself();
				return;
			}
			state[heady][headx] = 4;
			if (headx == fx && heady == fy) {
				setfood();
			} else
				nexttail();
		}
		lbl[heady][headx].setVisible(true);
	}

	// public synchronized void playing() {

	// move();

	// if (toward == 1 && headx == fx && heady - 1 == fy) {
	// eat();
	// } else if (toward == 2 && headx == fx && heady + 1 == fy) {
	// eat();
	// } else if (toward == 3 && headx + 1 == fx && heady == fy) {
	// eat();
	// } else if (toward == 4 && headx - 1 == fx && heady == fy) {
	// eat();
	// }

	// }

	/*
	 * public synchronized void eat() { switch (toward) { case 1: heady--;
	 * break; case 2: heady++; break; case 3: headx++; break; default: headx--;
	 * break; } setfood(); }
	 */

	public synchronized void setfood() {
		snakeLength++;
		lbl[fy][fx].setBackground(Color.BLACK);
		// lbl[fy][fx].setVisible(false);
		do {

			fx = rd.nextInt(framelength);
			fy = rd.nextInt(framewidth);

		} while (state[fy][fx] != 0);
		lbl[fy][fx].setBackground(Color.RED);
		lbl[fy][fx].setVisible(true);

	}

	private synchronized void hithimself() {
		running = false;
		Dialog dl = new Dialog(fr, "You hit yourself!");
		dl.setVisible(true);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		dl.setVisible(false);

	}

	private synchronized void hitwall() {
		running = false;
		Dialog dl = new Dialog(fr, "You hit the wall!");
		dl.setVisible(true);
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
		}
		dl.setVisible(false);
	}

	private synchronized void nexttail() {
		switch (state[taily][tailx]) {
		case 1:
			state[taily][tailx] = 0;
			lbl[taily][tailx].setVisible(false);
			taily--;
			break;
		case 2:
			state[taily][tailx] = 0;
			lbl[taily][tailx].setVisible(false);
			taily++;
			break;
		case 3:
			state[taily][tailx] = 0;
			lbl[taily][tailx].setVisible(false);
			tailx++;
			break;
		case 4:
			state[taily][tailx] = 0;
			lbl[taily][tailx].setVisible(false);
			tailx--;
			break;
		}
	}
}
