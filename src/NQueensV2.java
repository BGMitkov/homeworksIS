

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class NQueensV2 {

	public static void main(String[] args) {
		NQueensV2 play = new NQueensV2();
		Date data1 = new Date();
		play.start(10_000);
		Date data2 = new Date();
		long milis = data2.getTime() - data1.getTime();
		System.out.println(milis / 1000.0);
	}

	private int MAX_ITERATION;
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
	private boolean[] generatedQueens;
	private int nextDifferentQueen;
	private int differenceByX;
	private int differenceByY;
	private int countOfConflicts;
	private int[][] conflictsTracker;
	private List<Integer> list;

	public void start(int N) {
		if (!isValid(N)) {
			return;
		}

		queens = new int[N];
		conflicts = new int[N];
		MAX_ITERATION = 2 * N;
		dublicatesOfQueensWithMaxConflicts = new int[N];
		dublicatesWithMinConflicts = new int[N];

		do {
			solve(N);
			k++;
			printConflicts();

		} while (hasConflicts());
		// printBoardSolution();
	}

	public void solve(int N) {

		generator = new Random();

		initializeQueens();
		initializeConflicts();
		i = 0;
		colWithMaxConflicts = 0;

		System.out.println("Repositioning queens!");

		while (i <= MAX_ITERATION) {
			colWithMaxConflicts = getQueenWithMaxConflicts();// generator.nextInt(queens.length);
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
	}

	// This didnt work
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

		/*
		 * int leftDiagonalConflict = queens[queen1] - (queen2 - queen1); int
		 * rightDiagonalConflict = queens[queen1] + (queen2 - queen1);
		 * 
		 * if (queens[queen1] == queens[queen2]) { conflicts[queen1]--; } else {
		 * if (queens[queen2] == leftDiagonalConflict) { conflicts[queen1]--; }
		 * else if (queens[queen2] == rightDiagonalConflict) {
		 * conflicts[queen1]--; } } if (queens[queen1] == newPositionOfQueen2) {
		 * conflicts[queen1]++; } else { if (newPositionOfQueen2 ==
		 * leftDiagonalConflict) { conflicts[queen1]++; } else if
		 * (newPositionOfQueen2 == rightDiagonalConflict) { conflicts[queen1]++;
		 * } }
		 */
	}

	// seems to work
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

	private int getRowWithMinConflicts(int[] conflictsInColumn) {
		minConflicts = conflictsInColumn[0];
		/* int index = 0; */
		/* dublicatesWithMinConflicts = new int[conflicts.length]; */
		countOfMinConflictsDublicates = 0;
		dublicatesWithMinConflicts[0] = 0;

		for (int i = 1; i < conflictsInColumn.length; i++) {
			if (minConflicts > conflictsInColumn[i]) {
				minConflicts = conflictsInColumn[i];
				/* index = i; */
				/* dublicatesWithMinConflicts = new int[conflicts.length]; */
				countOfMinConflictsDublicates = 0;
				dublicatesWithMinConflicts[countOfMinConflictsDublicates] = i;
			} else if (minConflicts == conflictsInColumn[i]) {
				dublicatesWithMinConflicts[++countOfMinConflictsDublicates] = i;
			}
		}
		return dublicatesWithMinConflicts[generator
				.nextInt(countOfMinConflictsDublicates + 1)];
	}

	private int getQueenWithMaxConflicts() {
		maxConflicts = conflicts[0];
		/* int colIndex = 0; */
		/* dublicatesOfQueensWithMaxConflicts = new int[conflicts.length]; */
		countOfMaxConflictsDublicates = 0;
		dublicatesOfQueensWithMaxConflicts[0] = 0;
		for (int i = 1; i < conflicts.length; i++) {
			if (maxConflicts < conflicts[i]) {
				maxConflicts = conflicts[i];
				/* colIndex = i; */
				/*
				 * dublicatesOfQueensWithMaxConflicts = new
				 * int[conflicts.length];
				 */
				countOfMaxConflictsDublicates = 0;
				dublicatesOfQueensWithMaxConflicts[0] = i;
			} else if (maxConflicts == conflicts[i]) {
				dublicatesOfQueensWithMaxConflicts[++countOfMaxConflictsDublicates] = i;
			}
		}
		return dublicatesOfQueensWithMaxConflicts[generator
				.nextInt(countOfMaxConflictsDublicates + 1)];
	}

	private void initializeQueens() {
		Long t1 = System.nanoTime();
		System.out.print("Randomly Initializing queens : ");

		list = new ArrayList<Integer>();
		for (int i = 0; i < queens.length; i++) {
			list.add(new Integer(i));
		}
		Collections.shuffle(list);
		for (int i = 0; i < queens.length; i++) {
			queens[i] = list.get(i);
		}

		/*
		 * // generatedQueens = new boolean[queens.length]; conflictsTracker =
		 * new int[queens.length][queens.length]; for (int i = 0; i <
		 * queens.length; i++) { do { nextDifferentQueen =
		 * getRowWithMinConflicts(conflictsTracker[i]); } while
		 * (generatedQueens[nextDifferentQueen]);
		 * 
		 * queens[i] = nextDifferentQueen;
		 * updateConflictTracker(nextDifferentQueen, i);
		 * generatedQueens[nextDifferentQueen] = true;
		 * 
		 * System.out.printf("%d, ", queens[i]); }
		 */
		System.out.println();
		System.out.println("DONE");
		System.out.println("Time for initializing queens : "
				+ (System.nanoTime() - t1) / Math.pow(10, 9) + " seconds");
		/*
		 * queens = new int[] {1,2,0,3}; printBoardSolution();
		 */
	}

	// Modified to be transposed. conflictsTracker[i] should give a column of
	// the matrix of conflicts. Can modify the generation of queens to be by
	// rows not by columns instead.
	private void updateConflictTracker(int nextQueenRow, int nextQueenColumn) {
		for (int i = 0; i < queens.length; i++) {
			if (i != nextQueenColumn) {
				conflictsTracker[i][nextQueenRow]++;
				leftDiagonalConflict = queens[nextQueenRow]
						- (nextQueenRow - i);
				rightDiagonalConflict = queens[nextQueenRow]
						+ (nextQueenRow - i);
				if (leftDiagonalConflict >= 0
						&& leftDiagonalConflict < queens.length) {
					conflictsTracker[leftDiagonalConflict][i]++;
				}
				if (rightDiagonalConflict >= 0
						&& rightDiagonalConflict < queens.length) {
					conflictsTracker[rightDiagonalConflict][i]++;
				}
			}

		}

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
					System.out.print("\u25A1 ");
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

	// Should work
	private void initializeConflicts() {
		conflicts = new int[queens.length];
		/*
		 * for (int i = 0; i < queens.length; i++) { conflicts[i] =
		 * conflictsTracker[i][queens[i]]; }
		 */

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
		/*
		 * if (differenceByX == -differenceByY) { return true; }
		 */

		/*
		 * int leftDiagonalConflict = queens[i] - (j - i); int
		 * rightDiagonalConflict = queens[i] + (j - i);
		 * 
		 * if (queens[j] == leftDiagonalConflict || queens[j] ==
		 * rightDiagonalConflict) { return true; }
		 */
		return false;
	}
}
