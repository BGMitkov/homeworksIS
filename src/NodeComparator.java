
import homeworks.Board;
import java.util.Comparator;

public class NodeComparator implements Comparator<Board> {

	@Override
	public int compare(Board arg0, Board arg1) {
		if(arg0 == null) ;
		if(arg0.getEvaluationFunction() < arg1.getEvaluationFunction()) {
			return -1;
		} 
		if(arg0.getEvaluationFunction() > arg1.getEvaluationFunction()) {
			return 1;
		}
		return 0;
	}

}
