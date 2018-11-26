import java.util.Comparator;

public class HuffmanCodingNode implements Comparable<HuffmanCodingNode>  {
    char c;
    int fequency;
    HuffmanCodingNode parent, left, right;
    String coding="";
    public HuffmanCodingNode(char c, int frequency){ // constructor only used for creating a leaf node
        this.c = c;
        this.fequency = frequency;
    }
    public HuffmanCodingNode(HuffmanCodingNode left, HuffmanCodingNode right){ // constructor for creating parent nodes
        this.left = left;
        this.right = right;
    }
    public boolean isLeafNode(){//check if the node is a leaf node
        return (this.left==null && this.right == null);
    }



    public int compareTo(HuffmanCodingNode o) {
        return this.fequency - o.fequency;
    }
    public void setCoding(String s){
        this.coding = s;

    }
    public String getCoding(){
        return this.coding;
    }

    @Override
    public String toString() {
        return "HuffmanCodingNode{" +
                "c=" + c +
                ", fequency=" + fequency +
                ", coding='" + coding + '\'' +
                '}';
    }

    public void setFreq(HuffmanCodingNode left, HuffmanCodingNode right) {
        this.fequency = left.fequency + right.fequency;
    }
}
