package com.github.smirrorgame.compiler;

class Token {
	
	final TokenType type;
	final String lexeme;
	final Object literal;
	final int line;
	
	Token(TokenType type, String lexeme, Object literal, int line) {
		this.type = type;
		this.lexeme = lexeme;
		this.literal = literal;
		this.line = line;
	}
	
	@Override
	public String toString() {
		return type + ": " + lexeme + " " + literal;
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
		}else {
			sameLiteral = (oToken.literal.getClass() == this.literal.getClass());
		}
		return (oToken.type == this.type && oToken.lexeme.equals(this.lexeme) && oToken.line == this.line && sameLiteral);	
	}

}
