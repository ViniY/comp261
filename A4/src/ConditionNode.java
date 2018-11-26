public class ConditionNode implements ConditionalNode {


    @Override
    public boolean evaluate(Robot robot) {
        return false;
    }

    public class OrNode implements  ConditionalNode {
        ExpressionNode exp1;
        ExpressionNode exp2;
        public OrNode(ExpressionNode exp1, ExpressionNode exp2) {
            this.exp1 = exp1;
            this.exp2 = exp2;
        }

        @Override
        public boolean evaluate(Robot robot) {
            return false;
        }
    }
    public class AndNode extends ConditionNode implements  ConditionalNode {
        ExpressionNode exp1;
        ExpressionNode exp2;

        public AndNode(ExpressionNode exp1, ExpressionNode exp2) {
            this.exp1 = exp1;
            this.exp2 = exp2;
        }

        @Override
        public boolean evaluate(Robot robot) {
            return false;
        }
    }



        public class GreaterNode extends ConditionNode implements ConditionalNode {
            ExpressionNode exp1;
            ExpressionNode exp2;

            public GreaterNode(ExpressionNode exp1, ExpressionNode exp2) {
                this.exp1 = exp1;
                this.exp2 = exp2;
            }

            @Override
            public boolean evaluate(Robot robot) {
                return(exp1.evaluate(robot)>exp2.evaluate(robot));
            }


        }

        public class LessNode extends ConditionNode implements ConditionalNode {
            ExpressionNode exp1;
            ExpressionNode exp2;

            public LessNode(ExpressionNode exp1, ExpressionNode exp2) {
                this.exp1 = exp1;
                this.exp2 = exp2;
            }

            @Override
            public boolean evaluate(Robot robot) {
                return(exp1.evaluate(robot)<exp2.evaluate(robot));
            }


        }

        public class EqualNode  implements ConditionalNode {
            ExpressionNode exp1;
            ExpressionNode exp2;

            public EqualNode(ExpressionNode exp1, ExpressionNode exp2) {
                this.exp1 = exp1;
                this.exp2 = exp2;
            }

            @Override
            public boolean evaluate(Robot robot) {
                return(exp1.evaluate(robot)==exp2.evaluate(robot));
            }

        }

        public class NotNode extends ConditionNode implements ConditionalNode {
            ExpressionNode exp1;

            public NotNode(ExpressionNode exp1) {
                this.exp1 = exp1;

            }

            @Override
            public boolean evaluate(Robot robot) {
                return false;
            }

            public String toString() {
                return ("notNode");
            }


        }


    }





