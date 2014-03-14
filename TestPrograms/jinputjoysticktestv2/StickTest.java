package jinputjoysticktestv2;

import game.Joystick;

public class StickTest {

	public static void main(String[] args) {
		Joystick stick = new Joystick();
		while (true) {
			System.out.println((int)(stick.getX()*31));
		}
	}
}
