package nxt;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

/**
 * Sends instructions to the NXT. Instructions are sent as 2 ints from -511 to
 * 511, and can be received and executed by an NXT running InstRecive
 * 
 * @author Johan LG
 * 
 */

public class InstructionsSender {

	private DataOutputStream dos;

	public InstructionsSender(String deviceURL) {
		NXTConnector connector = new NXTConnector();
		System.out.println("Looking for NXT");

		// Looking for an NXT
		while (!connector.connectTo(deviceURL)) {
		}
		System.out.println("NXT connected");

		// Creating a DataOutputStream to the NXT through which we can send
		// instructions
		dos = new DataOutputStream(connector.getOutputStream());
	}

	public void sendInstruction(int rotation, int speed) {
		if (rotation > 511)
			rotation = 511;
		else if (rotation < -511)
			rotation = -511;
		if (speed > 511)
			speed = 511;
		else if (speed < -511)
			speed = -511;
		try {
			dos.writeInt((rotation + 512) * 1024 + speed + 512);
			dos.flush();
			System.out.println("sendt " + rotation + " " + speed);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Send the close instruction to the NXT and closes the connection
	 */
	public void close() {
		try {
			dos.writeInt(2000000);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
