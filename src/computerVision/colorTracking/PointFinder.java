package computerVision.colorTracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

import org.opencv.core.*;
import org.opencv.imgproc.*;

import computerVision.gui.BGRMatPanel;

/**
 * Finds points of within an HSVRange in an image (Mat)
 * 
 * @author Johan LG
 */

public class PointFinder {

	private Mat hsvMat;
	private final int MAX_NUM_OBJECTS = 50;
	private final double MIN_OBJECT_AREA = 20;// TODO

	public PointFinder(Mat bgrMat) {
		hsvMat = new Mat();
		Imgproc.cvtColor(bgrMat, hsvMat, Imgproc.COLOR_BGR2HSV_FULL);
	}

	public ArrayList<Point> findPoints(HSVRange range) {
		return findPoints(range, MAX_NUM_OBJECTS);
	}

	public void uppdateImage(Mat bgrMat) {
		hsvMat = new Mat();
		Imgproc.cvtColor(bgrMat, hsvMat, Imgproc.COLOR_BGR2HSV_FULL);
	}

	/**
	 * Looks for the expected amount of points within the HSVRange, and returns
	 * their coordinates as Points in an ArrayList. If more points are found
	 * then the expected amount, then only the largest points will be returned.
	 * If less points are found then the expected amount, then all point are
	 * returned.
	 * 
	 * @param range
	 * @param expectedPoints
	 * @return points
	 */
	public ArrayList<Point> findPoints(HSVRange range, int expectedPoints) {

		if (expectedPoints == 0)
			expectedPoints = MAX_NUM_OBJECTS;

		// Finding the threshold
		Mat grayMat = Thresholding.filterColor(hsvMat, range);
		// Core.inRange(hsvMat, range.getMinScalar(), range.getMaxScalar(),
		// grayMat);

		// Reduce noise
		// Imgproc.erode(grayMat, grayMat, new Mat(5, 5, 0));
		// Imgproc.dilate(grayMat, grayMat, new Mat(5, 5, 0));
		// Imgproc.blur(grayMat, grayMat, new Size(3, 3));

		ArrayList<MatOfPoint> contours = new ArrayList<>();
		// Will hold the an outline of white spots

		// Find the contour of all white spots in temp
		Mat temp = new Mat();
		grayMat.copyTo(temp);
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_CCOMP,
				Imgproc.CHAIN_APPROX_SIMPLE);

		double numObjects = contours.size();
		// Number of objects found

		ArrayList<Point> points = new ArrayList<>();
		// for holding the center of the objects that will be returned

		if (numObjects > 0) {
			// if (numObjects < MAX_NUM_OBJECTS) {

			double foundObjects = 0;
			HashMap<Double, Point> area2Points = new HashMap<>();
			// To select the largest objects

			double[] areas = new double[(int) numObjects];
			for (int i = 0; i < numObjects; i++) {
				Moments moment = Imgproc.moments(contours.get(i));
				double area = moment.get_m00();
				if (area > MIN_OBJECT_AREA) {
					area2Points.put(area, new Point(moment.get_m10() / area,
							moment.get_m01() / area));
					areas[i] = area;
					foundObjects++;
				}
			}

			// Add the center point of the largest areas to the points list
			Arrays.sort(areas);// TODO nødvendig?
			if (expectedPoints > foundObjects)
				expectedPoints = (int) foundObjects;
			for (int i = 0; i < expectedPoints; i++) {
				points.add(area2Points.get(areas[areas.length - 1 - i]));
			}
			// System.out.println(points.size());

			// } else
			// System.out.println("To mutch noise");
		}

		return points;
	}
}
