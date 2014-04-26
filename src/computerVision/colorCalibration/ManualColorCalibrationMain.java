package computerVision.colorCalibration;

public class ManualColorCalibrationMain {

	public static void main(String... strings) {
		if (strings.length > 0)
			new ManualColorCalibration(strings[0]);
		else
			new ManualColorCalibration();
	}
}
