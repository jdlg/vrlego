package nxt;

import lejos.nxt.Button;
import lejos.nxt.LCD;
import lejos.nxt.Motor;

public class HelloWorld {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		LCD.drawString("Hello World!",0,0);
        Motor.A.forward();
//        try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        Button.waitForAnyPress();
        Motor.A.backward();
	}

}
