package com.github.smirrorgame.compiler;

/**
 * @author Micha Halla
 *
 */
public final class Error extends RuntimeException {
	
	public static void error(int line, int column, String message) {
		report(line, column, message);
	}
	
	public static Error error(Token token, String message) {
		report(token.line, token.col, message);
		return new Error();
	}
	
	private static void report(int line, int col, String message) {
		System.err.println("[Line " + line + ", column " + col+ "] Error: " + message);
	}
}

