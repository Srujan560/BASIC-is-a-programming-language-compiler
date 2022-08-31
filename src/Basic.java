/**
 * @author srujan Vepuri
 */
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;

public class Basic {

	public static void main(String[] args) throws  Exception {
		// TODO Auto-generated method stub
		if(args.length==0||1<args.length) {// if does not have a argument then it will exit
			System.out.println("Invaild arguments "+args.length);
			System.exit(0);
		}
		Path path = Paths.get(args[0]);// set the path 
		try {
			List<String> data=Files.readAllLines(path);// Here we read all lines
			List<Token> temp= new ArrayList<Token>();// a temporary variable that can hold tokens 
			
			Lexer lexer = new Lexer();// here we make an instance of Lexer Class
			//data.forEach(System.out::println);
			Interpreter inte = new Interpreter();
			
			for (String s:data) {
				
				try {
					temp= lexer.lex(s);// we give string from file we just read and save it in 
					Parser par = new Parser(temp);
					StatementNode statementNode =null;
					try {
						statementNode= (StatementNode) par.parser();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
						System.out.println("From Parser class: "+e);
					}
					
					inte.interpret(statementNode);
				
					for (Token t:temp) {
						
						System.out.print(t.toString());
						//t.toString();
						
						
					}
				}catch (Exception e){// Catch any error message form Lexer class
					System.out.print("Lexer Class: "+e);
					e.printStackTrace();
					
				}
				//System.out.println();
			}
			
			
			inte.initialize();
			inte.test();
		} catch (IOException e) {// this if file was able find 
			// TODO Auto-generated catch block
			
			e.printStackTrace();
			System.out.println("File Was not found or does not exists");
		}
		
	}
	/*

	 */
	

}
