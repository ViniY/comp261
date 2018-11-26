public class whileNode  implements  RobotProgramNode {

    private final ConditionalNode condition;
    BlockNode block;

    public whileNode(ConditionalNode parseCon) {
        this.condition= (ConditionalNode) parseCon;
    }

    @Override
    public void execute(Robot robot) {
        while (condition.evaluate(robot)){
            block.execute(robot);
        }

    }
    public String toString(){
        return("if{"+condition+"}");
    }
    public boolean setBlock(BlockNode paresedBlock){
        this.block = paresedBlock;
        return true;
    }
}
