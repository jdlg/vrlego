package tracking;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.colorTracking.HSVRange;
import computerVision.colorTracking.HSVRangeSet;

public class HSVRangeSetTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		HSVRangeSet set = HSVRangeSerialization.unserializeSet();
		System.out.println(set.get("red").string());
		
//		HSVRange range = HSVRangeSerialization.unserialize("yellow");
	}

}
