package computerVision.video;

import org.opencv.core.Mat;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import computerVision.Global;

/**
 * Reads from a wabcam and continuously writes the data to a Mat
 * 
 * @author Johan LG
 * 
 */
public class VideoReader implements Runnable {

	private VideoCapture capture;
	private Mat imageMat;

	public VideoReader(Mat mat, int device) {
		capture = new VideoCapture(device);
		this.imageMat = mat;
		capture.read(this.imageMat);
		capture.read(this.imageMat);
		capture.read(this.imageMat);
		capture.read(this.imageMat);
		//TODO ?
	}

	@Override
	public void run() {
		if (capture.isOpened()) {
			while (true) {
				try {
					Thread.sleep(1000 / Global.framerate);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				capture.read(imageMat);
			}
		}
	}
}
