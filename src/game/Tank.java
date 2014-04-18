package game;

import nxt.InstructionsSender;

import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class Tank extends Node {

	private float x = 0, z = 0, angle = 0;
	private Geometry geom;

	public Tank(Geometry geom) {
		this.geom = geom;
		attachChild(geom);
	}

	public void setXZA(float x, float z, float a) {
		this.x = x;
		this.z = z;
		angle = a;
		updateXZA();
	}
	
	private void updateXZA() {
		setLocalTranslation(x, -12, z);
		Quaternion rotation = new Quaternion();
		rotation.fromAngles(0, angle, 0);
		setLocalRotation(rotation);
	}
}