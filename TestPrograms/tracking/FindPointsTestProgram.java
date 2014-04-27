package tracking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.colorTracking.HSVRange;
import computerVision.colorTracking.PointFinder;
import computerVision.gui.BGRMatPanel;
import computerVision.video.VideoReader;

/**
 * 
 * @author Johan LG
 * 
 */
public class FindPointsTestProgram {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		JFrame camFrame = new JFrame("Tracking test");
		Mat image = new Mat();
		VideoReader reader = new VideoReader(image, 0);
		new Thread(reader).start();
		boolean calibrate  = true;
		final HSVRange green = HSVRangeSerialization.unserialize("green", calibrate);
		final HSVRange blue = HSVRangeSerialization.unserialize("blue", calibrate);
		final HSVRange red = HSVRangeSerialization.unserialize("red", calibrate);
		final HSVRange yellow = HSVRangeSerialization.unserialize("yellow", calibrate);

		@SuppressWarnings("serial")
		BGRMatPanel camPanel = new BGRMatPanel(image) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.black);
				for (Point p : PointFinder.findPoints(mat, blue, 4)) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("blue", (int) p.x + 12, (int) p.y + 5);
				}
				for (Point p : PointFinder.findPoints(mat, yellow, 3)) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("yellow", (int) p.x + 12, (int) p.y + 12);
				}
				for (Point p : PointFinder.findPoints(mat, red, 2)) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("blue", (int) p.x + 12, (int) p.y + 5);
				}
				for (Point p : PointFinder.findPoints(mat, green, 1)) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("yellow", (int) p.x + 12, (int) p.y + 12);
				}

			}
		};
		camFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		camFrame.setContentPane(camPanel);
		camFrame.setVisible(true);
		camPanel.setPreferredSize(new Dimension(image.width(), image.height()));
		camFrame.pack();
	}
}
