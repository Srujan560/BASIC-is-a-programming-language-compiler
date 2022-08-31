/**
 * @author Srujan Vepuri
 * Lexer class used to tokinze strings
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.IllegalArgumentException;
public class Lexer {
	/**
	 * Lex method will token incoming String
	 * @param str - give math expression
	 * @return ArrayList <String> of Tokens 
	 */
	public List<Token> lex(String str){
		List<Token> tokens = new ArrayList<Token>();
		Map<String,Token>knowTokens= knowToken();



		char []c= str.toCharArray();
		int state=0;// state =0 it a empty space, state =1 it a digit, state=2 it a decimal, state =3 it a symbol
		// and state=4 it a negative number state =6 it means for no spaces // 7 & 8 is used for label
		// 14 and 15 is for number and letter with no spaces
		boolean quoteCheck=false;

		String temp="";// temporary

		if(str.isBlank()) {// Check if the line is empty
			Token tk=new Token(temp);
			tk.setToken(temp);
			tokens.add(tk);
			return tokens;
		}

		for (int x=0;x<c.length;x++) {
			int shouldThrowError=0;
			if (c[x]==' ') {
				if(quoteCheck==false)
				state=0;
			}else if(numberStateMachineCheck(c[x])&& quoteCheck==false) {
				try {
					if(c[x+1]==')'||c[x+1]=='(')
						state=6;
					else if(isLetter(c[x+1]))
						state=14;
					else if(symbolStateMachineCheck(c[x+1])) {
						state=22;
						
					}else
						state=1;
				}catch (ArrayIndexOutOfBoundsException e) {

				}
			}else if(c[x]=='.'&&quoteCheck==false) {
				try {// here we sill that if it a decimal then the next char be digit
					if (numberStateMachineCheck(c[x+1]))
						state=2;
					else 
						throw new IllegalArgumentException("Bad Input expected a digit but got this"+c[x]);// if not digit
				} catch (ArrayIndexOutOfBoundsException e) {// if not char then throw
					throw new IllegalArgumentException("Bad Input: check your format for ex'2.44' ");
				}

			}
			else if (symbolStateMachineCheck(c[x])&&quoteCheck==false) {
				try {// this for negative numbers 
					// It should tell if negative number for example *-6
					if(state==3&&c[x]=='-'&&numberStateMachineCheck(c[x+1]))
						state=4;
					// this should tell if - and it number for example -4
					else if(state==0&&c[x]=='-'&&numberStateMachineCheck(c[x+1]))
						state=4;
					// this for "<=" or >= less than equal or greater than equal
					else if (c[x]=='<'&&c[x+1]=='='||c[x]=='>'&&c[x+1]=='=') 
						state=4;
					else if(c[x]=='='&&c[x-1]=='<' || c[x]=='='&&c[x-1]=='>')
						state=4;
					// this for not equal to "<>"
					else if (c[x]=='<'&&c[x+1]=='>'|| c[x]=='>'&&c[x-1]=='<')
						state=4;
					// this for '(' or ')' with no space
					else if (c[x]=='('&&numberStateMachineCheck(c[x+1])||symbolStateMachineCheck(c[x+1]))
						state=6;
					else
						state=3;// this for symbol

				} catch(ArrayIndexOutOfBoundsException e) {
					// here it just check if next char is number or not

				}	
			} 
			else if (specialSymbol(c[x])||isLetter(c[x])) {
				try {
					// this for label token and state =7
					if(isLetter(c[x])&& c[x+1]==':') {
						state=7;
						//System.out.println("I am on");
					}else if(c[x]==':') {
						state=8;// this will tell us to make token and label token
						//System.out.println("I am avilve");
						// this for " "(String)
					}else if(c[x]=='"') {
						// this tell if it is first quoteCheck
						if(quoteCheck==false) {
							int tempIndex=x+1;
							try {
								while(c[tempIndex]!='"'){
									// Let look for second Quotation Mark
									tempIndex++;
								}

							}catch(Exception e) {
								shouldThrowError=1; 
							}
							quoteCheck=true;
							state =9;
						}
						else{// if we already found it then
							quoteCheck=false;
							state=12;// this tell us that it our String
						}

					}
					else if(isLetter(c[x])&&c[x+1]=='$'|| c[x+1]=='%') {
						state=10;// special string

					}else if (c[x]=='$'||c[x]=='%') {
						// we already check and add it to the token
						state=101;
					}

					else if (isLetter(c[x])&&c[x+1]==' ')
						state=11;// just IDENTIFIER
					else if (numberStateMachineCheck(c[x+1])||symbolStateMachineCheck(c[x+1]))// check if the next number is or not
						state=15;
					else
						state=13;
				}catch(Exception e) {


				}
			}
			else if(quoteCheck==true) {// 
				
			}
			else
				throw new IllegalArgumentException("Bad Input: "+ c[x]);
			if (shouldThrowError==1)
				throw new IllegalArgumentException("Exptced second Quotation Mark: Bad use of Quotation Mark");
			if (x==c.length-1&& state!=12&& state!=8&&state!=101&&state!=11)
				state=5;

			if (state == 0&&quoteCheck==false) {
				boolean tokenKnow=false;
				if(!temp.isBlank()) {
					Token tk = new Token(temp);
					for(Map.Entry<String, Token> m:knowTokens.entrySet()) {
						if(m.getKey().equals(temp)) {
							tokenKnow=true;
						}
					}
					if(tokenKnow==false) {	
						tk.setToken(temp);
						tokens.add(tk);
					}else {
						System.out.println("Know token "+temp);
						tk.setToken(temp);
						tokens.add(tk);
					}
				}

				temp="";// we reset the string
			}else if(quoteCheck==true) {// this for " " 
				if(c[x]!='"')
					temp+=c[x];
			}
			else if (state==1||state==2||state==4) {
				//System.out.println("From lex() "+c[x]);
				temp+=c[x];
				

				//System.out.println("From lex() temp =" +temp);
			} else if (state==3) {
				temp=""+c[x];
				Token tk = new Token(temp);
				tk.setToken(temp);
				temp="";
				tokens.add(tk);
				
			}else if (state==5) {// this last run of the loop
				if(c[x]!=' ')
				temp=temp+c[x];
				if(!temp.isBlank()) {
				Token tk=new Token(temp);
				tk.setToken(temp);
				tokens.add(tk);
				
				}
			} else if (state==6) {// is negative number state
				Token tk=new Token(""+c[x]);
				tk.setToken(""+c[x]);
				tokens.add(tk);

			}
			else if (state==7||state==8) {// Label
				if(state==7)
					temp+=c[x];
				//System.out.println(temp+" *** "+state);
				if (state==8) {
					Token tk = new Token(temp);
					tk.setTokenString(state);
					tokens.add(tk);
					temp="";
				}


			}
			else if (state==9||state==12) {// Quotation Mark
				// this for adding it our token
				if (state==12) {
					Token tk = new Token(temp);
					tk.setTokenString(state);
					tokens.add(tk);
					temp="";
				}
			} else if(state==10) {// $% put them as IDENTIFIER
				temp=temp+c[x]+c[x+1];
				boolean tokenKnow=false;
				if(!temp.isBlank()) {
					Token tk = new Token(temp);
					for(Map.Entry<String, Token> m:knowTokens.entrySet()) {
						if(m.getKey().equals(temp)) {
							tokenKnow=true;
						}
					}
					if(tokenKnow==false) {	
						tk.setToken(temp);
						tokens.add(tk);
					}else {
						System.out.println("Know token "+temp);
						tk.setToken(temp);
						tokens.add(tk);
					}
				}
				temp="";// we reset the string

			}else if(state==11||state==13) {
				if(state==13)// this just string contain
					temp+=c[x];
				if(state==11) {// just IDENTIFIER
					boolean tokenKnow=false;
					temp+=c[x];
					if(!temp.isBlank()) {
						Token tk = new Token(temp);
						for(Map.Entry<String, Token> m:knowTokens.entrySet()) {
							if(m.getKey().equals(temp)) {
								tokenKnow=true;
							}
						}
						if(tokenKnow==false) {	
							tk.setToken(temp);
							tokens.add(tk);
						}else {
							tk.setToken(temp);
							tokens.add(tk);
						}
					}

					temp="";// we reset the string
				}

			}else if(state==14||state==15) {// if letter and number come together ex 4a or a4 = NUMBER(4) ide..(a)
				temp+=c[x];
				if(state==14) {
					boolean tokenKnow=false;
					if(!temp.isBlank()) {
						Token tk = new Token(temp);
						for(Map.Entry<String, Token> m:knowTokens.entrySet()) {
							if(m.getKey().equals(temp)) {
								tokenKnow=true;
							}
						}
						if(tokenKnow==false) {	
							tk.setToken(temp);
							tokens.add(tk);
						}else {
							System.out.println("Know token "+temp);
							tk.setToken(temp);
							tokens.add(tk);
						}
					}
					temp="";// we reset the string
				} else {
					if(quoteCheck==false) {
						boolean tokenKnow=false;
						if(!temp.isBlank()) {
							Token tk = new Token(temp);
							for(Map.Entry<String, Token> m:knowTokens.entrySet()) {
								
								if(m.getKey().equals(temp)) {
									tokenKnow=true;
									
								}
							}
							
							if(tokenKnow==false) {	
								tk.setTokenString(11);
								tokens.add(tk);
							}else {
								//System.out.println("++++Know token "+temp);
								tk.setToken(temp);
								tokens.add(tk);
							}
						}
						temp="";// we reset the string
					}
				}
			} else if(state==22) {
				
				temp+=c[x];
				Token tk = new Token(temp);
				tk.setToken(temp);
				temp="";
				tokens.add(tk);
			}


		}
		//for(Map.Entry<String, Token> m:tokens.entrySet())
		//System.out.println(m.getKey());
		return tokens;
	}
	/**
	 * Check if char is digit
	 * @param c - any char value
	 * @return True if found [0-9]
	 */
	private boolean numberStateMachineCheck(char c) {
		return Character.isDigit(c);
	}
	/**
	 * It look for symbol 
	 * @param c - any char value
	 * @return True if found [+, -, *, /,=,<,>,(,)] else false
	 */
	private boolean symbolStateMachineCheck(char c) {
		if (c=='+'||c=='-'||c=='*'||c=='/'||c=='='||c=='<'||c=='>'||c=='('||c==')'||c==',')
			return true;
		return false;
	}
	/**
	 * This for special symbol that has do with string
	 * @param c - any car value
	 * @return True if found[;,",$,%]
	 */
	private boolean specialSymbol(char c) {
		return c==':'||c=='"'||c=='$'||c=='%';
	}
	private boolean isLetter(char c) {
		return Character.isLetter(c);
	}
	private Map<String,Token> knowToken(){
		Map<String,Token> knowTk = new HashMap<>();
		Token tempToken=new Token("");
		tempToken.setToken("PRINT");
		knowTk.put("PRINT",tempToken) ;
		tempToken.setToken("READ");
		knowTk.put("READ", tempToken);
		tempToken.setToken("DATA");
		knowTk.put("DATA", tempToken);
		tempToken.setToken("INPUT");
		knowTk.put("INPUT", tempToken);
		tempToken.setToken("GOSUB");
		knowTk.put("GOSUB",tempToken);
		tempToken.setToken("RETURN");
		knowTk.put("RETURN", tempToken);
		tempToken.setToken("FOR");
		knowTk.put("FOR", tempToken);
		tempToken.setToken("STEP");
		knowTk.put("STEP", tempToken);
		tempToken.setToken("NEXT");
		knowTk.put("NEXT", tempToken);
		tempToken.setToken("To");
		knowTk.put("To", tempToken);
		tempToken.setToken("THEN");
		knowTk.put("THEN", tempToken);
		tempToken.setToken("IF");
		knowTk.put("IF", tempToken);
		tempToken.setToken("RANDOM");
		knowTk.put("RANDOM", tempToken);
		tempToken.setToken("VAL");
		knowTk.put("VAL", tempToken);
		tempToken.setToken("LEFT$");
		knowTk.put("VAL", tempToken);
		tempToken.setToken("RIGHT$");
		knowTk.put("RIGHT$", tempToken);
		tempToken.setToken("VAL%");
		knowTk.put("VAL%", tempToken);
		tempToken.setToken("NUM$");
		knowTk.put("NUM$", tempToken);
		

		return knowTk;
	}



}

