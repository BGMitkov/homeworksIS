package homeworks;

import homeworks.Point;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.Stack;

import homeworks.Board;

public class Astar {

	public static void main(String[] args) {

		Astar play = new Astar();
		play.findPath();
	}

	private static final byte INITIAL_DISTANCE = 0;
	private static final byte LEFT = 1;
	private static final byte RIGHT = 2;
	private static final byte UP = 3;
	private static final byte DOWN = 4;

	private int[][] BOARD;
	private PriorityQueue<Board> prqueue;
	private Stack<Byte> path;
	private HashSet<Board> visited;
	private Stack<Board> traces;
	private int n;

	public Astar() {
		super();
		this.prqueue = new PriorityQueue<>();
		this.path = new Stack<>();
		this.visited = new HashSet<>();
		this.traces = new Stack<>();
	}

	public void findPath() {

		Board element = null;
		Board previousElement = initializeRoot();
		if(previousElement == null) {
			System.out.println("Input is not valid!");
			return;
		}
		while (!prqueue.isEmpty()) {
			element = prqueue.remove();
			if (element.getParrent() == null ? false
					: (!(element.getParrent() == previousElement))) {
				revertState(element, previousElement);
			}
			swap(element);
			if (isGoal(element)) {
				printPath(element);
				return;
			}
			generateMoves(element);
			visited.add(element);
			previousElement = element;

		}

	}

	private Board initializeRoot() {
		BOARD = getInput();
		if (BOARD == null) {
			return null;
		}
		n = BOARD.length;
		Board root = new Board((byte) 0, INITIAL_DISTANCE, evaluateHeuristic(),
				null, findZero());
		root.setId(calculateId());
		generateMoves(root);
		visited.add(root);
		return root;
	}

	private int[][] getInput() {
		Scanner scan = new Scanner(System.in);

		System.out.print("Enter amount of blocks :");
		int N = (int) Math.sqrt(scan.nextInt() + 1);

		System.out.println("Enter blocks : ");
		int[][] boardFromConsole = new int[N][N];
		for (int i = 0; i < boardFromConsole.length; i++) {
			for (int j = 0; j < boardFromConsole.length; j++) {
				boardFromConsole[i][j] = scan.nextInt();
			}
		}
		scan.close();
		if (verifyInput(boardFromConsole)) {
			return boardFromConsole;
		}
		return null;
	}

	private boolean verifyInput(int[][] boardFromConsole) {
		int n = boardFromConsole.length * boardFromConsole.length;
		boolean[] checked = new boolean[n];
		for (int[] b : boardFromConsole) {
			for (int i : b) {
				if (i < n && i >= 0)
					checked[i] = true;
			}
		}
		for (boolean b : checked) {
			if (b == false)
				return false;
		}
		return true;
	}

	private void swap(Board element) {
		int temp = BOARD[element.getZero().getX()][element.getZero().getY()];
		BOARD[element.getZero().getX()][element.getZero().getY()] = BOARD[element
				.getParrent().getZero().getX()][element.getParrent().getZero()
				.getY()];
		BOARD[element.getParrent().getZero().getX()][element.getParrent()
				.getZero().getY()] = temp;
	}

	private byte evaluateHeuristic() {
		byte sum = 0;
		int value;
		Point place;
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				if (BOARD[x][y] != 0) {
					value = BOARD[x][y];
					place = getFinalPlace(value);
					sum += Math.abs(x - place.getX())
							+ Math.abs(y - place.getY());
				}
			}
		}
		return sum;
	}

	private Point getFinalPlace(int value) {
		byte[] place = new byte[2];
		if (value != 0) {
			place[0] = (byte) (value / BOARD.length);
			if (value % BOARD.length == 0) {
				place[0]--;
			}
			place[1] = (byte) (value - 1 - place[0] * BOARD.length);
		} else {
			place[0] = place[1] = (byte) (BOARD.length - 1);
		}
		return new Point(place[0], place[1]);
	}

	private void generateMoves(Board element) {
		Board move;
		if (element.getZero().getY() - 1 >= 0 && !isParent(element, RIGHT)) {
			Point left = new Point(element.getZero().getX(), (byte) (element
					.getZero().getY() - 1));

			move = new Board(LEFT, (byte) (element.getDistance() + 1),
					evaluateHeuristicFromParrent(element, left), element, left);
			swap(move);
			move.setId(calculateId());
			swap(move);
			if (!visited.contains(move)) {
				prqueue.add(move);
			}
		}
		if (element.getZero().getY() + 1 < n && !isParent(element, LEFT)) {

			Point right = new Point(element.getZero().getX(), (byte) (element
					.getZero().getY() + 1));

			move = new Board(RIGHT, (byte) (element.getDistance() + 1),
					evaluateHeuristicFromParrent(element, right), element,
					right);
			swap(move);
			move.setId(calculateId());
			swap(move);
			if (!visited.contains(move)) {
				prqueue.add(move);
			}
		}
		if (element.getZero().getX() - 1 >= 0 && !isParent(element, DOWN)) {
			Point up = new Point((byte) (element.getZero().getX() - 1), element
					.getZero().getY());

			move = new Board(UP, (byte) (element.getDistance() + 1),
					evaluateHeuristicFromParrent(element, up), element, up);

			swap(move);
			move.setId(calculateId());
			swap(move);
			if (!visited.contains(move)) {
				prqueue.add(move);
			}
		}
		if (element.getZero().getX() + 1 < n && !isParent(element, UP)) {
			Point down = new Point((byte) (element.getZero().getX() + 1),
					element.getZero().getY());

			move = new Board(DOWN, (byte) (element.getDistance() + 1),
					evaluateHeuristicFromParrent(element, down), element, down);

			swap(move);
			move.setId(calculateId());
			swap(move);
			if (!visited.contains(move)) {
				prqueue.add(move);
			}
		}

	}

	private int calculateId() {
		return Arrays.deepHashCode(BOARD);
	}

	private byte evaluateHeuristicFromParrent(Board element, Point move) {
		return (byte) (element.getHeuristic() - removeBlock(move) + addBlock(
				element, move));
	}

	private int addBlock(Board element, Point move) {
		Point place = getFinalPlace(BOARD[move.getX()][move.getY()]);
		int currentMove = (Math.abs(element.getZero().getX() - place.getX()) + Math
				.abs(element.getZero().getY() - place.getY()));
		return currentMove;
	}

	private int removeBlock(Point move) {
		Point place = getFinalPlace(BOARD[move.getX()][move.getY()]);
		int previousMove = (Math.abs(move.getX() - place.getX()) + Math
				.abs(move.getY() - place.getY()));
		return previousMove;
	}

	private boolean isParent(Board element, byte direction) {
		return element.getDirection() == direction;
	}

	private void revertState(Board element, Board previousElement) {
		Board newPreviousElement = previousElement;
		Board trackedElement = element;
		int differenceInDistance = newPreviousElement.getDistance()
				- trackedElement.getDistance();

		while (differenceInDistance > 0) {
			swap(newPreviousElement);
			newPreviousElement = newPreviousElement.getParrent();
			differenceInDistance--;

		}

		while (differenceInDistance < 0) {
			traces.add(trackedElement.getParrent());
			trackedElement = trackedElement.getParrent();
			differenceInDistance = differenceInDistance + 1;
		}

		while (newPreviousElement.getParrent() == null ? false
				: (trackedElement.getParrent() != newPreviousElement
						.getParrent())) {
			swap(newPreviousElement);
			newPreviousElement = newPreviousElement.getParrent();

			traces.add(trackedElement.getParrent());
			trackedElement = trackedElement.getParrent();
		}

		swap(newPreviousElement);
		while (!traces.empty()) {
			swap(traces.pop());
		}
	}

	private void printPath(Board element) {
		System.out.println("Optimal number of moves to order blocks: "
				+ element.getDistance());

		while (element.getParrent() != null) {
			path.push(element.getDirection());
			element = element.getParrent();
		}

		while (!path.isEmpty()) {
			switch (path.pop()) {
			case (byte) 1:
				System.out.println("left");
				break;
			case (byte) 2:
				System.out.println("right");
				break;
			case (byte) 3:
				System.out.println("up");
				break;
			case (byte) 4:
				System.out.println("down");
				break;
			default:
				System.out.println("Beginning");
				break;
			}
		}

	}

	private static boolean isGoal(Board element) {
		return element.getHeuristic() == 0;
	}

	private Point findZero() {
		for (byte i = 0; i < BOARD.length; i++) {
			for (byte j = 0; j < BOARD.length; j++) {
				if (BOARD[i][j] == 0) {
					return new Point(i, j);
				}
			}
		}
		return null;
	}

}
