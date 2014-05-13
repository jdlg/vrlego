package computerVision.colorTracking;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.opencv.core.Point;

import computerVision.perspective.HomographyTransorm;
import computerVision.video.VideoReader;

/**
 * Has the same functionality as the PointTracker class except it applies a
 * homography transformation to all found points
 * 
 * @author Johan LG
 * 
 */
public class PointPoseTracker extends PointTracker {

	private HomographyTransorm ht;

	public PointPoseTracker(VideoReader reader, HomographyTransorm ht) {
		super(reader);
		this.ht = ht;
	}

	@Override
	public HashMap<String, ArrayList<Point>> findPointMap() {
		// return applyHT(super.findPointMap());
		return super.findPointMap();
	}

	@Override
	public HashMap<String, ArrayList<Point>> findPointMap(int numberOfPoints,
			String... colors) {
		return applyHT(super.findPointMap(numberOfPoints, colors));
	}

	private HashMap<String, ArrayList<Point>> applyHT(
			HashMap<String, ArrayList<Point>> pointMap) {
		HashMap<String, ArrayList<Point>> returnPointMap = new HashMap<>();
		Set<String> colors = pointMap.keySet();
		for (String color : colors) {
			ArrayList<Point> points = pointMap.get(color);
			returnPointMap.put(color, ht.applyTransform(points));
		}
		return returnPointMap;
	}
}
