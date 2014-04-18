package game;

import java.util.ArrayList;

import com.jme3.scene.Geometry;

public class TankFactory {

	private Geometry tankGeom;

	public TankFactory(Geometry tankGeom) {
		this.tankGeom = tankGeom;
	}

	public ArrayList<Tank> makeTankList(int numberOfTanks) {
		ArrayList<Tank> tanks = new ArrayList<>(numberOfTanks);
		for (int i = 0; i < tanks.size(); i++) {
			tanks.add(new Tank(tankGeom));
		}
		return null;
	}
}
