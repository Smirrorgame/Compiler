package com.github.smirrorgame.compiler;

import java.util.List;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
    	String src = "12 / 4 * (2 + 3)";
    	
    	if(args.length>1) {
    		System.out.println("Cant have more than one argument!");
    		System.out.println("Usage: lang [script]");
    		System.exit(64);
    	}
    	if(args.length > 0) {
    		src = args[0];
    	}
    	
    	System.out.println(src);
    	run(src);
    }
    
    public static void run(String src) {
    	Lexer lexer = new Lexer(src);
    	List<Token> tokens = lexer.tokenize();
    	Parser parser = new Parser(tokens);
    	Expr expression = parser.parse();
    	
    	System.out.println(new AstPrinter().print(expression));
    	
    }
}
