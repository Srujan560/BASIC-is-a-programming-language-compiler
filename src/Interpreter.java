import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Stack;
/**
 * In this class We Interpreter 
 * @author Srujan Vepuri
 *
 */
public class Interpreter {
	// Create 3 HasMap for the 3 data Storage variables 
	private HashMap<String,Integer> dataInt= new HashMap<>();
	private HashMap<String,Float> dataFloat=new HashMap<>();
	private HashMap<String,String>dataString = new HashMap<>();
	// Create HasMap for label statement
	private HashMap<String,Node>hasLabel= new HashMap<>();
	private ArrayList<Node>ast = new ArrayList<Node>();
	private Stack<Node> stak= new Stack<Node>();
	private Node currenteNode =null;

	public void interpret(StatementNode s) {
		StatementsNode statementNode = (StatementsNode)s;
		try {
			addToTheList(statementNode.getList());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	private void addToTheList(ArrayList<Node>l) {
		//System.out.println("The size is "+l.size()+" and "+l.get(0));
		for(Node n:l) {
			if(n!=null)
				ast.add(n);
		}
	}
	/**
	 * In there We set every StatementsNode next as reference
	 * @throws Exception 
	 */
	public void initialize() throws Exception {
		//System.out.println("Calling intitalize (For/Next) and also put in the next refernce");
		labelFinder();
		dataFinder();
		for (int x =0;x<ast.size();x++) {
			if(x+1<ast.size()) {// We want to make sure there is node after this
				try {
					StatementNode temp= (StatementNode)ast.get(x);
					temp.nextStatement=(StatementNode) ast.get(x+1);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					System.out.println("It look it not type StatmentNode");
					break;
				}
			}
			else {// if there is no next we put as null
				StatementNode temp= (StatementNode)ast.get(x);
				temp.nextStatement=null;
			}
		}
		boolean foundFor=false;
		ForNode tempForNode=null;
		for(int x=0;x<ast.size();x++) {
			if(ast.get(x).getClass().equals(ForNode.class)) {
				tempForNode = (ForNode)ast.get(x);
				foundFor=true;
			}
			else if(ast.get(x).getClass().equals(NextNode.class)) {
				if(foundFor==true) {
					foundFor=false;
					if(x+1<ast.size()) {
						forFinde(tempForNode,ast.get(x+1), (NextNode)ast.get(x));

					}
					else
						System.out.println("There is not Node after next");
				}else
					System.out.println("It was not able to Find ForNode but Found Next It is Out Of bouns");
			}
			else if (ast.get(x).getClass().equals(AssignmentNode.class)&&foundFor==false)// this for assigStateNode
				assigStateNode(ast.get(x));
			else if(ast.get(x).getClass().equals(InputNode.class)&&foundFor==false)
				input((InputNode)ast.get(x));
			else if (ast.get(x).getClass().equals(PrintNode.class)&&foundFor==false)
				print((PrintNode)ast.get(x));
			else if (ast.get(x).getClass().equals(FunctionNode.class)&&foundFor==false)
				function((FunctionNode)ast.get(x));
			else if( ast.get(x).getClass().equals(IfNode.class)&&foundFor==false)
				ifNode((IfNode)ast.get(x));
			else if (ast.get(x).getClass().equals(GOsubNode.class)&&foundFor==false) {
				int jump = goSub((GOsubNode)ast.get(x),ast.get(x+1));
				if(jump==-1)
					throw new Exception("Could No Find or was not able jump");
				else 
					x=jump;
			} else if(ast.get(x).getClass().equals(ReturnNode.class)&&foundFor==false) {

				int jump=returnNode();
				if(jump==-2)
					throw new Exception("Could Not Find Return ");
				else if (jump!=-1)
					x= jump;

			}else if (ast.get(x).getClass().equals(LabeledStatementNode.class)&&foundFor==false) {
				LabeledStatementNode l = (LabeledStatementNode)ast.get(x);
				Node tempNode= hasLabel.get(l.getLabel());
				this.helperFinderNode(tempNode);
			}



		}

	}
	private void helperFinderNode(Node n) throws Exception {

		if (n.getClass().equals(AssignmentNode.class))// this for assigStateNode
			assigStateNode(n);
		else if(n.getClass().equals(InputNode.class))
			input((InputNode)n);
		else if (n.getClass().equals(PrintNode.class))
			print((PrintNode)n);
		else if (n.getClass().equals(FunctionNode.class))
			function((FunctionNode)n);
		else if( n.getClass().equals(IfNode.class))
			ifNode((IfNode)n);
	}
	/**
	 * This to find Label walk
	 */
	public void labelFinder() {
		System.out.println("Calling label Finder");
		for(int x=0;x<ast.size();x++) {
			if(ast.get(x).getClass().equals(LabeledStatementNode.class)) {
				LabeledStatementNode labelNode= (LabeledStatementNode)ast.get(x);

				hasLabel.put(labelNode.getLabel(), labelNode.getStatement());
			}
		}
	}
	/**
	 * This also take care of our Next ref and ForNode next remembering next Node
	 * @param f - forNode
	 * @param n - is the next Node after next
	 * @param nk - is a Next
	 * @throws Exception 
	 */
	private void forFinde(ForNode f, Node n, NextNode nk) throws Exception {
		f.setNext(n);
		nk.setRef(f);
		//System.out.println("Next has assgined with forNode. Here is Next: "+f.getNext().toString());
		// here we going put the initial in our HashMap 
		IntegerNode initail = (IntegerNode)f.getintial();
		VariableNode var = (VariableNode)f.getVar();
		dataInt.put(var.getVariable(), initail.readNumber());
		IntegerNode max= (IntegerNode)f.getMaxNum();
		IntegerNode step = (IntegerNode)f.getStep();
		
		StatementNode current= (StatementNode)f;
		current = current.nextStatement;
		while(true) {
			if (dataInt.get(var.getVariable())==max.readNumber()) {
				break;
			}
			Node tempN = (Node)current;
			//System.out.println(tempN.getClass());
			if(tempN.getClass().equals(NextNode.class)) {// here we will know what when we reached our ending
				NextNode temp= (NextNode)current;
			//	System.out.println("Mine Next Node is "+ temp.getRef().getClass());
				
				current= (StatementNode)temp.getRef();
				current = current.nextStatement;
				//System.out.println("My next was "+ current.nextStatement.getClass());
				// now let increment 
				int ini= dataInt.get(var.getVariable())+step.readNumber();
				dataInt.put(var.getVariable(), ini);
				
			}else {
			
			if(!tempN.getClass().equals(ForNode.class))
			this.helperFinderNode(tempN);
			current = current.nextStatement;
			}
			
		}
		dataInt.remove(var.getVariable());
	}
	/**
	 * This look for Data and Read
	 * @throws Exception 
	 */
	public void dataFinder() throws Exception {
		System.out.println("Read/Data finder and Storage cap");
		ArrayList<String>listOfRead= new ArrayList<String>();
		for(int x=0;x<ast.size();x++) {// let get all Read variables 
			if(ast.get(x).getClass().equals(ReadNode.class)) {
				ReadNode temp = (ReadNode)ast.get(x);
				for(int i=0;i<temp.getList().size();i++) {
					VariableNode tempvar= (VariableNode)temp.getList().get(i);
					listOfRead.add(tempvar.getVariable());
				}
			}
		}
		// then we run this at Very Last
		for(int x=0;x<ast.size();x++) {
			if(ast.get(x).getClass().equals(DataNode.class)) {
				DataNode temp= (DataNode)ast.get(x);
				ArrayList<Node> tempList= temp.getList();
				for(int i=0;i<tempList.size();i++) {
					if(tempList.get(i).getClass().equals(IntegerNode.class)) {
						String str="";
						for(int z=0;z<listOfRead.size();z++) {

							if(listOfRead.get(z).matches("^[a-zA-Z]*$")) {
								//stem.out.println("I am here in the Int data read");
								str=listOfRead.get(z);
								//listOfRead.remove(i);
							}

						}
						IntegerNode tempInt= (IntegerNode)tempList.get(i);
						dataInt.put(str, (Integer)tempInt.readNumber());


					}else if(tempList.get(i).getClass().equals(StringNode.class)) {
						String tempStr="";
						for(int z=0;z<listOfRead.size();z++) {
							if(listOfRead.get(z).contains("$")) {

								tempStr=listOfRead.get(z);
								//listOfRead.remove(i);

							}


						}
						StringNode str= (StringNode)tempList.get(i);
						dataString.put(tempStr, str.getString());


					}else if(tempList.get(i).getClass().equals(FloatNode.class)) {
						String str="";
						for(int z=0;z<listOfRead.size();z++) {
							if(listOfRead.get(z).contains("%")) {
								str=listOfRead.get(z);
								//listOfRead.remove(i);

							}

						}
						FloatNode tempf= (FloatNode)tempList.get(i);
						dataFloat.put(str, tempf.getFloatNum());
					}
					else 
						throw new Exception("Look Read Does not match ");
				}
				ast.remove(x);// here we remove this statement
			}

		}
	}
	/**
	 * In here we will call if it AssignmentNode 
	 * It take care of MathOpNode both Integer and Float
	 * @param n - a node that will be turn into AssignmetNode
	 * @throws Exception - if not int or float or string 
	 */
	private void assigStateNode(Node n) throws Exception {
		AssignmentNode tempAssig = (AssignmentNode)n;
		// only where Something hold values 
		if(!tempAssig.getNode().getClass().equals(MathOpNode.class)) {// this for A = 3 
			System.out.print("System Knows It is a String = ");
			if(tempAssig.getNode().getClass().equals(IntegerNode.class)) {// if this type Integer 
				System.out.print("Integer. Now it is the Map.\n");
				IntegerNode temp = (IntegerNode)tempAssig.getNode();
				dataInt.put(tempAssig.getVariable(), temp.readNumber());
			}
			else {// than we It float
				System.out.print("Float. Now it is the Map.\n");
				FloatNode temp = (FloatNode)tempAssig.getNode();
				dataFloat.put(tempAssig.getVariable(), temp.getFloatNum());
			}

		} else if(tempAssig.getNode().getClass().equals(MathOpNode.class)) {// this for A= 2+ 3
			int num= mathOp((MathOpNode)tempAssig.getNode());
			if(num==1) {
				int tempInt = mathopNodeInt((MathOpNode)tempAssig.getNode());
				System.out.println("The result of Int opNode is "+tempInt+" Now it will be in Map");
				dataInt.put(tempAssig.getVariable(), tempInt);
			}else if(num==2) {
				float tempFloat =mathopNodefloat((MathOpNode)tempAssig.getNode());
				System.out.println("The result of Float opNode is "+tempFloat+" Now it will be in Map");
				dataFloat.put(tempAssig.getVariable(), tempFloat);
			}else
				throw new Exception("It is not int nor float");


		}
	}
	/**
	 * In here we will read What the user has to say 
	 * @param inputNode - taken in inputNode 
	 * @throws Exception - if not int/ float/ String than an error will throw 
	 */
	private void input(InputNode inputNode) throws Exception {
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(System.in);// We read from Console 
		for(int x=0; x<inputNode.getList().size();x++) {
			if(inputNode.getList().get(x).getClass().equals(StringNode.class)) {
				StringNode s = (StringNode)inputNode.getList().get(x);
				System.out.print(s.getString()+" ");
			}
			else {// if is variable Node that we wee need data
				VariableNode var = (VariableNode)inputNode.getList().get(x);
				if(var.getVariable().contains("$")) {
					String s = sc.next();
					dataString.put(var.getVariable(), s);


				}else if(var.getVariable().contains("%")) {
					Float f = sc.nextFloat();
					dataFloat.put(var.getVariable(), f);
				}
				else if(var.getVariable().matches("^[a-zA-Z]*$")){
					int i = sc.nextInt();
					dataInt.put(var.getVariable(),(Integer)i );

				}
				else 
					throw new Exception("We can only read String, Float, Integer only!");
			}

		}

		System.out.println("All the data is set in the map");
	}
	/**
	 * In were do print and read from Map also 
	 * @param printNode - will get List from PrintNode and will print String or get read Value from the map
	 * @throws Exception
	 */
	private void print(PrintNode printNode) throws Exception {
		for (int x=0;x<printNode.getList().size();x++) {
			if(printNode.getList().get(x).getClass().equals(StringNode.class)) {// if String type we just print 
				StringNode s =(StringNode) printNode.getList().get(x);
				System.out.print(s.getString()+" ");
			}else if(printNode.getList().get(x).getClass().equals(VariableNode.class)) {// if it is Variable that get it from the map
				VariableNode var = (VariableNode)printNode.getList().get(x);
				if(var.getVariable().contains("%")) {
					System.out.print(dataFloat.get(var.getVariable())+" ");
				}else if (var.getVariable().contains("$")) {
					System.out.print(dataString.get(var.getVariable())+" ");
				}else if (var.getVariable().matches("^[a-zA-Z]*$")) {
					System.out.print(dataInt.get(var.getVariable())+" ");
				}else
					throw new Exception("We can only read String, Float, Integer only!");


			}
			else // we just do String if it is not both of them 
				System.out.print(printNode.getList().get(x).toString());

		}
		System.out.println();
	}
	/**
	 * I already did this in my FunctionNode class. So, all I have to do is print message
	 * @param fun
	 */
	private void function(FunctionNode fun) {
		System.out.println(fun.getMessage());
	}
	/**
	 * In here figure out if it Integer or Float type by reading one left most value 
	 * @param m - just math Op Node
	 * @return
	 */
	private int mathOp(MathOpNode m){

		if(m.getLeft().getClass().equals(MathOpNode.class)) {
			return mathOp((MathOpNode)m.getLeft());
		}else if (m.getLeft().getClass().equals(IntegerNode.class)) {
			return 1; // this is for Int
		}else if(m.getLeft().getClass().equals(FloatNode.class))
			return 2;// this is for Float
		else if (m.getLeft().getClass().equals(VariableNode.class)) {
			VariableNode left= (VariableNode)m.getLeft();
			if(dataInt.containsKey(left.getVariable()))
				return 1;
			if (dataFloat.containsKey(left.getVariable()))
				return 2;
			System.out.println("It look like your Variable is not int or folat ");
			return 0;
		}

		return 0;
	}	
	/**
	 * In here we do math for int numbers 
	 * @param m
	 * @return
	 */
	private int mathopNodeInt(MathOpNode m) {
		int num = 0, left =0, right =0;

		if(m.getLeft().getClass().equals(MathOpNode.class)) {// we get the left most node 
			left=mathopNodeInt((MathOpNode)m.getLeft());
		}else if (m.getLeft().getClass().equals(VariableNode.class)) {
			VariableNode l  = (VariableNode)m.getLeft();
			left=dataInt.get(l.getVariable());
		}
		else {
			IntegerNode temp = (IntegerNode)m.getLeft();
			left=temp.readNumber();
		}
		if(m.getRight().getClass().equals(MathOpNode.class)) {// right most node
			right = mathopNodeInt((MathOpNode)m.getRight());
		}else if (m.getRight().getClass().equals(VariableNode.class)) {
			VariableNode r = (VariableNode)m.getRight();
			right =dataInt.get(r.getVariable());
		}
		else {
			IntegerNode temp = (IntegerNode)m.getRight();
			right =temp.readNumber();
		}
		// here we do math 
		if(m.getMathEnum()==MathOpNode.MathOpEnum.ADD) {
			num= left+right;
		}else if(m.getMathEnum()==MathOpNode.MathOpEnum.SUBTRACT) {
			num = left - right;
		}else if(m.getMathEnum()==MathOpNode.MathOpEnum.MULTIPLY) {
			num = left * right;
		}else if(m.getMathEnum()==MathOpNode.MathOpEnum.DIVIDE) {
			num = left/ right;
		}
		return num;
	}
	/**
	 * Same thing as the mathopNodeInt but this time for float
	 * @param m
	 * @return
	 */
	private float mathopNodefloat(MathOpNode m) {
		float num = 0, left =0, right =0;

		if(m.getLeft().getClass().equals(MathOpNode.class)) {
			left=mathopNodeInt((MathOpNode)m.getLeft());
		}else if (m.getLeft().getClass().equals(VariableNode.class)) {
			VariableNode l  = (VariableNode)m.getLeft();
			left=dataFloat.get(l.getVariable());
		}
		else {
			FloatNode temp = (FloatNode)m.getLeft();
			left=temp.getFloatNum();
		}
		if(m.getRight().getClass().equals(MathOpNode.class)) {
			right = mathopNodeInt((MathOpNode)m.getRight());
		}else if (m.getRight().getClass().equals(VariableNode.class)) {
			VariableNode r = (VariableNode)m.getRight();
			right =dataFloat.get(r.getVariable());
		}
		else {
			FloatNode temp = (FloatNode)m.getRight();
			right =temp.getFloatNum();
		}

		if(m.getMathEnum()==MathOpNode.MathOpEnum.ADD) {
			num= left+right;
		}else if(m.getMathEnum()==MathOpNode.MathOpEnum.SUBTRACT) {
			num = left - right;
		}else if(m.getMathEnum()==MathOpNode.MathOpEnum.MULTIPLY) {
			num = left * right;
		}else if(m.getMathEnum()==MathOpNode.MathOpEnum.DIVIDE) {
			num = left/ right;
		}
		return num;
	}
	/**
	 * In here we already did math op for boolean operation 
	 * @param If - It is IF Node have label and boolean poeration
	 * @return - true or false. if it is false than we skip the next boolean operation 
	 */
	private void ifNode(IfNode If) {

		System.out.println("Your Boolean Statement is "+ If.getBooleanStatement().getBoolean());
		if(If.getBooleanStatement().getBoolean()) {
			LabeledStatementNode l = (LabeledStatementNode)If.getLabel();
			if(dataInt.containsKey(l.getLabel())) {
				System.out.println("\t\tHashMap: "+l.getLabel()+", "+dataInt.get(l.getLabel()));
			}else if(dataFloat.containsKey(l.getLabel()))
				System.out.println("\t\tHashMap: "+l.getLabel()+", "+dataFloat.get(l.getLabel()));
			else if (dataString.containsKey(l.getLabel()))
				System.out.println("\t\tHashMap: "+l.getLabel()+", "+dataString.get(l.getLabel()));
			else if (hasLabel.containsKey(l.getLabel()))
				System.out.println("\t\tHashMap: "+l.getLabel()+", "+hasLabel.get(l.getLabel()).toString());
		}else
			System.out.println("It look like you do not have that Lable assigned yet");

		//return If.getBooleanStatement().getBoolean();
	}
	/**
	 * In here we you go that label using our hash map.  
	 * @param go - this has where to go in our hashmap
	 * @param next - This will be the next Node 
	 * @return 
	 * @throws Exception 
	 */
	private int goSub(GOsubNode go, Node next) throws Exception {

		stak.push(next);
		// now let our hashMap
		VariableNode v = (VariableNode)go.getVar();
		//System.out.println(v.getVariable()+" and +++++"+ hasLabel);
		if(hasLabel.containsKey(v.getVariable())==false) {
			System.out.println("It was not able find your LabelNode in the HashMap");
			return -1;
		}else {

			for(int x=0;x<ast.size();x++) {
				if(ast.get(x).getClass().equals(LabeledStatementNode.class)) {
					LabeledStatementNode tempL= (LabeledStatementNode)ast.get(x);
					if(tempL.getLabel().equals(v.getVariable())) {
						return x-1;

					}
				}
			}
		}
		return -1;

	}
	private int returnNode() {
		if(stak.size()==0)
			return -1;
		Node temp = stak.pop();
		for (int x=0;x<ast.size();x++) {
			if (temp==ast.get(x))
				return x-1;
		}
		return -2;

	}

	/**
	 * This to show that Each class remembers the list
	 */
	public void test() {
		Node getNext=ast.get(0);
		while(true) {
			if(getNext!=null) {
				System.out.print(getNext.getClass()+" -> ");
				StatementNode s = (StatementNode)getNext;
				getNext=s.nextStatement;
			}
			else {
				System.out.print("null");
				break;
			}
		}
	}

}
