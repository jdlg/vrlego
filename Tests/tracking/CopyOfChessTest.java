package tracking;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import computerVision.perspective.Calibration;

public class CopyOfChessTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// Mat rvec = new Mat(), tvec = new Mat(),h = new Mat();
		// VideoCapture capture = new VideoCapture(0);
		// Calibration.chessboardCalibration(rvec, tvec, h, capture, 5, 4, 3,
		// 20);
		// capture.release();
		int reads = 20;
		VideoCapture capture;
		Mat image;
		capture = new VideoCapture(0);
		image = new Mat();
		capture.read(image);
		capture.read(image);
		capture.read(image);
		capture.read(image);

		int w = 3, h = 4, r = 30 /* mm */;

		// Calib3d.drawChessboardCorners(image, new Size(w, h), chssPoints,
		// found);

		ArrayList<MatOfPoint2f> realPointsList = new ArrayList<>();
		// MatOfPoint2f chssPoints0 = new MatOfPoint2f();
		for (int i = 0; i < reads;) {
			capture.read(image);
			MatOfPoint2f chessPoints = new MatOfPoint2f();

			boolean found = Calib3d.findChessboardCorners(image,
					new Size(w, h), chessPoints);
			if (found) {
				realPointsList.add(chessPoints);
				i++;
				System.out.println("found " + (i));
			}
		}

		ArrayList<Point3> flatPointsArray = new ArrayList<>();
		for (int i = 0; i < h; i++) {
			for (int j = 0; j < w; j++) {
				flatPointsArray.add(new Point3(i * r - r / 2 * (h - 1), j * r
						- r / 2 * (w - 1), 0));
			}
		}

		MatOfPoint3f flatPoints = new MatOfPoint3f();
		flatPoints.fromList(flatPointsArray);
		ArrayList<MatOfPoint3f> oList = new ArrayList<>();
		for (int i = 0; i < realPointsList.size(); i++) {
			oList.add(flatPoints);
		}

		// System.out.println(points.dump());
		// System.out.println();
		// for (MatOfPoint2f matOfPoint : iList) {
		// System.out.println(matOfPoint.dump());
		// }

		Mat cameraMatrix = new Mat(3, 3, CvType.CV_64F);
		// Calib3d.initCameraMatrix2D(oList, iList, new Size(640, 480));
		double W = 640, H = 480, fl = H * 1.5; // TODO fl?
		cameraMatrix.put(0, 0, fl, 0, W / 2, 0, fl, H / 2, 0, 0, 1);
		// System.out.println(cameraMatrix.dump());
		// System.out.println();

		// System.out.println(cameraMatrix.dump());
		Mat rvec = new Mat(), tvec = new Mat();
		ArrayList<Point> avgPoints = new ArrayList<>();

		for (int i = 0; i < 12; i++) {
			avgPoints.add(new Point(0, 0));
		}
		// System.out.println(avgPoints.dump());
		// System.out.println(avgPoints.get(1, 0)[0]);

		// avgPoints.
		// double avg = 0;

		for (MatOfPoint2f matOfPoint : realPointsList) {
			// System.out.println(matOfPoint.dump());
			// avg += matOfPoint.get(0, 0)[0] / 10;
			// System.out.println(avg);
			for (int i = 0; i < 12; i++) {
				Point newPoint = new Point(avgPoints.get(i).x
						+ matOfPoint.get(i, 0)[0] / reads, avgPoints.get(i).y
						+ matOfPoint.get(i, 0)[1] / reads);
				avgPoints.set(i, newPoint);
				// avgPoints.set(i,
				// new Point(
				// (avgPoints.get(i).x + matOfPoint.get(i, 0)[0])
				// / (i + 1),
				// (avgPoints.get(i).y + matOfPoint.get(i, 0)[1]
				// / (i + 1))));
			}
			// avgPoints.
		}

		MatOfPoint2f avgPointsMat = new MatOfPoint2f();
		avgPointsMat.fromList(avgPoints);
		System.out.println(avgPointsMat.dump());
		Calib3d.solvePnP(flatPoints, avgPointsMat, cameraMatrix,
				new MatOfDouble(), rvec, tvec);
		System.out.println(rvec.dump());
		System.out.println(tvec.dump());
		// System.out.println("[0.7417095366217255; -3.673535269746958; 613.3235400043328]");
		// System.out.println(613.1160874201993);

		System.out.println(flatPoints.dump());

		capture.release();

	}
}
