import java.util.HashMap;
import java.util.PriorityQueue;

public class HuffmanCodingTree {
        HuffmanCodingNode root;
    public HuffmanCodingTree(){
    }
    public PriorityQueue constructTree(HashMap<Character, Integer> freqMap) {
        PriorityQueue<HuffmanCodingNode> queue = new PriorityQueue<>();
        //put all the characters into a priority Que then will use this Q to construct the tree
		for (HashMap.Entry<Character, Integer> n : freqMap.entrySet()) {
//            System.out.println("constructing Queue"+n.getValue().toString());
			HuffmanCodingNode node = new HuffmanCodingNode(n.getKey(), n.getValue());
			queue.offer(node);
		}
		//iterate all the leaf and construct a tree towards the root
		int sizeOfQue = queue.size();
		//for(int index= 1; index<sizeOfQue; index++){
		while(queue.size()>1){
			//the very first time of this code will poll the two character which have the smallest frequency
			HuffmanCodingNode left = queue.poll();
			HuffmanCodingNode right = queue.poll();
			HuffmanCodingNode parent = new HuffmanCodingNode(left, right);
//			System.out.println( parent.toString());
			parent.setFreq(left,right);//Set the freq for parents
//			System.out.println("tree create parent" + parent.fequency + parent.c);
			left.parent = parent;//set the parent of children towards this parent just created
			right.parent = parent;
//            System.out.println("constucting tree / parent / checking" +parent.toString());
			//the last node in Queue will be set as the root which has the greatest freq
            queue.offer(parent);
		}
		this.root = queue.peek();//set the root of the tree in the field will be used later
//        System.out.println("tree root" + this.root.toString());
        return queue;
    }
}
