package naiveBayesClassifier;

import java.util.ArrayList;

public class Trainers {
	private static final int CLASS_INDEX = 16;
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
		int[][] countOfYesVotes = new int[2][17];
		int[][] countOfUnknownVotes = new int[2][16];

		for (int i = 0; i < countOfYesVotes.length; i++) {
			for (int j = 0; j < countOfYesVotes[0].length - 1; j++) {
				countOfYesVotes[i][j]++;
				countOfUnknownVotes[i][j]++;
			}
			countOfYesVotes[i][16]++;
		}

		for (int i = 0; i < trainers.size(); i++) {
			Voter trainer = trainers.get(i);

			int type = 0;
			if (trainer.isDemocrat()) {
				type = 1;
			}

			for (int j = 0; j < 16; j++) {
				if (trainer.isVoteYes(j)) {
					countOfYesVotes[type][j]++;
				} else if (trainer.isVoteUnknown(j)) {
					countOfUnknownVotes[type][j]++;
				}
			}

			countOfYesVotes[type][16]++;
		}

		double[][][] probabilities = new double[2][17][3];

		for (int i = 0; i < probabilities.length; i++) {
			for (int j = 0; j < probabilities[0].length - 1; j++) {
				// Decide if to remove probability of unknown
				// increment by 2 total voters for corrections so that no 0
				// probability appears to zero out results.
				int totalVotersOfType = countOfYesVotes[i][CLASS_INDEX] + 1;

				probabilities[i][j][0] = (double) countOfYesVotes[i][j]
						/(double) totalVotersOfType;
				probabilities[i][j][1] = (double) (totalVotersOfType
						- countOfYesVotes[i][j] - countOfUnknownVotes[i][j] + 1)
						/(double) totalVotersOfType;

				probabilities[i][j][2] = (double) countOfUnknownVotes[i][j]
						/(double) totalVotersOfType;
			}

			probabilities[i][CLASS_INDEX][0] = (double) countOfYesVotes[i][CLASS_INDEX]
					/ (double)(countOfYesVotes[0][CLASS_INDEX] + countOfYesVotes[1][CLASS_INDEX]);
		}

		return probabilities;
	}
}
