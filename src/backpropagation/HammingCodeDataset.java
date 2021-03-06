package backpropagation;

import java.util.ArrayList;

import matrix.Matrix;
import neuralnet.Activator;
import neuralnet.NeuralNet;
import neuralnet.Relu;
import neuralnet.Sigmoid;

/**
 * Manual start of a program that trains a neural network using backpropagation
 * on the (11,4) HammingCode. This class also provides methods for loading test
 * and training data of the (11,4) HammingCode.
 * <p>
 * This class is used within the {@link gui.GUI} class to load and dispose data.
 * </p>
 * 
 * @author Fabian von der Warth
 * @version 1.0
 */

public class HammingCodeDataset implements Dataset {
	/**
	 * Test data Matrix, shape(2000,11)
	 * 
	 * @see Matrix
	 * @see #init()
	 */

	Matrix testData;

	/**
	 * Test label Matrix, shape(2000,4)
	 * 
	 * @see Matrix
	 * @see #init()
	 */
	Matrix testlabelData;

	/**
	 * Train data Matrix, shape(48,11)
	 * 
	 * @see Matrix
	 * @see #init()
	 */
	Matrix trainData;

	/**
	 * Train label Matrix, shape(48,4)
	 * 
	 * @see Matrix
	 * @see #init()
	 */
	Matrix trainlabelData;

	/**
	 * Indicates if the class fields have been initialized or not.
	 * 
	 * @see #init()
	 * @see #trainData
	 * @see #trainlabelData
	 * @see #testData
	 * @see #testlabelData
	 */
	boolean initialized = false;

	/**
	 * Manually starts training a NeuralNet with 2 hidden Layers � 20 neurons
	 * the Hamming Code
	 * <p>
	 * Neural Net has the Relu activation function for the first layer and
	 * sigmoid for the other layers It is trained in 15 000 epochs with a test
	 * in every 10 epochs using 4 (if available) cores Learning rate is 0.5
	 * Batch size is 128 NeuralNet will be written to "./nn1.nn"
	 * 
	 * @see NeuralNet
	 * @see NeuralNet#SGD(Matrix, Matrix, Matrix, Matrix, int, int, int, double,
	 *      int)
	 * 
	 *      </p>
	 *      <p>
	 *      Output is to be found on the command line.
	 *      </p>
	 * @param args
	 *            Command line parameters: None
	 */
	public static void main(String[] args) {

		// Aufbau des NN
		ArrayList<Integer> hiddenNeurons = new ArrayList<Integer>();
		hiddenNeurons.add(20);
		hiddenNeurons.add(20);
		ArrayList<Activator> activationFunctions = new ArrayList<Activator>();
		activationFunctions.add(new Relu());
		activationFunctions.add(new Sigmoid());
		activationFunctions.add(new Sigmoid());
		NeuralNet nn = new NeuralNet(11, 4, hiddenNeurons, activationFunctions);
		// NeuralNet nn = (NeuralNet) ea.ObjectLoader.loadObject("./nn1.nn");
		HammingCodeDataset t = new HammingCodeDataset();
		t.init();

		System.out.println(nn.hiddenNeurons.toString());
		System.out.println(nn.activationFunctions.toString());
		System.out.println(nn.evaluate(t.testData, t.testlabelData));
		// nn.SGD(train_inputs, train_labels, test_inputs, test_labels,
		// batch_size, epochs, test_every, learning_rate, anzahl_cores);
		// nn.SGD(train_inputs, train_labels, test_inputs, test_labels,
		// batch_size, epochs, test_every, learning_rate, anzahl_cores,
		// ausgabe);
		nn.SGD(t.trainData, t.trainlabelData, t.testData, t.testlabelData, 128, 15000, 10, 0.5, 4);
		ea.ObjectWriter.saveObject("./nn1.nn", nn);
	}

	/**
	 * Initializes the data fields responsible for train and test data and
	 * labels with random HammingCode data taken from the (11,4) HammingCode
	 * <p>
	 * Generates a data set which contains 2000 entries for train data and
	 * labels, leaving 48 for test data and labels (2.344%)
	 * </p>
	 * 
	 * @see testData
	 * @see testlabelData
	 * @see trainData
	 * @see trainlabelData
	 */
	public void init() {
		this.initialized = true;
		double[][] allData = new double[2048][11];
		double[][] allLabels = new double[2048][4];
		for (int i = 0; i < 2048; i++) {
			String s = Integer.toBinaryString(i);
			if (s.length() < 11) {
				int l = s.length();
				for (int j = 0; j < 11 - l; j++) {
					s = "0" + s;
				}
			}
			double[] dataRow = new double[11];
			for (int j = 0; j < 11; j++) {
				char c = s.charAt(j);
				double d = Double.parseDouble("" + c);
				dataRow[j] = d;
			}
			double p0 = (int) dataRow[0] ^ (int) dataRow[1] ^ (int) dataRow[3] ^ (int) dataRow[4] ^ (int) dataRow[6]
					^ (int) dataRow[8] ^ (int) dataRow[10];
			double p1 = (int) dataRow[0] ^ (int) dataRow[2] ^ (int) dataRow[5] ^ (int) dataRow[6] ^ (int) dataRow[9]
					^ (int) dataRow[3] ^ (int) dataRow[10];
			double p2 = (int) dataRow[1] ^ (int) dataRow[2] ^ (int) dataRow[3] ^ (int) dataRow[7] ^ (int) dataRow[8]
					^ (int) dataRow[9] ^ (int) dataRow[10];
			double p3 = (int) dataRow[4] ^ (int) dataRow[5] ^ (int) dataRow[6] ^ (int) dataRow[7] ^ (int) dataRow[8]
					^ (int) dataRow[9] ^ (int) dataRow[10];
			double[] labelRow = new double[4];
			labelRow[0] = p0;
			labelRow[1] = p1;
			labelRow[2] = p2;
			labelRow[3] = p3;
			allLabels[i] = labelRow;
			// System.out.println(s);
			// System.out.println(p0+" "+p1+" "+p2+" "+p3);
			allData[i] = dataRow;
		}
		double[][] testData = new double[48][11];
		double[][] testlabelData = new double[48][4];
		double[][] trainData = new double[2000][11];
		double[][] trainlabelData = new double[2000][4];
		ArrayList<Integer> index = new ArrayList<Integer>();
		for (int i = 0; i < 2048; i++) {
			index.add(i);
		}
		for (int j = 0; j < 48; j++) {
			int random = (int) (Math.random() * index.size());
			int indexI = index.get(random);
			index.remove(new Integer(indexI));
			testData[j] = allData[indexI];
			testlabelData[j] = allLabels[indexI];
		}
		for (int j = 0; j < 2000; j++) {
			int indexI = index.get(j);
			trainData[j] = allData[indexI];
			trainlabelData[j] = allLabels[indexI];
		}
		this.testData = new Matrix(48, 11);
		this.testData.setMatrix(testData);
		this.testlabelData = new Matrix(48, 4);
		this.testlabelData.setMatrix(testlabelData);
		this.trainData = new Matrix(2000, 11);
		this.trainData.setMatrix(trainData);
		this.trainlabelData = new Matrix(2000, 4);
		this.trainlabelData.setMatrix(trainlabelData);
	}

	/**
	 * Returns the already initialized field {@link #trainData}
	 * 
	 * @return Matrix training data, shape(2000,11)
	 * @see #trainData
	 * @see #init()
	 */
	@Override
	public Matrix getTrainData() {
		if (!this.initialized) {
			this.init();
		}
		return this.trainData;
	}

	/**
	 * Returns the already initialized field {@link #trainlabelData}
	 * 
	 * @return Matrix training label data, shape(2000,4)
	 * @see #trainlabelData
	 * @see #init()
	 */
	@Override
	public Matrix getTrainLabels() {
		if (!this.initialized) {
			this.init();
		}
		return this.trainlabelData;
	}

	/**
	 * Returns the already initialized field {@link #testData}
	 * 
	 * @return Matrix test data, shape(48,11)
	 * @see #testData
	 * @see #init()
	 */
	@Override
	public Matrix getTestData() {
		if (!this.initialized) {
			this.init();
		}
		return this.testData;
	}

	/**
	 * Returns the already initialized field {@link #testlabelData}
	 * 
	 * @return Matrix test label data, shape(48,4)
	 * @see #testlabelData
	 * @see #init()
	 */
	@Override
	public Matrix getTestLabels() {
		if (!this.initialized) {
			this.init();
		}
		return this.testlabelData;
	}
}
