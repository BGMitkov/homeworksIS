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
	private static final int NUMBER_OF_ENTRIES = 435;
	private List<Voter> voters;
	private ArrayList<ArrayList<Voter>> tenFoldVoters;

	public static void main(String[] args) {
		NaiveBayesClassifier nbc = new NaiveBayesClassifier();
		nbc.initializeShaffledInput("data.txt");
		nbc.tenFoldData();
		
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
					if (vote.equals("y")) {
						voter.voteYes(j);
					} else if (vote.equals("u")) {
						voter.voteUnknown(j);
					}
				}
				classification = scanner.next();
				if (classification.equals("democrat")) {
					voter.setToBeDemocrat();
				}
				voters.add(voter);
			}

			Collections.shuffle(voters);

		} catch (NullPointerException e) {
			System.out.println("File has not been provided");
		} catch (InputMismatchException e) {
			System.out
					.printf("Data is corrupted! Input is not valid at line: %d");
		} catch (FileNotFoundException e) {
			System.out.println("Data input file not found");
		}

		System.out.println("done");
	}
}
