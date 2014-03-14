package tracking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.perspective.PerspectiveChanger;
import computerVision.tracking.HSVRange;
import computerVision.tracking.PointTracker;
import computerVision.video.VideoReader;

public class HomographyTestProgram {

	public static void main(String[] args) {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// les fra kamera og start thread
		final Mat image = new Mat();
		VideoReader reader = new VideoReader(image, 0);
		new Thread(reader).start();

		// JFrame camFrame = new JFrame("Tracking test");
		// BGRMatPanel camPanel = new BGRMatPanel(image);
		// camFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// camFrame.setContentPane(camPanel);
		// camFrame.setVisible(true);
		// camFrame.setSize(640, 480);

		// sleep?
		// get quadrilateral (egen kalsse)?

		final HSVRange blue = HSVRangeSerialization.unserialize("blue");
		final HSVRange red = HSVRangeSerialization.unserialize("red");
		final HSVRange yellow = HSVRangeSerialization.unserialize("yellow");

		final PointTracker pointTracker = new PointTracker(image);

		// TODO temp høyest x verdi er blue1
		ArrayList<Point> bluePoints = pointTracker.findPoints(blue, 2);
		Point bluePoint1;
		Point bluePoint2;
		if (bluePoints.get(0).x > bluePoints.get(1).x) {
			bluePoint1 = bluePoints.get(0);
			bluePoint2 = bluePoints.get(1);
		} else {
			bluePoint1 = bluePoints.get(1);
			bluePoint2 = bluePoints.get(0);
		}
		//

		ArrayList<Point> corners = new ArrayList<>();
		corners.add(bluePoint1);
		corners.addAll(pointTracker.findPoints(red, 1));
		corners.add(bluePoint2);
		corners.addAll(pointTracker.findPoints(yellow, 1));

		// get perspective changer
		final PerspectiveChanger perspectiveChanger = new PerspectiveChanger(
				corners);

		// paintComponent:
		// finde points
		// paint points
		final JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				pointTracker.uppdateImage(image);
				int r = 20;
				int c = 250;

				ArrayList<Point> bluePoints = pointTracker.findPoints(blue,2);
				ArrayList<Point> redPoints = pointTracker.findPoints(red,1);
				ArrayList<Point> yellowPoints = pointTracker.findPoints(yellow,1);

				if (bluePoints.size() > 0) {
					for (Point p : perspectiveChanger
							.applyTransform(bluePoints)) {
						g.setColor(Color.blue);
						g.fillOval((int) p.x - r + c, (int) p.y - r + c, 2 * r,
								2 * r);
					}
				}

				if (redPoints.size() > 0) {
					for (Point p : perspectiveChanger.applyTransform(redPoints)) {
						g.setColor(Color.red);
						g.fillOval((int) p.x - r + c, (int) p.y - r + c, 2 * r,
								2 * r);
					}
				}

				if (yellowPoints.size() > 0) {
					for (Point p : perspectiveChanger
							.applyTransform(yellowPoints)) {
						g.setColor(Color.yellow);
						g.fillOval((int) p.x - r + c, (int) p.y - r + c, 2 * r,
								2 * r);
					}
				}
			}
		};

		JFrame frame = new JFrame("Homography");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setContentPane(panel);
		frame.setVisible(true);
		frame.setSize(600, 600);

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					panel.repaint();
				}
			}
		}).start();
	}
}
