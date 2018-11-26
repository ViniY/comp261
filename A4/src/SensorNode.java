public class SensorNode implements RobotProgramNode {
    @Override
    public void execute(Robot robot) {

    }
    public class feulLeftNode extends SensorNode implements  ExpressionNode{

        @Override
        public int evaluate(Robot robot) {
            return 0;
        }
    }
    public class oppLRNode extends SensorNode implements  ExpressionNode{

        public int evaluate(Robot robot) {
            return 0;
        }
    }
    public class oppFBNode extends SensorNode implements  ExpressionNode{

        public int evaluate(Robot robot) {
            return 0;
        }
    }
    public class numBarrelsNode extends SensorNode implements  ExpressionNode{

        public int evaluate(Robot robot) {
            return 0;
        }
    }
    public class barrelLRNode extends SensorNode implements  ExpressionNode{

        public barrelLRNode(ExpressionNode exp) {
            super();
        }
        public int evaluate(Robot robot) {
            return 0;
        }
    }
    public class barrelFBNode extends SensorNode implements  ExpressionNode{




        public barrelFBNode(ExpressionNode exp) {
            super();
        }
       // @Override
         //   public int execute(Robot robot) {
           //     return (int)robot.getClosestBarrelFB();

        //}
        @Override
        public int evaluate(Robot robot) {
            return 0;
        }
    }
    public class wallDistNode extends SensorNode implements  ExpressionNode{

        public int execute(Robot robot) {
            return robot.getDistanceToWall();
        }

        public int evaluate(Robot robot) {
            return execute(robot);
        }

        public String toString()  {
            return "Distance to wall";
        }
    }

}
