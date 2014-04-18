package computerVision.colorCalibration;

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
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import computerVision.Global;
import computerVision.colorTracking.HSVRange;
import computerVision.gui.BGRMatPanel;
import computerVision.gui.GrayMatPanel;
import computerVision.video.VideoReader;

public class ColorCalibrationPanel{
	
	public ColorCalibrationPanel(final HSVRange range) {
		JFrame camFrame = new JFrame("Kalibrering");

		double[] min = { 0.0, 0.0, 0.0 };
		double[] max = { 255.0, 255.0, 255.0 };
		range.setMaxValues(min);
		range.setMaxValues(max);


		JFrame sliderFrame = new JFrame();
		JPanel sliderPanel = new JPanel(new GridBagLayout());
		GridBagConstraints gbc = new GridBagConstraints();

		sliderFrame.setContentPane(sliderPanel);
		sliderFrame.setVisible(true);
		sliderFrame.setSize(300, 420);
		// sliderFrame.setLayout(new GridLayout(7, 2));

		final JSlider[] minSliders = { new JSlider(0, 179, 0),
				new JSlider(0, 255, 0), new JSlider(0, 255, 0) };
		final JSlider[] maxSliders = { new JSlider(0, 179, 179),
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

		final JTextField fileField = new JTextField();
		gbc.gridy++;
		sliderPanel.add(fileField, gbc);

		JButton saveButton = new JButton("Save Range");
		gbc.gridy++;
		sliderPanel.add(saveButton, gbc);
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				HSVRangeSerialization.serialize(range, fileField.getText());
			}
		});

		JButton loadButton = new JButton("Load Range");
		gbc.gridy++;
		sliderPanel.add(loadButton, gbc);
		loadButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				HSVRange newRange = HSVRangeSerialization.unserialize(fileField
						.getText());
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
	}
}
