package kNN;

public class Data implements Comparable<Data>{

	private double[] properties;
	private double euclideanDistance;
	private String classification;

	public Data(double[] properties, String classification) {
		super();
		this.properties = properties;
		this.classification = classification;
	}

	public String getClassification() {
		return classification;
	}

	public void setClassification(String classification) {
		this.classification = classification;
	}

	public double getEuclideanDistance() {
		return euclideanDistance;
	}

	public void setEuclideanDistance(double euclideanDistance) {
		this.euclideanDistance = euclideanDistance;
	}

	public double[] getProperties() {
		return properties;
	}

	public void setProperties(double[] properties) {
		this.properties = properties;
	}

	@Override
	public int compareTo(Data o) {
		if(this == o) return 0;
		if(this.euclideanDistance < o.euclideanDistance) return -1;
		if(this.euclideanDistance > o.euclideanDistance) return 1;
		return 0;
	}
}
