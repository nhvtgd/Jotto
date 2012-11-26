package ui;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

import model.JottoModel;
import model.ReturnMsg;
import model.Worker;

/**
 * JottoGUI class Have: A button to get new puzzle number A textfield for user
 * to input new puzzle number A label to display puzzle number
 */
@SuppressWarnings("serial")
public class JottoGUI extends JFrame {

	/**
	 * Button to generate new puzzle
	 */
	private JButton newPuzzleButton;
	/**
	 * TextField to enter new puzzle from users
	 */
	private JTextField newPuzzleNumber;
	/**
	 * Label to display puzzle number
	 */
	private JLabel puzzleNumber;
	/**
	 * TextField to enter a guess
	 */
	private JTextField guess;
	/**
	 * Table to display guesses
	 */
	private JTable guessTable;
	/**
	 * General label
	 */
	private JLabel guessLabel;
	/**
	 * Data model for the table
	 */
	private JottoModel model;
	/**
	 * length limit of the input guess
	 */
	private int GUESS_LIMIT = 30;
	/**
	 * Puzzle ID
	 */
	private static int puzzleID = 16952;

	private Random random;

	/**
	 * setting the layout for the GUI Layout consists of three Group Layout
	 * which make up puzzle number group, guess group and table group
	 */
	public void setLayout() {
		Container cp = this.getContentPane();
		GroupLayout grpLayout = new GroupLayout(cp);
		cp.setLayout(grpLayout);
		grpLayout.setAutoCreateGaps(true);
		grpLayout.setAutoCreateContainerGaps(true);

		GroupLayout.SequentialGroup horSeqGrp = grpLayout
				.createSequentialGroup();
		GroupLayout.SequentialGroup verSeqGrp = grpLayout
				.createSequentialGroup();

		GroupLayout.ParallelGroup horPaGrp1 = grpLayout.createParallelGroup();

		GroupLayout.SequentialGroup horSeqGrp1 = grpLayout
				.createSequentialGroup();
		GroupLayout.SequentialGroup horSeqGrp2 = grpLayout
				.createSequentialGroup();
		GroupLayout.SequentialGroup horSeqGrp3 = grpLayout
				.createSequentialGroup();

		horSeqGrp1.addComponent(puzzleNumber);
		horSeqGrp1.addComponent(newPuzzleButton);
		horSeqGrp1.addComponent(newPuzzleNumber);
		horPaGrp1.addGroup(horSeqGrp1);

		horSeqGrp2.addComponent(guessLabel);
		horSeqGrp2.addComponent(guess);
		horPaGrp1.addGroup(horSeqGrp2);

		horSeqGrp3.addComponent(guessTable);
		horPaGrp1.addGroup(horSeqGrp3);

		horSeqGrp.addGroup(horPaGrp1);

		GroupLayout.ParallelGroup row1 = grpLayout
				.createParallelGroup(GroupLayout.Alignment.BASELINE);
		GroupLayout.ParallelGroup row2 = grpLayout.createParallelGroup();
		GroupLayout.ParallelGroup row3 = grpLayout.createParallelGroup();

		row1.addComponent(puzzleNumber);
		row1.addComponent(newPuzzleButton);
		row1.addComponent(newPuzzleNumber);

		row2.addComponent(guessLabel);
		row2.addComponent(guess);

		row3.addComponent(guessTable);

		verSeqGrp.addGroup(row1);
		verSeqGrp.addGroup(row2);
		verSeqGrp.addGroup(row3);

		grpLayout.setHorizontalGroup(horSeqGrp);
		grpLayout.setVerticalGroup(verSeqGrp);
	}

	/**
	 * Main GUI of the game
	 */
	public JottoGUI() {
		super("Simple Jotto GUI");
		model = new JottoModel();
		newPuzzleButton = new JButton();
		newPuzzleButton.setName("newPuzzleButton");
		newPuzzleButton.setText("New Puzzle");

		newPuzzleNumber = new JTextField();
		newPuzzleNumber.setName("newPuzzleNumber");
		newPuzzleNumber.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				setNewPuzzle();
				
			}
			
		});

		puzzleNumber = new JLabel();
		puzzleNumber.setName("puzzleNumber");
		puzzleNumber.setText("Puzzle #16952");// Any random number initially is
												// fine

		guessLabel = new JLabel();
		guessLabel.setName("guessLabel");
		guessLabel.setText("Type a guess here:");

		guess = new JTextField(GUESS_LIMIT);// limit to 30 chacter long for the
											// guess
		guess.setName("guess");
		guessTable = new JTable(model);
		guessTable.setName("guessTable");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		setLayout();

		// adding action listener for the guess textbox
		guess.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getPuzzleResult();
			}
		});

		// adding action listenr for the newPuzzle button
		newPuzzleButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				setNewPuzzle();
			}

		});
		this.pack();
	}

	/**
	 * Get the result from the server and reset guess text field add the return
	 * message to table in correct format 
	 * Modifies: JottoModel by add one row to it,
	 * guessField resets to empty string
	 */
	private void getPuzzleResult() {
		String guessStr = guess.getText();
		int rowNo = model.addRow(new ReturnMsg(guessStr));
		new Worker(model, puzzleID, guessStr, rowNo).execute();
		guess.setText("");
		pack();
	}

	/**
	 * Set the puzzle number to retrieve puzzle from the server if the user
	 * inputs incorrect format puzzle number, puzzle number will be randomly
	 * generated. The puzzle number will be displayed on the GUI 
	 * Modifies: modelTable will be reset to ihe initial state,
	 * puzzleId field will be updated
	 */
	private void setNewPuzzle() {
		String puzzleId = newPuzzleNumber.getText();
		if (puzzleId.length() == 0) {
			random = new Random();
			puzzleID = random.nextInt(10000);
		} else {
			try {
				puzzleID = Integer.parseInt(puzzleId);
			} catch (NumberFormatException e) {
				puzzleID = random.nextInt(10000);
			}
		}
		puzzleId = "" + puzzleID;
		puzzleNumber.setText("Puzzle #" + puzzleId);
		newPuzzleNumber.setText("");
		model.clearRow();
		pack();

	}

	/**
	 * 
	 * @return: the current puzzle ID of the game
	 */
	public static int getPuzzleID() {
		return puzzleID;
	}

	public static void main(final String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JottoGUI main = new JottoGUI();

				main.setVisible(true);
			}
		});
	}
}
