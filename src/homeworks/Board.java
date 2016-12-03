package homeworks;

import homeworks.Point;

public class Board implements Comparable<Board> {

	/*
	 * private static final char LEFT = 'l'; private static final char RIGHT =
	 * 'r'; private static final char UP = 'u'; private static final char DOWN =
	 * 'd';
	 */
	/*
	 * private static int[][] state; private static int[][] goal;
	 */

	private byte direction;
	private byte distance;
	private byte evaluationfunction;
	private byte heuristic;
	private int id;

	private Board parrent;
	private Point zero;

	public Board(byte direction, byte distance, byte heuristic, Board parrent,
			Point point) {
		super();
		this.direction = direction;
		this.distance = distance;
		this.heuristic = heuristic;
		this.parrent = parrent;
		this.zero = point;
		this.evaluationfunction = (byte) (distance + heuristic);
	}

	public byte getDirection() {
		return direction;
	}

	public byte getDistance() {
		return distance;
	}

	public byte getEvaluationFunction() {
		return evaluationfunction;
	}

	public void setEvaluationFunction(byte f) {
		this.evaluationfunction = f;
	}

	public int getHeuristic() {
		return heuristic;
	}

	public void setHeuristic(byte heuristic) {
		this.heuristic = heuristic;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Board getParrent() {
		return parrent;
	}

	public Point getZero() {
		return zero;
	}

	/*
	 * public static int[][] getGoal() { return goal; }
	 */

	/*
	 * public static void setState(int[][] state) { Board.state = state; }
	 */

	/*
	 * public static void setGoal() { int x = state.length;
	 * System.out.println(x); Board.goal = new int[x][x]; int counter = 0; for
	 * (int i = 0; i < goal.length; i++) { for (int j = 0; j < goal.length; j++)
	 * { goal[i][j] = counter++; } } goal[x - 1][x - 1] = 0; }
	 */

	/*
	 * public List<Board> getMoves() { return moves; }
	 */

	/*
	 * public void setMoves() { moves = new ArrayList<>(); Board move; if
	 * (zero.getY() - 1 >= 0 && (parrent == null ? true : direction != RIGHT)) {
	 * Point left = new Point(zero.getX(), zero.getY() - 1); move = new
	 * Board(LEFT, this, this.distance + 1, left);
	 * move.calculateEvaluationFunctionFromParrent(left); moves.add(move); } if
	 * (zero.getY() + 1 < state.length && (parrent == null ? true : direction !=
	 * LEFT)) {
	 * 
	 * Point right = new Point(zero.getX(), zero.getY() + 1);
	 * 
	 * move = new Board(RIGHT, this, this.distance + 1, right);
	 * move.calculateEvaluationFunctionFromParrent(right); moves.add(move); } if
	 * (zero.getX() - 1 >= 0 && (parrent == null ? true : direction != DOWN)) {
	 * Point up = new Point(zero.getX() - 1, zero.getY());
	 * 
	 * move = new Board(UP, this, this.distance + 1, up);
	 * move.calculateEvaluationFunctionFromParrent(up); moves.add(move); } if
	 * (zero.getX() + 1 < state.length && (parrent == null ? true : direction !=
	 * UP)) { Point down = new Point(zero.getX() + 1, zero.getY());
	 * 
	 * move = new Board(DOWN, this, this.distance + 1, down);
	 * move.calculateEvaluationFunctionFromParrent(down); moves.add(move); } }
	 */

	/*
	 * public static void swapBack(Point currentZero, Point oldZero) { int temp
	 * = state[currentZero.getX()][currentZero.getY()];
	 * state[currentZero.getX()][currentZero.getY()] =
	 * state[oldZero.getX()][oldZero .getY()];
	 * state[oldZero.getX()][oldZero.getY()] = temp; }
	 */

	/*
	 * public void swap() { int temp; switch (direction) { case 'l': temp =
	 * state[zero.getX()][zero.getY() + 1]; state[zero.getX()][zero.getY() + 1]
	 * = state[zero.getX()][zero .getY()]; state[zero.getX()][zero.getY()] =
	 * temp; break; case 'r': temp = state[zero.getX()][zero.getY() - 1];
	 * state[zero.getX()][zero.getY() - 1] = state[zero.getX()][zero .getY()];
	 * state[zero.getX()][zero.getY()] = temp; break; case 'u': temp =
	 * state[zero.getX() + 1][zero.getY()]; state[zero.getX() + 1][zero.getY()]
	 * = state[zero.getX()][zero .getY()]; state[zero.getX()][zero.getY()] =
	 * temp; break; case 'd': temp = state[zero.getX() - 1][zero.getY()];
	 * state[zero.getX() - 1][zero.getY()] = state[zero.getX()][zero .getY()];
	 * state[zero.getX()][zero.getY()] = temp; break; default: break; } }
	 */
	/*
	 * public static int[][] getState() { return state; }
	 */

	/*
	 * public static int calculateManhatanHeuristic() { int sum = 0; int value;
	 * Point place; for (int x = 0; x < state.length; x++) { for (int y = 0; y <
	 * state.length; y++) { if (state[x][y] != 0) { value = state[x][y]; place =
	 * getFinalPlace(value); sum += Math.abs(x - place.getX()) + Math.abs(y -
	 * place.getY()); } } } return sum; }
	 */
	/*
	 * private static Point getFinalPlace(int value) { int[] place = new int[2];
	 * if (value != 0) { place[0] = value / state.length; if (value %
	 * state.length == 0) { place[0]--; } place[1] = value - 1 - place[0] *
	 * state.length; } else { place[0] = place[1] = state.length - 1; } return
	 * new Point(place[0], place[1]); }
	 */
	/*
	 * public void calculateEvaluationFunctionFromParrent(Point move) {
	 * setEvaluationFunction(parrent.evaluationfunction - previousMove(move) +
	 * currentMove(move) + 1);
	 */

	@Override
	public int hashCode() {
		// final int prime = 31;
		// int result = 1;
		// result = prime * result + id;
		return id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		/*if (getClass() != obj.getClass())
			return false;*/
		Board other = (Board) obj;
		if (id != other.id)
			return false;
		return true;
	}

	@Override
	public int compareTo(Board o) {
		if (this == o)
			return 0;
		if (this.evaluationfunction < o.evaluationfunction)
			return -1;
		else if (this.evaluationfunction > o.evaluationfunction)
			return 1;
		return 0;
	}
}
