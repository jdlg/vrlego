package computerVision.colorTracking;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.highgui.VideoCapture;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.video.VideoReader;

public class PointTracker {

	private HSVRangeSet rangeSet;
	private VideoReader videoReader;

	public PointTracker(VideoReader reader) {
		videoReader = reader;
		rangeSet = HSVRangeSerialization.unserialize4ColorSet();

	}

	/**
	 * Returns a HashMap with color (as String) as key pointing an ArrayList of
	 * points <br>
	 * <br>
	 * This method only returns one blue point and one red point (enough to
	 * track one Tank)
	 * 
	 * @return
	 */
	public HashMap<String, ArrayList<Point>> findPointMap() {
		return findPointMap(1, "blue", "red");
	}

	public HashMap<String, ArrayList<Point>> findPointMap(int numberOfPoints, String... colors) {
		HashMap<String, ArrayList<Point>> returnPointMap = new HashMap<>();
		for (int i = 0; i < colors.length; i++) {
			HSVRange range = rangeSet.get(colors[i]);
			ArrayList<Point> points = PointFinder.findPoints(
					videoReader.read(), range, numberOfPoints);
			returnPointMap.put(colors[i], points);
		}

		return returnPointMap;
	}

}
