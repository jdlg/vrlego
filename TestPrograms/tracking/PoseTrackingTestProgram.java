package tracking;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import computerVision.colorTracking.PointPoseTracker;
import computerVision.perspective.Calibration;
import computerVision.perspective.HomographyTransorm;
import computerVision.video.VideoReader;

public class PoseTrackingTestProgram {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		final int w = 400, h = 400;
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		final String[] colors = { "red", "blue", "yellow", "green" };
		JFrame camFrame = new JFrame("Pose Tracking test");
		Mat image = new Mat();
		final VideoReader reader = new VideoReader(image, 0);
		final int cols = 5, rows = 4, squareSize = 39;
		HomographyTransorm ht = Calibration.chessboardCalibration(reader, cols,
				rows, squareSize, 5, new Mat(), new Mat());
		final PointPoseTracker ppt = new PointPoseTracker(reader, ht);
		@SuppressWarnings("serial")
		final JPanel camPanel = new JPanel() {
			@Override
			protected void paintComponent(Graphics g) {
				super.paintComponent(g);
				g.setColor(Color.black);

				int r = 30;
				g.translate(w / 2, h / 2);

				int chessWidth = (int) (cols * squareSize), chessHeight = (int) (rows * squareSize);
				boolean blackSquare = false;
				for (int j = 0; j < rows; j++) {
					for (int i = 0; i < cols; i++) {
						Color color = blackSquare ? Color.black : Color.white;
						blackSquare = !blackSquare;
						g.setColor(color);
						g.fillRect(-chessWidth / 2 + i * squareSize,
								-chessHeight / 2 + j * squareSize, squareSize,
								squareSize);
					}
				}
				HashMap<String, ArrayList<Point>> points = ppt.findPointMap(1,
						colors);
				for (Point p : points.get("red")) {
					g.setColor(Color.red);
					g.fillOval((int) p.x - r, (int) p.y - r, 2 * r, 2 * r);
				}
				for (Point p : points.get("blue")) {
					g.setColor(Color.blue);
					g.fillOval((int) p.x - r, (int) p.y - r, 2 * r, 2 * r);
				}
				for (Point p : points.get("yellow")) {
					g.setColor(Color.yellow);
					g.fillOval((int) p.x - r, (int) p.y - r, 2 * r, 2 * r);
				}
				for (Point p : points.get("green")) {
					g.setColor(Color.green);
					g.fillOval((int) p.x - r, (int) p.y - r, 2 * r, 2 * r);
				}
			}
		};
		camFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		camFrame.setContentPane(camPanel);
		camFrame.setVisible(true);
		camPanel.setPreferredSize(new Dimension(400, 400));
		camFrame.pack();

		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					camPanel.repaint();
				}
			}
		}).start();
	}
}
