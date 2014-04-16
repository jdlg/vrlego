package computerVision.perspective;

import java.util.ArrayList;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

public class Calibration {

	// TODO defaults
	/**
	 * Looks for a chessboard pattern to calculate the camera extrinsic values
	 * 
	 * @param capture
	 *            The video source
	 * @param cols
	 *            Columns in the chessboard pattern
	 * @param rows
	 *            Rows in the chessboard pattern
	 * @param squareSize
	 *            Size of squares in the chessboard pattern
	 * @param reads
	 *            Number of times the patters is looked for to find a more
	 *            accurate average position
	 * @param rvec
	 *            Output value of the rotation of the camera
	 * @param tvec
	 *            Output value of the translation of the camera
	 * @param pc
	 *            Output HomographyTransform
	 */
	public static HomographyTransorm chessboardCalibration(
			VideoCapture capture, int cols, int rows, double squareSize,
			int reads, Mat rvec, Mat tvec) {

		// TODO check parameters + update doc

		Mat image = new Mat();
		capture.read(image);
		capture.read(image);
		capture.read(image);
		capture.read(image);

		cols--;
		rows--;
		int points = cols * rows;

		// Find the chess board [reads] amount of times
		ArrayList<MatOfPoint2f> realPointsList = new ArrayList<>();
		System.out.println("Looking for chessboard patern");
		// System.out.println();
		for (int i = 0; i < reads;) {
			capture.read(image);
			MatOfPoint2f chessPoints = new MatOfPoint2f();
			boolean found = Calib3d.findChessboardCorners(image, new Size(rows,
					cols), chessPoints);
			if (found) {
				realPointsList.add(chessPoints);
				i++;
				// System.out.println("found " + (i));
			}
		}

		// Find the average location of the chessboard
		ArrayList<Point> avgPointsArray = new ArrayList<>();
		for (int i = 0; i < points; i++) {
			avgPointsArray.add(new Point(0, 0));
		}
		for (MatOfPoint2f matOfPoint : realPointsList) {
			for (int i = 0; i < points; i++) {
				Point newPoint = new Point(avgPointsArray.get(i).x
						+ matOfPoint.get(i, 0)[0] / reads,
						avgPointsArray.get(i).y + matOfPoint.get(i, 0)[1]
								/ reads);
				avgPointsArray.set(i, newPoint);
			}
		}
		MatOfPoint2f avgPoints = new MatOfPoint2f();
		avgPoints.fromList(avgPointsArray);

		// Set the value of a 2D (z=0) chessboard pattern for a comparison with
		// the values found with the VideoCapture
		// The center of the chess board is set to be the origin
		ArrayList<Point3> flatPointsArray = new ArrayList<>();
		double dx = (cols - 1) * squareSize / 2, dy = (rows - 1) * squareSize
				/ 2;
		for (int x = 0; x < cols; x++) {
			for (int y = 0; y < rows; y++) {
				Point3 p = new Point3(x * squareSize - dx, y * squareSize - dy,
						0);
				flatPointsArray.add(p);
			}
		}
		MatOfPoint3f flatPoints = new MatOfPoint3f();
		flatPoints.fromList(flatPointsArray);

		// Find rvec and tvec
		double W = image.width(), H = image.height(), fl = H * 1.57; // TODO fl?
		Mat cameraMatrix = new Mat(3, 3, CvType.CV_64F);
		cameraMatrix.put(0, 0, fl, 0, W / 2, 0, fl, H / 2, 0, 0, 1);
		Calib3d.solvePnP(flatPoints, avgPoints, cameraMatrix,
				new MatOfDouble(), rvec, tvec);

		// Find the HomographyTransform
		ArrayList<Point> corners = new ArrayList<>();
		corners.add(avgPointsArray.get(cols * rows - 1));// 1
		corners.add(avgPointsArray.get(rows - 1));// 2
		corners.add(avgPointsArray.get(0));// 3
		corners.add(avgPointsArray.get(cols * rows - rows));// 4

		HomographyTransorm ht = new HomographyTransorm(corners, (cols - 1)
				* squareSize, (rows - 1) * squareSize);

		return ht;
	}
}
