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
import computerVision.perspective.HomographyTransorm;
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
		ArrayList<Point> bluePoints = pointTracker.findPoints(blue, 4);
		// Point bluePoint1;
		// Point bluePoint2;
		// if (bluePoints.get(0).x > bluePoints.get(1).x) {
		// bluePoint1 = bluePoints.get(0);
		// bluePoint2 = bluePoints.get(1);
		// } else {
		// bluePoint1 = bluePoints.get(1);
		// bluePoint2 = bluePoints.get(0);
		// }
		//

		// ArrayList<Point> yellowPoints = pointTracker.findPoints(yellow, 2);
		// Point yellowPoint1;
		// Point yellowPoint2;
		// if (yellowPoints.get(0).x > bluePoints.get(1).x) {
		// yellowPoint1 = yellowPoints.get(0);
		// yellowPoint2 = yellowPoints.get(1);
		// } else {
		// yellowPoint1 = yellowPoints.get(1);
		// yellowPoint2 = yellowPoints.get(0);
		// }
		//
		// ArrayList<Point> corners = new ArrayList<>();
		// corners.add(bluePoint1);
		// corners.add(yellowPoint1);
		// corners.add(bluePoint2);
		// corners.add(yellowPoint2);

		// corners.add(bluePoint1);
		// corners.addAll(pointTracker.findPoints(red, 1));
		// corners.add(bluePoint2);
		// corners.addAll(pointTracker.findPoints(yellow, 1));

		// get perspective changer
		// final PerspectiveChanger perspectiveChanger = new PerspectiveChanger(
		// corners);

		// find center point
		double xsum = 0, ysum = 0;
		for (int i = 0; i < 4; i++) {
			xsum += bluePoints.get(i).x;
			ysum += bluePoints.get(i).y;
		}
		Point center = new Point(xsum / 4, ysum / 4);

		ArrayList<Point> topPoints = new ArrayList<>();
		ArrayList<Point> botPoints = new ArrayList<>();

		for (Point p : bluePoints) {
			if (p.y < center.y)
				topPoints.add(p);
			else {
				botPoints.add(p);
			}
		}

		Point tl = topPoints.get(0).x > topPoints.get(1).x ? topPoints.get(0)
				: topPoints.get(1);
		Point tr = topPoints.get(0).x > topPoints.get(1).x ? topPoints.get(1)
				: topPoints.get(0);
		Point bl = botPoints.get(0).x > botPoints.get(1).x ? botPoints.get(1)
				: botPoints.get(1);
		Point br = botPoints.get(0).x > botPoints.get(1).x ? botPoints.get(0)
				: botPoints.get(0);

		ArrayList<Point> points = new ArrayList<>();
		points.add(tl);
		points.add(tr);
		points.add(br);
		points.add(bl);

		final HomographyTransorm perspectiveChanger = new HomographyTransorm(
				points);

		// paintComponent:
		// finde points
		// paint points
		final JPanel panel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);

				// boolean b = true;
				// int i = b ? 1 : 2;
				// System.out.println(i);
				// b = false;
				// i = b ? 1 : 2;
				// System.out.println(i);

				pointTracker.uppdateImage(image);
				int r = 20;
				int c = 250;

				ArrayList<Point> bluePoints = pointTracker.findPoints(blue, 4);
				// ArrayList<Point> redPoints = pointTracker.findPoints(red,1);
				// ArrayList<Point> yellowPoints =
				// pointTracker.findPoints(yellow,2);

				if (bluePoints.size() > 0) {
					for (Point p : perspectiveChanger
							.applyTransform(bluePoints)) {
						g.setColor(Color.blue);
						g.fillOval((int) p.x - r + c, (int) p.y - r + c, 2 * r,
								2 * r);
					}
				}

				// if (redPoints.size() > 0) {
				// for (Point p : perspectiveChanger.applyTransform(redPoints))
				// {
				// g.setColor(Color.red);
				// g.fillOval((int) p.x - r + c, (int) p.y - r + c, 2 * r,
				// 2 * r);
				// }
				// }
				//
				// if (yellowPoints.size() > 0) {
				// for (Point p : perspectiveChanger
				// .applyTransform(yellowPoints)) {
				// g.setColor(Color.yellow);
				// g.fillOval((int) p.x - r + c, (int) p.y - r + c, 2 * r,
				// 2 * r);
				// }
				// }
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
