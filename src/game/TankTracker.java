package game;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Point;

import computerVision.colorTracking.PointPoseTracker;

public class TankTracker {
	private PointPoseTracker ppt;
	private Tank tank;
//	private ArrayList<Tank> tankList;
	
	public void setTankPosition() {
		HashMap<String, ArrayList<Point>> pointPose;
		pointPose = ppt.findPoints();
	}
}
