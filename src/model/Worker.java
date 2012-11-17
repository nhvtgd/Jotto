package model;

import java.io.IOException;

import javax.swing.SwingWorker;

import ui.JottoGUI;

/**
 * Represent the SwingWoker to compute the long running task in the background
 * and make the game responsive to any input from user
 */
public class Worker extends SwingWorker<ReturnMsg, Void> {
	private final JottoModel model;

	private final int puzzleID;

	private final String guessStr;

	private final int rowNo;

	/**
	 * Worker that takes in JottoModel, id, guess String from server and row
	 * number
	 * 
	 * @param model
	 *            : JottoModel The model of the Jotto Game
	 * @param puzzleID
	 *            : int the Id of the puzzle to be call from server
	 * @param guessStr
	 *            : String guess from user
	 * @param rowNo
	 *            : int row Number to insert the return message
	 */
	public Worker(JottoModel model, int puzzleID, String guessStr, int rowNo) {
		this.model = model;
		this.puzzleID = puzzleID;
		this.guessStr = guessStr;
		this.rowNo = rowNo;
	}

	@Override
	protected ReturnMsg doInBackground() {
		ReturnMsg msg = null;
		try {
			msg = JottoModel.makeGuess(puzzleID, guessStr);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return msg;
	}

	@Override
	protected void done() {
		try {
			ReturnMsg serverMsg = get();
			System.out.println(serverMsg);
			if (puzzleID == JottoGUI.getPuzzleID()) {
				synchronized (model) {
					model.setRow(serverMsg, rowNo);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
