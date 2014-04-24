package computerVision.colorTracking;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

public class Thresholding {
	public static Mat filterColor(Mat HSVMat, HSVRange range) {
		Mat grayMat = new Mat();
		if (range.isSpilt()) {
			Mat grayMat1 = new Mat();
			Mat grayMat2 = new Mat();
			
			double[] min = range.getMinValues();
			double[] max = range.getMaxValues();
			
			Core.inRange(HSVMat, new Scalar(0,min[1],min[2]),
					range.getMaxScalar(), grayMat1);
			Core.inRange(HSVMat, range.getMinScalar(), new Scalar(255, max[1], max[2]),
					grayMat2);
			Core.bitwise_or(grayMat1, grayMat2, grayMat);
		} else
			Core.inRange(HSVMat, range.getMinScalar(), range.getMaxScalar(),
					grayMat);
		return grayMat;
	}
}
