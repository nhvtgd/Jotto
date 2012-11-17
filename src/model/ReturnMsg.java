package model;

/**
 * 
 * @author viettran
 * 
 *         ReturnMsg class: encapsulating returnMsg datatype This class
 *         represents the return message from the server including valid and
 *         invalid
 * 
 */
public class ReturnMsg {
	private final int common;
	private final int correct;
	private final String guess;
	private final ERROR error;
	private final STATE state;

	/** Possible error received from the server */
	public enum ERROR {
		error0, error1, error2, noerror;
	}

	/** Represent the state of the message */
	public enum STATE {
		undone, done;
	}

	/**
	 * Represent the number of character in common between the guess and the
	 * correct answer
	 * 
	 * @return int number of common character
	 */
	public int getCommon() {
		return common;
	}

	/**
	 * Represent the number of character in correct position between the guess
	 * and the correct answer
	 * 
	 * @return int number of character in correct position
	 */
	public int getCorrect() {
		return correct;
	}

	/**
	 * @return: ERROR from user
	 */
	public ERROR getError() {
		return this.error;
	}

	/**
	 * @return: guess from the user
	 */
	public String getGuess() {
		return this.guess;
	}

	/**
	 * @return: true if there is an error, false otherwise
	 */
	public boolean isError() {
		return (error != ERROR.noerror);
	}

	/**
	 * Constructor that takes guess as parameter initial state is undone,
	 * correct and common value are -1 and no error
	 * 
	 * @param guess
	 *            : Guess String from user
	 */
	public ReturnMsg(String guess) {
		this.guess = guess;
		this.state = STATE.undone;
		this.correct = -1;
		this.common = -1;
		this.error = ERROR.noerror;
	}

	/**
	 * Constructor that takes guess, number of character in common and number of
	 * character in correct position as parameter initial state is done,and no
	 * error
	 * 
	 * @param guess
	 *            : Guess String from user
	 * @param common
	 *            : number of character in common
	 * @param correct
	 *            : number of character in correct position
	 */
	public ReturnMsg(String guess, int common, int correct) {
		this.common = common;
		this.correct = correct;
		this.guess = guess;
		this.error = ERROR.noerror;
		this.state = STATE.done;
	}

	/**
	 * Constructor that takes guess and error as parameter initial state is
	 * done, correct and common value are -1 and *
	 * 
	 * @param guess
	 *            : Guess String from user
	 */
	public ReturnMsg(String guess, ERROR error) {
		this.error = error;
		this.guess = guess;
		this.common = -1;
		this.correct = -1;
		this.state = STATE.done;
	}

	public boolean isDone() {
		return (state == STATE.done);
	}

	/**
	 * Parse the return messeage if it is an error
	 * 
	 * @param s
	 *            : String error message from the server
	 * @return ERROR :type, ie. error0, error1, error2
	 */
	public static ERROR parseError(String s) {
		if (s.equals("error 0")) {
			return ERROR.error0;
		} else if (s.equals("error 1")) {
			return ERROR.error1;
		} else if (s.equals("error 2")) {
			return ERROR.error2;
		}
		// should not get here
		return ERROR.noerror;
	}

	/**
	 * Parse the return messeage from the server after submitting the guess
	 * 
	 * @param s
	 *            : String return message from the server
	 * @param guess
	 *            : String guess message from the user
	 * @return ReturnMsg: type, ie. error0, error1, error2
	 */
	public static ReturnMsg parseMsg(String guess, String s) {
		if (s.matches(("(error 0|error 1|error 2)"))) {
			return new ReturnMsg(guess, parseError(s));
		} else {
			String[] tokens = s.split(" ");
			int common = Integer.parseInt(tokens[1]);
			int correct = Integer.parseInt(tokens[2]);
			return new ReturnMsg(guess, common, correct);
		}
	}

	public String toString() {
		if (error == ERROR.error0) {
			return "Ill-formatted request";
		} else if (error == ERROR.error1) {
			return "Non-number puzzle ID";
		} else if (error == ERROR.error2) {
			return "Invalid guess. Length of guess != 5 or guess is not a dictionary word.";
		} else {
			return "guess " + common + " " + correct;
		}
	}
}