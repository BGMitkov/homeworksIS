package KnapSackSolverGenetics;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Scanner;

public class KnapsackProblem {

	public static void main(String[] args) {
		KnapsackProblem kp = new KnapsackProblem();
		kp.solveTheKnapsackProblem(50);
	}

	private static final int NUMBER_OF_POPULATION = 200;
	private static final double PERCENT_POPULATION_TO_CROSS = 0.2;
	private static final double PER_CENT_CHILDREN_TO_MUTATE = 0.05;

	private int maxStorage;
	private int numberOfItems;
	private List<Configuration> population;
	private ArrayList<Item> items;
	private Random randomNumberGenerator;
	private ArrayList<Item> itemsToShaffle;
	private int childsMutated;

	public KnapsackProblem() {
		super();
		this.randomNumberGenerator = new Random();
	}

	public void solveTheKnapsackProblem(int N) {
		int generations = 0;

		createPopulation();
		sortPopulationByFitness();
		System.out.println("Best is : " + population.get(0).getTotalValue());

		while (generations < N) {

			crossoverCutAndSplice();

			sortPopulationByFitness();

			printFinestCost(++generations, N);
		}
	}

	private void printFinestCost(int generations, int n) {
		if (generations == 10 || generations == n / 5 * 2
				|| generations == n / 5 * 3 || generations == n / 5 * 4
				|| generations == n) {
			System.out.printf(
					"At %d generation the best is : %d value and %d weight\n",
					generations, population.get(0).getTotalValue(), population
							.get(0).getTotalWeight());
			 printConfigurationItems(population.get(0));
			System.out.println(population.size());
		}
	}

	private void printConfigurationItems(Configuration configuration) {

		for (Item item : configuration.getItems()) {
			System.out.printf("%d, ", item.getId());
		}
		System.out.println();
	}

	public void crossoverCutAndSplice() {

		ArrayList<Configuration> children = createChildren();

		prunePopulation();

		mutateChildren(children);

		addChildrenToPopulation(children);
	}

	private ArrayList<Configuration> createChildren() {
		ArrayList<Configuration> p = new ArrayList<>(population);
		ArrayList<Configuration> children = new ArrayList<>();
		double numberOfCrossings = (population.size() * PERCENT_POPULATION_TO_CROSS) / 2;
		for (int i = 0; i < numberOfCrossings; i++) {
			Configuration firstParent = selectConfiguration(p);
			Configuration secondParent = selectConfiguration(p);

			cross(firstParent, secondParent, children);
		}

		return children;
	}

	private Configuration selectConfiguration(ArrayList<Configuration> p) {
		int maxValue = p.get(0).getTotalValue();
		boolean notAccepted = true;
		int index = 0;

		while (notAccepted) {
			index = (int) (p.size() * randomNumberGenerator.nextDouble());
			if (randomNumberGenerator.nextDouble() < p.get(index)
					.getTotalValue() / maxValue) {
				notAccepted = false;
			}
		}

		return p.get(index);
	}

	private void mutateChildren(ArrayList<Configuration> children) {

		for (int i = 0; i < children.size(); i++) {
			if (randomNumberGenerator.nextDouble() < PER_CENT_CHILDREN_TO_MUTATE) {
				childsMutated++;
				mutateChild(children.get(i));
			}
		}
	}

	// TODO Make changes to the values in result of the mutation
	private void mutateChild(Configuration child) {

		boolean[] childContains = child.getContains();
		int randomItem;

		int itemToChange = randomNumberGenerator.nextInt(child.getItems()
				.size());

		do {
			randomItem = randomNumberGenerator.nextInt(items.size());
		} while (childContains[items.get(randomItem).getId()]
				|| (child.getTotalWeight()
						- child.getItems().get(itemToChange).getWeight() + items
						.get(randomItem).getWeight()) > maxStorage);

		child.replace(itemToChange, items.get(randomItem));
	}

	private void addChildrenToPopulation(ArrayList<Configuration> children) {
		for (int i = 0; i < children.size(); i++) {
			population.add(children.get(i));
		}
	}

	// fix to include every item in either children
	private void cross(Configuration parent1, Configuration parent2,
			ArrayList<Configuration> children) {

		int minSize = Math.min(parent1.size(), parent2.size());

		int cutIndex = (minSize > 1) ? randomNumberGenerator
				.nextInt(minSize - 1) : 0;

		ArrayList<Item> firstParentItems = parent1.getItems();
		ArrayList<Item> secondParentItems = parent2.getItems();

		Configuration firstChild = new Configuration();
		Configuration secondChild = new Configuration();

		boolean[] firstChildContains = new boolean[items.size()];

		Item nextItem = null;

		fillFirstChildLeftSide(cutIndex, firstParentItems, firstChild,
				firstChildContains);

		boolean[] secondChildContains = new boolean[items.size()];

		fillSecondChildLeftSide(cutIndex, firstParentItems, secondChild,
				secondChildContains);

		int startIteratorOverSecondParentItems = secondParentItems.size()
				- cutIndex - 1;

		for (int i = startIteratorOverSecondParentItems; i < secondParentItems
				.size(); i++) {
			nextItem = secondParentItems.get(i);

			if (!firstChildContains[nextItem.getId()]
					&& firstChild.getTotalWeight() + nextItem.getWeight() <= maxStorage) {
				firstChild.addItem(nextItem);
				firstChildContains[nextItem.getId()] = true;
			} else if (!secondChildContains[nextItem.getId()]
					&& secondChild.getTotalWeight() + nextItem.getWeight() <= maxStorage) {
				secondChild.addItem(nextItem);
				secondChildContains[nextItem.getId()] = true;
			}
		}

		for (int i = startIteratorOverSecondParentItems - 1; i >= 0; i--) {
			nextItem = secondParentItems.get(i);

			if (!secondChildContains[nextItem.getId()]
					&& secondChild.getTotalWeight() + nextItem.getWeight() <= maxStorage) {
				secondChild.addItem(nextItem);
				secondChildContains[nextItem.getId()] = true;

			} else if (!firstChildContains[nextItem.getId()]
					&& firstChild.getTotalWeight() + nextItem.getWeight() <= maxStorage) {
				firstChild.addItem(nextItem);
				firstChildContains[nextItem.getId()] = true;

			}
		}

		firstChild.setContains(firstChildContains);
		secondChild.setContains(secondChildContains);

		children.add(firstChild);
		children.add(secondChild);
	}

	private void fillSecondChildLeftSide(int cutIndex,
			ArrayList<Item> firstParentItems, Configuration secondChild,
			boolean[] secondChildContains) {
		Item nextItem;
		for (int j = cutIndex + 1; j < firstParentItems.size(); j++) {
			nextItem = firstParentItems.get(j);
			secondChild.addItem(nextItem);
			secondChildContains[nextItem.getId()] = true;
		}
	}

	private void fillFirstChildLeftSide(int cutIndex,
			ArrayList<Item> firstParentItems, Configuration firstChild,
			boolean[] firstChildContains) {
		Item nextItem;
		for (int i = 0; i <= cutIndex; i++) {
			nextItem = firstParentItems.get(i);
			firstChild.addItem(nextItem);
			firstChildContains[nextItem.getId()] = true;
		}
	}

	public void prunePopulation() {
		double prunningBound = population.size()
				* (1 - PERCENT_POPULATION_TO_CROSS);

		while (population.size() > prunningBound) {
			population.remove(population.size() - 1);
		}
	}

	/**
	 * Sorts population by cost into descending order
	 */
	public void sortPopulation() {
		Collections.sort(population);
	}

	public void createPopulation() {

		population = new ArrayList<>();

		setItemsFromConsole();

		Collections.shuffle(items);

		/* itemsToShaffle = (ArrayList<Item>) items.clone(); */

		for (int j = 0; j < NUMBER_OF_POPULATION; j++) {
			createIndividual();
		}
		System.out.println("First Population  is : " + population.size());
	}

	private void createIndividual() {
		int i = 0;
		int sumOfWeights = 0;
		int sumOfValues = 0;

		LinkedList<Item> chromosomes = new LinkedList<>();

		Collections.shuffle(items);
		Item nextItem = items.get(i++);

		while (i < numberOfItems
				&& sumOfWeights + nextItem.getWeight() <= maxStorage) {
			sumOfWeights += nextItem.getWeight();
			sumOfValues += nextItem.getValue();
			chromosomes.add(nextItem);
			nextItem = items.get(i++);
		}
		population.add(new Configuration(new ArrayList<Item>(chromosomes),
				sumOfWeights, sumOfValues));
	}

	public void sortPopulationByFitness() {
		population.sort(null);
	}

	public void setItemsFromConsole() {
		int counter = 0;
		int mi;
		int ci;

		try (Scanner scan = new Scanner(new File("input.txt"))) {

			maxStorage = scan.nextInt();
			numberOfItems = scan.nextInt();

			items = new ArrayList<>();

			while (counter < numberOfItems) {
				ci = scan.nextInt();
				mi = scan.nextInt();

				items.add(new Item(ci, mi, counter));

				counter++;
			}

		} catch (InputMismatchException e) {
			System.out
					.println("A value different from number has been entered");
		} catch (NoSuchElementException e) {
			System.out.println("Missing input at: " + (counter) + " entry ");
		} catch (FileNotFoundException e1) {
			System.out.println("Missing input file");
			e1.printStackTrace();
		}
	}

}
