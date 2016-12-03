package TicTacToe;

public class UtilityValue implements Comparable<UtilityValue> {
	private int value;
	private int numberOfMoves;
	
	public UtilityValue(){
		
	}

	public UtilityValue(int value, int numberOfMoves) {
		super();
		this.value = value;
		this.numberOfMoves = numberOfMoves;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public int getNumberOfMoves() {
		return numberOfMoves;
	}

	public void setNumberOfMoves(int numberOfMoves) {
		this.numberOfMoves = numberOfMoves;
	}

	@Override
	public int compareTo(UtilityValue o) {
		if (this == o)
			return 0;
		if (this.value < o.value)
			return -1;
		if (this.value > o.value)
			return 1;
		return 0;
	}
}
