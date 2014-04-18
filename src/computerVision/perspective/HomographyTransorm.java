package computerVision.perspective;

import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

/**
 * Calculates a homography from 4 points, and applies it to other point
 * 
 * @author Johan LG
 * 
 */
public class HomographyTransorm {
	private double width, heigth;
	private Mat h;

	/**
	 * 
	 * @param points
	 *            A list of 4 points corresponding to the corners of
	 *            quadrilateral
	 */
	public HomographyTransorm(ArrayList<Point> points) {
		// TODO 4 POINTS?
		width = 100;
		heigth = 100;
		h = calculatePerspectiveTransform(points);
	}

	public HomographyTransorm(ArrayList<Point> points, double width,
			double heigth) {
		this.width = width;
		this.heigth = heigth;
		h = calculatePerspectiveTransform(points);
	}

	private Mat calculatePerspectiveTransform(ArrayList<Point> points) {
		ArrayList<Point> toPoints = new ArrayList<>();
		toPoints.add(new Point(-width / 2, -heigth / 2));
		toPoints.add(new Point(width / 2, -heigth / 2));
		toPoints.add(new Point(width / 2, heigth / 2));
		toPoints.add(new Point(-width / 2, heigth / 2));

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

		if (points.size() > 0) {
			Mat pointMat = Converters.vector_Point_to_Mat(points);
			pointMat.convertTo(pointMat, CvType.CV_32FC2);

			Core.perspectiveTransform(pointMat, pointMat, h);

			ArrayList<Point> results = new ArrayList<Point>();
			Converters.Mat_to_vector_Point(pointMat, results);

			return results;
		}
		return points;
	}

	public Point applyTransform(Point point) {
		ArrayList<Point> points = new ArrayList<>();
		points.add(point);

		Mat pointMat = Converters.vector_Point_to_Mat(points);
		pointMat.convertTo(pointMat, CvType.CV_32FC2);

		Core.perspectiveTransform(pointMat, pointMat, h);

		ArrayList<Point> results = new ArrayList<Point>();
		Converters.Mat_to_vector_Point(pointMat, results);

		return results.get(0);
	}
}
