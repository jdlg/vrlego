package game;

import net.java.games.input.*;
import net.java.games.input.Component.Identifier;

public class Joystick {
	private Controller stick;

	public Joystick() {
		stick = findJoystick();
	}

	public Controller findJoystick() {
		Controller[] controllers = ControllerEnvironment
				.getDefaultEnvironment().getControllers();

		for (int i = 0; i < controllers.length; i++) {
			Controller controller = controllers[i];

			if (controller.getType() == Controller.Type.STICK) {
				return controller;
			}
		}
		System.out.println("No joystick found");
		return null;
	}

	public float getX() {
		stick.poll();
		Identifier i = Component.Identifier.Axis.X;
		Component c = stick.getComponent(i);
		return c.getPollData();
	}

	public float getY() {
		Identifier i = Component.Identifier.Axis.Y;
		return stick.getComponent(i).getPollData();
	}
}
