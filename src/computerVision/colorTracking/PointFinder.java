package computerVision.colorTracking;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.opencv.core.*;
import org.opencv.imgproc.*;

/**
 * Finds points within an HSVRange in an image (Mat)
 * 
 * @author Johan LG
 */

public class PointFinder {

	private final static int MAX_NUM_OBJECTS = 10;
	private final static double MIN_OBJECT_AREA = 20;

	public static ArrayList<Point> findPoints(Mat bgrMat, HSVRange range) {
		return findPoints(bgrMat, range, 0);
	}

	/**
	 * Looks for the expected amount of points within the HSVRange, and returns
	 * their coordinates as Points in an ArrayList. If more points are found
	 * then the expected amount, then only the largest points will be returned.
	 * If less points are found then the expected amount, then all point are
	 * returned.
	 * 
	 * 
	 * @param range
	 * @param expectedPoints
	 * @return points
	 */
	public static ArrayList<Point> findPoints(Mat bgrMat, HSVRange range,
			int expectedPoints) {
	
		Mat hsvMat = new Mat();
		Imgproc.cvtColor(bgrMat, hsvMat, Imgproc.COLOR_BGR2HSV_FULL);

		if (expectedPoints <= 0 || expectedPoints > MAX_NUM_OBJECTS)
			expectedPoints = MAX_NUM_OBJECTS;

		// Finding the threshold
		Mat binaryMat = Thresholding.filterColor(hsvMat, range);
		
		// Reduce noise
		// Imgproc.erode(grayMat, grayMat, new Mat(5, 5, 0));
		// Imgproc.dilate(grayMat, grayMat, new Mat(5, 5, 0));
		// Imgproc.blur(grayMat, grayMat, new Size(3, 3));
		
		// Find the contour of all white spots in the threshold
		ArrayList<MatOfPoint> contours = new ArrayList<>();
		Mat temp = new Mat();
		binaryMat.copyTo(temp);
		Mat hierarchy = new Mat();
		Imgproc.findContours(temp, contours, hierarchy, Imgproc.RETR_CCOMP,
				Imgproc.CHAIN_APPROX_SIMPLE);

		// Adding the center point of the largest ares an ArrayList
		ArrayList<Point> points = new ArrayList<>();
		double numObjects = contours.size();
		if (numObjects > 0) {
			double foundObjects = 0;
			HashMap<Double, Point> area2Points = new HashMap<>();
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
			Arrays.sort(areas);
			if (expectedPoints > foundObjects)
				expectedPoints = (int) foundObjects;
			for (int i = 0; i < expectedPoints; i++) {
				points.add(area2Points.get(areas[areas.length - 1 - i]));
			}
		}
		return points;
	}
}
