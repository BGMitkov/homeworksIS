package TicTacToe;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Game {

	public static void main(String[] args) {
		Game game = new Game();
		game.play();
	}

	private Board board;

	public void play() {
		this.board = new Board(new int[3][3]);
		Scanner scanner = new Scanner(System.in);

		System.out.println("Would you like to be first? (y/n)");
		char order;
		try {
			order = (char) System.in.read();
			if (order == 'y') {
				playerGoesFirst(scanner);
			} else if (order == 'n') {
				computerGoesFirst(scanner);
			}
		} catch (IOException e) {
			
		}
		
		printEndGame();
		scanner.close();
	}

	private void computerGoesFirst(Scanner scanner) {

		while (!isTerminal(board)) {

			computerTurn();

			if (isTerminal(board)) {
				break;
			}

			while (playerTurn(board, scanner)) {
			}
		}
	}

	private void playerGoesFirst(Scanner scanner) {

		while (!isTerminal(board)) {

			while (playerTurn(board, scanner)) {
			}
			
			if (isTerminal(board)) {

				break;
			}
			computerTurn();
			
		}

	}

	private void computerTurn() {
		System.out.println("Computer move...");

		Action computerAction = minimaxDecision(board);

		System.out.printf("Computer made a move : %d %d\n", computerAction.getX(), computerAction.getY());

		board.getState()[computerAction.getX()][computerAction.getY()] = 1;
		board.setSuccessors(board.successors());
		board.setUtilityValue(new UtilityValue());
		
		printBoard();
	}

	private void printEndGame() {
		if (board.getUtilityValue().getValue() == -1) {
			System.out.println("Congratz you won!");
		} else if (board.getUtilityValue().getValue() == 1) {
			System.out.println("You lost!");
		} else {
			System.out.println("It's a drow.");
		}
	}

	private void printBoard() {
		int[][] state = board.getState();
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < state.length; j++) {
				printState(state[i][j]);
			}
			System.out.println();
		}
	}

	private void printState(int i) {
		if(i == 1) {
			System.out.print("x ");
		} else if( i == -1 ){
			System.out.print("o ");
		} else {
			System.out.print("_ ");
		}
			
		
	}

	private boolean playerTurn(Board board2, Scanner scanner) {
		System.out.print("Make a move : ");
		int x = scanner.nextInt();
		int y = scanner.nextInt();

		if (notValidMove(x, y)) {
			System.out.println("Invalid move!");
			return true;
		}

		board2.getState()[x][y] = -1;
		board2.setSuccessors(board2.successors());
		board2.setUtilityValue(new UtilityValue());

		printBoard();
		return false;
	}

	private boolean notValidMove(int x, int y) {
		if ((x > board.getState().length - 1 || x < 0) && (y > board.getState().length - 1 || y < 0)) {
			return true;
		}
		if (board.getState()[x][y] == 0) {
			return false;
		}
		return true;
	}

	private Action minimaxDecision(Board state) {

		List<Action> actions = state.getSuccessors();

		for (Action a : actions) {
			a.setUtilityValue(minValue(result(a, state, true)));
		}

		return maximalAction(actions);
	}

	private Action maximalAction(List<Action> actions) {
		Action maximalAction = actions.get(0);
		for (Action action : actions) {
			if (action.getUtilityValue() == max(maximalAction.getUtilityValue(), action.getUtilityValue())) {
				maximalAction = action;
			}
		}
		return maximalAction;
	}

	private UtilityValue max(UtilityValue v, UtilityValue minValue) {
		int compareResult = v.compareTo(minValue);
		if (compareResult == 1) {
			// TODO if

			return v;
		}
		if (compareResult == -1) {
			return minValue;
		}
		if (v.getValue() == 0) {
			return v;
		}
		if (v.getValue() == -1) {
			if (v.getNumberOfMoves() < minValue.getNumberOfMoves()) {
				return minValue;
			}
			return v;
		}
		if (v.getValue() == 1) {
			if (v.getNumberOfMoves() < minValue.getNumberOfMoves()) {
				return v;
			}
			return minValue;
		}
		return v;
	}

	private Board result(Action a, Board state, boolean isComputer) {
		int[][] resultState = copy(state.getState());

		if (isComputer) {
			resultState[a.getX()][a.getY()] = 1;
		} else {
			resultState[a.getX()][a.getY()] = -1;
		}

		Board result = new Board(resultState);

		return result;
	}

	private int[][] copy(int[][] state) {
		int[][] copy = new int[state.length][state.length];
		for (int i = 0; i < state.length; i++) {
			for (int j = 0; j < copy.length; j++) {
				copy[i][j] = state[i][j];
			}
		}
		return copy;
	}

	private UtilityValue maxValue(Board state) {
		if (isTerminal(state)) {
			return state.getUtilityValue();
		}
		UtilityValue v = new UtilityValue(-100, 0);
		for (Action action : state.getSuccessors()) {
			Board nextState = result(action, state, true);
			v = max(v, minValue(nextState));
		}
		return v;
	}

	private boolean isTerminal(Board board) {

		if (board.crossedLine(0, 0, 0, 1, 0, 2)) {
			// System.out.println("At first row");
			return true;
		}
		if (board.crossedLine(1, 0, 1, 1, 1, 2)) {
			// System.out.println("At 2nd row");
			return true;
		}
		if (board.crossedLine(2, 0, 2, 1, 2, 2)) {
			// System.out.println("At 3rd row");
			return true;
		}
		if (board.crossedLine(0, 0, 1, 0, 2, 0)) {
			// System.out.println("At first col");
			return true;
		}
		if (board.crossedLine(0, 1, 1, 1, 2, 1)) {
			// System.out.println("At 2nd col");
			return true;

		}
		if (board.crossedLine(0, 2, 1, 2, 2, 2)) {
			// System.out.println("At 3rd col");
			return true;
		}
		if (board.crossedLine(0, 0, 1, 1, 2, 2)) {
			// System.out.println("At first diagonal");
			return true;
		}
		if (board.crossedLine(2, 0, 1, 1, 0, 2)) {
			// System.out.println("At second diagonal");
			return true;
		}

		if (board.getSuccessors().isEmpty()) {
			// System.out.println("Nowhere");
			board.setUtilityValue(new UtilityValue(0, 9));
			return true;
		}

		return false;
	}

	private UtilityValue minValue(Board state) {
		if (isTerminal(state)) {
			return state.getUtilityValue();
		}
		UtilityValue v = new UtilityValue(100, 0);
		for (Action action : state.getSuccessors()) {
			Board nextState = result(action, state, false);
			v = min(v, maxValue(nextState));
		}
		return v;
	}

	private UtilityValue min(UtilityValue v, UtilityValue maxValue) {
		int compareResult = v.compareTo(maxValue);
		if (compareResult == -1) {
			return v;
		}
		if (compareResult == 1) {
			return maxValue;
		}
		if (v.getValue() == 0) {
			return v;
		}
		if (v.getValue() == -1) {
			if (v.getNumberOfMoves() > maxValue.getNumberOfMoves()) {
				return maxValue;
			}
			return v;
		}
		if (v.getValue() == 1) {
			if (v.getNumberOfMoves() > maxValue.getNumberOfMoves()) {
				return v;
			}
			return maxValue;
		}
		return v;
	}
}
