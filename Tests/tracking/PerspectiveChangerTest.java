package tracking;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Point;

import computerVision.perspective.HomographyTransorm;

public class PerspectiveChangerTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		ArrayList<Point> corners = new ArrayList<>();
		corners.add(new Point(292, 298));
		corners.add(new Point(82, 148));
		corners.add(new Point(272, 25));
		corners.add(new Point(476, 133));
		HomographyTransorm perspectiveChanger = new HomographyTransorm(corners);

		ArrayList<Point> points = new ArrayList<>();
		points.add(new Point(335, 261));
		points.add(new Point(182, 145));
		points.add(new Point(291, 148));
		points.add(new Point(366, 120));
		points.add(new Point(285, 197));

		ArrayList<Point> resultPoints = perspectiveChanger
				.applyTransform(points);

		ArrayList<Point> expectedPoints = new ArrayList<>();
		expectedPoints.add(new Point(0, 20));
		expectedPoints.add(new Point(75, 25));
		expectedPoints.add(new Point(45, 50));
		expectedPoints.add(new Point(35, 80));
		expectedPoints.add(new Point(30, 30));

		for (int i = 0; i < resultPoints.size(); i++) {
			Point resultPoint = resultPoints.get(i);
			Point expectedPoint = expectedPoints.get(i);
			assertEquals(resultPoint.x, expectedPoint.x, 1.0);
			assertEquals(resultPoint.y, expectedPoint.y, 1.0);
		}
	}

}
