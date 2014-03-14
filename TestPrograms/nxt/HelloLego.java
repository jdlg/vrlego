package nxt;

import lejos.nxt.Button;

public class HelloLego {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Hello");
		Button.waitForAnyPress();
		System.out.println("lego!");
		Button.waitForAnyPress();
	}

}
