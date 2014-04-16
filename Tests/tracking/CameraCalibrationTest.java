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
import org.opencv.imgproc.Imgproc;

import computerVision.perspective.HomographyTransorm;

public class CameraCalibrationTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		ArrayList<Point> imagePointList = new ArrayList<>();
		imagePointList.add(new Point(292, 298));
		imagePointList.add(new Point(82, 148));
		imagePointList.add(new Point(272, 25));
		imagePointList.add(new Point(476, 133));

		// imagePointList.add(new Point(247, 102));
		// imagePointList.add(new Point(351, 220));
		// imagePointList.add(new Point(186, 307));
		// imagePointList.add(new Point(95, 167));

		// imagePointList.add(new Point(0, 0));
		// imagePointList.add(new Point(101, 0));
		// imagePointList.add(new Point(102, 102));
		// imagePointList.add(new Point(0, 101));

		// imagePointList.add(new Point(325, 53));
		// imagePointList.add(new Point(398, 48));
		// imagePointList.add(new Point(410, 91));
		// imagePointList.add(new Point(337, 97));

		// PerspectiveChanger perspectiveChanger = new
		// PerspectiveChanger(corners);

		// ArrayList<Point> objectPointList = new ArrayList<>();
		// objectPointList.add(new Point(335, 261));
		// objectPointList.add(new Point(182, 145));
		// objectPointList.add(new Point(291, 148));
		// objectPointList.add(new Point(366, 120));
		// objectPointList.add(new Point(285, 197));
		ArrayList<Point3> objectPointList = new ArrayList<>();
		objectPointList.add(new Point3(0, 0, 0));
		objectPointList.add(new Point3(100, 0, 0));
		objectPointList.add(new Point3(100, 100, 0));
		objectPointList.add(new Point3(0, 100, 0));

		MatOfPoint2f imagePoints = new MatOfPoint2f();
		imagePoints.fromList(imagePointList);
		ArrayList<MatOfPoint2f> iList = new ArrayList<>();
		iList.add(imagePoints);

		MatOfPoint3f objectPoints = new MatOfPoint3f();
		objectPoints.fromList(objectPointList);
		ArrayList<MatOfPoint3f> oList = new ArrayList<>();
		oList.add(objectPoints);
//		System.out.println();
//		System.out.println(objectPoints.dump());
//System.out.println();
		Mat cameraMatrix = Calib3d.initCameraMatrix2D(oList, iList, new Size(
				617, 421));
		// Mat cameraMatrix = Calib3d.initCameraMatrix2D(oList, iList, new Size(
		// 462, 330));
		// Mat cameraMatrix = Calib3d.initCameraMatrix2D(oList, iList, new Size(
		// 1000,1000));
		// Mat cameraMatrix = Calib3d.initCameraMatrix2D(oList, iList, new Size(
		// 617, 438));

		System.out.println(cameraMatrix.dump());

		Mat rvec = new Mat(), tvec = new Mat();

		Calib3d.solvePnP(objectPoints, imagePoints, cameraMatrix,
				new MatOfDouble(), rvec, tvec);
		// System.out.println(rvec.dump());
		// System.out.println(-0.335 * 360 / 3.14);
		System.out.println(tvec.dump());
		Mat rotation = new Mat();
		Calib3d.Rodrigues(rvec, rotation);
		System.out.println(rotation.dump());

		Mat point = new Mat(1, 3, CvType.CV_64FC1);
		point.put(0, 0, 291, 148, 0);
		// System.out.println(point.dump());
		// Mat mat1 = new Mat(), mat2 = new Mat();

		// double[][] uvPoint = { { 291, 0, 0 }, { 0, 148, 0 }, { 0, 0, 1 } },
		// irot = matToArray(rotation
		// .inv()), icam = matToArray(cameraMatrix.inv());
		// double[][] tempMat = multiplicar(multiplicar(irot, icam), uvPoint);
		// double[][] tv = { { -5.167687694250257, 0, 0 },
		// { 0, 27.0913768702002, 0 }, { 0, 0, 411.8947347427826 } };
		// double[][] tempMat2 = multiplicar(irot, tv);
		//
		// double s = tempMat2[2][0];
		// s /= tempMat[2][0];
		// double[][] sm = { { s, 0, 0 }, { 0, s, 0 }, { 0, 0, s } };

		// double[][] result = multiplicar(irot, multiplicar(sm,
		// multiplicar(icam, uvPoint)));
		// double[][] result = multiplicar(sm, multiplicar(icam, uvPoint));
		// double[][] x = { { -5.167687694250257 - 291, 0.0, 5.469580102505426
		// },
		// { 0.0, -2.628239789515595 - 148, 3.729259160799155 },
		// { 0.0, 0.0, -22.880601023937444 - 1 } };
		// double[][] result = multiplicar(irot, x);

		// mat1 = rotation.inv().mul(cameraMatrix.inv());
		// mat2 = rotation.inv().mul(tvec);
		// Core.multiply(rotation.inv(), point, mat2);

		// Core.gemm(rotation.inv(), point, 1, new Mat(), 0, mat2, 0);
		// ArrayList<Point> resultPoints = perspectiveChanger
		// .applyTransform(points);

		// ArrayList<Point> expectedPoints = new ArrayList<>();
		// expectedPoints.add(new Point(0, 20));
		// expectedPoints.add(new Point(75, 25));
		// expectedPoints.add(new Point(45, 50));
		// expectedPoints.add(new Point(35, 80));
		// expectedPoints.add(new Point(30, 30));

		// for (int i = 0; i < resultPoints.size(); i++) {
		// Point resultPoint = resultPoints.get(i);
		// Point expectedPoint = expectedPoints.get(i);
		// assertEquals(resultPoint.x, expectedPoint.x, 1.0);
		// assertEquals(resultPoint.y, expectedPoint.y, 1.0);
		// }
		// double[][] result = tempMat;
		// matToArray(rotation);
		// for (int i = 0; i < result.length; i++) {
		// for (int j = 0; j < result[i].length; j++)
		// System.out.print(result[i][j] + " ");
		// System.out.println();
		// }

	}

	public static double[][] matToArray(Mat mat) {

		double[][] array = new double[mat.width()][mat.height()];

		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				array[i][j] = mat.get(i, j)[0];
			}
		}

		return array;
	}

	public static double[][] multiplicar(double[][] A, double[][] B) {

		int aRows = A.length;
		int aColumns = A[0].length;
		int bRows = B.length;
		int bColumns = B[0].length;

		if (aColumns != bRows) {
			throw new IllegalArgumentException("A:Rows: " + aColumns
					+ " did not match B:Columns " + bRows + ".");
		}

		double[][] C = new double[aRows][bColumns];
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				C[i][j] = 0.00000;
			}
		}

		for (int i = 0; i < aRows; i++) { // aRow
			for (int j = 0; j < bColumns; j++) { // bColumn
				for (int k = 0; k < aColumns; k++) { // aColumn
					C[i][j] += A[i][k] * B[k][j];
				}
			}
		}

		return C;
	}
}
