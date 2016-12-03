

import homeworks.Point;

import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Stack;

public class Astar2 {

	public static void main(String[] args) {
		Astar2 play = new Astar2();
		play.findGoal(START4s);

	}

	private static final int INITIAL_DISTANCE = 0;
	private static int[][] START = { { 6, 5, 3 }, { 2, 4, 8 }, { 7, 0, 1 } };
	private static int[][] START1 = { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 0 } };
	private static int[][] START2 = { { 3, 8, 4 }, { 1, 0, 5 }, { 7, 6, 2 } };
	private static int[][] HARDEST = { { 6, 4, 7 }, { 8, 5, 0 }, { 3, 2, 1 } };
	private static int[][] START4s = { { 15, 14, 12, 0 }, { 11, 13, 2, 8 },
			{ 7, 10, 6, 9 }, { 3, 5, 4, 1 } };
	private static int[][] START4s1 = { { 5, 1, 12, 3 }, { 14, 0, 2, 7 },
			{ 8, 10, 15, 4 }, { 9, 13, 6, 11 } };
	private static int[][] START5s1 = { { 1, 0, 7, 10, 4 },
			{ 14, 22, 3, 2, 5 }, { 19, 17, 6, 20, 9 }, { 12, 16, 8, 11, 15 },
			{ 21, 13, 18, 23, 24 } };

	private PriorityQueue<Board2> prqueue;
	private Stack<Character> path;
	private HashSet<Board2> visited;
	private int counter;

	public Astar2() {
		super();
		this.prqueue = new PriorityQueue<>();
		this.path = new Stack<>();
		this.visited = new HashSet<>();
		this.counter = 0;
	}

	public void findGoal(int[][] start) {
		Board2 root = new Board2('s', start, null, INITIAL_DISTANCE,
				findZero(start));

		prqueue.add(root);

		Board2 element = null;

		while (!prqueue.isEmpty()) {

			element = prqueue.remove();

			/*
			 * printState(element); System.out.println("Heuristic : " +
			 * element.getHeuristic());
			 */
			if (isGoal(element)) {
				printPath(element);
				return;
			}
			if (!visited.contains(element)) {
				counter++;
				element.setMoves();
				for (Board2 move : element.getMoves()) {
					prqueue.add(move);
				}
				visited.add(element);

			}
		}

	}

	private void printState(Board2 element) {
		for (int i = 0; i < element.getState().length; i++) {
			for (int j = 0; j < element.getState().length; j++) {
				System.out.print(element.getState()[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
	}

	private void printPath(Board2 element) {
		int distance = element.getDistance();

		while (element.getParrent() != null) {
			path.push(element.getDirection());
			element = element.getParrent();
		}

		while (!path.isEmpty()) {
			switch (path.pop()) {
			case 'l':
				System.out.println("left");
				break;
			case 'r':
				System.out.println("right");
				break;
			case 'u':
				System.out.println("up");
				break;
			case 'd':
				System.out.println("down");
				break;
			default:
				System.out.println("Beginning");
				break;
			}
		}
		System.out.println("Total distance : " + distance);

		System.out.println(counter);
	}

	private static boolean isGoal(Board2 element) {
		return element.getHeuristic() == 0;
	}

	private static Point findZero(int[][] start) {
		for (byte i = 0; i < start.length; i++) {
			for (byte j = 0; j < start.length; j++) {
				if (start[i][j] == 0) {
					System.out.printf("zero is at %d %d\n", i, j);
					return new Point(i, j);
				}
			}
		}
		return null;
	}

}
