package model;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import model.ReturnMsg.ERROR;

import org.junit.Test;

public class JottoModelTest {
	/*
	 * Test Strategy: 
	 * valid input, valid id 
	 * input of length 0 
	 * input of length > 5 
	 * input of lengh 0 < input < 5
	 * input with invalid character
	 * input with asterisk
	 * Gui Test:
	 * Check by type new ID, incorrect format ID
	 * Check to see if word with asterisk won't make the GUI unresponsive
	 * Check all types of error for words
	 * Check to see if table redraw after hitting new puzzle
	 * Check too see if table handles long list of guest
	 * 
	 */

	@Test
	public void validInput() throws IOException {
		ReturnMsg msg = JottoModel.makeGuess(16952, "crazy");
		String expectedOutput = "guess 3 1";
		assertEquals(expectedOutput, msg.toString());
	}

	@Test
	public void longInput() throws IOException {
		ReturnMsg msg = JottoModel.makeGuess(16952, "elephant");
		ERROR expectedOutput = ERROR.error2;
		assertEquals(expectedOutput, msg.getError());
	}

	@Test
	public void inputWithAsterisk() throws IOException {
		ReturnMsg msg = JottoModel.makeGuess(16952, "*bean");
		String expectedOutput = "guess 1 0";
		assertEquals(expectedOutput, msg.toString());
	}

	@Test
	public void shortInput() throws IOException {
		ReturnMsg msg = JottoModel.makeGuess(16952, "*bea");
		ERROR expectedOutput = ERROR.error2;
		assertEquals(expectedOutput, msg.getError());
	}

	
	@Test
	public void zeroLengthInput() throws IOException {
		ReturnMsg msg = JottoModel.makeGuess(16952, "");
		ERROR expectedOutput = ERROR.error0;
		assertEquals(expectedOutput, msg.getError());

	}

	@Test
	public void inValidCharacter() throws IOException {
		ReturnMsg msg = JottoModel.makeGuess(16952, "cra!y");
		ERROR expectedOutput = ERROR.error2;
		assertEquals(expectedOutput, msg.getError());
	}


	@Test
	public void longInputWithAsterisk() throws IOException {
		ReturnMsg msg = JottoModel.makeGuess(16952, "cra*yDCM");
		ERROR expectedOutput = ERROR.error2;
		assertEquals(expectedOutput, msg.getError());
	}

}