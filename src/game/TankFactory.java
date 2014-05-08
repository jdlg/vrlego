package game;

import java.util.ArrayList;

import com.jme3.asset.AssetManager;
import com.jme3.scene.Geometry;

public class TankFactory {

	private AssetManager assetManager;
	private String geomPath = "Models/Teapot/Teapot.obj";
	// TODO deffinere geom i parameter
	
	// TODO handle colors (every tank is tracked by to colors)

	public TankFactory(AssetManager assetManager) {
		this.assetManager = assetManager;
	}

	public ArrayList<Tank> makeTankList(int numberOfTanks) {
		ArrayList<Tank> tanks = new ArrayList<>(numberOfTanks);
		for (int i = 0; i < numberOfTanks; i++) {
			tanks.add(makeTank());
		}
		return tanks;
	}

	public Tank makeTank() {
		Geometry tankGeom = (Geometry) assetManager
				.loadModel(geomPath);
		return new Tank(tankGeom/* TODOcolors */);
	}
}
