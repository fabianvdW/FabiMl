package backpropagation;

import java.util.ArrayList;

import matrix.Matrix;
import neuralnet.Activator;
import neuralnet.NeuralNet;
import neuralnet.Relu;
import neuralnet.Sigmoid;

/**
 * Manual start of a program that trains a neural network using backpropagation
 * on 4-bit XOR. This class also provides methods for loading test and training
 * data of 4-bit XOR.
 * <p>
 * This class is used within the {@link gui.GUI} class to load and dispose data.
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */
public class VierBitXorDataSet implements Dataset {
	/**
	 * This method manually starts a training session of a Neural Net which will
	 * be learning 4-bit XOR.
	 * <p>
	 * The net has 2 hidden layers � 16 neurons and has relu as activation
	 * function and sigmoid as activation function of the last layer It has 4
	 * Inputs and 1 Output indicating if the output bit should be 1
	 * (Output&gt;0.5) or 0 (Output &lt;=0.5) The batch size is 4 and the net is
	 * trained in 10000 epochs, while an evaluation of the net is made every
	 * epoch. A learning rate of 0.01 was chosen with all 4 cores (if available)
	 * being used.
	 * </p>
	 * <p>
	 * Output is to be found on the command line.
	 * </p>
	 * 
	 * @param args
	 *            command line parameters - None
	 * @see NeuralNet#SGD(Matrix, Matrix, Matrix, Matrix, int, int, int, double,
	 *      int)
	 */
	public static void main(String[] args) {
		VierBitXorDataSet t = new VierBitXorDataSet();
		ArrayList<Integer> hiddenNeurons = new ArrayList<Integer>();
		hiddenNeurons.add(16);
		hiddenNeurons.add(16);
		ArrayList<Activator> activationFunctions = new ArrayList<Activator>();
		activationFunctions.add(new Relu());
		activationFunctions.add(new Relu());
		activationFunctions.add(new Sigmoid());
		NeuralNet nn = new NeuralNet(4, 1, hiddenNeurons, activationFunctions);
		Matrix inputs = t.getTestData();
		double[][] inputM = inputs.getMatrix();
		Matrix labels = t.getTestLabels();
		double[][] labelM = labels.getMatrix();
		// nn.SGD(train_inputs, train_labels, test_inputs, test_labels,
		// batch_size, epochs, test_every, learning_rate, anzahl_cores);
		nn.SGD(inputs, labels, inputs, labels, 4, 10000, 1, 0.01, 4);
		for (int i = 0; i < inputM.length; i++) {
			double[] input = inputM[i];
			double[] label = labelM[i];
			System.out.println("Feeding Forward:\n");
			System.out.println(
					input[0] + " ," + input[1] + " ," + input[2] + " ," + input[3] + " : " + nn.feedForward(input));
			System.out.println("Label:" + label[0]);
		}
		System.out.println("------------------------------------");
		System.out.println("Neural Net Weights nach dem Training: ");
		// System.out.println(nn);
	}

	/**
	 * Returns test data, train and test data are identical in this case, since
	 * overfitting in 4-bit xor or any logical function with a finitely big
	 * scope of definition is not pointless.
	 * 
	 * @return matrix of train data, shape(16,4)
	 * @see #getTestData()
	 */
	@Override
	public Matrix getTrainData() {
		return getTestData();
	}

	/**
	 * Returns test data labels, train and test data are identical in this case,
	 * since overfitting in 4-bit xor or any logical function with a finitely
	 * big scope of definition is not pointless.
	 * 
	 * @return matrix of train data labels, shape (16,1)
	 * @see #getTestLabels()
	 */
	@Override
	public Matrix getTrainLabels() {
		return getTestLabels();
	}

	/**
	 * returns a matrix consisting of all 2^4 = 16 different possible 4-bit
	 * sequences
	 * 
	 * @return matrix of test data, shape(16,4)
	 */
	@Override
	public Matrix getTestData() {
		Matrix inputs = new Matrix(16, 4);
		double[][] inputM = { { 0, 0, 0, 0 }, { 0, 0, 0, 1 }, { 0, 0, 1, 0 }, { 0, 0, 1, 1 }, { 0, 1, 0, 0 },
				{ 0, 1, 0, 1 }, { 0, 1, 1, 0 }, { 0, 1, 1, 1 }, { 1, 0, 0, 0 }, { 1, 0, 0, 1 }, { 1, 0, 1, 0 },
				{ 1, 0, 1, 1 }, { 1, 1, 0, 0 }, { 1, 1, 0, 1 }, { 1, 1, 1, 0 }, { 1, 1, 1, 1 } };
		// 0000 = 0j
		// 0001 = 1j
		// 0010= 1j
		// 0011= 0j
		// 0100= 1j
		// 0101 = 0j
		// 0110= 0j
		// 0111 = 1j
		// 1000=1j
		// 1001 =0j
		// 1010 = 0j
		// 1011=1j
		// 1100 =0j
		// 1101 =1j
		// 1110 = 1j
		// 1111 =0j
		inputs.setMatrix(inputM);
		return inputs;
	}

	/**
	 * returns a matrix consisting of 4-bit XOR labels corresponding to the 16
	 * 4-bit sequences from {@link #getTestData()}
	 * 
	 * @return matrix of test label, shape(16,1)
	 */
	@Override
	public Matrix getTestLabels() {
		Matrix labels = new Matrix(16, 1);
		double[][] labelM = { { 0 }, { 1 }, { 1 }, { 0 }, { 1 }, { 0 }, { 0 }, { 1 }, { 1 }, { 0 }, { 0 }, { 1 }, { 0 },
				{ 1 }, { 1 }, { 0 } };
		labels.setMatrix(labelM);
		return labels;
	}
}
