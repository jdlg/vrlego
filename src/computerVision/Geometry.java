package computerVision;

import org.opencv.core.Point;

public class Geometry {

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
