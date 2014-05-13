package game;

import nxt.pilots.PilotManager;
import org.opencv.core.Core;

/**
 * Same as the SimpleAR application, with communication to an NXT robot that can
 * be controlled with the arrow keys
 * 
 * @author Johan LG
 * 
 */
public class SimpleARWithNXT extends SimpleAR {

	private PilotManager pilotManager;

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		SimpleARWithNXT app = new SimpleARWithNXT();
		app.setShowSettings(false);
		app.start();
	}

	@Override
	public void simpleInitApp() {
		super.simpleInitApp();
		pilotManager = new PilotManager(inputManager);
	}

	@Override
	public void destroy() {
		pilotManager.closeAll();
		super.destroy();
	}

}
