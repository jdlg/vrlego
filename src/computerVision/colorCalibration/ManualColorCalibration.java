package computerVision.colorCalibration;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.opencv.core.Core;
import org.opencv.core.Mat;

import computerVision.colorTracking.HSVRange;
import computerVision.gui.BGRMatPanel;
import computerVision.gui.GrayMatPanel;
import computerVision.video.VideoReader;

public class ManualColorCalibration {
	private Mat image;

	public ManualColorCalibration() {
		this("");
	}

	/**
	 * @param args
	 */
	public ManualColorCalibration(String initialText) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		image = new Mat();
		VideoReader reader = new VideoReader(image, 0);
		new Thread(reader).start();

		double[] min = { 0.0, 0.0, 0.0 };
		double[] max = { 255.0, 255.0, 255.0 };
		final HSVRange range = new HSVRange(min, max);
		// Holds max and min HSV values for the color we want to track

		JFrame binaryFrame = new JFrame("Threshold");
		GrayMatPanel binaryPanel = new GrayMatPanel(image, range);
		binaryPanel.setPreferredSize(new Dimension(image.width(), image
				.height()));
		binaryFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		binaryFrame.setContentPane(binaryPanel);
		binaryFrame.setVisible(true);
		binaryFrame.setLocation(955, 0);
		binaryFrame.pack();

		JFrame sliderFrame = new JFrame();
		JPanel sliderPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		sliderFrame.setContentPane(sliderPanel);
		sliderFrame.setVisible(true);
		sliderFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		sliderPanel.setPreferredSize(new Dimension(300, 480));
		sliderFrame.setLocation(640, 0);

		final JSlider[] minSliders = { new JSlider(0, 255, 0),
				new JSlider(0, 255, 0), new JSlider(0, 255, 0) };
		final JSlider[] maxSliders = { new JSlider(0, 255, 255),
				new JSlider(0, 255, 255), new JSlider(0, 255, 255) };

		ChangeListener minListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				double[] values = new double[3];
				for (int i = 0; i < values.length; i++) {
					values[i] = (double) minSliders[i].getValue();
				}
				range.setMinValues(values);
			}
		};
		ChangeListener maxListener = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent arg0) {
				double[] values = new double[3];
				for (int i = 0; i < values.length; i++) {
					values[i] = (double) maxSliders[i].getValue();
				}
				range.setMaxValues(values);
			}
		};
		gbc.fill = GridBagConstraints.HORIZONTAL;
		for (int i = 0; i < 3; i++) {
			String letter = "";
			switch (i) {
			case 0:
				letter = "H";
				break;
			case 1:
				letter = "S";
				break;
			case 2:
				letter = "V";
				break;
			}
			gbc.gridy = i * 2;
			gbc.gridx = 0;
			sliderPanel.add(new Label(letter + "-MIN"), gbc);
			gbc.gridx = 1;
			sliderPanel.add(minSliders[i], gbc);
			gbc.gridy++;
			gbc.gridx = 0;
			sliderPanel.add(new Label(letter + "-MAX"), gbc);
			gbc.gridx = 1;
			sliderPanel.add(maxSliders[i], gbc);

			minSliders[i].addChangeListener(minListener);
			maxSliders[i].addChangeListener(maxListener);
		}

		final JTextField fileField = new JTextField(initialText);
		gbc.gridy++;
		sliderPanel.add(fileField, gbc);

		JButton saveButton = new JButton("Save Range");
		gbc.gridy++;
		sliderPanel.add(saveButton, gbc);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				HSVRangeSerialization.serialize(range, fileField.getText());
				// System.out.println(fileField.getText() + "\n" +
				// range.string());
			}
		});

		JButton loadButton = new JButton("Load Range");
		gbc.gridy++;
		sliderPanel.add(loadButton, gbc);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				HSVRange newRange = HSVRangeSerialization.unserialize(fileField
						.getText(), false);
				double[] minValues = newRange.getMinValues();
				double[] maxValues = newRange.getMaxValues();
				for (int i = 0; i < 3; i++) {
					minSliders[i].setValue((int) minValues[i]);
					maxSliders[i].setValue((int) maxValues[i]);
				}
			}
		});
		JButton resetButton = new JButton("Reset");
		gbc.gridy++;
		sliderPanel.add(resetButton, gbc);
		resetButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (int i = 0; i < 3; i++) {
					minSliders[i].setValue(0);
					maxSliders[i].setValue(255);
				}
			}
		});
		sliderFrame.pack();

	}

	public Mat getImage() {
		return image;
	}

	public static void main(String... strings) {
		ManualColorCalibration mcc = null;
		if (strings.length > 0)
			mcc = new ManualColorCalibration(strings[0]);
		else
			mcc = new ManualColorCalibration();

		JFrame rgbFrame = new JFrame("Camera feed");
		BGRMatPanel rgbPanel = new BGRMatPanel(mcc.getImage());
		rgbPanel.setPreferredSize(new Dimension(mcc.getImage().width(), mcc
				.getImage().height()));
		rgbFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		rgbFrame.setContentPane(rgbPanel);
		rgbFrame.setVisible(true);
		rgbFrame.setLocation(0, 0);
		rgbFrame.pack();
	}
}
