package nxt;

import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
import lejos.nxt.comm.BTConnection;
import lejos.nxt.comm.Bluetooth;
import lejos.nxt.comm.USB;
import lejos.nxt.comm.USBConnection;

/**
 * Recives and executes instructions
 * 
 * @author Johan LG
 * 
 */
public class InstReceive {

	static DataInputStream dis;
	static boolean connected = false;

	public static void main(String[] args) {
		connect();
	}

	private static void connect() {
		LCD.drawString("Waiting for connection", 0, 0);
		LCD.refresh();
		// TODO Wait for USB and BT
		// Waiting for a USB connection
		USBConnection conn = USB.waitForConnection();
		// BTConnection conn = Bluetooth.waitForConnection(); /*(not tested)*/
		// Creating a DataInputStream to receive instructions
		dis = conn.openDataInputStream();
		connected = true;
		
		// Waiting for instructions
		receiveInstructions();
	}

	private static void receiveInstructions() {
		LCD.clear();
		LCD.drawString("Waiting for instructions", 0, 0);
		LCD.refresh();
		while (connected) {
			// TODO if disconnect: wait for new connection
			int inst = 0;
			try {
				inst = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (inst == 2000000) {
				preformeInstructions(0, 0);
				connected = false;
			} else {
				int x = inst / 1024 - 512;
				int y = inst % 1024 - 512;
				preformeInstructions(x, y);
			}
		}
		try {
			dis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	private static void preformeInstructions(int x, int y) {
		LCD.clear();
		LCD.drawString("Recived " + x + " " + y, 0, 0);
		LCD.refresh();
		int v = y;
		int r = x / 1;
		if (v > 0)
			r *= -1;
		int aspeed = v + r;
		int bspeed = v - r;
		Motor.A.setSpeed(aspeed);
		Motor.B.setSpeed(bspeed);
		if (aspeed > 0)
			Motor.A.backward();
		else
			Motor.A.forward();
		if (bspeed > 0)
			Motor.B.backward();
		else
			Motor.B.forward();
	}

}
