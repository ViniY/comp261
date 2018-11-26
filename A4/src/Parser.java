import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.locks.Condition;
import java.util.regex.*;
import javax.swing.JFileChooser;


/**
 * The parser and interpreter. The top level parse function, a main method for
 * testing, and several utility methods are provided. You need to implement
 * parseProgram and all the rest of the parser.
 */
public class Parser {


	/**
	 * Top level parse method, called by the World
	 */
	static RobotProgramNode parseFile(File code) {
		Scanner scan = null;
		try {
			scan = new Scanner(code);

			// the only time tokens can be next to each other is
			// when one of them is one of (){},;
			scan.useDelimiter("\\s+|(?=[{}(),;])|(?<=[{}(),;])");

			RobotProgramNode n = parseProgram(scan); // You need to implement this!!!

			scan.close();
			return n;
		} catch (FileNotFoundException e) {
			System.out.println("Robot program source file not found");
		} catch (ParserFailureException e) {
			System.out.println("Parser error:");
			System.out.println(e.getMessage());
			scan.close();
		}
		return null;
	}

	/** For testing the parser without requiring the world */

	public static void main(String[] args) {
		if (args.length > 0) {
			for (String arg : args) {
				File f = new File(arg);
				if (f.exists()) {
					System.out.println("Parsing '" + f + "'");
					RobotProgramNode prog = parseFile(f);
					System.out.println("Parsing completed ");
					if (prog != null) {
						System.out.println("================\nProgram:");
						System.out.println(prog);
					}
					System.out.println("=================");
				} else {
					System.out.println("Can't find file '" + f + "'");
				}
			}
		} else {
			while (true) {
				JFileChooser chooser = new JFileChooser(".");// System.getProperty("user.dir"));
				int res = chooser.showOpenDialog(null);
				if (res != JFileChooser.APPROVE_OPTION) {
					break;
				}
				RobotProgramNode prog = parseFile(chooser.getSelectedFile());
				System.out.println("Parsing completed");
				if (prog != null) {
					System.out.println("Program: \n" + prog);
				}
				System.out.println("=================");
			}
		}
		System.out.println("Done");
	}

	// Useful Patterns
	static Pattern NUMPAT = Pattern.compile("-?\\d+"); // ("-?(0|[1-9][0-9]*)");
	static Pattern OPENPAREN = Pattern.compile("\\(");
	static Pattern CLOSEPAREN = Pattern.compile("\\)");
	static Pattern OPENBRACE = Pattern.compile("\\{");
	static Pattern CLOSEBRACE = Pattern.compile("\\}");
	static Pattern SEMICOLOUMN = Pattern.compile(";");
	static Pattern ACT = Pattern.compile("move|turnL|turnR|takeFuel|wait|shieldOn|shieldOff");
	static Pattern Loop = Pattern.compile("loop");
	static Pattern IF = Pattern.compile("if");
	static Pattern WHILE = Pattern.compile("while");
	static Pattern COND = Pattern.compile("RELOP|\\(|SEN|NUM|\\)");
	static Pattern RELOP = Pattern.compile("lt|gt|eq");
	static Pattern SEN = Pattern.compile("fuelLeft|oppLR|oppFB|numBarrels|barrelLR|barrelFB|wallDist");
	static Pattern COMMA = Pattern.compile(",");

	/**
	 * PROG ::= STMT+
	 */
	static RobotProgramNode parseProgram(Scanner s) {
		// THE PARSER GOES HERE
		ArrayList<STMTNode> statements = new ArrayList<>();
		while(s.hasNext()){
			statements.add(parseStmt(s));
		}
		return new ProgramNode(statements);
	}

	static STMTNode parseStmt(Scanner s){
		if(s.hasNext(ACT)){
			 STMTNode parsedStmt= new STMTNode(parseAction(s)) ;
			 require(SEMICOLOUMN,"syntex error statement not completed",s);//check statement completed
			return parsedStmt;
		}
		 if(s.hasNext(Loop)){
 			STMTNode parsedStmt= new STMTNode(parseLoop(s));
			return parsedStmt;
		}
		if(s.hasNext(IF)){
			STMTNode parseStmt = new STMTNode(parseIf(s));
			return parseStmt;
		}
		 if(s.hasNext(WHILE)){
		 	s.next();//move the cursor
			STMTNode parseStmt = new STMTNode(pareseWhile(s));
			return parseStmt;
		}
		fail("no Actions or Loop to parse",s);
		return null;
	}
	private static whileNode pareseWhile(Scanner s){
		require(OPENPAREN,"no Openparen found for while loop",s);
		whileNode whileNode = new whileNode(parseCon(s));
		require(CLOSEPAREN,"no CLOSEPAREN found for while loop",s);
		whileNode.setBlock(parseBlock(s));
		return whileNode;
	}

	public static BlockNode parseBlock(Scanner s) {
		require(OPENBRACE,"no open brace for block",s);
		BlockNode block = new BlockNode();
		while(!s.hasNext(CLOSEBRACE)){
			block.addNode(parseStmt(s));
		}
		if(block.statements.size()==0) fail("nothing parsed in the block",s);
		require(CLOSEBRACE,"no Close Brace found for block construction",s);
		return block;
	}

	private static IfNode parseIf(Scanner s) {
		int count=0;//count how many conditions to match the close brace
		if(s.hasNext("if")||s.hasNext("elif")){
			s.next();//move the pointer pass if
			if(s.hasNext(OPENPAREN)){
				s.next(); //move the pointer
//				if(s.hasNext(COND)) {multiple condition
//					ConditionNode cond = (ConditionNode) parseCon(s);
//				}
				IfNode ifNode = new IfNode(parseCon(s));
				if(s.hasNext(CLOSEPAREN)){
					s.next();// moving pointer
					ifNode.setBlock(parseBlock(s));
					return ifNode;
				}
				fail("no close paren found for if ", s);
			}
			fail("no open paren found for if",s);
		}
		return null;
	}


	static ConditionalNode parseCon(Scanner s){
		if(s.hasNext("lt")){
			s.next(); //moving the pointer
			require(OPENPAREN,"no OPen paren founded for if statement",s);
			ExpressionNode exp1 = parseExp(s);
			if(s.hasNext(",")){
				s.next();
				ExpressionNode exp2 = parseExp(s);
				require(CLOSEPAREN,"syntex error on the close of if statement", s);
				ConditionNode lessthanNode = new ConditionNode().new LessNode(exp1,exp2);
				return (ConditionalNode) lessthanNode;
			}
			fail("no , found no enough arguments",s);
		}
		else if(s.hasNext("gt")){
			s.next();//move pointer
			require(OPENPAREN,"No Open paren founded for if statement",s);
			ExpressionNode exp1 = parseExp(s);
			//System.out.println(exp1.toString());
			//s.next();
			//require(",","no enough statement",s);
			//if(s.hasNext(",")){
				//s.next();
			require(COMMA,"no Comma here",s);
				ExpressionNode exp2 = parseExp(s);
				require(CLOSEPAREN,"No close paren found for if statement", s);
				ConditionNode greaterthanNode = new ConditionNode().new GreaterNode(exp1,exp2);
				return (ConditionalNode) greaterthanNode;
		//	}
			//else{fail("not enough argument for condition statement",s);}
		}
		else if(s.hasNext("eq")){
			s.next();
			require(OPENPAREN,"No open paren founded for if statement",s);
			ExpressionNode exp1 = parseExp(s);
			if(s.hasNext(",")){
				s.next();
				ExpressionNode exp2 = parseExp(s);
				require(CLOSEPAREN,"No close paren found  for if statement",s);
				ConditionalNode eqNode = new ConditionNode().new EqualNode(exp1,exp2);
				return eqNode;
			}
			fail("Not enough argument",s);
		}
		else if(s.hasNext("and")){
			s.next();
			require(OPENPAREN,"No open paren founded for if statement",s);
			ExpressionNode exp1 = parseExp(s);
			if(s.hasNext(",")){
				s.next();
				ExpressionNode exp2 = parseExp(s);
				require(CLOSEPAREN,"No close paren found for if statement", s);
				ConditionalNode andNode = new ConditionNode().new AndNode(exp1,exp2) ;
				return andNode;
			}
			fail("not enough arguments for if statement",s);
		}
		else if(s.hasNext("or")){
			s.next();
			require(OPENPAREN,"No open paren founded for if statement",s);
			ExpressionNode exp1 = parseExp(s);
			if(s.hasNext(",")){
				s.next();
				ExpressionNode exp2 = parseExp(s);
				require(CLOSEPAREN,"No close paren found for if statement", s);
				ConditionalNode orNode = new ConditionNode().new OrNode(exp1,exp2) ;
				return orNode;
			}
			fail("not enough arguments for if statement",s);
		}
		else if(s.hasNext("not")){
			s.next();
			require(OPENPAREN,"No open paren found for if statement",s);
			ExpressionNode exp1 = parseExp(s);
			require(CLOSEPAREN,"No close paren found for if statement",s);
			ConditionNode notNode = new ConditionNode().new NotNode(exp1);
			return (ConditionalNode) notNode;
		}
		fail("Invalid condition expression",s);
		return null;
	}

	static ExpressionNode parseExp(Scanner s) {
		if(s.hasNext("-?[0-9]+")){
			return new NumNode(s.nextInt());
		}
		else if(s.hasNext(SEN)){
			//s.next();

			return (ExpressionNode) parseSen(s);
		}
		return null;
	}

	static SensorNode parseSen(Scanner s) {
		if(s.hasNext("fuelLeft")){
			s.next();
			// System.out.print(s.next()+"asdaskjdhjahdjahd");
			SensorNode node = new SensorNode().new feulLeftNode();
			return node;
		}
		else if(s.hasNext("oppLR")){
			s.next();
			SensorNode node = new SensorNode().new oppLRNode();
			return node;
		}
		else if(s.hasNext("oppFB")){
			s.next();
			SensorNode node = new SensorNode().new oppFBNode();
			return node;
		}
		else if(s.hasNext("numBarrels")){
			s.next();
			SensorNode node = new SensorNode().new numBarrelsNode();
			return node;
		}
		else if(s.hasNext("barreLLR")){
			s.next();
			require(OPENPAREN,"no OPENPAREN found for barrlLR",s);
			ExpressionNode exp = parseExp(s);
			require(CLOSEPAREN,"no CLOSEPAREN founded for barrerlLR",s);
			SensorNode node = new SensorNode().new barrelLRNode(exp);
			return node;
		}
		else if(s.hasNext("barrelFB")){
			s.next();
			require(OPENPAREN,"no OPENPAREN found for barrlFB",s);
			ExpressionNode exp = parseExp(s);
			require(CLOSEPAREN,"no CLOSEPAREN founded for barrerlFB",s);
			SensorNode node = new SensorNode().new barrelFBNode(exp);
			return node;
		}
		else if(s.hasNext("wallDist")){
			s.next();
			SensorNode node = new SensorNode().new wallDistNode();
			return node;
		}
		return null; // to compile
	}



	static LoopNode parseLoop(Scanner s) {
		ArrayList<STMTNode> statements = new ArrayList<>();
		require(Loop,"loop starts",s);
		require(OPENBRACE, "loop does not have open brace", s);
		if(s.hasNext(CLOSEBRACE))fail("nothing in the loop",s);
		while(!s.hasNext(CLOSEBRACE)){
			statements.add(parseStmt(s));
		}

		require(CLOSEBRACE,"loop doesnot have close brace",s );
				BlockNode block = new BlockNode(statements);
		return new LoopNode(block);
	}
	public ExpressionNode parseExpression(Scanner s){
		if(s.hasNext("-?[1-9]+")){
			return new NumNode(s.nextInt());
		}
		else if(s.hasNext(SEN)){
			return paresOperator(s);
		}
		else if(s.hasNext("add")||s.hasNext("sub")||s.hasNext("mul")||s.hasNext("div")){
			return paresOperator(s);
		}
		//return new ExpressionNode();
		return null;
	}

	private ExpressionNode paresOperator(Scanner s) {
		return null;
	}


	static ACTNode parseAction(Scanner s) {//parsing actions(move, turnleft turn right take_feul wait)
		if (s.hasNext("move")) {//move statement
			s.next();
			return new ACTNode.moveNode();
		}

		else if(s.hasNext("turnL")){//turn left
			s.next();
			return new ACTNode.turnLNode();

		}
		else if(s.hasNext("turnR")){//turn right
			s.next();
			return new ACTNode.turnRNode();
		}
		else if(s.hasNext("takeFuel")){//take feul
			s.next();
			return new ACTNode.takeFuelNode();
		}
		else if(s.hasNext("wait")){//wait
			s.next();

			/*if(s.hasNext(OPENPAREN)){
				s.next();
				ACTNode nd = new ACTNode.waitNode(parseExpression(s));
				if(s.hasNext(CLOSEPAREN)){
					s.next();
					return nd;
				}
				fail("No close parenthesis foundecd after expression",s);
			}
			return new ACTNode.waitNode();// no right type expression find return a null wait node.*/
			return new ACTNode.waitNode();
		}
		else if(s.hasNext("shieldOn")){
			s.next();
			return new ACTNode.shieldOn();
		}
		else if(s.hasNext("shieldOff")){
			s.next();
			return new ACTNode.shieldOff();
		}
		return new ACTNode();

	}

	// utility methods for the parser

	/**
	 * Report a failure in the parser.
	 */
	static void fail(String message, Scanner s) {
		String msg = message + "\n   @ ...";
		for (int i = 0; i < 5 && s.hasNext(); i++) {
			msg += " " + s.next();
		}
		throw new ParserFailureException(msg + "...");
	}

	/**
	 * Requires that the next token matches a pattern if it matches, it consumes
	 * and returns the token, if not, it throws an exception with an error
	 * message
	 */
	static String require(String p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	static String require(Pattern p, String message, Scanner s) {
		if (s.hasNext(p)) {
			return s.next();
		}
		fail(message, s);
		return null;
	}

	/**
	 * Requires that the next token matches a pattern (which should only match a
	 * number) if it matches, it consumes and returns the token as an integer if
	 * not, it throws an exception with an error message
	 */
	static int requireInt(String p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	static int requireInt(Pattern p, String message, Scanner s) {
		if (s.hasNext(p) && s.hasNextInt()) {
			return s.nextInt();
		}
		fail(message, s);
		return -1;
	}

	/**
	 * Checks whether the next token in the scanner matches the specified
	 * pattern, if so, consumes the token and return true. Otherwise returns
	 * false without consuming anything.
	 */
	static boolean checkFor(String p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

	static boolean checkFor(Pattern p, Scanner s) {
		if (s.hasNext(p)) {
			s.next();
			return true;
		} else {
			return false;
		}
	}

}

// You could add the node classes here, as long as they are not declared public (or private)

class ProgramNode implements  RobotProgramNode{//program node start
    ArrayList<STMTNode> statementNode;
	public ProgramNode(ArrayList<STMTNode> children){
		this.statementNode = children;
	}



    @Override
	public void execute(Robot robot) {
		for (int i=0; i < this.statementNode.size();i++){//iterate the nodes in the array list
			this.statementNode.get(i).execute(robot);
	}
	}
	public void  addNode(STMTNode newStatement){
		this.statementNode.add(newStatement);
	}
	public String toString() {// print  the children arraylist
		String str = "";
		for (int i = 0; i < this.statementNode.size(); i++){
			str += this.statementNode.get(i).toString() + "\n";
		}
		return str;
	}
}