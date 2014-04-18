package game;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import nxt.InstructionsSender;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.VideoCapture;
import org.opencv.imgproc.Imgproc;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.scene.Geometry;
import com.jme3.texture.Image;
import com.jme3.texture.Texture;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.ui.Picture;

import computerVision.colorCalibration.HSVRangeSerialization;
import computerVision.perspective.Calibration;
import computerVision.perspective.PerspectiveCalibration;
import computerVision.perspective.HomographyTransorm;
import computerVision.tracking.HSVRange;
import computerVision.tracking.PointFinder;
import computerVision.utils.MatConvert;

public class newSimpleARWithNXT extends SimpleApplication {

	// TODO rydd opp
	private static VideoCapture capture;
	private Picture cameraPicture = new Picture("background");
	private Tank tanx;
	private static HomographyTransorm perspectiveChanger;
	private static PointFinder pointTracker;
	private static Mat image;
	private HSVRange blue, red, yellow, green;
	private static Mat rvec, tvec;
	Camera cam2;
	double ty, tx;
	double chessz;
	Tank center;
	static InstructionsSender sender;
	static TankTracker tankTracker;

	// private static double xa = 0;

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		// 2
		// sender = new InstructionsSender("usb://");

		// 4
		capture = new VideoCapture(0);
		image = new Mat();
		for (int i = 0; i < 4; i++) {
			capture.read(image);
		}
		// perspectiveChanger = PerspectiveCalibration.getPerspectiveChanger(
		// image, PerspectiveCalibration.SET_POINTS);
		// perspectiveChanger = PerspectiveCalibration.getPerspectiveChanger(
		// image, PerspectiveCalibration.BLUE_POINTS);

		rvec = new Mat();
		tvec = new Mat();

		newSimpleARWithNXT app = new newSimpleARWithNXT();
		app.setShowSettings(false);
		app.start();

	}

	@Override
	public void simpleInitApp() {
		// 3
		cameraPicture.setWidth(settings.getWidth());
		cameraPicture.setHeight(settings.getHeight());
		cameraPicture.setPosition(0, 0);

		setBackground();

		viewPort.clearScenes();
		viewPort.attachScene(cameraPicture);
		viewPort.setClearFlags(false, true, true);

		cameraPicture.updateGeometricState();

		// 6
		Geometry teaGeom = (Geometry) assetManager
				.loadModel("Models/Teapot/Teapot.obj");

		tanx = new Tank(teaGeom);
		tanx.scale(20f);
		rootNode.attachChild(tanx);

		Geometry centerGeom = (Geometry) assetManager
				.loadModel("Models/Teapot/Teapot.obj");
		center = new Tank(centerGeom);
		center.scale(3f);
		rootNode.attachChild(center);

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(Vector3f.UNIT_XYZ.negate());
		rootNode.addLight(dl);

		cam2 = cam.clone();
		ViewPort viewPort2 = renderManager.createPostView("Gui 2", cam2);
		viewPort2.setClearFlags(false, true, true);
		viewPort2.attachScene(rootNode);

		blue = HSVRangeSerialization.unserialize("blue");
		red = HSVRangeSerialization.unserialize("red");
		yellow = HSVRangeSerialization.unserialize("yellow");
		green = HSVRangeSerialization.unserialize("green");

		setPosition();
		// 8
		chessCalib();

		ActionListener actionListener = new ActionListener() {
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (name.equals("chessCalib") && keyPressed) {
					chessCalib();
				}
			}
		};

		inputManager.addMapping("chessCalib",
				new KeyTrigger(KeyInput.KEY_SPACE));

		// 13
		// inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
		// inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));
		// inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
		// inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));
		// inputManager.addListener(actionListener, "chessCalib");
		// inputManager.addListener(new ActionListener() {
		//
		// int value = 10, r = 0, u = 0, l = 0, d = 0;
		//
		// @Override
		// public void onAction(String name, boolean press, float arg2) {
		// int x = 0, y = 0;
		// if (press) {
		// if (name.equals("right"))
		// r = value;
		// if (name.equals("up"))
		// u = value;
		// if (name.equals("left"))
		// l = value;
		// if (name.equals("down"))
		// d = value;
		// } else {
		// if (name.equals("right"))
		// r = 0;
		// if (name.equals("up"))
		// u = 0;
		// if (name.equals("left"))
		// l = 0;
		// if (name.equals("down"))
		// d = 0;
		// }
		//
		// x = r - l;
		// y = d - u;
		// sender.sendInstruction(x, y);
		// }
		// }, "right", "up", "left", "down");

		// if (!keyPressed)
		// sender.sendInstruction(0, 0);

		// inputManager.addListener(new AnalogListener() {
		//
		// @Override
		// public void onAnalog(String name, float arg1, float arg2) {
		// int x = 0, y = 0;
		// if (name.equals("right"))
		// x += 10;
		// if (name.equals("up"))
		// y -= 10;
		// if (name.equals("left"))
		// x -= 10;
		// if (name.equals("down"))
		// y += 10;
		// sender.sendInstruction(x, y);
		// }
		// }, "right", "up", "left", "down");

	}

	private void chessCalib() {
		perspectiveChanger = Calibration.chessboardCalibration(capture, 5, 4,
				4.1, 10, rvec, tvec);

		double pi = Math.PI, rx = rvec.get(0, 0)[0], ry = rvec.get(1, 0)[0], rz = rvec
				.get(2, 0)[0], tx = tvec.get(0, 0)[0], ty = tvec.get(1, 0)[0], tz = tvec
				.get(2, 0)[0];

		float[] angles = { (float) rx, (float) ry, (float) rz };
		Quaternion rotation = new Quaternion(angles);

		Transform tm = new Transform(new Vector3f((float) tx, (float) ty,
				(float) tz), rotation);

		Vector3f camPose = new Vector3f(0, 0, 0);
		tm.transformInverseVector(camPose, camPose);
		// System.out.println(camPose);

		// float[] angles2 = { (float) (rx), (float) (ry),
		// (float) (rz - pi * 0) };
		float[] angles2 = { (float) (rx - pi * 4 / 8), (float) (ry - pi * 1),
				(float) (rz - pi * 0) };

		for (int i = 0; i < angles2.length; i++) {
			System.out.print(angles2[i] + " ");
		}
		System.out.println();

		cam2.setLocation(new Vector3f(camPose.getX(), camPose.getZ(), -camPose
				.getY()));
		rotation = new Quaternion(angles2);
		cam2.setRotation(rotation);
		// cam2.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));

		// Vector3f centerVec = new Vector3f(0, 0, 0);
		// tm.transformVector(centerVec, centerVec);
		// System.out.println(centerVec);
		// cam2.lookAt(centerVec, new Vector3f(0, 1, 0));
		// center.setXZA(centerVec.x, -centerVec.z, 0);

	}

	@Override
	public void simpleUpdate(float tpf) {
		// 10
		setPosition();
		setBackground();
	}

	/**
	 * Reads a frame from the webcam, and sets it to the background
	 */
	private void setBackground() {
		if (capture.isOpened()) {
			capture.read(image);
			Core.rectangle(image, new Point(640 / 2 - 5, 480 / 2 - 5),
					new Point(640 / 2 + 5, 480 / 2 + 5), new Scalar(0, 255, 0));
			BufferedImage bImage = MatConvert.matToBufferedImage(image);
			Image image = new AWTLoader().load(bImage, true);
			Texture bgTeaxture = new Texture2D(image);
			cameraPicture
					.setTexture(assetManager, (Texture2D) bgTeaxture, true);
		}
	}

	private void setPosition() {
		// 5 
		tankTracker.setTankPosition();
//		tanx.setXZA(-(float) (midPint.x - tx * 0) * 1,
//				(float) (midPint.y + chessz * 0), angle);
		// tanx.setXZA(0, 0, 0);

	}

}
