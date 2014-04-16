package game;

import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class Tanx extends Node {

	private float x = 0, z = 0, angle = 0;
	private Geometry geom;

	public Tanx(Geometry geom) {
		this.geom = geom;
		attachChild(geom);
	}

	public void setXZA(float x, float z, float a) {
		this.x = x;
		this.z = z;
		angle = a;
		updateXZA();
	}
	
	//TODO find tank: To HSVranges som felt. finner to punkter som er nerme hverandre (HT som felt?)

	private void updateXZA() {
		setLocalTranslation(x, -12, z);
		Quaternion rotation = new Quaternion();
		rotation.fromAngles(0, angle, 0);
		setLocalRotation(rotation);
	}
}