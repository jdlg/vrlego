package tracking;

import static org.junit.Assert.*;

import java.io.File;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.colorTracking.HSVRange;

public class HSVRangeSerializationTest {

	@Before
	public void setUp() throws Exception {
		new File(HSVRangeSerialization.getFilePath("test")).delete();
	}

	@After
	public void tearDown() throws Exception {
		new File(HSVRangeSerialization.getFilePath("test")).delete();
	}

	@Test
	public void test() {
		double[] min = { 10.0, 20.0, 40.0 };
		double[] max = { 20.0, 40.0, 80.0 };
		HSVRange range = new HSVRange(min, max);
		HSVRangeSerialization.serialize(range, "test");
		HSVRange newRange = HSVRangeSerialization.unserialize("test",false);
		for (int i = 0; i < 3; i++) {
			assertTrue(range.getMinValues()[i] == newRange
					.getMinValues()[i]);
			assertTrue(range.getMaxValues()[i] == newRange
					.getMaxValues()[i]);

		}
	}

}
