package computerVision.utils;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import computerVision.colorTracking.HSVRange;
import computerVision.colorTracking.Thresholding;

public class MatConvert {

	public static BufferedImage matToBufferedImage(Mat mat) {

		byte[] sourcePixels = new byte[mat.width() * mat.height()
				* mat.channels()];

		mat.get(0, 0, sourcePixels);
		BufferedImage image = new BufferedImage(mat.width(), mat.height(),
				BufferedImage.TYPE_3BYTE_BGR);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, targetPixels.length);

		return image;
	}
	
	public static BufferedImage matToBufferedImage(Mat imageMat, HSVRange range) {
		Mat hsvMat = new Mat();
		Imgproc.cvtColor(imageMat, hsvMat, Imgproc.COLOR_BGR2HSV_FULL);
		Mat grayMat = Thresholding.filterColor(hsvMat, range);
		byte[] sourcePixels = new byte[imageMat.width() * imageMat.height()
				* imageMat.channels()];
		grayMat.get(0, 0, sourcePixels);
		BufferedImage image = new BufferedImage(imageMat.width(), imageMat.height(),
				BufferedImage.TYPE_BYTE_GRAY);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster()
				.getDataBuffer()).getData();
		System.arraycopy(sourcePixels, 0, targetPixels, 0, targetPixels.length);

		return image;
	}
}
