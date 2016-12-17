package naiveBayesClassifier;

public class Voter {
	/**
	 * 16 field is true for democrat
	 */
	boolean[] votes;
	boolean[] unknownVotes;
	public Voter() {
		votes = new boolean[17];
		unknownVotes = new boolean[16];
	}
	public void voteYes(int j) {
		votes[j] = true;
	}
	public void voteUnknown(int j) {
		unknownVotes[j] = true;
	}
	
	public void setToBeDemocrat(){
		votes[16] = true;
	}
}
