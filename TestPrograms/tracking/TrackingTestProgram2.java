package tracking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Range;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import computerVision.Global;
import computerVision.colorCalibration.ColorCalibrationPanel;
import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.colorCalibration.ManualColorCalibration;
import computerVision.gui.BGRMatPanel;
import computerVision.gui.GrayMatPanel;
import computerVision.tracking.HSVRange;
import computerVision.tracking.PointTracker;
import computerVision.video.VideoReader;

public class TrackingTestProgram2 {

	public static Lock lock = new ReentrantLock();
	public static Condition newFrame = lock.newCondition();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		Mat image = new Mat();
		VideoReader reader = new VideoReader(image, 0);
		new Thread(reader).start();
		final HSVRange red = HSVRangeSerialization.unserialize("red");
		final HSVRange blue = HSVRangeSerialization.unserialize("blue");

		/*
		 * final HSVRange green = HSVRangeSerialization.unserialize("green");
		 * final HSVRange yellow = HSVRangeSerialization.unserialize("yellow");
		 */
		@SuppressWarnings("serial")
		BGRMatPanel camPanel = new BGRMatPanel(image) {
			PointTracker pointTracker = new PointTracker(mat);;
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.black);
				ArrayList<Point> points = pointTracker.findPoints(blue, 99);
				System.out.println(points.size());
				for (Point p : points) {
					g.drawRect((int) p.x - 5, (int) p.y - 5, 10, 10);
					g.drawString("blue", (int) p.x + 12, (int) p.y + 12);

				}
				
//				Point p = new Point(100,100);
//				g.drawRect((int) p.x - 5, (int) p.y - 5, 10, 10);
//				g.drawString("test", (int) p.x + 12, (int) p.y + 12);

//				for (Point p : new PointTracker(mat, this)
//						.findePoints(green, 2)) {
//					g.drawRect((int) p.x - 5, (int) p.y - 5, 10, 10);
//					g.drawString("green", (int) p.x + 12, (int) p.y + 12);
//				}
//				for (Point p : pointTracker.findePoints(blue, 8)) {
//					g.drawRect((int) p.x - 5, (int) p.y - 5, 10, 10);
//					g.drawString("blue", (int) p.x + 12, (int) p.y + 12);
//				}
//
//				for (Point p : new PointTracker(mat, this).findePoints(yellow,
//						1)) {
//					g.drawRect((int) p.x - 5, (int) p.y - 5, 10, 10);
//					g.drawString("yellow", (int) p.x + 12, (int) p.y + 12);
//				}

			}
		};
		JFrame camFrame = new JFrame("Tracking test 2");
		camFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		camFrame.setContentPane(camPanel);
		camFrame.setVisible(true);
		camFrame.setSize(640, 480);

		Mat blurImage = new Mat();
		Imgproc.blur(image, blurImage, new Size(3.0, 3.0));
		BGRMatPanel blurPanel = new BGRMatPanel(blurImage);
		JFrame blurFrame = new JFrame("Blur");
		blurFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		blurFrame.setContentPane(blurPanel);
		blurFrame.setVisible(true);
		blurFrame.setSize(640, 480);

		Mat hsvImage = new Mat();
		Imgproc.cvtColor(blurImage, hsvImage, Imgproc.COLOR_BGR2HSV);
		BGRMatPanel hsvPanel = new BGRMatPanel(hsvImage);
		JFrame hsvFrame = new JFrame("HSV");
		hsvFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		hsvFrame.setContentPane(hsvPanel);
		hsvFrame.setVisible(true);
		hsvFrame.setSize(640, 480);

		// Mat binImage = new Mat();
		HSVRange range = new HSVRange();
		GrayMatPanel binPanel = new GrayMatPanel(hsvImage, range);
		JFrame binFrame = new JFrame("Blur");
		binFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		binFrame.setContentPane(binPanel);
		binFrame.setVisible(true);
		binFrame.setSize(640, 480);
		new ColorCalibrationPanel(range);
		
		// Mat calibMat = new Mat();
		// Imgproc.blur(image, calibMat, new Size(0, 0));

		// ManualColorCalibration blurBinFrame = new
		// ManualColorCalibration(blurImage);
		// ManualColorCalibration binFrame = new
		// ManualColorCalibration(blurImage);

		while (true) {
			try {
				Thread.sleep(1000 / Global.framerate);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			Imgproc.blur(image, blurImage, new Size(3.0, 3.0));
			Imgproc.cvtColor(blurImage, hsvImage, Imgproc.COLOR_BGR2HSV);
		}
	}
}
