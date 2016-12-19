package naiveBayesClassifier;

import java.util.ArrayList;

public class Trainers {
	private static final int CLASS_INDEX = 16;
	private static final int NUMBER_OF_PROPERTIES = 16;
	private static final int NUBMER_OF_CLASSES = 2;
	private ArrayList<Voter> trainers;

	public Trainers(ArrayList<Voter> trainers) {
		this.trainers = trainers;
	}

	/**
	 * Returns the probabilities for of every vote to be "yes" for every class.
	 *
	 * @return a two dimensional array of type double. The first dimension is
	 *         for different classes(0 for republicans and 1 for democrats) and
	 *         the second is for the probabilities of characteristics for that
	 *         class. The last index is for the probability of the class itself.
	 */
	public double[][][] GetProbabilities() {
		int[][] countOfYesVotes = new int[NUBMER_OF_CLASSES][17];
		int[][] countOfUnknownVotes = new int[NUBMER_OF_CLASSES][NUMBER_OF_PROPERTIES];

		for (int i = 0; i < NUBMER_OF_CLASSES; i++) {
			for (int j = 0; j < NUMBER_OF_PROPERTIES; j++) {
				countOfYesVotes[i][j]++;
				// countOfUnknownVotes[i][j]++;
			}
			countOfYesVotes[i][16]++;
		}

		for (int i = 0; i < trainers.size(); i++) {
			Voter trainer = trainers.get(i);

			int type = 0;
			if (trainer.isDemocrat()) {
				type = 1;
			}

			for (int j = 0; j < NUMBER_OF_PROPERTIES; j++) {
				if (trainer.isVoteYes(j)) {
					countOfYesVotes[type][j]++;
				} else if (trainer.isVoteUnknown(j)) {
					countOfUnknownVotes[type][j]++;
				}
			}

			countOfYesVotes[type][16]++;
		}

		double[][][] probabilities = new double[2][17][3];

		for (int i = 0; i < NUBMER_OF_CLASSES; i++) {
			for (int j = 0; j < NUMBER_OF_PROPERTIES; j++) {
				// Decide if to remove probability of unknown
				// increment by 2 total voters for corrections so that no 0
				// probability appears to zero out results.
				double totalVotersOfType = countOfYesVotes[i][CLASS_INDEX] - countOfUnknownVotes[i][j];

				probabilities[i][j][0] = (double) countOfYesVotes[i][j] / totalVotersOfType;
				if (probabilities[i][j][0] <= 0.0) {
					System.out.println("Probability of yes is 0 or less");
				}
				double noes = totalVotersOfType - (double) countOfYesVotes[i][j] + 1;

				probabilities[i][j][1] = noes / totalVotersOfType;
				if (probabilities[i][j][1] <= 0.0) {
					System.out.println(
							"Probability of no is 0 or less " + probabilities[i][j][1] + " i:" + i + " j:" + j);
				}
				System.out.printf("");
				/*
				 * probabilities[i][j][2] = (double) countOfUnknownVotes[i][j]
				 * /(double) totalVotersOfType; if(probabilities[i][j][2] ==
				 * 0.0) { System.out.println("Probability is 0"); }
				 */
			}

			probabilities[i][CLASS_INDEX][0] = (double) countOfYesVotes[i][CLASS_INDEX]
					/ (double) (countOfYesVotes[0][CLASS_INDEX] + countOfYesVotes[1][CLASS_INDEX]);
			if (probabilities[i][CLASS_INDEX][0] <= 0.0) {
				System.out.println("Probability of class is 0 or less");
			}
		}

		return probabilities;
	}
}
