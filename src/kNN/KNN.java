package kNN;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;

public class KNN {

	private static final int TEST_DATA_SIZE = 20;
	private List<Data> input;
	private List<Data> testData;

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		KNN knn = new KNN();
		knn.classifyTestData();
		/*
		 * List<Data> knearest = knn.kNearestNeighbors(30, knn.testData.get(0));
		 * for (Data data : knearest) {
		 * System.out.println(data.getEuclideanDistance()); }
		 */
		System.out.println("OK");
	}

	public int getInput() {
		int k = 0;
		try (Scanner scanner = new Scanner(System.in)) {
			k = scanner.nextInt();
		} catch (InputMismatchException e) {
			System.out.println("Positive number is needed!");
		} catch (NoSuchElementException e) {
			System.out.println("No input");
		}

		return k;
	}

	public void classifyTestData() {
		int k = getInput();
		if (k <= 0) {
			System.out.println("Positive number is required!");
			return;
		}

		getInput("kNN.data.txt");

		setTestData();

		getPercentPredicted(k);
	}

	private void getPercentPredicted(int k) {
		int countOfTruePredictions = 0;
		for (int i = 0; i < testData.size(); i++) {
			if (classifyData(testData.get(i), k)) {
				countOfTruePredictions++;
			}
		}
		System.out.printf("Per cent correctly classified: %d\n",
				(countOfTruePredictions*100 / testData.size()));
	}

	public boolean classifyData(Data unclassifed, int k) {
		List<Data> knearest = kNearestNeighbors(k, unclassifed);

		HashMap<String, Integer> classesCount = new HashMap<>();

		for (Data data : knearest) {
			Integer sum = classesCount.get(data.getClassification());
			classesCount.put(data.getClassification(),
					(sum != null ? sum : 0) + 1);
		}

		Set<Map.Entry<String, Integer>> classSet = classesCount.entrySet();
		String[] mostCommonClass = new String[classesCount.size()];
		mostCommonClass[0] = "None";
		int maxCount = 0;
		int equalCount = 0;
		for (Map.Entry<String, Integer> entry : classSet) {
			if (entry.getValue() > maxCount) {
				mostCommonClass[0] = entry.getKey();
				maxCount = entry.getValue();
				equalCount = 0;
			} else if (entry.getValue() == maxCount) {
				mostCommonClass[++equalCount] = entry.getKey();
			}
		}
		if (equalCount == 0) {
			double[] properties = unclassifed.getProperties();
			if (mostCommonClass[0].equals(unclassifed.getClassification())) {
				System.out.printf("%f %f %f %f %s is classified as %s\n",
						properties[0], properties[1], properties[2],
						properties[3], unclassifed.getClassification(),
						mostCommonClass[0]);
				return true;
			}
			System.out.printf("%f %f %f %f %s is classified as %s\n",
					properties[0], properties[1], properties[2], properties[3],
					unclassifed.getClassification(), mostCommonClass[0]);
			return false;
		} else {
			return classifyWithClosest(equalCount, mostCommonClass,
					unclassifed, knearest);
		}
	}

	private boolean classifyWithClosest(int equalCount,
			String[] mostCommonClass, Data unclassifed, List<Data> knearest) {
		double[] properties = unclassifed.getProperties();
		for (int i = 0; i < knearest.size(); i++) {
			for (int j = 0; j < mostCommonClass.length; j++) {
				if (knearest.get(i).getClassification()
						.equals(mostCommonClass[j])) {
					System.out.printf("%f %f %f %f %s is classified as %s\n",
							properties[0], properties[1], properties[2],
							properties[3], unclassifed.getClassification(),
							mostCommonClass[j]);
					return unclassifed.getClassification().equals(
							mostCommonClass[j]);
				}
			}
		}
		System.out.printf("%f %f %f %f %s is classified as %s\n",
				properties[0], properties[1], properties[2], properties[3],
				unclassifed.getClassification(), mostCommonClass[0]);
		return false;
	}

	private List<Data> kNearestNeighbors(int k, Data unclassified) {
		List<Data> trainers = new ArrayList<Data>(input.size());

		generateNeighborsDistance(unclassified, trainers);

		List<Data> kNearest = new LinkedList<>();

		for (int i = 0; i < k; i++) {
			kNearest.add(trainers.get(i));
		}

		return kNearest;
	}

	private void generateNeighborsDistance(Data unclassified,
			List<Data> trainers) {
		for (Data trainer : input) {
			trainer.setEuclideanDistance(euclideanDistance(unclassified,
					trainer));
			trainers.add(trainer);
		}
		trainers.sort(null);
	}

	private double euclideanDistance(Data toBeClassified, Data trainer) {
		double sumOfDiffSquares = 0;
		double[] propertiesOfToBeClassified = toBeClassified.getProperties();
		double[] propertiesOfTrainer = trainer.getProperties();

		for (int i = 0; i < propertiesOfTrainer.length; i++) {
			sumOfDiffSquares += Math.pow(propertiesOfToBeClassified[i]
					- propertiesOfTrainer[i], 2);
		}
		return Math.sqrt(sumOfDiffSquares);
	}

	// TODO check if data order needs to be kept
	private void setTestData() {
		Collections.shuffle(input);
		testData = new LinkedList<>();
		for (int i = 0; i < TEST_DATA_SIZE; i++) {
			testData.add(input.remove(0));
		}
	}

	private void getInput(String file) {
		input = new LinkedList<>();
		int line = 0;
		try (Scanner scanner = new Scanner(new File(file)).useLocale(Locale.US)) {
			while (scanner.hasNext()) {
				line++;
				double[] properties = new double[4];
				for (int i = 0; i < properties.length; i++) {
					properties[i] = scanner.nextDouble();
					// System.out.printf("%f ", properties[i]);
				}
				String classification = scanner.nextLine();
				// System.out.println(classification);

				input.add(new Data(properties, classification));

			}
		} catch (NullPointerException e) {
			System.out.println("File has not been provided");
		} catch (InputMismatchException e) {
			System.out.printf(
					"Data is corrupted! Input is not valid at line: %d", line);
		} catch (FileNotFoundException e) {
			System.out.println("Data input file not found");
		}
	}
}
