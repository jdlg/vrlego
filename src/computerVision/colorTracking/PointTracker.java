package computerVision.colorTracking;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.VideoCapture;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.video.VideoReader;

public class PointTracker {

	private PointFinder pointFinder;
	private HSVRangeSet rangeSet;
	private VideoReader videoReader;

	public PointTracker(VideoReader reader) {
		videoReader = reader;
		pointFinder = new PointFinder(reader.getImage());
		rangeSet = HSVRangeSerialization.unserializeSet();

	}

	/**
	 * Returns a HashMap with color (as String) as key pointing an ArrayList of
	 * points <br>
	 * <br>
	 * Currently only returns one blue point and one yellow point (enough to
	 * track one Tank)
	 * 
	 * @return
	 */
	public HashMap<String, ArrayList<Point>> findPoints() {
		return findPoints("blue", "yellow");
	}

	public HashMap<String, ArrayList<Point>> findPoints(String... colors) {
		HashMap<String, ArrayList<Point>> returnPointMap = new HashMap<>();
		pointFinder.uppdateImage(videoReader.read());
		for (int i = 0; i < colors.length; i++) {
			HSVRange range = rangeSet.get(colors[i]);
			ArrayList<Point> points = pointFinder.findPoints(range, 1);
			// System.out.println(points.size());
			returnPointMap.put(colors[i], points);
		}

		return returnPointMap;
	}

}
