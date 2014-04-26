package tracking;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JFrame;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.colorTracking.HSVRange;
import computerVision.colorTracking.PointFinder;
import computerVision.gui.BGRMatPanel;
import computerVision.perspective.HomographyTransorm;
import computerVision.video.VideoReader;

public class ChessTestProgram {

	public static Lock lock = new ReentrantLock();
	public static Condition newFrame = lock.newCondition();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		JFrame camFrame = new JFrame("Tracking test");
		final Mat image = new Mat();
		VideoReader reader = new VideoReader(image, 0);
		new Thread(reader).start();

		@SuppressWarnings("serial")
		BGRMatPanel camPanel = new BGRMatPanel(image) {
			@Override
			protected void paintComponent(Graphics g) {
				int w = 3, h = 4, r = 41 /* mm */;
				MatOfPoint2f chssPoints = new MatOfPoint2f();
				boolean found = Calib3d.findChessboardCorners(image, new Size(
						w, h), chssPoints);
				Calib3d.drawChessboardCorners(image, new Size(w, h),
						chssPoints, found);
				if (found) {
					ArrayList<Point3> pointList = new ArrayList<>();
					for (int i = 0; i < h; i++) {
						for (int j = 0; j < w; j++) {
							pointList.add(new Point3(i * r - r / 2 * (h - 1), j
									* r - r / 2 * (w - 1), 0));
						}
					}
					MatOfPoint3f points = new MatOfPoint3f();
					points.fromList(pointList);
					double W = 640, H = 480, fl = 480 * 2.16; // TODO fl?
					Mat cameraMatrix = new Mat(3, 3, CvType.CV_64F);
					cameraMatrix.put(0, 0, fl, 0, W / 2, 0, fl, H / 2, 0, 0, 1);
					Mat rvec = new Mat(), tvec = new Mat();

					Calib3d.solvePnP(points, chssPoints, cameraMatrix,
							new MatOfDouble(), rvec, tvec);
					
				}
				super.paintComponent(g);
			}
		};
		camFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		camFrame.setContentPane(camPanel);
		camFrame.setVisible(true);
		camFrame.setSize(640, 480);
	}
}
