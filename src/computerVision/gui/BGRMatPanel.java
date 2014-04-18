package computerVision.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.colorTracking.HSVRange;
import computerVision.colorTracking.PointFinder;
import computerVision.utils.MatConvert;

/**
 * A JPanel displaying a Mat as an BGR image.
 * @author Johan LG
 *
 */
public class BGRMatPanel extends JPanel {

	protected Mat mat;

	public BGRMatPanel(Mat mat) {
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
		image = MatConvert.matToBufferedImage(mat);
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
}
