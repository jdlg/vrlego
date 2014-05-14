package computerVision.gui;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import org.opencv.core.Mat;

import computerVision.colorTracking.HSVRange;
import computerVision.utils.MatConvert;

/**
 * A JPanel displaying a Mat as an black and white image where white represents
 * colors that are within the HSVRange.
 * 
 * @author Johan LG
 * 
 */
@SuppressWarnings("serial")
public class GrayMatPanel extends JPanel {

	private Mat mat;
	private HSVRange range;

	public GrayMatPanel(Mat mat, HSVRange range) {
		this.range = range;
		this.mat = mat;
		new Thread(new Runnable() {
			@Override
			public void run() {
				while (true) {
					repaint();
				}
			}
		}).start();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage image = null;
		image = MatConvert.matToBufferedImage(mat, range);
		g.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
	}
}
