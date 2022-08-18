package com.github.smirrorgame.compiler;

import java.util.List;

public class Lexer {
	
	private String src;
	private List<Token> tokens;
	
	
	public Lexer(String src) {
		this.src = src;
	}
	
	public List<Token> tokenize() {
		return tokens;
	}

}
