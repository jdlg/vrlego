package tracking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import computerVision.colorTracking.PointTracker;
import computerVision.gui.BGRMatPanel;
import computerVision.video.VideoReader;

public class PointTrackingTestProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		final String[] colors = { "red", "blue", "yellow", "green" };
		JFrame camFrame = new JFrame("Point Tracking test");
		Mat image = new Mat();
		final VideoReader reader = new VideoReader(image, 0);
		final PointTracker pt = new PointTracker(reader);
		BGRMatPanel camPanel = new BGRMatPanel(image) {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.black);
				HashMap<String, ArrayList<Point>> points = pt.findPointMap(1,
						colors);
				for (int i = 0; i < colors.length; i++) {
					for (Point p : points.get(colors[i])) {
						g.drawOval((int) p.x - 5, (int) p.y - 5, 10, 10);
						g.drawString(colors[i], (int) p.x + 12, (int) p.y + 5);
					}
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
