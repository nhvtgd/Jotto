package model;

import java.net.*;
import java.util.ArrayList;
import java.io.*;

import javax.swing.table.AbstractTableModel;

/**
 * JottoModel is the data model for GUI JTable Have an internal array of
 * returnMsg Fully implement AbstractTableModel data type with its methods
 * (getColumnCount, getRowCount, ...) Have three columns ("Guess", "Common",
 * "Correct") For each valid cell position must have an appropriate value for
 * each use case
 */
@SuppressWarnings("serial")
public class JottoModel extends AbstractTableModel {
	/** Three columns for the table */
	private String[] columnNames = { "Guess", "Common", "Correct" };
	/** Arrasy to store the return message from the server */

	private ArrayList<ReturnMsg> data = new ArrayList<ReturnMsg>();

	public int getColumnCount() {
		return columnNames.length;
	}

	public int getRowCount() {
		return data.size();
	}

	/**
	 * return the table value at row, col
	 * 
	 */
	public Object getValueAt(int row, int col) {
		ReturnMsg msg = data.get(row);
		if (col == 0) {
			return msg.getGuess();
		} else {
			if (msg.isDone()) {
				if (msg.isError()) {
					if (col == 1) {
						return msg.toString();
					}
					return "";
				} else {
					int common = msg.getCommon();
					int correct = msg.getCorrect();
					if (common == 5 && correct == 5) {
						if (col == 1)
							return "You win!The secret word was "
									+ msg.getGuess() + "!";
						return "";
					} else {
						if (col == 1)
							return common;
						return correct;
					}
				}
			} else {
				if (col == 1)
					return "waiting for result from server...";
				return "";
			}
		}
	}

	/**
	 * Add the return message to the table
	 * 
	 * @return int: current size of the array of message
	 * @param returnMsg
	 *            : message returned from the server modifes: redraw table
	 * */
	public int addRow(ReturnMsg msg) {
		data.add(msg);
		this.fireTableDataChanged();
		return data.size() - 1;
	}

	/**
	 * Set the value of a specific rowNo to the return message
	 * 
	 * @param returnMsg
	 *            : return message from server
	 * @param int: row Number modifes: redraw table
	 * */
	public void setRow(ReturnMsg msg, int rowNo) {
		data.set(rowNo, msg);
		this.fireTableDataChanged();
	}

	/**
	 * Clear all rows of the table modifes: redraw table
	 * */
	public void clearRow() {
		data.clear();
		this.fireTableDataChanged();
	}

	/**
	 * make a string guess to the server, read back the reply and return it
	 * requires guess, id is not null
	 * 
	 * @param puzzleID
	 *            : the ID of the puzzle
	 * @param guess
	 *            : the guess string from the user
	 * @return returnMsg from the server
	 * @throws IOException
	 */
	public static ReturnMsg makeGuess(int puzzleID, String guess)
			throws IOException {
		String url = "http://6.005.scripts.mit.edu//jotto.py?puzzle="
				+ puzzleID + "&guess=" + guess;
		URL server = new URL(url);
		BufferedReader in = new BufferedReader(new InputStreamReader(
				server.openStream()));
		String input = in.readLine();
		return ReturnMsg.parseMsg(guess, input);
	}
}