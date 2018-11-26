import java.util.ArrayList;

public class LoopNode implements  RobotProgramNode{
    BlockNode blkND;

    public LoopNode(BlockNode blockNode) {
        this.blkND=blockNode;
    }

    @Override
    public void execute(Robot robot) {
      while(true){//infinite loop through the statements
         blkND.execute(robot);
          }

    }
    public String toString(){
        return ("Loop{"+this.blkND+"}");
    }
}
