package computerVision.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.tracking.HSVRange;
import computerVision.tracking.PointFinder;
import computerVision.utils.MatConvert;

/**
 * A JPanel displaying a Mat as an black and white image where white represents
 * colors that are within the HSVRange.
 * 
 * @author Johan LG
 * 
 */
public class GrayMatPanel extends JPanel {

	private Mat mat;
	private HSVRange range;

	public GrayMatPanel(Mat mat, HSVRange range) {
		this.range = range;
		this.mat = mat;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					repaint();
				}
			}
		}).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage image = null;
//		Imgproc.blur(mat, mat, new Size(9, 9));
//		Imgproc.erode(mat, mat, new Mat(3, 3, 0));
//		Imgproc.dilate(mat, mat, new Mat(3, 3, 0));
//		Imgproc.erode(mat, mat, new Mat(5, 5, 0));
//		Imgproc.dilate(mat, mat, new Mat(5, 5, 0));
//		Imgproc.blur(mat, mat, new Size(3, 3));
		image = MatConvert.matToBufferedImage(mat, range);
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
}
