package computerVision.colorTracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.opencv.core.Mat;
import org.opencv.core.Point;

import computerVision.perspective.HomographyTransorm;
import computerVision.video.VideoReader;

public class PointPoseTracker extends PointTracker {

	private HomographyTransorm ht;

	public PointPoseTracker(VideoReader reader, HomographyTransorm ht) {
		super(reader);
		this.ht = ht;
	}

	@Override
	public HashMap<String, ArrayList<Point>> findPoints() {
		HashMap<String, ArrayList<Point>> pointMap = super.findPoints();
		HashMap<String, ArrayList<Point>> returnPointMap = new HashMap<>();
		Set<String> colors = pointMap.keySet();
		for (String color : colors) {
			ArrayList<Point> points = pointMap.get(color);
			returnPointMap.put(color, ht.applyTransform(points));
		}
		return returnPointMap;

	}
}
