import java.util.ArrayList;
import java.util.Random;
/**
 * This function holds name  and list 
 * @author Srujan Vepuri
 *
 */
public class FunctionNode extends StatementNode{
	private String nameFunction;
	private ArrayList<Node>list;
	public FunctionNode(String n, ArrayList<Node>l) {
		nameFunction=n;
		list=l;
		runFunction();
	}
	private String message;// this for function 
	public String getMessage() {
		return message;
	}
	private void runFunction() {
		if(nameFunction.equals("RANDOM"))
			randnom();
		else if(nameFunction.equals("LEFT$"))
			left();
		else if(nameFunction.equals("RIGHT$"))
			right();
		else if(nameFunction.equals("NUM$"))
			num();
		else if(nameFunction.equals("VAL"))
			valInt();
		else if(nameFunction.equals("VAL%"))
			valFloat();

	}
	private void randnom() {
		Random r = new Random();
		//IntegerNode n = new IntegerNode(r.nextInt());
		int rand= r.nextInt();
		message="Random number is "+rand;
	}
	private void left() {
		boolean foundString=false;
		for(Node n:list) {
			if(n.getClass().equals(StringNode.class)) {
				StringNode temp= (StringNode)n;
				message="The left most String is from that List is "+temp.getString().charAt(0);
				foundString=true;
				break;
			}
		}
		if(foundString==false)
			message="There Was no String in that list ";
	}
	private void right() {
		boolean foundString=false;
		for(int x=list.size()-1;x>=0;x--) {
			if(list.get(x).getClass().equals(StringNode.class)) {
				StringNode temp= (StringNode)list.get(x);
				message="The Right most String is from that List is "+temp.toString().charAt(0);
				foundString=true;
				break;
			}
		}
		if(foundString==false)
			message="There Was no String in that list ";
	}
	private void num() {
		boolean wasConverted=false;
		
		StringNode str = (StringNode)list.get(0);
		try {
			int n = Integer.parseInt(str.getString());
			message="It has Now is Int "+n;
			wasConverted=true;
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		if(wasConverted==false) {
			try {
				float f = Float.parseFloat(str.getString());
				message="It has Now is float "+f;
				wasConverted=true;
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
		if(wasConverted==false)
			message="It is not Int or Float";
	}
	private void valInt() {
		StringNode str = (StringNode)list.get(0);
		try {
			int n = Integer.parseInt(str.getString());
			message="It has Now is Int "+n;
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			message="It was not albe to convert to INT";
		}
	}
	private void valFloat() {
		StringNode str = (StringNode)list.get(0);
		try {
			float f = Float.parseFloat(str.getString());
			message="It has Now is float "+f;
			
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			message="It was not albe to convert to FLOAT";
		}
	}
	public String getFucntionName() {
		return nameFunction;
	}
	public ArrayList<Node> getList(){
		return list;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		String s="The list in Function is: ";
		for(Node n:list) {
			if(n!=null)
				s+=n.toString()+" ";
		}
		if(list.size()==0)
			s+="Is empty";

		return "FunctionNode(Function is "+nameFunction+" "+s+"\nThe message is " +message;
	}

}
