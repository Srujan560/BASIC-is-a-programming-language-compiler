
public class MathOpNode extends Node{
	/**
	 * In Here we have Math Enum
	 * @author Srujan Vepuri
	 *
	 */
	enum MathOpEnum{

		ADD("+"), SUBTRACT("-"), MULTIPLY("*"), DIVIDE("/");
		private String op="";
		private MathOpEnum(String s) {
			op=s;
		}
		public String getEnum() {
			return op;
		}


	}
	private Node right;
	private Node left;
	private String mathOp;
	public MathOpNode(String s, Node r, Node left) {
		mathOp=s;
		this.left=left;
		right =r;
		setToken();
		checkForPEDMAS();


	}
	/**
	 *
	 * @return - get the right side of the node
	 */
	public Node getRight() {
		return right;
	}


	public Node getLeft() {
		return left;
	}

	@Override
	public String toString() {
		
		/*
		if(left.getClass().equals(MathOpNode.class)) {
			String s= "["+left.toString()+"]";
			return s+" -> ("+mathOp+") <- "+right.toString();
		}
		else if(right.getClass().equals(MathOpNode.class)) {
			String s= "["+right.toString()+"]";
			return left.toString()+" -> ("+mathOp+") <- "+s;

		}
		return left.toString()+" -> ("+mathOp+") <- "+right.toString();*/
		
		return "MathOpNode("+mathOp+","+left.toString()+","+right.toString()+") ";

	}
	public MathOpEnum getMathEnum() {
		return opEnum;
	}
	
	/**
	 * Here We set our current math sign 
	 */
	private MathOpEnum opEnum;
	private void setToken() {
		if(mathOp.equals("+"))
			opEnum= MathOpEnum.ADD;
		else if (mathOp.endsWith("-"))
			opEnum= MathOpEnum.SUBTRACT;
		else if (mathOp.endsWith("*"))
			opEnum= MathOpEnum.MULTIPLY;
		else if (mathOp.endsWith("/"))
			opEnum= MathOpEnum.DIVIDE;


	}
	/**
	 * This will check for PEDMAS 
	 * if finds + or -
	 * if the current is * or /
	 * than we need to do PEDMAS
	 * So we need to remove and make changes
	 */
	private void checkForPEDMAS() {

		if(left.getClass().equals(MathOpNode.class)) {
			MathOpNode leftOp = (MathOpNode)left;
			MathOpEnum tempEnumAdd= MathOpEnum.ADD;
			MathOpEnum tempEnumSub= MathOpEnum.SUBTRACT;

			if(leftOp.getMathEnum()==tempEnumAdd||leftOp.getMathEnum()==tempEnumSub) {
				MathOpEnum tempEnumMult= MathOpEnum.MULTIPLY;
				MathOpEnum tempEnumDiv= MathOpEnum.DIVIDE;
				if(opEnum==tempEnumMult||opEnum==tempEnumDiv) {
					Node tempRight=leftOp.getRight();
					String token="";
					if(opEnum==tempEnumMult)
						token="*";
					else
						token="/";
					MathOpNode rightSide= new MathOpNode(token,right,tempRight);
					opEnum=leftOp.getMathEnum();
					if(leftOp.getMathEnum()==tempEnumAdd) {
						mathOp="+";
					}
					else
						mathOp="-";
					setToken();
					left=leftOp.getLeft();
					right= rightSide;
				}
			}

		}

	}

}
