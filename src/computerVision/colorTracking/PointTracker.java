package computerVision.colorTracking;

import java.util.ArrayList;
import java.util.HashMap;

import org.opencv.core.Point;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.video.VideoReader;

/**
 * Used to track objects by there color. This class currently only loads the
 * HSVRanges for the four colors red, blue, yellow, and green.
 * 
 * @author Johan LG
 * 
 */
public class PointTracker {

	private HSVRangeSet rangeSet;
	private VideoReader videoReader;

	public PointTracker(VideoReader reader) {
		videoReader = reader;
		rangeSet = HSVRangeSerialization.unserialize4ColorSet();
	}

	/**
	 * Looks for center points of objects by filtering there colors This method
	 * only returns one blue point and one red point (enough to track one Tank)
	 * 
	 * @return Returns a HashMap with color (as String) as key pointing to an
	 *         ArrayList of points
	 */
	public HashMap<String, ArrayList<Point>> findPointMap() {
		return findPointMap(1, "blue", "red");
	}

	/**
	 * Looks for center points of objects by filtering there colors
	 * 
	 * @param numberOfPoints
	 *            The maximum amount of points that can be returned of every
	 *            color
	 * @param colors
	 * @return Returns a HashMap with color (as String) as key pointing to an
	 *         ArrayList of points
	 */
	public HashMap<String, ArrayList<Point>> findPointMap(int numberOfPoints,
			String... colors) {
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
