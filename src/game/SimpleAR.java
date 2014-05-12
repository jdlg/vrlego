package game;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;

import com.jme3.app.SimpleApplication;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Transform;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.renderer.ViewPort;
import com.jme3.texture.Image;
import com.jme3.texture.Texture2D;
import com.jme3.texture.plugins.AWTLoader;
import com.jme3.ui.Picture;

import computerVision.colorTracking.PointPoseTracker;
import computerVision.perspective.Calibration;
import computerVision.perspective.HomographyTransorm;
import computerVision.utils.MatConvert;
import computerVision.video.VideoReader;

public class SimpleAR extends SimpleApplication {

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

		cameraPicture.setWidth(settings.getWidth());
		cameraPicture.setHeight(settings.getHeight());
		cameraPicture.setPosition(0, 0);

		viewPort.clearScenes();
		viewPort.attachScene(cameraPicture);

		cameraPicture.updateGeometricState();

		DirectionalLight dl = new DirectionalLight();
		dl.setColor(ColorRGBA.White);
		dl.setDirection(Vector3f.UNIT_XYZ.negate());
		rootNode.addLight(dl);

		cam2 = cam.clone();
		ViewPort viewPort2 = renderManager.createPostView("Gui 2", cam2);
		viewPort2.setClearFlags(false, true, true);
		viewPort2.attachScene(rootNode);
		cam2.setFrustumPerspective(32, (float) (640.0 / 480.0), 1, 1000);

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

		// Finding the extrinsic parameters
		Mat rvec = new Mat(), tvec = new Mat();
		homographyTransorm = Calibration.chessboardCalibration(videoReader, 5,
				4, 4.1, 10, rvec, tvec);

		// Creating a PointPoseTracker which will be used to track the tank
		PointPoseTracker ppt = new PointPoseTracker(videoReader,
				homographyTransorm);
		tankTracker = new TankTracker(tankList.get(0), ppt);

		// Setting the extrinsic parameters into variables. rz and ry must be
		// approximately 0 for the virtual camera to be positioned correctly
		double rx = rvec.get(0, 0)[0], ry = rvec.get(1, 0)[0], rz = rvec.get(2,
				0)[0], tx = tvec.get(0, 0)[0], ty = tvec.get(1, 0)[0], tz = tvec
				.get(2, 0)[0];

//		ry = 0;
//		rz = 0;

		// Finding the translation between the chessboard and the camera, and
		// use it to find the position of the camera
		float[] angles = { (float) rx, (float) ry, (float) rz };
		Quaternion rotation = new Quaternion(angles);
		Transform tm = new Transform(new Vector3f((float) tx, (float) ty,
				(float) tz), rotation);
		Vector3f camPose = new Vector3f(0, 0, 0);
		tm.transformInverseVector(camPose, camPose);
		cam2.setLocation(new Vector3f(camPose.getX(), camPose.getZ(), -camPose
				.getY()));

		// Applying the rotation to the camera
		float[] angles2 = { (float) (rx - Math.PI * 4 / 8),
				(float) (ry - Math.PI), (float) (rz) };
		rotation = new Quaternion(angles2);
		cam2.setRotation(rotation);

		System.out.printf("Camera Pose: %.1f  %.1f  %.1f\n", camPose.getX(),
				camPose.getZ(), -camPose.getY());
		System.out.printf("Camera Rotation: %.3f  %.3f  %.3f\n", rx - Math.PI * 4
				/ 8, ry - Math.PI, rz);
	}

	@Override
	public void simpleUpdate(float tpf) {
		tankTracker.updatePositions();
		setBackground();
	}

	private void setBackground() {
		// videoReader.read(); //TODO This is done in pointTracker; move it?

		// Core.rectangle(image, new Point(640 / 2 - 5, 480 / 2 - 5), new Point(
		// 640 / 2 + 5, 480 / 2 + 5), new Scalar(0, 255, 0));

		// Convert the image to BufferedImage
		BufferedImage bImage = MatConvert.matToBufferedImage(image);

		// Create a texture with the image and apply it to the background
		Image image = new AWTLoader().load(bImage, true);
		Texture2D bgTeaxture = new Texture2D(image);
		cameraPicture.setTexture(assetManager, (Texture2D) bgTeaxture, true);
	}

	@Override
	public void destroy() {
		videoReader.close();
		super.destroy();
	}
}
