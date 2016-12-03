package KnapSackSolverGenetics;

import java.util.ArrayList;

public class Configuration implements Comparable<Configuration> {

	private ArrayList<Item> items;

	private int totalWeight, totalValue;
	private boolean[] contains;

	public Configuration() {
		super();
		this.items = new ArrayList<Item>();
		this.totalWeight = 0;
		this.totalValue = 0;
	}

	public Configuration(ArrayList<Item> items, int totalWeight, int totalValue) {
		super();
		this.items = items;
		this.totalWeight = totalWeight;
		this.totalValue = totalValue;
	}

	public Configuration(ArrayList<Item> items, int totalWeight, int totalCost,
			boolean[] contains) {
		super();
		this.items = items;
		this.totalWeight = totalWeight;
		this.totalValue = totalCost;
		this.contains = contains;
	}

	public void addItem(Item item) {
		items.add(item);
		totalWeight += item.getWeight();
		totalValue += item.getValue();
	}

	public boolean[] getContains() {
		return contains;
	}

	public void setContains(boolean[] contains) {
		this.contains = contains;
	}

	public ArrayList<Item> getItems() {
		return items;
	}

	public void setItems(ArrayList<Item> items) {
		this.items = items;
	}

	public int getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(int totalWeight) {
		this.totalWeight = totalWeight;
	}

	public int getTotalValue() {
		return totalValue;
	}

	public void setTotalValue(int totalCost) {
		this.totalValue = totalCost;
	}

	public int size() {
		return items.size();
	}

	/*
	 * private void setTotalCostAndWeight() { int sumOfWeight = 0; int sumOfCost
	 * = 0; for (Item item : items) { sumOfCost += item.getCost(); sumOfWeight
	 * += item.getMass(); } setTotalCost(sumOfCost);
	 * setTotalWeight(sumOfWeight); }
	 */

	@Override
	public int compareTo(Configuration o) {
		if (this == o)
			return 0;
		if (this.totalValue > o.totalValue)
			return -1;
		if (this.totalValue < o.totalValue)
			return 1;
		return 0;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((items == null) ? 0 : items.hashCode());
		result = prime * result + totalValue;
		result = prime * result + totalWeight;
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
		Configuration other = (Configuration) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		if (totalValue != other.totalValue)
			return false;
		if (totalWeight != other.totalWeight)
			return false;
		return true;
	}

	public void replace(int itemToChange, Item newitem) {
		totalValue -= items.get(itemToChange).getValue();
		totalWeight -= items.get(itemToChange).getWeight();

		items.set(itemToChange, newitem);

		totalValue += newitem.getValue();
		totalWeight += newitem.getWeight();
	}
}
