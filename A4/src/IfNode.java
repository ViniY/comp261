import jdk.nashorn.internal.ir.Block;

public class IfNode extends ConditionNode  implements  RobotProgramNode{

    public  ConditionalNode parsedIfNode;
    public BlockNode block;

    public IfNode(ConditionalNode parseCon) {
        this.parsedIfNode = parseCon;
    }

    @Override
    public void execute(Robot robot) {

    }

    @Override
    public boolean evaluate(Robot robot) {
        return false;
    }

    public String toString(){
        return("if{"+parsedIfNode+"}");
    }



    public void setBlock(BlockNode block){
        this.block = block;
    }
}
