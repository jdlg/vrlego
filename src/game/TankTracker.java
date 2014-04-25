package game;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Point;

import computerVision.colorTracking.PointPoseTracker;
import computerVision.perspective.HomographyTransorm;

public class TankTracker {
	private PointPoseTracker ppt;
	private Tank tank;

	// private ArrayList<Tank> tankList;

	public TankTracker(Tank tank, PointPoseTracker ppt) {
		this.ppt = ppt;
		this.tank = tank;
	}

	public void updatePositions() {
		// TODO Expand to support multiple tanks
		HashMap<String, ArrayList<Point>> pointPose;
		pointPose = ppt.findPoints();

		if (pointPose.get(tank.getColor1()).size() > 0
				&& pointPose.get(tank.getColor2()).size() > 0) {
			Point bluePoint = pointPose.get(tank.getColor1()).get(0);
			Point redPoint = pointPose.get(tank.getColor2()).get(0);
			float angle = (float) computerVision.Geometry.calculateAngle(
					bluePoint, redPoint);
			Point midPint = new Point((bluePoint.x + redPoint.x) / 2,
					(bluePoint.y + redPoint.y) / 2);
			tank.setXZA(-(float) (midPint.x), (float) (midPint.y), (float) (angle + Math.PI/2));
			// System.out.println("tank pose: " + midPint.x + ", " + midPint.y);
		} else {
			tank.setXZA(0, 0, 0);
		}
	}
}
