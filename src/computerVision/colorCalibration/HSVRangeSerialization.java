package computerVision.colorCalibration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import computerVision.colorTracking.HSVRange;
import computerVision.colorTracking.HSVRangeSet;

import java.lang.String;

import javax.swing.JOptionPane;

public class HSVRangeSerialization {

	private static String path = "HSVRanges/";

	public static void serialize(HSVRange range, String color) {
		try {
			FileOutputStream fos = new FileOutputStream(path + color + ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(range);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HSVRange unserialize(String color, Boolean askForCalibration) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(path + color + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			HSVRange range = (HSVRange) ois.readObject();
			ois.close();
			return range;
		} catch (FileNotFoundException e) {
			if (askForCalibration) {
				int option = javax.swing.JOptionPane
						.showConfirmDialog(
								null,
								color
										+ " color range not set.\nPerform color calibration?",
								"Range not Found", JOptionPane.YES_NO_OPTION);
				if (option == 0) {
					ManualColorCalibration.main(color);
					askForCalibration = false;
				}
			} else {
				javax.swing.JOptionPane.showConfirmDialog(null, color
						+ " color reange not found", "Range not Found",
						JOptionPane.PLAIN_MESSAGE);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		double[] min = { 0, 0, 0 };
		return new HSVRange(min, min);
	}

	public static HSVRangeSet unserializeSet() {
		HSVRangeSet set = new HSVRangeSet();
		File folder = new File(path);
		FilenameFilter nameFilter = new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.toLowerCase().endsWith(".ser");
			}
		};
		File[] files = folder.listFiles(nameFilter);

		for (int i = 0; i < files.length; i++) {
			String name = files[i].getName().substring(0,
					files[i].getName().length() - 4);
			set.put(name, unserialize(name, true));
		}

		return set;
	};

	public static HSVRangeSet unserialize4ColorSet() {
		String[] colors = { "red", "blue", "yellow", "green" };
		return unserializeSet(true, colors);
	}

	public static HSVRangeSet unserializeSet(boolean askForCalibration, String... colors) {
		HSVRangeSet set = new HSVRangeSet();
		boolean calibrate  = askForCalibration;
		for (int i = 0; i < colors.length; i++) {
			String name = colors[i];
			set.put(name, unserialize(name, calibrate));
		}

		return set;
	}

	public static String getFilePath(String color) {
		return path + color + ".ser";
	}

}
