package oldSnake;
public class Snake {
	public static void main(String args[]) {
		mainframe f = new mainframe();
		mainframe.init();
		while (true) {
			while (mainframe.running && !mainframe.pausing) {
				f.move();
	//			for(int i=0;i<mainframe.framewidth;i++){
	//				for(int j=0;j<mainframe.framelength;j++){
	//					System.out.print(""+mainframe.state[i][j]);
	//				}
	//				System.out.println();
	//			}
	//			System.out.println(mainframe.snakeLength);
				
				try {
					Thread.sleep(mainframe.waittime);
				} catch (InterruptedException e) {
				}
			}
		}
	}
}
