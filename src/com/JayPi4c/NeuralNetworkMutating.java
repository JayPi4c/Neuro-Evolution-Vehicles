package com.JayPi4c;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 * @author JayPi4c
 * @version 1.0.1
 */
public class NeuralNetworkMutating implements Serializable {

	private static final long serialVersionUID = 5795625580326323029L;
	private static final String err_Message = "An error has occured. Please contact jonas4c@freenet.de to get help on this problem. Please consider to add the following errorcode for debugging purposes: ";

	private int inputnodes, hiddennodes, outputnodes;
	private double learningrate;

	private Matrix wih, who;

	/**
	 * Dieser Konstruktor ist nur dazu da, um {@link #sigmoid(double)} und
	 * {@link #random(double)} f&uumlr
	 * {@link Matrix#map(Object, java.lang.reflect.Method)} erreichbar zu machen
	 * 
	 * @deprecated
	 * @since 1.0.0
	 */
	/*
	 * @Deprecated public NeuralNetworkMutating() { }
	 */

	/**
	 * Erstellt ein Neuronales Netz mit der jeweiligen Anzahl an Input-, Hidden- und
	 * Outputnodes. Dieses Neuronale Netz kann mit
	 * {@link #train(double[][], double[][])} und {@link #query(double[][])} benutzt
	 * werden.
	 * 
	 * @param inputnodes
	 * @param hiddennodes
	 * @param hiddennodes
	 * @param learningrate
	 * @see #train(double[][], double[][]), {@link #train(Matrix, Matrix)},
	 *      {@link #query(double[][])}, {@link #query(Matrix)}
	 * @since 1.0.0
	 */
	public NeuralNetworkMutating(int inputnodes, int hiddennodes, int outputnodes, double learningrate) {
		this.inputnodes = inputnodes;
		this.hiddennodes = hiddennodes;
		this.outputnodes = outputnodes;
		this.learningrate = learningrate;
		this.wih = new Matrix(this.hiddennodes, this.inputnodes).randomize(-0.5, 0.5);
		this.who = new Matrix(this.outputnodes, this.hiddennodes).randomize(-0.5, 0.5);

	}

	// ****************************************************************************************************************//
	/**
	 * Durch die gegebenen Eingaben l&aumlsst sich mit den Gewichten eine Ausgabe
	 * ermitteln, die die Sch&aumltzung des Neuronalen Netzes wider spiegeln.
	 * 
	 * @param inputs_list
	 * @return die Sch&aumltzung des Neuronalen Netzes.
	 * @since 1.0.0
	 */
	public Matrix query(Matrix inputs_list) {
		Matrix final_outputs = null;
		try {
			Matrix inputs = Matrix.transpose(inputs_list);
			Matrix hidden_inputs = Matrix.dot(this.wih, inputs);
			Matrix hidden_outputs;
				hidden_outputs = hidden_inputs.map(this,
						NeuralNetworkMutating.class.getMethod("sigmoid", double.class));
				Matrix final_inputs = Matrix.dot(this.who, hidden_outputs);
				final_outputs = final_inputs.map(this, NeuralNetworkMutating.class.getMethod("sigmoid", double.class));
			
			
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			System.out.println(err_Message);
			System.err.println(e);
		} catch (Exception e) {
			// TODO Auto-generated catch block Die Richtige Exception muss nur oben hinzugef�gt werden.
			e.printStackTrace();
		}
		return final_outputs;
	}

	/**
	 * Durch die gegebenen Eingaben l&aumlsst sich mit den Gewichten eine Ausgabe
	 * ermitteln, die die Sch&aumltzung des Neuronalen Netzes wider spiegeln.
	 * 
	 * @param inputs_list
	 * @return die Sch&aumltzung des Neuronalen Netzes.
	 * @since 1.0.0
	 */
	public Matrix query(double inputs_list[][]) {
		return this.query(new Matrix(inputs_list));
	}

	// ****************************************************************************************************************//
	/**
	 * Durch angabe der Eingabe und der gew&uumlnschten Ausgabe k&oumlnnen die
	 * Gewichte des Neuronalen Netzes angepasst werden.
	 * 
	 * @param inputs_list  die Eingaben in das Neuronale Netz
	 * @param targets_list die Ausgabe des Neuronalen Netzes
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @since 1.0.0
	 */
	public void train(Matrix inputs_list, Matrix targets_list) {
		try {
			Matrix inputs = Matrix.transpose(inputs_list);
			Matrix targets = Matrix.transpose(targets_list);

			Matrix hidden_inputs = Matrix.dot(this.wih, inputs);
			Matrix hidden_outputs = hidden_inputs.map(this,
					NeuralNetworkMutating.class.getMethod("sigmoid", double.class));
			Matrix final_inputs = Matrix.dot(this.who, hidden_outputs);
			Matrix final_outputs = final_inputs.map(this,
					NeuralNetworkMutating.class.getMethod("sigmoid", double.class));

			Matrix output_errors = Matrix.sub(targets, final_outputs);
			Matrix hidden_errors = Matrix.dot(Matrix.transpose(this.who), output_errors);

			this.who.add(Matrix.mult(
					Matrix.dot(Matrix.mult(output_errors, Matrix.mult(final_outputs, Matrix.sub(1, final_outputs))),
							Matrix.transpose(hidden_outputs)),
					this.learningrate));

			this.wih.add(Matrix.mult(
					Matrix.dot(Matrix.mult(hidden_errors, Matrix.mult(hidden_outputs, Matrix.sub(1, hidden_outputs))),
							Matrix.transpose(inputs)),
					this.learningrate));
		} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException
				| SecurityException e) {
			System.out.println(err_Message);
			System.err.println(e);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * Durch angabe der Eingabe und der gew&uumlnschten Ausgabe k&oumlnnen die
	 * Gewichte des Neuronalen Netzes angepasst werden.
	 * 
	 * @param inputs_list  die Eingaben in das Neuronale Netz
	 * @param targets_list die Ausgabe des Neuronalen Netzes
	 * @since 1.0.0
	 */
	public void train(double inputs_list[][], double targets_list[][]) {
		this.train(new Matrix(inputs_list), new Matrix(targets_list));
	}

	// ****************************************************************************************************************//

	/**
	 * Diese Funktion gibt einen zuf&aumllligen Wert zwischen -0.5 und 0.5 aus.
	 * 
	 * @param d Notwendig f&uumlr die {@link com.JayPi4c.Matrix.Matrix}
	 * @return zuf&aumllliger Wert zwischen -0.5 und 0.5
	 * @since 1.0.0
	 */
	public double random(double d) {
		return Math.random() - 0.5;
	}

	/**
	 * Ermittelt zu einem gegebenen x-Wert den passenden y-Wert der
	 * Sigmoid-Funktion.
	 * 
	 * @param x
	 * @return Der Wert der Sigmoid-Funktion f&uumlr das angegebene x
	 * @since 1.0.0
	 */
	public double sigmoid(double x) {
		return (1 / (1 + Math.pow(Math.E, -x)));
	}

	/**
	 * 
	 * Mit dieser Funktion l&aumlsst sich das Objekt des Neuronalen Netzes in einer
	 * Datei speichern, sodass auch nach beenden des Programms der Fortschritt bzw.
	 * der Zustand des Netzes gespeichert bleiben kann. Die entstandene Datei kann
	 * anschlie&szligend bzw. bei einem Neustart des Programms mit
	 * {@link #deserialize(File)} wieder eingelesen werden.
	 * 
	 * @param nn
	 * @throws IOException
	 * @see #deserialize(File)
	 * @since 1.0.0
	 */
	public void serialize() throws IOException {
		String absolutePath = new File(".").getAbsolutePath();
		File file = new File(absolutePath);
		absolutePath = file.getParentFile().toString();
		FileOutputStream fos = new FileOutputStream(new File(absolutePath + "/NeuralNetwork.nn"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(this);
		oos.close();
	}

	/**
	 * 
	 * Mit dieser Funktion l&aumlsst sich ein Objekt des Neuronalen Netzes in einer
	 * Datei speichern, sodass auch nach beenden des Programms der Fortschritt bzw.
	 * der Zustand des Netzes gespeichert bleiben kann. Die entstandene Datei kann
	 * anschlie&szligend bzw. bei einem neustart des Programms mit
	 * {@link #deserialize(File)} wieder eingelesen werden.
	 * 
	 * @param nn
	 * @throws IOException
	 * @see #deserialize(File)
	 * @since 1.0.0
	 */
	public static void serialize(NeuralNetworkMutating nn) throws IOException {
		String absolutePath = new File(".").getAbsolutePath();
		File file = new File(absolutePath);
		absolutePath = file.getParentFile().toString();
		FileOutputStream fos = new FileOutputStream(new File(absolutePath + "/NeuralNetwork.nn"));
		ObjectOutputStream oos = new ObjectOutputStream(fos);
		oos.writeObject(nn);
		oos.close();
	}

	/**
	 * Mit dieser Funktion l&aumlsst sich ein Neuronales Netz Objekt aus einer Datei
	 * erstellen, die mit {@link #serialize()} erstellt wurde.
	 * 
	 * @param f Ein File Object, welches die Daten des Neuronalen Netzes beinhaltet
	 * @return gibt ein Neuronales Netz zur&uumlck
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @see #serialize()
	 * @since 1.0.0
	 */
	public static NeuralNetworkMutating deserialize(File f) throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(f);
		ObjectInputStream ois = new ObjectInputStream(fis);

		// NeuralNetwork output = null;
		NeuralNetworkMutating output = (NeuralNetworkMutating) ois.readObject();
		ois.close();
		return output;
	}

	/**
	 * 
	 * @return Gibt eine Kopie des neuronalen Netzes zur&uumlck, welche in die exakt
	 *         gleichen Werte hat, wie das originale Netz, aber keinen Bezug hat.
	 * @since 1.0.0
	 */
	public NeuralNetworkMutating copy() {
		NeuralNetworkMutating output = null;
		output = new NeuralNetworkMutating(this.inputnodes, this.hiddennodes, this.outputnodes, this.learningrate);
		output.who = this.who.copy();
		output.wih = this.wih.copy();

		return output;
	}

	// TODO: adding mutation to make the NeuralNetwork generic
	/**
	 * Durch eine Mutation des Neuronalen Netzes werden ein Anteil (mutationrate)
	 * der Gewichte im Netz zuf&aumlllig neu festgelegt.
	 * 
	 * Durch diese Funktion wird das aufrufende Neuronale Netz Objekt bearbeitet,
	 * das heisst, dass der R&uumlckgabewert dieser Funktion nicht zwangsl&aumlufig
	 * benutzt werden muss.
	 * 
	 * Diese Funktion soll eigentlich in eine weitere, spezifischere Klasse
	 * untergebracht werden. (GenericNeuralNetwork)
	 * 
	 * @param mutationrate Der Anteil der Gewichte, die neu festgelegt werden.
	 * @return Gibt das mutierte Neuronale Netz zur&uumlck.
	 */

	public NeuralNetworkMutating mutate(double mutationrate) {
		double weights[][] = wih.toArray();
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				if (Math.random() < mutationrate)
					weights[i][j] = random(1);
			}
		}
		wih = new Matrix(weights);

		weights = who.toArray();
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[0].length; j++) {
				if (Math.random() < mutationrate)
					weights[i][j] = random(1);
			}
		}
		who = new Matrix(weights);
		return this;
	}

	// TODO: crossover
	/**
	 * Durch einen Crossover zweier Neuraler Netze wird, wie bei in echter DNA, eine
	 * zuf&aumlllige Mischung dieser beiden Netze erstellt.
	 * 
	 * @deprecated Diese Funktion soll eigentlich in eine weitere, spezifischere
	 *             Klasse untergebracht werden. (GenericNeuralNetwork)
	 * @param other Das andere Neuronale Netz, welches mit dem Neuronalen Netz
	 *              gemischt werden soll.
	 * @param rate  Die Mutationsrate.
	 * @return
	 */
	@Deprecated
	public NeuralNetworkMutating crossover(NeuralNetworkMutating other, double rate) {
		return null;
	}

	// TODO: subclass: GenericNeuralNetwork with crossover and mutation

}