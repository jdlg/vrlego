package nxt;

import java.io.DataInputStream;
import java.io.IOException;

import lejos.nxt.LCD;
import lejos.nxt.Motor;
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
	
	private static void connect(){
		LCD.drawString("Connecting", 0, 0);
		LCD.refresh();

		//TODO Wait for USB and BT
		USBConnection conn = USB.waitForConnection();
		dis = conn.openDataInputStream();
		for (int i = 0; i < 100000; i++) {
			
		}
		connected = true;
		
		receiveInstructions();
	}

	private static void receiveInstructions() {
		LCD.clear();
		LCD.drawString("Waiting for instructions", 0, 0);
		LCD.refresh();
		while (connected) {
			//TODO if disconct: wait for new connection
			int inst = 0;
			try {
				inst = dis.readInt();
			} catch (IOException e) {
				e.printStackTrace();
			}
			if (inst == 100000){
				preformeInstructions(0, 0);
				connected = false;
			}else{
				int x = inst / 512 - 256;
				int y = inst % 512 - 256;
				preformeInstructions(x, y);
			}
		}
		System.exit(0);
	}

	private static void preformeInstructions(int x, int y) {
			LCD.clear();
			LCD.drawString("Recived " + x + " " + y, 0, 0);
			LCD.refresh();
			int v = y * 2;
			int r = x * 1;
			if (v>0) r *= -1;
			int aspeed = v + r;
			int bspeed = v - r;
			Motor.A.setSpeed(aspeed);
			Motor.B.setSpeed(bspeed);
			if (aspeed > 0) Motor.A.backward();
			else Motor.A.forward();
			if (bspeed > 0) Motor.B.backward();
			else Motor.B.forward();
	}
	
	
}
