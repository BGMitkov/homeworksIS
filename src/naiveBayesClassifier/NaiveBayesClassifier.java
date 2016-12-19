package naiveBayesClassifier;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class NaiveBayesClassifier {

	private static final int NUMBER_OF_SETS = 10;
	private static final int NUMBER_OF_ATTRIBUTES_PLUS_CLASS = 17;
	private static final int NUMBER_OF_ATTRIBUTES = 16;
	private static final int NUMBER_OF_ENTRIES = 435;
	private static final int NUMBER_OF_CLASSES = 2;
	private List<Voter> voters;
	private ArrayList<ArrayList<Voter>> tenFoldVoters;

	public static void main(String[] args) {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
		nbc.initializeShaffledInput("data.txt");
		nbc.tenFoldData();
		nbc.crossValidate();
	}

	private void crossValidate() {
		int averagePercentPredicted = 0;
		for (int i = 0; i < tenFoldVoters.size(); i++) {

			int percentPredicted = classifyData(i);
			System.out.printf("Iteration %d prediction success : %d\n", i, percentPredicted);
			averagePercentPredicted += percentPredicted;
			/*
			 * averagePercentPredicted = averagePercentPredicted +
			 * (percentPredicted - averagePercentPredicted) / (i + 1);
			 */
		}
		System.out.println("Average percent of prediction is : " + averagePercentPredicted / tenFoldVoters.size());
	}

	private int classifyData(int i) {
		ArrayList<Voter> testVoters = tenFoldVoters.get(i);
		boolean[] result = new boolean[testVoters.size()];
		Trainers trainers = getTrainers(i);
		double[][][] probabilities = trainers.GetProbabilities();

		for (int j = 0; j < testVoters.size(); j++) {

			result[j] = isClassifiedAsDemocrat(testVoters.get(j), i, probabilities);
		}
		return compareResult(testVoters, result);
	}

	private int compareResult(ArrayList<Voter> testVoters, boolean[] result) {
		int numberPredicted = 0;
		for (int i = 0; i < result.length; i++) {
			if (testVoters.get(i).isDemocrat() == result[i]) {
				numberPredicted++;
			}
		}
		int percentPredicted = numberPredicted * 100 / testVoters.size();
		return percentPredicted;
	}

	private Trainers getTrainers(int testDataIndexToSkip) {
		ArrayList<Voter> trainers = new ArrayList<>(NUMBER_OF_ENTRIES);
		for (int i = 0; i < tenFoldVoters.size(); i++) {
			if (i != testDataIndexToSkip) {
				ArrayList<Voter> set = tenFoldVoters.get(i);
				for (int j = 0; j < set.size(); j++) {
					trainers.add(set.get(j));
				}
			}
		}
		return new Trainers(trainers);
	}

	private boolean isClassifiedAsDemocrat(Voter voter, int indexOfSet, double[][][] probabilities) {
		double[] classesProbability = new double[NUMBER_OF_CLASSES];
		for (int i = 0; i < classesProbability.length; i++) {
			classesProbability[i]++;
		}

		for (int i = 0; i < NUMBER_OF_CLASSES; i++) {
			for (int j = 0; j < NUMBER_OF_ATTRIBUTES; j++) {
				if (voter.isVoteYes(j)) {
					classesProbability[i] *= probabilities[i][j][0];
				} else if (!voter.isVoteYes(j) && !voter.isVoteUnknown(j)) {
					classesProbability[i] *= probabilities[i][j][1];
					// System.out.println("Probability of 0 is being used " +
					// probabilities[i][j][1]);
				} /*
					 * else { classesProbability[i] *= probabilities[i][j][2]; }
					 */
			}
			classesProbability[i] *= probabilities[i][16][0];
		}
		if (classesProbability[1] > classesProbability[0]) {
			return true;
		}
		return false;
	}

	private void tenFoldData() {
		tenFoldVoters = new ArrayList<ArrayList<Voter>>(10);
		int sizeOfSet = voters.size() / NUMBER_OF_SETS;

		for (int i = 0; i < NUMBER_OF_SETS; i++) {
			ArrayList<Voter> set = new ArrayList<Voter>();
			for (int j = 0; j < sizeOfSet; j++) {
				set.add(voters.remove(0));
			}
			tenFoldVoters.add(set);
		}
		int j = voters.size() % NUMBER_OF_SETS;
		System.out.println(j);
		for (int k = 0; k < j; k++) {
			tenFoldVoters.get(k).add(voters.remove(0));
		}
		System.out.println("Done creating sets!");
	}

	private void initializeShaffledInput(String file) {
		voters = new LinkedList<Voter>();
		try (Scanner scanner = new Scanner(new File(file)).useLocale(Locale.US)) {

			String vote;
			String classification;
			for (int i = 0; i < NUMBER_OF_ENTRIES; i++) {
				Voter voter = new Voter();
				for (int j = 0; j < NUMBER_OF_ATTRIBUTES_PLUS_CLASS - 1; j++) {
					vote = scanner.next();
					// System.out.print(vote + " ");
					if (vote.equals("y")) {
						voter.voteYes(j);
					} else if (vote.equals("u")) {
						voter.voteUnknown(j);
					}
				}

				classification = scanner.next();
				// System.out.print(classification + " ");
				// System.out.println();
				if (classification.equals("democrat")) {
					voter.setToBeDemocrat();
				}
				voters.add(voter);
			}

			Collections.shuffle(voters);

		} catch (NullPointerException e) {
			System.out.println("File has not been provided");
		} catch (InputMismatchException e) {
			System.out.printf("Data is corrupted! Input is not valid at line: %d");
		} catch (FileNotFoundException e) {
			System.out.println("Data input file not found");
		}

		System.out.println("done");
	}
}
