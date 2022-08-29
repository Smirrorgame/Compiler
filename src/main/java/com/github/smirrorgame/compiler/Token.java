package com.github.smirrorgame.compiler;

public class Token {
	
	final TokenType type;
	final String lexeme;
	final Object literal;
	final int line, col;
	
	Token(TokenType type, String lexeme, Object literal, int line, int col) {
		this.type = type;
		this.lexeme = lexeme;
		this.literal = literal;
		this.line = line;
		this.col = col;
	}
	
	@Override
	public String toString() {
		return type + ": \"" + lexeme + "\" " + literal + " at " + line+":"+col;
//		return type + ": " + lexeme + " " + literal;
	}
	
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof Token)) return false;
		Token oToken = (Token) o;
		boolean sameLiteral = false;
		if(oToken.literal == null && this.literal !=null) return false;
		if(oToken.literal != null && this.literal ==null) return false;
		if(oToken.literal == null && this.literal == null) {
			sameLiteral = true;
		}else if(oToken.literal.getClass() == this.literal.getClass()) {
			sameLiteral = oToken.literal.equals(this.literal);
		}
		return (oToken.type == this.type && oToken.lexeme.equals(this.lexeme) && oToken.line == this.line && oToken.col == this.col && sameLiteral);	
	}

}
