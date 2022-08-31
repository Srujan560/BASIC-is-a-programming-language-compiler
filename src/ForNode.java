/**
 * 
 * @author Srujan Vepuri
 *
 */
public class ForNode extends StatementNode{
	private Node var, Next;
	private Node intailNum;
	private Node maxNum;
	private Node step;
	public StatementNode nextStatement;
	
	public ForNode(Node v, Node i, Node m, Node s){
		var=v;
		intailNum=i;
		maxNum=m;
		if(s==null)
			step= new IntegerNode(1);// here we assume it default so we set to 1
		else
			step=s;
		
	}
	public Node getNext() {
		return Next;
	}
	public void setNext(Node next) {
		Next = next;
	}
	/**
	 * We can run the loop 
	 */
	
	public void runloop() {
		IntegerNode intail= (IntegerNode)this.intailNum;
		IntegerNode max = (IntegerNode)maxNum;
		IntegerNode STEP= (IntegerNode)step;
		if(intail.readNumber()<=max.readNumber()) {
			
		}else {
			int tempNum= intail.readNumber()+STEP.readNumber();
			VariableNode tempVar= (VariableNode)var;
			System.out.println(tempVar.getVariable()+" = "+ tempNum);
			this.intailNum= new IntegerNode(tempNum);// here 
		}
			
	}
	public Node getintial() {
		return intailNum;
	}
	public Node getMaxNum() {
		return maxNum;
	}
	public Node getStep() {
		return step;
	}
	public Node getVar() {
		return var;
	}
	/**
	 * This for TO String method
	 */
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "ForNode("+var.toString()+", inital Value "+this.intailNum.toString()+", Goes Up to "+ maxNum.toString()
		+", Step "+step.toString()+")";
	}

}
