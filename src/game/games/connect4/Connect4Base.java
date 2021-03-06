package game.games.connect4;

import java.util.ArrayList;

import game.Game;
import game.GameVisualizer;
import neuralnet.NeuralNet;
import neuroevolution.Genome;
import processing.core.PApplet;

/**
 * Class which is used for basic Connect4 Logic, namely transforming a current
 * gamestate to an array of doubles, visualizing a gamestate, making a neural
 * net choose a move with a given gamestate, checking if the game is finished
 * and updating the board after a turn
 * <p>
 * Note that a gamestate is defined as a sequence of doubles (double array),
 * since this represents the inputs of Neural Nets and Genomes
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 * @see NeuralNet#feedForward(double[])
 * @see Genome#feedForward(double[])
 */
public class Connect4Base extends GameVisualizer {
	/**
	 * Saves the course of the game in an event list, so the game can be
	 * visualized asynchronously at any given time
	 */
	public ArrayList<Connect4Event> events;

	/**
	 * Defines the size of the board
	 */
	public int rows, columns;

	/**
	 * Debug variable, if it is set to true, any random move will be printed to
	 * the command line
	 */
	public static boolean print_random = false;

	/**
	 * This constructor is used to create a Connect4Base class which saves the
	 * board size and the course of the game. It is only used to then visualize
	 * the game with {@link #visualize()}
	 * 
	 * @param g
	 *            base class game, it should always be a game containing
	 *            Connect4Event
	 * @param events
	 *            course of the game in events
	 * @param rows
	 *            amount of rows in board
	 * @param columns
	 *            amount of columns in board
	 */
	public Connect4Base(Game g, ArrayList<Connect4Event> events, int rows, int columns) {
		super(g);
		this.events = events;
		this.rows = rows;
		this.columns = columns;
	}

	/**
	 * Visualizes a Connect4Game. It uses a PApplet Sketch
	 * 
	 * @see Connect4Visualizer
	 * @see PApplet
	 */
	@Override
	public void visualize() {
		Connect4Visualizer c4v = new Connect4Visualizer(this.events, this.rows, this.columns);
		PApplet.runSketch(new String[] { "game.games.connect4.Connect4Visualizer" }, c4v);

	}

	/**
	 * Transforms a board into a gamestate which is represented as double array.
	 * <p>
	 * The resulting array has a size of rows * columns * 3. It is calculated as
	 * follows: For each field in the board there is 3 entries. The first
	 * determines if the field is empty. If the field is empty, the resulting
	 * entry is 1, else 0. The second entry is 1, if player 1 has a token on the
	 * field, else it is 0. The third entry is 1, if player 2 has a token on the
	 * field, else it is 0.
	 * </p>
	 * 
	 * @param board
	 *            current board
	 * @return gamestate as double array
	 */
	public static double[] getInput(int[][] board) {
		int rows = board.length;
		int columns = board[0].length;
		double[] input = new double[rows * columns * 3];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				int index = i * columns * 3 + j * 3;
				if (board[i][j] == 0) {
					input[index] = 1;
					input[index + 1] = 0;
					input[index + 2] = 0;
				} else if (board[i][j] == 1) {
					input[index] = 0;
					input[index + 1] = 1;
					input[index + 2] = 0;
				} else {
					input[index] = 0;
					input[index + 1] = 0;
					input[index + 2] = 1;
				}
			}
		}
		return input;
	}

	/**
	 * Transforms a board into a gamestate which is represented as double array.
	 * <p>
	 * The resulting array has a size of rows * columns. It is calculated as
	 * follows: For each field in the board there is 1 entry. The entry is 0, if
	 * there is no token on the field, 1 if player 1 has a token on the field,
	 * and -1 if player 2 has a token on the field.
	 * </p>
	 * 
	 * @param board
	 *            current board
	 * @return gamestate as double array
	 */
	public static double[] getInput2(int[][] board) {
		int rows = board.length;
		int columns = board[0].length;
		double[] input = new double[rows * columns];
		for (int i = 0; i < rows; i++) {
			for (int j = 0; j < columns; j++) {
				int index = i * columns + j;
				if (board[i][j] == 0) {
					input[index] = 0;
				} else if (board[i][j] == 1) {
					input[index] = 1;
				} else {
					input[index] = -1;
				}
			}
		}
		return input;
	}

	/**
	 * Generates a double array with a size of the amount of columns in a board,
	 * which is filled with random doubles between 0 and 1. This array can then
	 * be used to determine a random move, for example by saying that the
	 * resulting move is in the column which has the highest or lowest random
	 * number.
	 * 
	 * @param board
	 *            current board
	 * @return randomly filled double array
	 */
	public static double[] getRandom(int[][] board) {
		if (print_random) {
			System.out.println("random");
		}
		double[] output = new double[board[0].length];
		for (int i = 0; i < output.length; i++) {
			output[i] = Math.random();
		}
		return output;
	}

	/**
	 * Makes a neural net feed forward a gamestate of a board. Returns a
	 * Connect4ThinkObject which has {@link Connect4ThinkObject#random} set to
	 * true, if the neural net was null.
	 * 
	 * @param nn
	 *            NeuralNet which the gamestate should be fed into
	 * @param board
	 *            current board
	 * @return Connect4ThinkObject result of the forward propagation, it
	 *         contains a random double array if the neural net was null
	 * @see NeuralNet#feedForward(double[])
	 * @see #getInput(int[][])
	 * @see #getInput2(int[][])
	 * @see #getRandom(int[][])
	 * @see Connect4ThinkObject
	 */
	public static Connect4ThinkObject think(NeuralNet nn, int[][] board) {
		if (nn != null) {
			double[] input = getInput2(board);

			return new Connect4ThinkObject(nn.feedForward(input).transpose().getMatrix()[0], false);
		} else {

			return new Connect4ThinkObject(getRandom(board), true);
		}
	}

	/**
	 * Makes a neural net represented as genome feed forward a gamestate of a
	 * board. Returns a Connect4ThinkObject which has
	 * {@link Connect4ThinkObject#random} set to true, if the genome was null.
	 * 
	 * @param g
	 *            Genome which the input should be fed into
	 * @param board
	 *            current board
	 * @return Connect4ThinkObject result of the forward propagation, it
	 *         contains a random double array if the genome was null
	 * @see Genome#feedForward(double[])
	 * @see #getInput(int[][])
	 * @see #getInput2(int[][])
	 * @see #getRandom(int[][])
	 * @see Connect4ThinkObject
	 */
	public static Connect4ThinkObject think(Genome g, int[][] board) {
		if (g != null) {
			return new Connect4ThinkObject(g.feedForward(getInput2(board)), false);
		} else {
			return new Connect4ThinkObject(getRandom(board), true);
		}
	}

	/**
	 * returns true if the specified column is already filled with tokens, false
	 * if it is not.
	 * 
	 * @param column
	 *            to be checked column
	 * @param board
	 *            current board
	 * @return true if full, false if not full
	 */
	public static boolean columnFilled(int column, int[][] board) {
		return board[0][column] != 0;
	}

	/**
	 * Gets the next row which a token will be placed in in the specified column
	 * 
	 * @param column
	 *            to be checked column
	 * @param board
	 *            current board
	 * @return -1 if column is full, else the row which is next to be filled
	 *         with a token.
	 */
	public static int getRow(int column, int[][] board) {
		int row = 0;
		for (; row <= board.length; row++) {
			if (row == board.length) {
				break;
			}
			if (board[row][column] != 0) {
				break;
			}
		}
		return row - 1;
	}

	/**
	 * Adds a token to the board, where the token should be put is specified in
	 * the given event.
	 * 
	 * @param e
	 *            event which should be entered on the board
	 * @param board
	 *            current board
	 * @return updated board
	 */
	public static int[][] enterGameEventInBoard(Connect4Event e, int[][] board) {
		board[e.y][e.x] = e.player;
		return board;
	}

	/**
	 * Checks if the specified game event created a row of four.
	 * 
	 * @param e
	 *            event which was last entered on board
	 * @param board
	 *            current board
	 * @return true if the game is finished, false if not
	 */
	public static boolean ueberpruefeEndbedingung(Connect4Event e, int[][] board) {
		int primary = 1;
		if (board[e.y][e.x] == 0) {
			throw new RuntimeException();
		} else if (board[e.y][e.x] == 2) {
			primary = 2;
		}
		// Nach unten checken,also y erh�hen
		int bottom = 0;
		for (int i = 1; i <= 3; i++) {
			int newY = e.y + i;
			if (newY == board.length) {
				break;
			}
			if (board[newY][e.x] == primary) {
				bottom += 1;
			} else {
				break;
			}
		}
		// Nach oben checken, also y reduzieren
		int top = 0;
		for (int i = 1; i <= 3; i++) {
			int newY = e.y - i;
			if (newY < 0) {
				break;
			}
			if (board[newY][e.x] == primary) {
				top += 1;
			} else {
				break;
			}
		}
		if (bottom + top + 1 >= 4) {
			return true;
		}

		// Nach rechts checken, also x erh�hen
		int right = 0;
		for (int i = 1; i <= 3; i++) {
			int newX = e.x + i;
			if (newX == board[0].length) {
				break;
			}
			if (board[e.y][newX] == primary) {
				right += 1;
			} else {
				break;
			}
		}
		// Nach rechts checken, also x erh�hen
		int left = 0;
		for (int i = 1; i <= 3; i++) {
			int newX = e.x - i;
			if (newX < 0) {
				break;
			}
			if (board[e.y][newX] == primary) {
				left += 1;
			} else {
				break;
			}
		}
		if (left + right + 1 >= 4) {
			return true;
		}

		// Nach oben links und unten rechts checken
		int topLeft = 0;
		int bottomRight = 0;
		for (int i = 1; i <= 3; i++) {
			int newX = e.x - i;
			int newY = e.y - i;
			if (newX < 0 || newY < 0) {
				break;
			}
			if (board[newY][newX] == primary) {
				topLeft += 1;
			} else {
				break;
			}
		}
		for (int i = 1; i <= 3; i++) {
			int newX = e.x + i;
			int newY = e.y + i;
			if (newX == board[0].length || newY == board.length) {
				break;
			}
			if (board[newY][newX] == primary) {
				bottomRight += 1;
			} else {
				break;
			}
		}
		if (topLeft + bottomRight + 1 >= 4) {
			return true;
		}
		// Nach oben rechts und unten links schauen
		int topRight = 0;
		int bottomLeft = 0;
		for (int i = 1; i <= 3; i++) {
			int newX = e.x + i;
			int newY = e.y - i;
			if (newX == board[0].length || newY < 0) {
				break;
			}
			if (board[newY][newX] == primary) {
				topRight += 1;
			} else {
				break;
			}
		}
		for (int i = 1; i <= 3; i++) {
			int newX = e.x - i;
			int newY = e.y + i;
			if (newY == board.length || newX < 0) {
				break;
			}
			if (board[newY][newX] == primary) {
				bottomLeft += 1;
			} else {
				break;
			}
		}
		if (topRight + bottomLeft + 1 >= 4) {
			return true;
		}
		return false;
	}

}
