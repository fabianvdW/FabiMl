package backpropagation;

import java.util.ArrayList;

import matrix.Matrix;
import neuralnet.Activator;
import neuralnet.NeuralNet;
import neuralnet.Sigmoid;

/**
 * Manual start of a program that trains a neural network using backpropagation
 * on 2-bit XOR. This class also provides methods for loading test and training
 * data of 2-bit XOR.
 * <p>
 * This class is used within the {@link gui.GUI} class to load and dispose data.
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class XORDataset implements Dataset {
	/**
	 * Manually starts training a neural net on XOR
	 * <p>
	 * The Neural Net consists of 1 hidden layer � 2 Neurons, with both layer
	 * activation functions being the sigmoid function. The current network form
	 * is the smallest possible for fitting XOR. The Net is trained in 100 000
	 * epochs with an evaluation after each epoch. The learning rate is 1 with a
	 * batch size of 4. Only 1 core is used during training since the train data
	 * size is capped at 4 and multithreading is slow for low batch sizes, see
	 * at
	 * {@link NeuralNet#SGD(Matrix, Matrix, Matrix, Matrix, int, int, int, double, int)}
	 * </p>
	 * <p>
	 * Output is to be found on the command line.
	 * </p>
	 * 
	 * @param args
	 *            command line parameters - None
	 */
	public static void main(String[] args) {
		XORDataset x = new XORDataset();
		ArrayList<Integer> hiddenNeurons = new ArrayList<Integer>();
		hiddenNeurons.add(2);
		ArrayList<Activator> activationFunctions = new ArrayList<Activator>();
		activationFunctions.add(new Sigmoid());
		activationFunctions.add(new Sigmoid());
		NeuralNet nn = new NeuralNet(2, 1, hiddenNeurons, activationFunctions);
		Matrix inputs = x.getTrainData();
		Matrix labels = x.getTrainLabels();
		double[][] inputM = inputs.getMatrix();
		double[][] labelM = labels.getMatrix();
		// nn.SGD(train_inputs, train_labels, test_inputs, test_labels,
		// batch_size, epochs, test_every, learning_rate, anzahl_cores);
		nn.SGD(inputs, labels, inputs, labels, 4, 100000, 1, 1, 1);
		for (int i = 0; i < inputM.length; i++) {
			double[] input = inputM[i];
			double[] label = labelM[i];
			System.out.println("Feeding Forward:\n");
			System.out.println(input[0] + " ," + input[1] + " : " + nn.feedForward(input));
			System.out.println("Label:" + label[0]);
		}
		System.out.println("------------------------------------");
		System.out.println("Neural Net Weights nach dem Training: ");
		// System.out.println(nn);
	}

	/**
	 * Generates Matrix of train data for the XOR problem
	 * <p>
	 * Note that the train data is equal to the test data in the XOR case
	 * </p>
	 * 
	 * @return matrix of all 4 possible 2-bit sequences, shape(4,2)
	 * @see #getTestData()
	 */
	@Override
	public Matrix getTrainData() {
		Matrix inputs = new Matrix(4, 2);
		double[][] inputM = { { 0, 0 }, { 0, 1 }, { 1, 0 }, { 1, 1 } };
		inputs.setMatrix(inputM);
		return inputs;
	}

	/**
	 * Generates Matrix of train data labels for the XOR problem. The labels
	 * correspond to the bit sequences of {@link #getTrainData()}
	 * <p>
	 * Note that the train data label is equal to the test data label in the XOR
	 * case
	 * </p>
	 * 
	 * @return matrix of all 4 corresponding 1-bit XOR result for the input at
	 *         {@link #getTrainData()}, shape(4,1)
	 * @see #getTestLabels()
	 * @see #getTrainData()
	 */
	@Override
	public Matrix getTrainLabels() {
		Matrix labels = new Matrix(4, 1);
		double[][] labelM = { { 0 }, { 1 }, { 1 }, { 0 } };
		labels.setMatrix(labelM);
		return labels;
	}

	/**
	 * Returns the same as {@link #getTrainData()}
	 * 
	 * @return matrix of all 4 possible 2-bit sequences, shape(4,2)
	 */
	@Override
	public Matrix getTestData() {
		return getTrainData();
	}

	/**
	 * Returns the same as {@link #getTrainLabels()}
	 * 
	 * @return matrix of all 4 corresponding 1-bit XOR result for the input at
	 *         {@link #getTestData()}, shape(4,1)-
	 */
	@Override
	public Matrix getTestLabels() {
		return getTrainLabels();
	}

}
