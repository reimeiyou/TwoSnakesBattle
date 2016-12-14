package util;

public class AIStats {
	int numWin;
	int numLost;
	int numTie;
	int totalGames;
	long totalResponseTime;
	int searchCount;
	int depth;

	public AIStats(String depth) {
		this.depth = Integer.parseInt(depth);
		numWin = 0;
		numLost = 0;
		numTie = 0;
		totalGames = 0;
		searchCount = 0;
		totalResponseTime = 0l;
	}

	public void search() {
		searchCount++;
	}

	public void addResponseTime(Long duration) {
		totalResponseTime += duration;
	}

	public void win() {
		numWin++;
	}

	public void setTotalGames(int evalTotalGames) {
		totalGames = evalTotalGames;
	}

	public void lose() {
		numLost++;
	}

	public void tie() {
		numTie++;
	}

	public void finishGame() {
		totalGames++;
	}

	public String report() {
		return String
				.format("Win: %s/%s, lose: %s/%s, tie: %s/%s. AI search depth %s. Average AI response time: %sms",
						numWin, totalGames, numLost, totalGames, numTie,
						totalGames, depth, totalResponseTime / searchCount
								/ Constants.NANO_TO_MS);
	}
}
