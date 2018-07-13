
import java.util.Comparator;

public class NodeComparator  implements Comparator <Node>{
	
	// This is a comparator that the Priority Queue uses to determine the order of nodes within it. 
	// Nodes with higher values are places before nodes with lower values
	
	public int compare(Node n, Node m) {
        if (m.value > n.value){
            return -1;
        }
        else if (m.value < n.value){
            return 1;
        }
        else
            return 0;
        }

}
