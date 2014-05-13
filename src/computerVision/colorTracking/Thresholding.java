package computerVision.colorTracking;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 * For finding the threshold of an image by applying an HSVRange
 * 
 * @author Johan LG
 * 
 */
public class Thresholding {
	public static Mat filterColor(Mat hsvMat, HSVRange range) {
		Mat grayMat = new Mat();
		if (range.isSpilt()) {
			Mat grayMat1 = new Mat();
			Mat grayMat2 = new Mat();

			double[] min = range.getMinValues();
			double[] max = range.getMaxValues();

			// Finding two thresholds
			Core.inRange(hsvMat, new Scalar(0, min[1], min[2]),
					range.getMaxScalar(), grayMat1);
			Core.inRange(hsvMat, range.getMinScalar(), new Scalar(255, max[1],
					max[2]), grayMat2);

			// Adding the two thresholds together
			Core.bitwise_or(grayMat1, grayMat2, grayMat);

		} else
			Core.inRange(hsvMat, range.getMinScalar(), range.getMaxScalar(),
					grayMat);
		return grayMat;
	}
}
