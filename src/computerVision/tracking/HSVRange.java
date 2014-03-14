package computerVision.tracking;

import java.io.Serializable;

import org.opencv.core.Scalar;

/**
 * Holds upper and lower values of Hue, Saturation and Value
 * 
 * @author Johan LG
 * 
 */
public class HSVRange implements Serializable {
	private double[] minValues;
	private double[] maxValues;

	public HSVRange(double[] min, double[] max) {
		minValues = min;
		maxValues = max;
	}

	public HSVRange() {
		double[] min = { 0.0, 0.0, 0.0 };
		double[] max = { 255.0, 255.0, 255.0 };
		minValues = min;
		maxValues = max;
	}

	/**
	 * Returns the minimum values as a scalar
	 * 
	 * @return minScalar
	 */
	public Scalar getMinScalar() {
		return new Scalar(minValues);
	}

	/**
	 * Returns the maximum values as a scalar
	 * 
	 * @return maxScalar
	 */
	public Scalar getMaxScalar() {
		return new Scalar(maxValues);
	}

	public void setHMin(double arg) {
		minValues[0] = arg;
	}

	public void setSMin(double arg) {
		minValues[1] = arg;
	}

	public void setVMin(double arg) {
		minValues[2] = arg;
	}

	public void setHMax(double arg) {
		maxValues[0] = arg;
	}

	public void setSMax(double arg) {
		maxValues[1] = arg;
	}

	public void setVMax(double arg) {
		maxValues[2] = arg;
	}

	public void setMaxValues(double[] maxValues) {
		this.maxValues = maxValues;
	}

	public void setMinValues(double[] minValues) {
		this.minValues = minValues;
	}

	public double[] getMaxValues() {
		return maxValues;
	}

	public double[] getMinValues() {
		return minValues;
	}

	public String string() {
		String s = "";
		for (int i = 0; i < 3; i++) {
			s += "[" + minValues[i] + " " + maxValues[i] + "]\n";
		}
		s += "\n";
		return s;
	}

}
