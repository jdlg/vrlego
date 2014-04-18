package tracking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.colorTracking.HSVRange;
import computerVision.colorTracking.PointFinder;
import computerVision.colorTracking.PointTracker;
import computerVision.gui.BGRMatPanel;
import computerVision.video.VideoReader;

public class PointTrackingTestProgram {

	public static Lock lock = new ReentrantLock();
	public static Condition newFrame = lock.newCondition();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		JFrame camFrame = new JFrame("Point Tracking test");
		Mat image = new Mat();
		final VideoReader reader = new VideoReader(image, 0);
		final PointTracker pt = new PointTracker(reader);
		@SuppressWarnings("serial")
		BGRMatPanel camPanel = new BGRMatPanel(image) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.red);

				HashMap<String, ArrayList<Point>> points = pt.findPoints();
				for (Point p : points.get("blue")) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("new", (int) p.x + 12, (int) p.y + 5);
				}
				
				for (Point p : points.get("yellow")) {
					g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("new", (int) p.x + 12, (int) p.y + 5);
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
