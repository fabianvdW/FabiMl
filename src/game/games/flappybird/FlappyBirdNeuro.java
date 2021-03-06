package game.games.flappybird;

import neuroevolution.Genome;

/**
 * This class implements the coincidation of NeuralNetworks and FlappyBird. It's
 * main purpose is to provide the inputs to the neural network.
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class FlappyBirdNeuro {

	/**
	 * Frame rate of the FlappyBird game
	 */
	public final static double FRAME_RATE = 10;

	/**
	 * Neural network representation.
	 */
	Genome g;

	/**
	 * Flappybird game instance
	 */
	FlappyBird b;

	/**
	 * Initializing class fields
	 * 
	 * @param g
	 *            neural network representation
	 */
	public FlappyBirdNeuro(Genome g) {
		this.g = g;
		this.b = new FlappyBird();
	}

	/**
	 * Plays a whole game of FlappyBird with the neural network. Thus this
	 * function also calculates necessary inputs for the neural network.
	 * <p>
	 * The network currently has the following inputs:
	 * </p>
	 * <ul>
	 * <li>y-velocity of player, normalized by factor 1/150</li>
	 * <li>x-velocity of player, normalized by factor 1/150</li>
	 * <li>delta y to the upper part of the next pipe, normalized by factor
	 * 1/150</li>
	 * <li>pipe-height, normalized by factor 1/150</li>
	 * <li>delta x to the next pipe, normalized by factor 1/150</li>
	 * <li>pipe-width, normalized by factor 1/150</li>
	 * </ul>
	 * <p>
	 * And one output, indicating if the player should jump (output&gt;0.5) or not
	 * (output&lt;=0.5)
	 * </p>
	 */
	public void play() {
		while (this.b.ingame) {
			double[] input = new double[6];
			// Input1 = vely;
			input[0] = b.velY / 150;
			// Input2 = velx;
			input[1] = b.velX / 150;
			// Pipe die n�her ist
			Pipe p = null;
			if (b.pipe1.endX < b.playerX - FlappyBird.PLAYER_WIDTH / 2) {
				// pipe2 ist n�her dran
				p = b.pipe2;
			} else {
				p = b.pipe1;
			}

			// Input 3 = delta y zu oberer pipe
			input[2] = (b.playerY - p.minY) / 150;
			// Input 4 pipe height
			input[3] = FlappyBird.PIPE_HEIGHT / 150;
			// Input 5 delta x zu pipe
			input[4] = (b.playerX - p.startX) / 150;
			// Input 6 pipe width
			input[5] = FlappyBird.PIPE_WIDTH / 150;
			double[] output = g.feedForward(input);
			boolean jump = false;
			if (output[0] > 0.5) {
				jump = true;
			}
			FlappyBirdMove fbm = new FlappyBirdMove(1.0 / (FlappyBirdNeuro.FRAME_RATE + 0.0), jump);
			this.b.update(fbm);
		}
	}
}
