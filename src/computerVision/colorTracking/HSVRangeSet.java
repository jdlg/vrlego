package computerVision.colorTracking;

import java.io.Serializable;
import java.util.HashMap;

import javax.swing.JOptionPane;

import computerVision.colorCalibration.ManualColorCalibration;
import computerVision.colorCalibration.ManualColorCalibrationMain;

/**
 * A HashMar with String as keys and HSVRange as values
 * 
 * @author Johan LG
 * 
 */
public class HSVRangeSet extends HashMap<String, HSVRange> implements
		Serializable {
}
