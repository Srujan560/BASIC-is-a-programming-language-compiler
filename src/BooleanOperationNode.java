/**
 * Hold 2 expression and symbol
 * @author Srujan Vepuri
 *
 */
public class BooleanOperationNode extends Node{
	private Node firstExp, secondExep;
	private String symbol;
	private boolean statementCheck;
	public BooleanOperationNode(Node f, String s, Node o) {
		firstExp=f;
		symbol=s;
		secondExep=o;
		check();
	}
	public boolean getBoolean() {
		return statementCheck;
	}
	/**
	 * Here We check For number that operator
	 */
	private void check() {
		if(firstExp.getClass().equals(IntegerNode.class)) {
			IntegerNode tempInt= (IntegerNode)firstExp;
			IntegerNode otherInt=(IntegerNode)secondExep;
			if(symbol.equals("<")) {
				if(tempInt.readNumber()<otherInt.readNumber())
					statementCheck=true;
				else
					statementCheck=false;
			}
			if(symbol.equals("<=")) {
				if(tempInt.readNumber()<= otherInt.readNumber())
					statementCheck=true;
				else
					statementCheck=false;
			}
			if(symbol.equals(">")) {
				if(tempInt.readNumber()> otherInt.readNumber())
					statementCheck=true;
				else
					statementCheck=false;
			}
			if(symbol.equals(">=")) {
				if(tempInt.readNumber() >=otherInt.readNumber())
					statementCheck=true;
				else
					statementCheck=false;
			}
			if(symbol.equals("=")) {
				if(tempInt.readNumber()==otherInt.readNumber())
					statementCheck=true;
				else
					statementCheck=false;
			}
			if(symbol.equals("<>")) {
				if(tempInt.readNumber() !=otherInt.readNumber())
					statementCheck=true;
				else
					statementCheck=false;
			}
		}
	}
	public Node getFirstExp() {
		return firstExp;
	}
	public Node getSecondExp() {
		return secondExep;
	}
	public String getSymbol() {
		return symbol;
	}
	@Override
	public String toString() {
		return "BooleanOperationNode("+ firstExp.toString()+" "+symbol+" "+secondExep.toString()+" IS "+statementCheck+")"; 
	}
}
