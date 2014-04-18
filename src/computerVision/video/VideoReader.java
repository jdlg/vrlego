package computerVision.video;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Size;
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
		// TODO ?
	}
	
	public Mat getImage() {
		return imageMat;
	}
	
	public Mat read(){
		capture.read(imageMat);
		return imageMat;
	};

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
