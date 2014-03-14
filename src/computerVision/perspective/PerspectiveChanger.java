package computerVision.perspective;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

/**
 * Calculates a perspective homography from 4 points, and applies it to other
 * point
 * 
 * @author Johan LG
 * 
 */
public class PerspectiveChanger {
	private double width, height;
	private Mat h;

	/**
	 * 
	 * @param points
	 *            A list of 4 points corresponding to the corners of
	 *            quadrilateral
	 */
	public PerspectiveChanger(ArrayList<Point> points) {
		width = 100;
		height = 100;
		h = calculatePerspectiveTransform(points);
	}

	private Mat calculatePerspectiveTransform(ArrayList<Point> points) {
		ArrayList<Point> toPoints = new ArrayList<>();
		toPoints.add(new Point(0, 0));
		toPoints.add(new Point(width, 0));
		toPoints.add(new Point(width, height));
		toPoints.add(new Point(0, height));

		Mat fromMat = Converters.vector_Point_to_Mat(points);
		Mat toMat = Converters.vector_Point_to_Mat(toPoints);

		fromMat.convertTo(fromMat, CvType.CV_32FC2);
		toMat.convertTo(toMat, CvType.CV_32FC2);

		return Imgproc.getPerspectiveTransform(fromMat, toMat);
	}

	/**
	 * Finds the position of an ArrayList of Points in the homographyc plane
	 * 
	 * @param points
	 *            The points in perspective
	 * @return
	 */

	public ArrayList<Point> applyTransform(ArrayList<Point> points) {
		Mat pointMat = Converters.vector_Point_to_Mat(points);
		pointMat.convertTo(pointMat, CvType.CV_32FC2);

		Core.perspectiveTransform(pointMat, pointMat, h);

		ArrayList<Point> results = new ArrayList<Point>();
		Converters.Mat_to_vector_Point(pointMat, results);

		return results;
	}
}
