package com.github.smirrorgame.compiler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ErrorTest {
	
	private ByteArrayOutputStream errors = new ByteArrayOutputStream();
	private static PrintStream original_err;
	
	@BeforeAll
	public static void storeOriginalStream() {
		original_err = System.err;
	}
	
	@BeforeEach
	public void setUpStream() {
		System.out.println("Setting up error Printstream");
		System.setErr(new PrintStream(errors));
	}
	
	@AfterEach
	public void cleanUpStreams() {
		System.out.println("Clearing and restoring error Printstream");
		errors.reset();
		System.setErr(original_err);
	}
	
	@Test
	public void test_error() {
		Error.error(0, 7, "Not supported");
		assertEquals("[Line 0, column 7] Error: Not supported\r\n", errors.toString());
	}
}
