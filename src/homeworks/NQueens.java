package homeworks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class NQueens {

	public static void main(String[] args) {
		NQueens play = new NQueens();
		/*
		 * Long time1 = System.nanoTime();
		 */		
		int N = 0;
		try {
			N = getNumberOfQueens();
		} catch (InputMismatchException e) {
			System.out.println("Input can only be whole number!");
			return;
		}
		play.start(N);
		/*System.out.println("Time total : " + (System.nanoTime() - time1)
				/ Math.pow(10, 9) + " seconds");*/

	}

	private static int getNumberOfQueens() throws InputMismatchException {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter number of queens : ");
		int queens = scanner.nextInt();
		scanner.close();
		return queens;
	}

	private int MAX_ITERATIONS;
	private int[] queens;
	private int[] conflicts;
	private Random generator;
	private long k;
	private int[] dublicatesWithMinConflicts;
	private int[] dublicatesOfQueensWithMaxConflicts;
	private int maxConflicts;
	private int minConflicts;
	private int countOfMinConflictsDublicates;
	private int countOfMaxConflictsDublicates;
	private int i;
	private int colWithMaxConflicts;
	private int minConflictsPosition;
	private int[] columnConflicts;
	private int leftDiagonalConflict;
	private int rightDiagonalConflict;
	private int differenceByX;
	private int differenceByY;
	private int countOfConflicts;
	private List<Integer> list;

	public void start(int N) {
		if (!isValid(N)) {
			return;
		}

		queens = new int[N];
		conflicts = new int[N];
		MAX_ITERATIONS = (int) (2 * N);
		System.out.println("Max iterations set to : " + MAX_ITERATIONS);
		dublicatesOfQueensWithMaxConflicts = new int[N];
		dublicatesWithMinConflicts = new int[N];

		do {
			solve(N);
			k++;
			printConflicts();

		} while (hasConflicts());
		 printBoardSolution();
	}

	public void solve(int N) {

		generator = new Random();

		initializeQueens();
		initializeConflicts();
		i = 0;
		colWithMaxConflicts = 0;

		System.out.println("Repositioning queens!");

		while (i <= MAX_ITERATIONS) {

			colWithMaxConflicts = getQueenWithMaxConflicts();// generator.nextInt(queens.length);
			if (colWithMaxConflicts == -1) {
				break;
			}
			minConflictsPosition = minConflicts(colWithMaxConflicts);
			if (minConflictsPosition != queens[colWithMaxConflicts]) {
				for (int j = 0; j < queens.length; j++) {
					if (j != colWithMaxConflicts) {
						resolveConflicts(colWithMaxConflicts, j,
								minConflictsPosition);
					}
				}
				queens[colWithMaxConflicts] = minConflictsPosition;
			}
			i++;
			// printBoardSolution();
		}
		System.out.println("Number of iterations for this try: " + i);
	}

	private void resolveConflicts(int col1, int colj, int newPositionOfQueen) {

		if (areInConflict(col1, colj)) {
			conflicts[col1]--;
			conflicts[colj]--;
		}

		if (queens[colj] == newPositionOfQueen
				|| Math.abs(queens[colj] - newPositionOfQueen) == Math.abs(col1
						- colj)) {
			conflicts[col1]++;
			conflicts[colj]++;
		}
	}

	private int minConflicts(int collumn) {
		columnConflicts = new int[queens.length];

		for (int i = 0; i < queens.length; i++) {
			if (i != collumn) {
				columnConflicts[queens[i]]++;

				leftDiagonalConflict = queens[i] - (collumn - i);
				rightDiagonalConflict = queens[i] + (collumn - i);

				if (leftDiagonalConflict >= 0
						&& leftDiagonalConflict < columnConflicts.length)
					columnConflicts[leftDiagonalConflict]++;
				if (rightDiagonalConflict >= 0
						&& rightDiagonalConflict < columnConflicts.length)
					columnConflicts[rightDiagonalConflict]++;
			}
		}
		return getRowWithMinConflicts(columnConflicts);
	}

	private int getRowWithMinConflicts(int[] conflicts) {
		minConflicts = conflicts[0];
		countOfMinConflictsDublicates = 0;
		dublicatesWithMinConflicts[0] = 0;

		for (int i = 1; i < conflicts.length; i++) {
			if (minConflicts > conflicts[i]) {
				minConflicts = conflicts[i];
				countOfMinConflictsDublicates = 0;
				dublicatesWithMinConflicts[countOfMinConflictsDublicates] = i;
			} else if (minConflicts == conflicts[i]) {
				dublicatesWithMinConflicts[++countOfMinConflictsDublicates] = i;
			}
		}
		return dublicatesWithMinConflicts[generator
				.nextInt(countOfMinConflictsDublicates + 1)];
	}

	private int getQueenWithMaxConflicts() {
		maxConflicts = conflicts[0];
		countOfMaxConflictsDublicates = 0;
		dublicatesOfQueensWithMaxConflicts[0] = 0;
		for (int i = 1; i < conflicts.length; i++) {
			if (maxConflicts < conflicts[i]) {
				maxConflicts = conflicts[i];
				countOfMaxConflictsDublicates = 0;
				dublicatesOfQueensWithMaxConflicts[0] = i;
			} else if (maxConflicts == conflicts[i]) {
				dublicatesOfQueensWithMaxConflicts[++countOfMaxConflictsDublicates] = i;
			}
		}
		if (maxConflicts == 0) {
			return -1;
		}
		return dublicatesOfQueensWithMaxConflicts[generator
				.nextInt(countOfMaxConflictsDublicates + 1)];
	}

	private void initializeQueens() {
		Long time1 = System.nanoTime();
		System.out.print("Randomly Initializing queens : ");

		list = new ArrayList<Integer>();
		for (int i = 0; i < queens.length; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		for (int i = 0; i < queens.length; i++) {
			queens[i] = list.get(i);
		}

		System.out.println();
		System.out.println("DONE");
		System.out.println("Time for initializing queens : "
				+ (System.nanoTime() - time1) / Math.pow(10, 9) + " seconds");
	}

	public void printBoardSolution() {
		System.out.print("   ");
		for (int i = 0; i < conflicts.length; i++) {
			System.out.printf("%d ", i);
		}
		System.out.println();
		for (int i = 0; i < queens.length; i++) {
			System.out.printf(" %d ", i);
			for (int j = 0; j < queens.length; j++) {
				if (i != queens[j])
					System.out.print("_ ");
				else
					System.out.print("* ");
			}
			System.out.println();

		}
		System.out.printf("number of tryouts : %d\n", k);
	}

	private boolean isValid(int n) {
		if (n < 4) {
			System.out.println("Dimention is too small!");
			return false;
		}
		return true;
	}

	private void printConflicts() {
		countOfConflicts = 0;
		for (int i = 0; i < conflicts.length; i++) {
			countOfConflicts += conflicts[i];
		}
		System.out.println("Number of conflicts after last try : "
				+ countOfConflicts);
	}

	private boolean hasConflicts() {
		for (int i = 0; i < conflicts.length; i++) {
			if (conflicts[i] != 0)
				return true;
		}
		return false;
	}

	private void initializeConflicts() {
		conflicts = new int[queens.length];

		for (int i = 0; i < queens.length; i++) {
			for (int j = i + 1; j < queens.length; j++) {
				if (areInConflict(i, j)) {
					conflicts[i]++;
					conflicts[j]++;
				}
			}
		}
		// printBoardSolution();
		// printConflicts();
	}

	private boolean areInConflict(int column1, int column2) {
		if (queens[column1] == queens[column2]) {
			return true;
		}
		differenceByX = queens[column1] - queens[column2];
		differenceByY = column1 - column2;

		if (Math.abs(differenceByX) == Math.abs(differenceByY)) {
			return true;
		}

		return false;
	}
}
