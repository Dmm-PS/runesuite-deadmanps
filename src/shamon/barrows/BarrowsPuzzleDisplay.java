package shamon.barrows;

import java.util.Random;

import shamon.barrows.data.BarrowsConstants;
import shamon.barrows.data.BarrowsPuzzleData;
import wind.model.players.Client;

public class BarrowsPuzzleDisplay {

	private static final Random random = new Random();

	/**
	 * Buttons.
	 */
	private static final int FIRST_OPTION_BUTTON = 4550;
	private static final int SECOND_OPTION_BUTTON = 4551;
	private static final int THIRD_OPTION_BUTTON = 4552;

	/**
	 * Main interface.
	 */
	private static final int BARROWS_PUZZLE_INTERFACE = 4543;

	/**
	 * Model childs.
	 */
	private static final int SEQUENCE_CHILD_START = 4545;
	private static final int OPTIONS_CHILD_START = 4550;

	/**
	 * The puzzle being displayed.
	 */
	private BarrowsPuzzleData puzzle;

	/**
	 * The current shuffled options.
	 */
	private int[] shuffledOptions;

	private boolean solved = false;

	public void reset() {
		setSolved(false);
	}

	/**
	 * Generates a randomized puzzle.
	 */
	private void randomizePuzzle() {
		// Choose random puzzles, shuffle the options.
		this.puzzle = BarrowsPuzzleData.PUZZLES[(int) (Math.random() * BarrowsPuzzleData.PUZZLES.length)];
		this.shuffledOptions = shuffleIntArray(puzzle.getOptions());
	}

	public boolean handleClick(Client client, int buttonClicked) {
		switch (buttonClicked) {
			case FIRST_OPTION_BUTTON:
			case SECOND_OPTION_BUTTON:
			case THIRD_OPTION_BUTTON:
				if (checkCorrectAnswer(buttonClicked)) {
					setSolved(true);
					client.sendMessage(BarrowsConstants.PUZZLE_SUCCESS);
				} else {
					client.sendMessage(BarrowsConstants.PUZZLE_FAILURE);
					client.getBarrows().getMaze().randomizeMaze();
				}
				client.getPA().closeAllWindows();
				return true;
		}
		return false;
	}

	/**
	 * Display the puzzle interface with random options.
	 * 
	 * @param player
	 *            The instance of the player.
	 */
	public void displayInterface(Client client) {
		randomizePuzzle();

		client.sendMessage(BarrowsConstants.PUZZLE_PROMPT);
		for (int i = 0; i < 3; i++) {
			int sequenceModel = puzzle.getSequenceModel(i);
			client.getPA().setWidgetModel(SEQUENCE_CHILD_START + i, sequenceModel);

			int optionModel = shuffledOptions[i];
			client.getPA().setWidgetModel(OPTIONS_CHILD_START + i, optionModel);
		}
		client.getPA().showInterface(BARROWS_PUZZLE_INTERFACE);
	}

	/**
	 * @param buttonClicked
	 *            The button clicked on puzzle interface.
	 * 
	 * @return true if the button clicked is the correct shape.
	 */
	private boolean checkCorrectAnswer(int buttonClicked) {
		int index = buttonClicked - FIRST_OPTION_BUTTON;

		int model = shuffledOptions[index];

		return model == puzzle.getAnswer();
	}

	public void setSolved(boolean solved) {
		this.solved = solved;
	}

	public boolean isSolved() {
		return solved;
	}

	private static int[] shuffleIntArray(int[] array) {
		int[] shuffledArray = new int[array.length];
		System.arraycopy(array, 0, shuffledArray, 0, array.length);

		for (int i = shuffledArray.length - 1; i > 0; i--) {
			int index = random.nextInt(i + 1);
			int a = shuffledArray[index];
			shuffledArray[index] = shuffledArray[i];
			shuffledArray[i] = a;
		}
		return shuffledArray;
	}
}