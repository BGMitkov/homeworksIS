package KnapSackSolverGenetics;

public class Item {
	private int weight, value;
	private int id;

	public Item(int value, int weight, int id) {
		super();
		this.value = value;
		this.weight = weight;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int mass) {
		this.weight = mass;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int cost) {
		this.value = cost;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + value;
		result = prime * result + id;
		result = prime * result + weight;
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
		Item other = (Item) obj;
		if (value != other.value)
			return false;
		if (id != other.id)
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

}
