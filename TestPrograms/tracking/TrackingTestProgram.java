package tracking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.gui.BGRMatPanel;
import computerVision.tracking.HSVRange;
import computerVision.tracking.PointTracker;
import computerVision.video.VideoReader;

public class TrackingTestProgram {

	public static Lock lock = new ReentrantLock();
	public static Condition newFrame = lock.newCondition();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		JFrame camFrame = new JFrame("Tracking test");
		Mat image = new Mat();
		VideoReader reader = new VideoReader(image, 0);
		new Thread(reader).start();
//		final HSVRange green = HSVRangeSerialization.unserialize("green");
		final HSVRange blue = HSVRangeSerialization.unserialize("blue");
		final HSVRange red = HSVRangeSerialization.unserialize("red");
		final HSVRange yellow = HSVRangeSerialization.unserialize("yellow");
		final PointTracker pointTracker = new PointTracker(image);

		@SuppressWarnings("serial")
		BGRMatPanel camPanel = new BGRMatPanel(image) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				pointTracker.uppdateImage(mat);
				g.setColor(Color.black);
//				for (Point p : new PointTracker(mat, this)
//						.findePoints(green, 2)) {
//					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
//					g.drawString("green", (int) p.x + 12, (int) p.y + 12);
//				}
				for (Point p : pointTracker.findPoints(blue, 2)) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("blue", (int) p.x + 12, (int) p.y + 5);
				}
				for (Point p : pointTracker.findPoints(red, 1)) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("red", (int) p.x + 12, (int) p.y + 12);

				}
				for (Point p : pointTracker.findPoints(yellow, 1)) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("yellow", (int) p.x + 12, (int) p.y + 12);
				}
			}
		};
		camFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		camFrame.setContentPane(camPanel);
		camFrame.setVisible(true);
		camFrame.setSize(640, 480);
	}
}
