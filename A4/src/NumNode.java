public class NumNode implements ExpressionNode{
    int intNode;
    public NumNode(int i) {
        this.intNode = i;

    }

    @Override
    public int evaluate(Robot robot) {
        return 0;
    }
    public String toString(){
        return String.valueOf(this.intNode);
    }
}
