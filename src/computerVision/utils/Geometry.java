package computerVision.utils;

import org.opencv.core.Point;

public class Geometry {

	/**
	 * Finds the angle between the line between two points an a horizontal line
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	public static double calculateAngle(Point p1, Point p2) {
		double dx = p2.x - p1.x;
		double dy = p2.y - p1.y;
		double h = Math.sqrt(dy * dy + dx * dx);
		double a = Math.asin(dy / h);
		if (p2.x < p1.x) {
			a *= -1;
			a += Math.PI;
		}
		return a;
	}
}
