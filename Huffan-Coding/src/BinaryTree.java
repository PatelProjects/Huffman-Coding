
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class BinaryTree {
	
	/**
	 * @param map is the map containing the characters and their frequency
	 * @return the root node of the tree created from this frequency
	 */
	
	public static Node TreeCreator(HashMap<String, Integer> map) {

		//A custom comparator that compares nodes based on their value
		NodeComparator NC = new NodeComparator();
		
		//This PriorityQueue queues the nodes based on their frequency using the custom comparator
		PriorityQueue<Node> pq = new PriorityQueue<Node>(NC);
		
        for (Map.Entry<String, Integer> entry : map.entrySet()){

        	//Creates a node for each map entry and adds it to the PriorityQueuqe
            Node n = new Node(entry.getKey(), entry.getValue());
            pq.add(n);
        }	
        
        //This while loop combines nodes until there is only one root node
        while (pq.size() != 1) {
        	
        	Node x = pq.poll();
        	Node y = pq.poll();
        	
        	
        	Node Parent = new Node("*" , x.value + y.value);
        	Parent.leftChild = x;
        	Parent.rightChild = y;
        
        	
        	pq.add(Parent);

        }
        
        return pq.poll();
	}

	/**
	 * @param the empty HashMap where the pathing to each character will be stored
	 * @param node is the root node
	 * @param x
	 */
	public static void inOrderTraverseTree(HashMap<String, String> map, Node node, String x) {
		// TODO Auto-generated method stub
		
		if (node != null){
					
			
			if (node.key != "*"){
				map.put(node.key , x);
			}
			
			//Add 0 to the string if path to leaf goes left
			inOrderTraverseTree(map, node.leftChild, (x + "0"));
			
			//Add 1 to the string if path to leaf goes right
			inOrderTraverseTree(map, node.rightChild, (x + "1") );
		}
		
	}
}



/**
 * defines what a node is
 */
class Node {
	
	String key;
	int value;
		
	Node leftChild;
	Node rightChild;
	
	
	Node(String key, int value){
		
		this.key = key;
		
		this.value = value;
		
	}

	public String toString(){
		
		return key + " appears " + value + " times";
		
	}
}