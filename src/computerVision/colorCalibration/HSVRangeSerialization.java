package computerVision.colorCalibration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import computerVision.tracking.HSVRange;

public class HSVRangeSerialization {
	
	private static String path = "HSVRanges/";
	
	public static void serialize(HSVRange range, String color) {
		try {
			FileOutputStream fos = new FileOutputStream(path + color
					+ ".ser");
			ObjectOutputStream oos = new ObjectOutputStream(fos);
			oos.writeObject(range);
			oos.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static HSVRange unserialize(String color) {
		FileInputStream fis;
		try {
			fis = new FileInputStream(path + color + ".ser");
			ObjectInputStream ois = new ObjectInputStream(fis);
			HSVRange range = (HSVRange) ois.readObject();
			ois.close();
			return range;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String getFilePath(String color) {
		return path + color + ".ser";
	}
	
	
}
