import java.util.ArrayList;
import java.util.List;
/**
 * 
 * @author Srujan Vepuri
 *
 */
public class Parser {
	private List<Token> token;
	//private ArrayList<Node>list;
	public Parser(List<Token> temp) {
		token =temp;
	}

	public Node parser() throws Exception {

		return this.Statements();
	}
	/**
	 * Well mine checks for tokens but does not remove it 
	 * @param tk - need Token Enums
	 * @return Token which is real ignored but it very important for the null
	 */
	private Token.token matchAndRemove(Token.token tk) {
		if(tk==token.get(0).getToken())
			return tk;
		return null;
	}
	/**
	 * In this method we call Statement()
	 * @return - null for now
	 * @throws Exception- if any thing happens from other method or Node Class 
	 */
	private Node Statements() throws Exception {
		ArrayList<Node>list=new ArrayList<Node>();// this where have list to keep our Printlist
		Node AST=Statement(list);// Let first try
		list.add(AST);// we just add it our list
		if(token.size()!=0) {

			do {// let keep trying even though my list could be empty

				AST=Statement(list);

				if(AST!=null&&!AST.getClass().equals(FloatNode.class)){// here we will pass in dummy node so we do not add twice to 
					// our list
					list.add(AST);
				}
			}
			while(AST!=null);// if AST is null then loop will stop 
		}
		StatementsNode tempStateNode= new StatementsNode(list);
		if(list.size()!=0)
			//System.out.println(tempStateNode.toString());
		if(token.size()==1&&matchAndRemove(Token.token.RPAREN)!=null)
			token.remove(0);
		return tempStateNode;
	}
	/**
	 * This only look for 2 things is print or comma token and it looks for Identifier and much more 
	 * @param list - for PrintList Method
	 * @return - PrintStatement or Assignment() or null if not both of them
	 * @throws Exception - any error message from other Node class or methods()
	 */
	private Node Statement(ArrayList<Node> list) throws Exception {
		try {// we check if there is more to the list if there is than we keep going if the token size == 0 than return null
			if(matchAndRemove(Token.token.LABEL)!=null) {// if it is Token Type Label 
				String label= token.get(0).getString();// here save the label 
				token.remove(0);// this remove label 
				Node tempState = Statement(list);
				if(tempState==null)// if statement is null then we know this wrong 
					throw new Exception("It look the Label is used incorrectly. Label need to followed by a Statement.");
				else 
					return new LabeledStatementNode(tempState, label);// then we know it can only one Statement
			}
			if(matchAndRemove(Token.token.PRINT)!=null||this.matchAndRemove(Token.token.COMMA)!=null||
					matchAndRemove(Token.token.STRING)!=null) {
				return PrintStatement();
			}
			if(matchAndRemove(Token.token.IDENTIFIER)!=null) {// we know that Assignment the first things as to be an Identifier

				return Assignment();
			}
			if(matchAndRemove(Token.token.READ)!=null) {
				return read(list);
			}if(matchAndRemove(Token.token.DATA)!=null)
				return data();
			if(matchAndRemove(Token.token.INPUT)!=null)
				return input();
			if(matchAndRemove(Token.token.GOSUB)!=null)
				return goSub();
			if(matchAndRemove(Token.token.RETURN)!=null)
				return Return();
			if(matchAndRemove(Token.token.FOR)!=null) 
				return For();
			if(matchAndRemove(Token.token.NEXT)!=null)
				return Next();
			if(matchAndRemove(Token.token.IF)!=null)
				return IF(list);
			if(matchAndRemove(Token.token.FUNCTION)!=null)
				return this.functionInvocation();
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return null;
	}
	/**
	 * It has to be PRINT token or Comma token
	 * @param list - list for PRintNode class
	 * @return - a dummy when it a comma or PrintNode class
	 * @throws Exception 
	 */
	private Node PrintStatement() throws Exception {
		if(this.matchAndRemove(Token.token.PRINT)!=null) {

			//System.out.println(print.toString());
			token.remove(0);// remove Token PRINT
			return PrintList();
		}
		if(matchAndRemove(Token.token.COMMA)!=null) {// all we do is remove token 
			token.remove(0);
			return new FloatNode(2.34f);// here were returning a dummy node 
		}
		if(matchAndRemove(Token.token.STRING)!=null) {// Here String token
			StringNode s= new StringNode(token.get(0).getString());// We make StringNode 
			token.remove(0);// remove out String
			return s;
		}

		return null;	// 
	}
	/**
	 * As wanted in word Doc ... it prints the list
	 * @param n - Wnat a PrintNode
	 * @throws Exception 
	 */
	private Node PrintList() throws Exception {
		ArrayList<Node> list = new ArrayList<Node>();
		try {
			while(true ) {
				// This for expression 
				if(matchAndRemove(Token.token.NUMBER)!=null||matchAndRemove(Token.token.LPAREN)!=null) {
					list.add(expression());// We remove everything else 
					//System.out.println(list.get(0)+" WOWOWOWOWOWOWOWOW");
				}else if (matchAndRemove(Token.token.STRING)!=null) {
					list.add(new StringNode(token.get(0).getString()));
					token.remove(0);// here we remove our String 
				}else if (matchAndRemove(Token.token.IDENTIFIER)!=null) {
					list.add(new VariableNode(token.get(0).getString()));
					token.remove(0);
				}
				
				if(matchAndRemove(Token.token.COMMA)!=null)// if it comma than there is more to our list
					token.remove(0);// remove the comma
				else
					break;// we came end of our list
			}
		} catch (IndexOutOfBoundsException e) {// if it end of the list
			//System.out.println("What Error am seeing "+ e);
			//e.printStackTrace();
		}
		PrintNode print = new PrintNode(list);// we just return print list 
		return print;
	}
	/**
	 * In here we call expression and AssignmentNode class to make an assignment
	 * @return - an assignment node where it holds an variable and expression 
	 * @throws Exception - if not second token is not = than throw exception .. or error message from other methods and 
	 * Node class
	 */ 
	private Node Assignment() throws Exception {
		// so we know our first thing is identifier 
		if(token.size()==3) {
			VariableNode var= new VariableNode(token.get(0).getString());
			token.remove(0);
			if(this.matchAndRemove(Token.token.EQUAL)==null){// our second token is not equal
				throw new IllegalArgumentException("Expected '=' but got this "+token.get(0).getToken());
			}

			token.remove(0);
			Node tempNode= factor();
			AssignmentNode assignment= new AssignmentNode(tempNode,var);
			return assignment;// we return this expression with variable 
		}

		VariableNode var= new VariableNode(token.get(0).getString());
		token.remove(0);
		if(this.matchAndRemove(Token.token.EQUAL)==null){// our second token is not equal
			throw new IllegalArgumentException("Expected '=' but got this "+token.get(0).getToken());
		}

		token.remove(0);// we know it is equal  and remove it 
		Node tempNode=null;// for expression()
		try {
			while(this.matchAndRemove(Token.token.COMMA)==null||this.matchAndRemove(Token.token.PRINT)==null||token.size()!=0) {
				if(this.matchAndRemove(Token.token.PRINT)!=null||this.matchAndRemove(Token.token.COMMA)!=null
						||matchAndRemove(Token.token.STRING)!=null|| token.size()==1)
					break;// we reacted the end of that expression 
				tempNode=expression();

				//System.out.println("AM I here?"+ tempNode.toString()+ " and my size is "+token.size());
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		AssignmentNode assignment= new AssignmentNode(tempNode,var);
		//System.out.println(assignment.toString());
		return assignment;// we return this expression with variable 
	}
	private Node read(ArrayList<Node> list2ReadFromData) throws Exception {

		ArrayList<Node> tempList= new ArrayList<Node>();
		token.remove(0);// so remove READ
		if(matchAndRemove(Token.token.IDENTIFIER)==null)
			throw new Exception("IT should be an Identifire but got somethig else for read");
		try {
			while(true) {// keep ruining 
				if(matchAndRemove(Token.token.IDENTIFIER)!=null) {
					VariableNode varTemp = new VariableNode(token.get(0).getString());
					token.remove(0);
					tempList.add(varTemp);
				}
				if(matchAndRemove(Token.token.COMMA)!=null)// if is comma then we remove it 
					token.remove(0);
				else // if not than we know that are end of you READ token
					break;
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//System.out.println("I am Here "+e);
			//e.printStackTrace();
		}

		try {
			for(Node n:list2ReadFromData) {

				if(n.getClass().equals(DataNode.class)) {
					DataNode tempData= (DataNode)n;
					for(Node node:tempList) {
						tempData.getData(node);
					}
					break;
				}
			}
		} catch (NullPointerException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();

		}
		return new ReadNode(tempList);
	}
	private Node data() throws Exception {
		ArrayList<Node> tempList= new ArrayList<Node>();
		token.remove(0);// so remove DATA
		if(matchAndRemove(Token.token.STRING)==null &&matchAndRemove(Token.token.NUMBER)==null)
			throw new Exception("IT should be an Token type STRING or NUMBER but got somethig else for data");
		try {
			while(true) {// keep ruining 
				if(matchAndRemove(Token.token.STRING)!=null) {
					StringNode StrTemp = new StringNode(token.get(0).getString());
					token.remove(0);
					tempList.add(StrTemp);
				}else if(matchAndRemove(Token.token.NUMBER)!=null) {
					tempList.add(factor());
				}
				if(matchAndRemove(Token.token.COMMA)!=null)// if is comma then we remove it 
					token.remove(0);
				else // if not than we know that are end of you DATA token
					break;
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return new DataNode(tempList);
	}
	private Node input() throws Exception{
		ArrayList<Node> tempList= new ArrayList<Node>();
		token.remove(0);// so remove INPUT
		if(matchAndRemove(Token.token.IDENTIFIER)==null&&matchAndRemove(Token.token.STRING)==null)
			throw new Exception("IT should be an Identifire or STRING but got somethig else for INPUT");
		try {
			while(true) {// keep ruining 
				if(matchAndRemove(Token.token.IDENTIFIER)!=null) {
					VariableNode varTemp = new VariableNode(token.get(0).getString());
					token.remove(0);
					tempList.add(varTemp);
				} else if(matchAndRemove(Token.token.STRING)!=null) {
					tempList.add(new StringNode(token.get(0).getString()));
					token.remove(0);
				}
				if(matchAndRemove(Token.token.COMMA)!=null)// if is comma then we remove it 
					token.remove(0);
				else // if not than we know that are end of you INPUT token
					break;
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		return new InputNode(tempList);
	}
	/**
	 * Hold variableNode
	 * @return - GoSubNode
	 * @throws Exception if it wrong 
	 */
	private Node goSub() throws Exception {
		token.remove(0);//this remove the The Token GOSUB
		if(matchAndRemove(Token.token.IDENTIFIER)!=null) {
			VariableNode var= new VariableNode(token.get(0).getString());
			token.remove(0);// this remove VariableNode
			return new GOsubNode(var);

		}
		else
			throw new Exception("It look like your GOSUB is wrong. It need to be have an Idenetifier but got something else");


	}
	private Node Return() throws Exception {
		token.remove(0);// this remove token
		if(token.size()==0) {
			return new ReturnNode();
		}
		else
			throw new Exception("Return must single line of Token or last Token");
	}
	/**
	 * This for Statement taken in initial number, Max number and step (default is 1) 
	 * @return ForNode
	 * @throws Exception - if order or FOR is simple wrong 
	 */
	private Node For() throws Exception {
		try {// this for ForNode to see if there is next Node in token list
			int increment=1;// this for count
			while(true) {
				if(token.get(increment).getToken()==Token.token.NEXT) {
					break;
				}
				increment++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//System.out.println("It look like You do not have NEXT after FOR not sure if you wanted Throw AN error");

		}
		token.remove(0);// this remove FOR
		if(matchAndRemove(Token.token.IDENTIFIER)!=null) {
			VariableNode var= new VariableNode(token.get(0).getString());
			token.remove(0);// remove the variable
			if(matchAndRemove(Token.token.EQUAL)!=null) {
				token.remove(0);// remove the EQUAL
				if(matchAndRemove(Token.token.NUMBER)!=null) {
					Node intail=factor();
					if(matchAndRemove(Token.token.TO)!=null) {
						token.remove(0);// this remove the to
						if(matchAndRemove(Token.token.NUMBER)!=null) {
							Node max= factor();
							try {// We know if the list is empty 
								if(matchAndRemove(Token.token.STEP)!=null) {// this for step if we have it
									token.remove(0);// this remove the STEP
									if(matchAndRemove(Token.token.NUMBER)!=null) {
										Node step = factor();
										return new ForNode(var,intail,max,step);
									}
									else throw new Exception("You Used STEP but did not follow by number");
								}
								else // if we do not have step
									return new ForNode(var,intail,max,null);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								//e.printStackTrace();
								return new ForNode(var,intail,max,null);
							}
						}else // if not number
							throw new Exception("After TO the token should be number but something else");
					}else// if it is  not TO
						throw new Exception("After give initial value you have not give TO");
				}else // if it is not NUMBER
					throw new Exception("After EQUAL you have not give us NUMBER");
			}else// if it is not EQUAL
				throw new Exception("After Identifier it should be followed by EQUAL");

		}
		else // just WRONG FOR statement
			throw new Exception("After FOR it must be IDENTIFIRE");

	}
	/**
	 * It has more to do with For Statement
	 * @return - NexNode
	 * @throws Exception - if Next is wrong 
	 */
	private Node Next() throws Exception {
		token.remove(0);// this remove NEXT
		if(matchAndRemove(Token.token.IDENTIFIER)!=null) {
			VariableNode tempVar= new VariableNode(token.get(0).getString());
			return new NextNode(tempVar);
		}
		else 
			throw new Exception("It only taken in VariableNode but got something else");

	}
	private Node IF(ArrayList<Node> list)throws Exception{
		token.remove(0);// remove IF
		try {
			if(matchAndRemove(Token.token.NUMBER)!=null||matchAndRemove(Token.token.IDENTIFIER)!=null) {
				if(matchAndRemove(Token.token.NUMBER)!=null) {
					Node firstExp =factor();
					return helperIfNode(firstExp,list);

				}
				if(matchAndRemove(Token.token.IDENTIFIER)!=null) {
					Node firstExp=null;
					for(Node n: list) {

						if(n.getClass().equals(AssignmentNode.class)) {
							AssignmentNode tempAssi= (AssignmentNode)n;
							if(tempAssi.getVariable().equals(token.get(0).getString())) {
								firstExp = tempAssi.getNode();	
								break;
							}
						}
					}
					if(firstExp!=null) {
						if(!firstExp.getClass().equals(IntegerNode.class))
							throw new Exception("It Look like We can not compare BECAUSE you gave us InVaild Identifier");
						return helperIfNode(firstExp,list);

					}
					else
						throw new Exception("We could not find Value of your Variable");
				}
			}
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			throw new Exception("InVaild Exception for IF node");
		}

		throw new Exception("It Should be Number or Identifier but got something else");
	}
	/**
	 * This for our operator and right side of the operator 
	 * @param firstExp - left side
	 * @param list - to see if need list check if x is to something
	 * @return - BooleanExpresionNode
	 * @throws Exception
	 */
	private Node helperIfNode(Node firstExp, ArrayList<Node>list)throws Exception {
		String symbol=this.booleanExpressionSymbol().getString();
		if(symbol!=null) {
			if(matchAndRemove(Token.token.NUMBER)!=null) {
				Node secExp= factor();
				if(matchAndRemove(Token.token.THEN)!=null) {
					token.remove(0);//remove then
					if(matchAndRemove(Token.token.LABEL)!=null) {
						Node label=null;
						for (Node n:list) {// here we compare our whole list to see if matched label
							if(n.getClass().equals(LabeledStatementNode.class)) {
								LabeledStatementNode tempLabel= (LabeledStatementNode)n;
								if(tempLabel.getLabel().equals(token.get(0).getString())) {
									label= tempLabel;
									token.get(0);
									break;
								}
							}
						}
						if(label==null) {
							System.out.println("It look like We are not able to Find your Label.. But Do not worry We will make a new "
									+ "label");
							label= new LabeledStatementNode(null,token.get(0).toString());
							token.remove(0);
						}
						BooleanOperationNode b = new BooleanOperationNode(firstExp,symbol,secExp);
						return new IfNode(label,b);


					}
					else
						throw new Exception("It has to be Label but got something ELSE");
				}
				else
					throw new Exception("It has to THEN but got something Else");
			}
			else
				throw new Exception("It has to be Number but something else for IF Statement");
		}
		else
			throw new Exception("There is no symbol");

	}
	/**
	 * Here Get first exp and check  if other one
	 * @return
	 */
	private Token booleanExpressionSymbol() {
		if(matchAndRemove(Token.token.GreaterThan)!=null||matchAndRemove(Token.token.GreaterThanOrEqualTo)!=null||
				matchAndRemove(Token.token.LessThan)!=null||matchAndRemove(Token.token.LessThanOrEqualTo)!=null||
				matchAndRemove(Token.token.EQUAL)!=null||matchAndRemove(Token.token.NOTEQUAL)!=null){
			Token tempToken= token.get(0);
			token.remove(0);
			return tempToken;
		}
		return null;
	}
	/**
	 * THis for Function Where we check for Function 
	 * @return - functionNode 
	 * @throws Exception - if anything is wrong with function
	 */
	private Node functionInvocation() throws Exception {
		String functionName=token.get(0).getString();
		token.remove(0);//this remove FUNCTION
		if(matchAndRemove(Token.token.LPAREN)!=null) {
			token.remove(0);// this remove LPAREN
			ArrayList<Node> tempList = new ArrayList<Node>();

			while(true) {
				//System.out.println("I am here also here " + token.size()+" and "+token.get(0).getString());
				if(matchAndRemove(Token.token.NUMBER)!=null||matchAndRemove(Token.token.STRING)!=null) {
					
					try {// this for expression
						if(matchAndRemove(Token.token.NUMBER)!=null&&Token.token.PLUS==token.get(1).getToken()|| 
								matchAndRemove(Token.token.NUMBER)!=null&&Token.token.MINUS==token.get(1).getToken()||
								matchAndRemove(Token.token.NUMBER)!=null&&Token.token.TIMES==token.get(1).getToken()||
								matchAndRemove(Token.token.NUMBER)!=null&&Token.token.DIVIDE==token.get(1).getToken()) {
							tempList.add(this.expression());
							System.out.println("I am here also here");
						}
					} catch (IndexOutOfBoundsException  e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}
					if(matchAndRemove(Token.token.NUMBER)!=null) {// number
						tempList.add(factor());
					}
					else {
						tempList.add(new StringNode(token.get(0).getString()));// just String
					token.remove(0);// we remove STRING token
					}

				} else if(matchAndRemove(Token.token.COMMA)!=null)
					token.remove(0);// we remove Comma;
				else if(matchAndRemove(Token.token.RPAREN)!=null) {
					token.remove(0);// remove the Right parenthesis
					return new FunctionNode(functionName,tempList);
				}
				else 
					throw new Exception("Only STRING, NUMBER, EXPRESSION, COMMA, AND RPAREN but got something else");
			}
		}
		else
			throw new Exception("It was not follwed by Left Parenthesis");

	}

	private Node expression() throws Exception {

		Node toTheLeft=null;
		Token.token numberToken= Token.token.NUMBER;

		// This for factor .. If we read number 
		if(matchAndRemove(numberToken)!=null||matchAndRemove(Token.token.IDENTIFIER)!=null)
			toTheLeft=factor();// so we know it going to be on the left side
		// all this for to match our case to the token
		Token.token plusToken= Token.token.PLUS;
		Token.token subToken = Token.token.MINUS;
		Token.token mult = Token.token.TIMES;
		Token.token div = Token.token.DIVIDE;
		Token.token rParen= Token.token.RPAREN;
		Token.token lParen= Token.token.LPAREN;
		// if we find plus sign than we remove it and check if there is something to the left side
		if(matchAndRemove(plusToken)!=null) {

			if(toTheLeft==null)
				throw new Exception("left side of + sign is null");

			try {
				return rightSideOp(toTheLeft);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new Exception(e);
			}

		}
		// this for subtraction 
		else if(matchAndRemove(subToken)!=null) {// same as addtion but Error message would be different

			if(toTheLeft==null)
				throw new Exception("left side of - sign is null");
			try {
				return rightSideOp(toTheLeft);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new Exception(e);
			}
		}
		// if not a number and + or - symbol than it has be one of the following

		else if(matchAndRemove(mult)!=null) {
			return term(toTheLeft);

		}// This for Term

		else if(matchAndRemove(div)!=null) {
			return term(toTheLeft);
		}
		// this for right parentheses 
		else if(matchAndRemove(rParen)!=null) {
			token.remove(0);

		}
		//this left parentheses 
		else if(matchAndRemove(lParen)!=null) {

			return parentheses();
		}
		else if(this.matchAndRemove(Token.token.COMMA)!=null||this.matchAndRemove(Token.token.PRINT)!=null) {
			return null;
		}
		else// if not the case 
			throw new Exception("Not implemented yet or Not expression token\n"+token.get(0).getString());
		return null;
	}

	private Node term(Node toTheLeft) throws Exception {
		// In there factor should come after that
		Token.token mult = Token.token.TIMES;

		if(matchAndRemove(mult)!=null) {
			if(toTheLeft==null)
				throw new Exception("Left side of * sign is null");
			try {
				return rightSideOp(toTheLeft);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new Exception(e);
			}
		}
		Token.token div = Token.token.DIVIDE;
		if(matchAndRemove(div)!=null) {
			if(toTheLeft==null)
				throw new Exception("Left side of / sign is null");
			try {
				return rightSideOp(toTheLeft);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
				throw new Exception(e);
			}
		}

		return null;
	}

	private Node factor() throws Exception {
		Token.token numberToken= Token.token.NUMBER;
		if(matchAndRemove(numberToken)!=null|| matchAndRemove(Token.token.IDENTIFIER)!=null) {
			// here we would do the remove part
			if(isInteger(token.get(0).getString())) {// this for Integer 
				IntegerNode intNode= new IntegerNode(Integer.parseInt(token.get(0).getString()));
				token.remove(0);
				return intNode;
			}// this for float
			if(isFloat(token.get(0).getString())) {
				FloatNode floatNode= new FloatNode(Float.parseFloat(token.get(0).getString()));
				token.remove(0);
				return floatNode;
			}
			VariableNode t = new VariableNode(token.get(0).getString());
			token.remove(0);
			return t;
		}
		Token.token identifierToken =Token.token.IDENTIFIER;
		if(matchAndRemove(identifierToken)!=null) {
			String tempStr = token.get(0).getString();
			token.remove(0);
			return new VariableNode(tempStr);
		}
		Token.token lParen= Token.token.LPAREN;
		if(matchAndRemove(lParen)!=null) {
			return this.parentheses();
		}






		return null;
	}
	/**
	 * 
	 * @param toTheLeft - we get the left side of the equation
	 * @return MathOpNode - where it look for sign and parentheses
	 * @throws Exception - if not number and not left parentheses
	 */
	private Node rightSideOp(Node toTheLeft)throws Exception {
		Token.token rParen= Token.token.RPAREN;
		Token.token numberToken= Token.token.NUMBER;
		Token tempToken = token.get(0);// now let save our + symbol 
		token.remove(0);
		Node tempNode=null;
		Node toTheRight=null;// this going to be right side of sign 
		if(matchAndRemove(numberToken)!=null||matchAndRemove(Token.token.IDENTIFIER)!=null)
			toTheRight=factor();// if it number than we have finished our expression
		if(toTheRight==null) {
			Token.token lParen= Token.token.LPAREN;
			if(matchAndRemove(lParen)==null) {// if not ( after symbol than something went wrong
				throw new Exception("If it is not number and not '(' than something went Wrong.");
			}else {
				try {
					token.remove(0);
					int x=0;
					while(token.get(x).getToken()!=rParen)
						x++;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					throw new Exception("It was NOT able to find ')'");
				}
				Node locaNode;
				locaNode= expression();
				tempNode= new MathOpNode(tempToken.getString(),locaNode,toTheLeft);
			}
		}
		Node opNode;
		if(tempNode!=null)
			opNode= tempNode;
		else
			opNode= new MathOpNode(tempToken.getString(),toTheRight,toTheLeft);
		opNode=repateSymbol(opNode);
		if(opNode==null)
			throw new Exception("The Right side of the sing is null");
		return opNode;
	}
	/**
	 * parentheses look for the right and call expression
	 * @return- return an expression 
	 * @throws Exception- if it could not find right parentheses
	 */
	private Node parentheses() throws Exception{
		Token.token lParen= Token.token.LPAREN;
		if(matchAndRemove(lParen)==null)
			return null;
		//throw new Exception("If it is not number and not '(' than something went Wrong");
		else {

			try {
				Token.token rParen = Token.token.RPAREN;
				token.remove(0);

				int x=0;
				while(token.get(x).getToken()!=rParen)
					x++;

			} catch (Exception e) {
				// TODO Auto-generated catch block
				throw new Exception("It was NOT able to find ')'");
			}

			return expression();

		}
		//return  null;
	}
	/**
	 * This for if Symbol and factor for example 2 + 2 + 2 + 2
	 * @param left - left side of the oppression 
	 * @return- once it no loners find a symbol and return a MathOpNode 
	 * @throws Exception
	 */
	private Node repateSymbol(Node left) throws Exception {

		try {
			if(token.get(0).getToken()==Token.token.PLUS||token.get(0).getToken()==Token.token.MINUS||
					token.get(0).getToken()==Token.token.TIMES||token.get(0).getToken()==Token.token.DIVIDE) {
				Token tempSymbol=token.get(0);
				token.remove(0);
				Node right= factor();

				if( right==null) {
					if(parentheses()==null)
						throw new Exception("It should be Number or Left parentheses");
					else
						parentheses();
				}else {

					MathOpNode node = new MathOpNode(tempSymbol.getString(),right,left);

					try {
						if(token.get(0).getToken()==Token.token.PLUS||token.get(0).getToken()==Token.token.MINUS||
								token.get(0).getToken()==Token.token.TIMES||token.get(0).getToken()==Token.token.DIVIDE) {

							return repateSymbol(node);

						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						//e.printStackTrace();
					}


					return node;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}

		try {
			if(token.get(0).getToken()==Token.token.RPAREN) {
				if(token.get(1).getToken()==Token.token.PLUS||token.get(1).getToken()==Token.token.MINUS||
						token.get(1).getToken()==Token.token.TIMES||token.get(1).getToken()==Token.token.DIVIDE) {
					expression();
					return rightSideOp(left);
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
		}
		return left;	
	}

	/**
	 * Only used for factor()
	 * @param s
	 * @return
	 */
	private boolean isInteger(String s) {
		try { 
			Integer.parseInt(s); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
	/**
	 * Only used for factor()
	 * @param s
	 * @return
	 */
	private boolean isFloat(String s) {
		try { 
			Float.parseFloat(s); 
		} catch(NumberFormatException e) { 
			return false; 
		} catch(NullPointerException e) {
			return false;
		}
		// only got here if we didn't return false
		return true;
	}
}
