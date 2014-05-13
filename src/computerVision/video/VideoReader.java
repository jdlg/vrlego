package computerVision.video;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;

/**
 * For reading from a webcam. This Class implements Runnable, and can be started
 * in a tread to continuously read from the device. 
 * 
 * @author Johan LG
 * 
 */
public class VideoReader implements Runnable {

	private VideoCapture capture;
	private Mat imageMat;

	/**
	 * 
	 * @param image
	 *            The image will be loaded into this Mat whenever a read is
	 *            performed.
	 * @param device
	 *            The device from which the reads will be performed.
	 */
	public VideoReader(Mat image, int device) {
		capture = new VideoCapture(device);
		this.imageMat = image;

		// Some cameras will read a dark image on the first few reads
		for (int i = 0; i < 4; i++) {
			capture.read(this.imageMat);
		}

	}

	public Mat getImage() {
		return imageMat;
	}

	/**
	 * Reads an image from the the device.
	 * 
	 * @return
	 */
	public Mat read() {
		capture.read(imageMat);
		return imageMat;
	};

	@Override
	public void run() {
		if (capture.isOpened()) {
			while (true) {
				try {
					Thread.sleep(1000 / 30);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				capture.read(imageMat);
			}
		}
	}

	public void close() {
		capture.release();
	}
}
