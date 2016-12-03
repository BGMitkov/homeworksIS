package TicTacToe;

import java.util.LinkedList;
import java.util.List;

public class Board {

	private int[][] state;
	private UtilityValue utilityValue;
	private List<Action> successors;

	public Board(int[][] state) {
		super();
		this.state = state;
		this.utilityValue = new UtilityValue();
		this.successors = successors();
		
	}

	public int[][] getState() {
		return state;
	}

	public void setState(int[][] state) {
		this.state = state;
	}

	public UtilityValue getUtilityValue() {
		return utilityValue;
	}

	public void setUtilityValue(UtilityValue utilityValue) {
		this.utilityValue = utilityValue;
	}
	
	public List<Action> getSuccessors() {
		return successors;
	}

	public void setSuccessors(List<Action> successors) {
		this.successors = successors;
	}

	public boolean crossedLine(int x1, int y1, int x2, int y2, int x3, int y3) {
		if (state[x1][y1] == state[x2][y2]) {
			if (state[x2][y2] == state[x3][y3]) {
				if (state[x1][y1] == 1) {
					utilityValue.setValue(1);
					utilityValue.setNumberOfMoves(9 - successors.size());
					return true;
				}
				if(state[x1][y1] == -1) {
					utilityValue.setValue(-1);
					utilityValue.setNumberOfMoves(9 - successors.size());
					return true;
				}
				
			}
		}
		return false;
	}
	
	public List<Action> successors() {
		LinkedList<Action> actions = new LinkedList<>();

		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				if (state[i][j] == 0) {
					actions.add(new Action(i, j));
				}
			}
		}

		return actions;
	}
}
