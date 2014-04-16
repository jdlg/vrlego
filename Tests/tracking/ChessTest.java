package tracking;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.MatOfPoint3f;
import org.opencv.core.Point;
import org.opencv.core.Point3;
import org.opencv.core.Size;
import org.opencv.highgui.VideoCapture;

import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

import computerVision.perspective.Calibration;
import computerVision.perspective.HomographyTransorm;

public class ChessTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Mat rvec = new Mat(), tvec = new Mat();
		VideoCapture capture = new VideoCapture(2);
		HomographyTransorm ht = Calibration.chessboardCalibration(capture, 5, 4, 3, 10, rvec, tvec);
		capture.release();
	}
}
