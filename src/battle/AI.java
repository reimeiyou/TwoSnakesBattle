
public class AI {
	private Board board;
	private Direction lastDir = null;
	
	public AI(Board board){
		this.board = board;
	}
	
	public void updateBoard(Direction d){
		
	}
	
	public Direction nextStep(boolean one, int depth){
		
		return Direction.Left;
	}
	
	protected int evaluation(){
		return 0;
	}
}
