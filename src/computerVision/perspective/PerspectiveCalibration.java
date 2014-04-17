package computerVision.perspective;

import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.tracking.HSVRange;
import computerVision.tracking.PointFinder;

public class PerspectiveCalibration {

	public final static int BLUE_POINTS = 0, SET_POINTS = 1;

	/**
	 * Finds the PerspectiveChanger from an image
	 * 
	 * @param image
	 * @param method
	 *            The method fore finding the PerspectiveChanger. <br>
	 *            Currently supported methods:
	 *            <ul>
	 *            <li>BLUE_POINTS: Looks for four blue points making a square</li>
	 *            </ul>
	 * @return
	 */
	public static HomographyTransorm getPerspectiveChanger(Mat image, int method) {
		HomographyTransorm perspectiveChanger = null;
		ArrayList<Point> points = null;
		switch (method) {
		case BLUE_POINTS:

			HSVRange blue = HSVRangeSerialization.unserialize("blue");
			final PointFinder pointTracker = new PointFinder(image);
			ArrayList<Point> bluePoints = pointTracker.findPoints(blue, 4);

			// TODO if fail
			// TODO calibration over time

			double xsum = 0,
			ysum = 0;
			for (int i = 0; i < 4; i++) {
				xsum += bluePoints.get(i).x;
				ysum += bluePoints.get(i).y;
			}
			Point center = new Point(xsum / 4, ysum / 4);

			ArrayList<Point> topPoints = new ArrayList<>();
			ArrayList<Point> botPoints = new ArrayList<>();
			for (Point p : bluePoints) {
				if (p.y < center.y)
					topPoints.add(p);
				else {
					botPoints.add(p);
				}
			}
			Point tl = topPoints.get(0).x > topPoints.get(1).x ? topPoints
					.get(0) : topPoints.get(1);
			Point tr = topPoints.get(0).x > topPoints.get(1).x ? topPoints
					.get(1) : topPoints.get(0);
			Point bl = botPoints.get(0).x > botPoints.get(1).x ? botPoints
					.get(1) : botPoints.get(1);
			Point br = botPoints.get(0).x > botPoints.get(1).x ? botPoints
					.get(0) : botPoints.get(0);

			points = new ArrayList<>();
			points.add(tl);
			points.add(tr);
			points.add(bl);
			points.add(br);

			perspectiveChanger = new HomographyTransorm(points);

			break;
		case SET_POINTS:
			points = new ArrayList<>();

			// points.add(new Point(478, 238));
			// points.add(new Point(389, 243));
			// points.add(new Point(371, 182));
			// points.add(new Point(455, 174));

			points.add(new Point(359,214));
			points.add(new Point(270,218));
			points.add(new Point(276,254));
			points.add(new Point(366,246));

//			for (Point p : points) {
//				System.out.println((int) p.x + " " + (int) p.y);
//			}
			perspectiveChanger = new HomographyTransorm(points);
			break;
		}
		return perspectiveChanger;
	}

}
