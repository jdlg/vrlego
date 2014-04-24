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
import computerVision.colorTracking.HSVRange;
import computerVision.colorTracking.PointFinder;
import computerVision.colorTracking.PointPoseTracker;
import computerVision.perspective.Calibration;
import computerVision.perspective.PerspectiveCalibration;
import computerVision.perspective.HomographyTransorm;
import computerVision.utils.MatConvert;
import computerVision.video.VideoReader;

public class SimpleAR extends SimpleApplication {

	// TODO rydd opp
	private Picture cameraPicture = new Picture("background");
	private HomographyTransorm homographyTransorm;
	private Mat image;
	private Camera cam2;
	private TankTracker tankTracker;
	private VideoReader videoReader;
	private ArrayList<Tank> tankList;
	private float scale = 1f;

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		SimpleAR app = new SimpleAR();
		app.setShowSettings(false);
		app.start();
	}

	@Override
	public void simpleInitApp() {

		// 3
		cameraPicture.setWidth(settings.getWidth());
		cameraPicture.setHeight(settings.getHeight());
		cameraPicture.setPosition(0, 0);

		viewPort.clearScenes();
		viewPort.attachScene(cameraPicture);
		viewPort.setClearFlags(false, true, true);

		cameraPicture.updateGeometricState();

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(Vector3f.UNIT_XYZ.negate());
		rootNode.addLight(dl);

		cam2 = cam.clone();
		ViewPort viewPort2 = renderManager.createPostView("Gui 2", cam2);
		viewPort2.setClearFlags(false, true, true);
		viewPort2.attachScene(rootNode);

		// setPosition();
		// 8
		image = new Mat();
		videoReader = new VideoReader(image, 0);

		TankFactory tankFactory = new TankFactory(assetManager);
		tankList = tankFactory.makeTankList(1);
		for (Tank tank : tankList) {
			rootNode.attachChild(tank);
		}

		chessCalib();

		setBackground();

		ActionListener actionListener = new ActionListener() {
			public void onAction(String name, boolean keyPressed, float tpf) {
				if (name.equals("chessCalib") && keyPressed) {
					chessCalib();
				}
			}
		};
		inputManager.addMapping("chessCalib",
				new KeyTrigger(KeyInput.KEY_SPACE));
		inputManager.addListener(actionListener, "chessCalib");

		rootNode.scale(scale);
	}

	private void chessCalib() {
		Mat rvec = new Mat(), tvec = new Mat();
		homographyTransorm = Calibration.chessboardCalibration(videoReader, 5,
				4, 4.1, 1, rvec, tvec);

		PointPoseTracker ppt = new PointPoseTracker(videoReader,
				homographyTransorm);
		tankTracker = new TankTracker(tankList.get(0), ppt);

		double pi = Math.PI, rx = rvec.get(0, 0)[0], ry = rvec.get(1, 0)[0], rz = rvec
				.get(2, 0)[0], tx = tvec.get(0, 0)[0], ty = tvec.get(1, 0)[0], tz = tvec
				.get(2, 0)[0];

		// System.out.println(ry);
		rz = 0;
		ry = 0;

		float[] angles = { (float) rx, (float) ry, (float) rz };
		Quaternion rotation = new Quaternion(angles);

		Transform tm = new Transform(new Vector3f((float) tx * 1, (float) ty,
				(float) tz), rotation);

		Vector3f camPose = new Vector3f(0, 0, 0);
		tm.transformInverseVector(camPose, camPose);

		float[] angles2 = { (float) (rx - pi * 4 / 8), (float) (ry - pi),
				(float) (rz) };

		cam2.setLocation(new Vector3f(camPose.getX(), camPose.getZ(), -camPose
				.getY()));
		rotation = new Quaternion(angles2);
		cam2.setRotation(rotation);
		// cam2.setFrustumPerspective(60, 640 / 480, 1, 1000);
		// cam2.lookAt(new Vector3f(0, 0, 0), new Vector3f(0, 1, 0));
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
		// videoReader.read(); TODO This is done in pointTracker; move it?
		Core.rectangle(image, new Point(640 / 2 - 5, 480 / 2 - 5), new Point(
				640 / 2 + 5, 480 / 2 + 5), new Scalar(0, 255, 0));
		BufferedImage bImage = MatConvert.matToBufferedImage(image);
		Image image = new AWTLoader().load(bImage, true);
		Texture bgTeaxture = new Texture2D(image);
		cameraPicture.setTexture(assetManager, (Texture2D) bgTeaxture, true);

	}

	private void setPosition() {
		// 5
		tankTracker.updatePositions();
	}

	@Override
	public void destroy() {
		videoReader.close();
		super.destroy();
	}
}
