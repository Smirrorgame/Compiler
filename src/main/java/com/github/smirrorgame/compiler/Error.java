package com.github.smirrorgame.compiler;

/**
 * @author Micha Halla
 *
 */
public final class Error {
	
	public static void error(int line, int column, String message) {
		report(line, column, message);
	}
	
	private static void report(int line, int col, String message) {
		System.err.println("[Line " + line + ", column " + col+ "] Error: " + message);
	}
}

