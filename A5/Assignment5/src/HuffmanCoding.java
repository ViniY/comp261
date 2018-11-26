/**
 * A new instance of HuffmanCoding is created for every run. The constructor is
 * passed the full text to be encoded or decoded, so this is a good place to
 * construct the tree. You should store this tree in a field and then use it in
 * the encode and decode methods.
 */
import java.util.*;

public class HuffmanCoding {
	private final HashMap<Character, Integer> frequency_map;// freq map
	private final HashMap<Character, String> codingMap;//encoding map
	private PriorityQueue<HuffmanCodingNode> huffmanTree;
	/**
	 * This would be a good place to compute and store the tree.
	 */
	HuffmanCodingNode root;

	public HuffmanCoding(String text) {
		// TODO fill this in.
//		Queue<HuffmanCodingNode> queue = new PriorityQueue<>(new NodeComparator());
		// constructing the table which contains the freq of the char
		HashMap<Character, Integer> table = new HashMap<>();
		for (int index = 0; index < text.length(); index++) {
			char c = text.charAt(index);
			if (table.keySet().contains(c)) {// if the table has this character then increase the frequency by one
				table.put(c, table.get(c) + 1);
			} else table.put(c, 1);// else the put this character in the table and set the freq to one
		}
		System.out.println("freq table" + table.values());
		this.frequency_map = table;
		HuffmanCodingTree tree = new HuffmanCodingTree();
		this.huffmanTree = tree.constructTree(frequency_map);//constructing the huffmancoding tree
		this.root = tree.root;
		this.codingMap = conversionMap();
//		System.out.println(codingMap.values());
	}

	/**
	 * Take an input string, text, and encode it with the stored tree. Should
	 * return the encoded text as a binary string, that is, a string containing
	 * only 1 and 0.
	 */
	public String encode(String text) {
		// TODO fill this in.
		StringBuilder encoding = new StringBuilder();
		for(int index = 0; index< text.length(); index++){
			encoding.append(codingMap.get(text.charAt(index)));
		}
		return encoding.toString();

	}
	private  HashMap<Character,String> conversionMap(){
// by doing this we can only make the conversion once then encoding and decoding will be very fast
		HashMap<Character, String> codings = new HashMap<>();
		Stack<HuffmanCodingNode> stack = new Stack<>();
		stack.push(this.root);
		System.out.println("root:" +this.root.toString());
		while (!stack.isEmpty()) {
			HuffmanCodingNode poppedNode = stack.pop();
			System.out.println("poppedNode" + poppedNode.toString());
			HuffmanCodingNode left = poppedNode.left;
			HuffmanCodingNode right = poppedNode.right;
			if (left != null ) {
				System.out.println("left :" + left.toString());
				left.coding = (poppedNode.coding + '0');
				stack.push(left);}
			if (right !=null){
				System.out.println("right :" + left.toString());
				right.coding=  (poppedNode.coding+ '1');
				stack.push(right);
			} else {//already leaf node
				System.out.println(poppedNode.getCoding().toString());
				codings.put(poppedNode.c, poppedNode.getCoding());
			}
		}
		return codings;
	}
//unused
	private HuffmanCodingNode toFindNode(char c, HuffmanCodingNode node) {
		if (node == null) return null;
		if (c == node.c) return node;
		//recursion start
		HuffmanCodingNode left = toFindNode(c, node.left);
		HuffmanCodingNode right = toFindNode(c, node.right);
		if (left != null) return left;
		if (right != null) return right;
		return null;//cant find this node
	}

	/**
	 * Take encoded input as a binary string, decode it using the stored tree,
	 * and return the decoded text as a text string.
	 */
	public String decode(String encoded) {
		// TODO fill this in.
		HuffmanCodingNode root = this.root;
//		System.out.println(root.toString());
		StringBuilder decoded = new StringBuilder();
		char[] charArray = encoded.toCharArray();// separate the string in to an char array
		HuffmanCodingNode pointer = this.root; // create a pointer here and initialise with the root node
		int index=0;
		while(index<charArray.length){
			char convertingChar = charArray[index];
			if(convertingChar == '0'){
				pointer = pointer.left;
				if(pointer.left == null ) {//reach the leaf
					decoded.append(pointer.c);
					pointer = root; // reset the pointer back to root
				}
			}
			else if(convertingChar =='1'){
				pointer = pointer.right;
				if(pointer.right == null){//reach the leaf
					decoded.append(pointer.c);
					pointer = root;
				}
			}
			index++;
		}
		return decoded.toString();
	}

	/**
	 * The getInformation method is here for your convenience, you don't need to
	 * fill it in if you don't wan to. It is called on every run and its return
	 * value is displayed on-screen. You could use this, for example, to print
	 * out the encoding tree.
	 */
	public String getInformation() {
		StringBuilder sb = new StringBuilder("\nThe encoding of each char:\n");
		for (Map.Entry<Character, String> entry : codingMap.entrySet()) {
			sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\n");
		}
		return sb.toString();
 	}

//	class NodeComparator implements Comparator<HuffmanCodingNode> {
//		public int compare(HuffmanCodingNode n1, HuffmanCodingNode n2) {
//			return n1.fequency - n2.fequency;
//		}
//	}
}

