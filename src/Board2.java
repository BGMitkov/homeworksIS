

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import homeworks.Point;

public class Board2 implements Comparable<Board2> {

	private static final char LEFT = 'l';
	private static final char RIGHT = 'r';
	private static final char UP = 'u';
	private static final char DOWN = 'd';

	private char direction;
	private int distance;
	private int evaluationfunction;
	private int heuristic;
	private Board2 parrent;
	private Point zero;
	private List<Board2> moves;
	private int[][] state;

	public Board2(char direction, int[][] state, Board2 parrent, int distance,
			Point point) {
		super();
		this.direction = direction;
		this.parrent = parrent;
		this.distance = distance;
		this.zero = point;
		this.state = state;
		setHeuristic();
		setEvaluationFunction();
	}

	public char getDirection() {
		return direction;
	}

	public int getDistance() {
		return distance;
	}

	public int getEvaluationFunction() {
		return evaluationfunction;
	}

	public void setEvaluationFunction() {
		this.evaluationfunction = distance + heuristic;
	}

	public int getHeuristic() {
		return heuristic;
	}

	public Board2 getParrent() {
		return parrent;
	}

	public Point getZero() {
		return zero;
	}

	public void setState(int[][] state) {
		this.state = state;
	}

	public List<Board2> getMoves() {
		return moves;
	}

	public void setMoves() {
		moves = new ArrayList<>();
		Board2 move;

		if (zero.getY() - 1 >= 0
				&& (parrent == null ? true : parrent.direction != RIGHT)) {
			Point left = new Point((byte) zero.getX(), (byte)(zero.getY() - 1));
			int[][] newState = deepClone(state);
			swap(left, newState);
			move = new Board2(LEFT, newState, this, distance + 1, left);
			moves.add(move);
		}
		if (zero.getY() + 1 < state.length
				&& (parrent == null ? true : parrent.direction != LEFT)) {

			Point right = new Point(zero.getX(), (byte) (zero.getY() + 1));
			int[][] newState = deepClone(state);
			swap(right, newState);
			move = new Board2(RIGHT, newState, this, distance + 1, right);
			moves.add(move);
		}
		if (zero.getX() - 1 >= 0
				&& (parrent == null ? true : parrent.direction != DOWN)) {
			Point up = new Point((byte)(zero.getX() - 1), zero.getY());
			int[][] newState = deepClone(state);
			swap(up, newState);
			move = new Board2(UP, newState, this, distance + 1, up);
			moves.add(move);
		}
		if (zero.getX() + 1 < state.length
				&& (parrent == null ? true : parrent.direction != UP)) {
			Point down = new Point((byte)(zero.getX() + 1), zero.getY());
			int[][] newState = deepClone(state);
			swap(down, newState);
			move = new Board2(DOWN, newState, this, distance + 1, down);
			moves.add(move);
		}
	}

	private void swap(Point left2, int[][] state) {
		int temp = state[getZero().getX()][getZero().getY()];
		state[getZero().getX()][getZero().getY()] = state[left2.getX()][left2
				.getY()];
		state[left2.getX()][left2.getY()] = temp;
	}

	private int[][] deepClone(int[][] state2) {
		int[][] newState = new int[state2.length][state2.length];
		for (int i = 0; i < state2.length; i++) {
			newState[i] = state2[i].clone();
		}
		return newState;
	}

	public int[][] getState() {
		return state;
	}

	public void setHeuristic() {
		int sum = 0;
		int value;
		Point place;
		for (int x = 0; x < state.length; x++) {
			for (int y = 0; y < state.length; y++) {
				if (state[x][y] != 0) {
					value = state[x][y];
					place = getFinalPlace(value);
					sum += Math.abs(x - place.getX())
							+ Math.abs(y - place.getY());
				}
			}
		}
		this.heuristic = sum;
	}

	private Point getFinalPlace(int value) {
		int[] place = new int[2];
		if (value != 0) {
			place[0] = value / state.length;
			if (value % state.length == 0) {
				place[0]--;
			}
			place[1] = value - 1 - place[0] * state.length;
		} else {
			place[0] = place[1] = state.length - 1;
		}
		return new Point((byte)place[0], (byte)place[1]);
	}

	@Override
	public int compareTo(Board2 o) {
		return Integer.compare(evaluationfunction, o.evaluationfunction);
		// if (this == o)
		// return 0;
		// if (this.evaluationfunction < o.evaluationfunction)
		// return -1;
		// else if (this.evaluationfunction > o.evaluationfunction)
		// return 1;
		// return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.deepHashCode(state);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board2 other = (Board2) obj;
		if (!Arrays.deepEquals(state, other.state))
			return false;
		return true;
	}

}