/**
 * 
 * @author Srujan Vepuri
 * 
 *
 */

public class Token {
	private String val="";
	enum token{
		MINUS("MINUS"),NUMBER("NUMBER"),PLUS("PLUS"),TIMES("TIMES"), DIVIDE("DIVIDE"),EndOfLine("EndOfLine"),
		GreaterThan("GreaterThan"),LessThanOrEqualTo("LessThanOrEqualTo"),
		GreaterThanOrEqualTo("GreaterThanOrEqualTo"),LessThan("LessThan"),
		EQUAL("EQUAL"),NOTEQUAL("NOTEQUAL"),LPAREN("LPAREN"),RPAREN("RPAREN"),
		STRING("STRING"),LABEL("LABEL"),IDENTIFIER("IDENTIFIER"),DEFAULT("Failed: to Make Token"),
		PRINT("PRINT"),COMMA("COMMA"),READ("READ"),DATA("DATA"),INPUT("INPUT"), GOSUB("GOSUB"), RETURN("RETURN"),
		FOR("FOR"), STEP("STEP"), NEXT("NEXT"), TO("TO"), IF("IF"),THEN("THEN"),FUNCTION("FUCTION");
		private String strVal="";
		public String getval() {
			return strVal;
		}
		private token(String s) {
			strVal=s;
		}
	}
	private token tk= token.DEFAULT;
	Token(String s){
		val=s;
	}
	/**
	 * In this method would be the default and if string is symbol that we know
	 * @param s -String token
	 */
	public void setToken(String s) {
		try {
			int num = tokenType(s);
			switch(num) {
			case 1:
				tk=token.NUMBER;
				break;
			case 2:
				tk=token.PLUS;
				break;
			case 3:
				tk=token.MINUS;
				break;
			case 4: 
				tk = token.TIMES;
				break;
			case 5:
				tk = token.DIVIDE;
				break;
			case 6:
				tk = token.EndOfLine;
				break;
			case 7 :
				tk = token.LessThan;
				break;
			case 8:
				tk = token.GreaterThan;
				break;
			case 9:
				tk = token.LessThanOrEqualTo;
				break;
			case 10:
				tk = token.GreaterThanOrEqualTo;
				break;
			case 11:
				tk = token.EQUAL;
				break;
			case 12:
				tk = token.NOTEQUAL;
				break;
			case 13:
				tk = token.LPAREN;
				break;
			case 14:
				tk = token.RPAREN;
				break;
			case 100:
				tk=token.IDENTIFIER;
				break;
			case 15:
				tk=token.PRINT;
				break;
			case 16:
				tk=token.COMMA;
				break;
			case 17:
				tk=token.READ;
				break;
			case 18:
				tk=token.DATA;
				break;
			case 19:
				tk=token.INPUT;
				break;
			case 20:
				tk=token.GOSUB;
				break;
			case 21:
				tk=token.RETURN;
				break;
			case 22:
				tk=token.FOR;
				break;
			case 23:
				tk=token.STEP;
				break;
			case 24:
				tk=token.NEXT;
				break;
			case 25:
				tk=token.TO;
				break;
			case 26:
				tk=token.IF;
				break;
			case 27:
				tk=token.THEN;
				break;
			case 28:
				tk=token.FUNCTION;
				break;
				
			}
		}catch(Exception e) {
			System.out.print(e);
		}

	}
	/**
	 * This For String token
	 * @param num- This would the state we get from our Lexer.java class in our lex();
	 */
	public void setTokenString(int num) {
		try {
			switch(num) {
			case 8:
				tk=token.LABEL;// any for label
				break;
			case 11:
				tk=token.IDENTIFIER;
				break;
			case 12:
				tk=token.STRING;
				break;



			}
		}catch(Exception e) {
			System.out.print(e);
		}
	}
	// here is what each we int value of each symbol
	private int tokenType(String s) {
		int num =0;
		if (s.matches("-?\\d+(\\.\\d+)?")) {// this check number formation 
			num=1;

		} else if (s.equals("+"))
			num=2;
		else if (s.equals("-"))
			num=3;
		else if (s.equals("*"))
			num=4;
		else if (s.equals("/"))
			num=5;
		else if(s.isBlank())
			num=6;
		else if (s.equals("<"))
			num=7;
		else if (s.equals(">"))
			num=8;
		else if (s.equals("<="))
			num=9;
		else if (s.equals(">="))
			num =10;
		else if (s.equals("="))
			num=11;
		else if (s.equals("<>"))
			num=12;
		else if (s.equals("("))
			num=13;
		else if (s.equals(")"))
			num=14;
		else if(s.equals("PRINT"))
			num=15;
		else if(s.equals(","))
			num=16;
		else if(s.equals("READ"))
			num=17;
		else if(s.equals("DATA"))
			num=18;
		else if(s.equals("INPUT"))
			num=19;
		else if(s.equals("GOSUB"))
			num=20;
		else if(s.equals("RETURN"))
			num=21;
		else if(s.equals("FOR"))
			num=22;
		else if(s.equals("STEP"))
			num=23;
		else if(s.equals("NEXT"))
			num=24;
		else if(s.equals("TO"))
			num=25;
		else if(s.equals("IF"))
			num=26;
		else if(s.equals("THEN"))
			num=27;
		else if(s.equals("RANDOM")|| s.equals("LEFT$") ||s.equals("RIGHT$") ||s.equals("NUM$")||s.equals("VAL")||s.equals("VAL%") )
			num=28;
		else if (s.contains("%")||s.contains("$")||s.matches("[a-zA-Z]+")) {
			num=100;
			//System.out.println("Why am I here "+s);
		}


		return num;
	}
	@Override
	public String toString() {
		if(token.DEFAULT==tk) {
			System.out.println("What I got "+val);
			throw new RuntimeException("Check Your Input. MAKE sure you have proper space");
		}
		return tk.getval()+"("+val+")";

	}
	public token getToken() {
		return tk;
	}
	
	public String getString() {
		return val;
	}
}
