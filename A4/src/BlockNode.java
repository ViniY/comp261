import java.util.ArrayList;

public class BlockNode implements RobotProgramNode {
    ArrayList<STMTNode> statements;

    public BlockNode(ArrayList<STMTNode> statements) {
        this.statements = statements;
    }

    public BlockNode() {
        ArrayList<STMTNode> empty = new ArrayList<>();
        this.statements = empty;
    }

    @Override
    public void execute(Robot robot) {

        for (STMTNode statement : statements) {
            statement.execute(robot);
        }
    }
    public void addNode(STMTNode node){
        this.statements.add(node);
    }
    public String toString(){
        return("Block start");
    }
}
