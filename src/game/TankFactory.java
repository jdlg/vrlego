package game;

import java.util.ArrayList;

import com.jme3.scene.Geometry;

public class TankFactory {

	private Geometry tankGeom;
	//TODO handle colors (every tank is tracked by to colors)

	public TankFactory(Geometry tankGeom) {
		this.tankGeom = tankGeom;
	}

	public ArrayList<Tank> makeTankList(int numberOfTanks) {
		ArrayList<Tank> tanks = new ArrayList<>(numberOfTanks);
		for (int i = 0; i < tanks.size(); i++) {
			tanks.add(makeTank());
		}
		return null;
	}
	
	public Tank makeTank() {
		return new Tank(tankGeom/*TODOcolors*/);
	}
}
