import com.sun.javafx.tk.TKClipboard;

import java.nio.channels.AcceptPendingException;

public class ACTNode implements RobotProgramNode {
    ACTNode actNode;
    public ACTNode(ACTNode actNode){
        this.actNode=actNode;
    }
    public ACTNode() {

    }
    public String toString(){
        return (this.actNode.toString());
    }

    @Override
    public void execute(Robot robot) {

    }

    static class moveNode extends ACTNode implements  RobotProgramNode{
        public moveNode(ACTNode actNode) {
            super(actNode);
        }

        public moveNode() {
            super();
        }

        //  Expression exp;
       // moveNode(){
       //     this.exp=expression;
     //  }
        @Override
        public void execute(Robot robot) {
               robot.move();
        }

        @Override
        public String toString() {
            return "moveNode{}";
        }
    }
    static  class turnLNode extends ACTNode implements  RobotProgramNode{
        turnLNode(turnLNode actNode){
            super(actNode);
        }

        public turnLNode() {

        }

        @Override
        public void execute(Robot robot) {
            robot.turnLeft();
        }
        public String toString(){
            return "Robot turn Left";
        }
    }
    static  class turnRNode extends ACTNode implements  RobotProgramNode{
        turnRNode(turnRNode actNode){
            super(actNode);
        }

        public turnRNode() {

        }

        @Override
        public void execute(Robot robot) {
            robot.turnRight();
        }
        public String toString(){
            return "Robot turn Right";
        }
    }
    static  class takeFuelNode extends  ACTNode implements RobotProgramNode{
        takeFuelNode(takeFuelNode actNode){
            super(actNode);
        }

        public takeFuelNode() {

        }

        @Override
        public void execute(Robot robot) {
            robot.takeFuel();
        }
        public String toString(){
            return "Robot taking fuel";
        }
    }
    static  class waitNode extends ACTNode{
        waitNode(waitNode actNode){
            super(actNode);
        }

        public waitNode() {

        }

        public void execute(Robot robot){
            robot.idleWait();
        }

        @Override
        public String toString() {
            return "waitNode";
        }
    }
    static  class shieldOn extends ACTNode implements RobotProgramNode{
        shieldOn(shieldOn shieldOn){
            super(shieldOn);
        }

        public shieldOn() {

        }

        @Override
        public void execute(Robot robot) {
            robot.setShield(true);
        }
        public String toString(){
            return("turn on the shield");
        }
    }
    static  class shieldOff extends ACTNode implements RobotProgramNode{
        shieldOff(shieldOff shieldoff){
            super(shieldoff);
        }

        public shieldOff() {

        }

        @Override
        public void execute(Robot robot) {
            robot.setShield(false);
        }
        public String toString(){
            return("turn off the shield");
        }
    }
}
