package game;

import nxt.InstructionsSender;

import com.jme3.math.Quaternion;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;

public class Tank extends Node {

	private float x = 0, z = 0, angle = 0;

	private String color1, color2;

	public Tank(Geometry geom) {
		this(geom, "blue", "yellow");
	}

	public Tank(Geometry geom, String color1, String color2) {
		this.color1 = color1;
		this.color2 = color2;
		attachChild(geom);
		scale(10f);
	}

	public void setXZA(float x, float z, float a) {
		this.x = x;
		this.z = z;
		angle = a;
		updateXZA();
	}

	private void updateXZA() {
//		setLocalTranslation(x, -12, z);
		setLocalTranslation(x, 0, z);
		Quaternion rotation = new Quaternion();
		rotation.fromAngles(0, angle, 0);
		setLocalRotation(rotation);
	}

	public String getColor1() {
		return color1;
	}

	public String getColor2() {
		return color2;
	}
}