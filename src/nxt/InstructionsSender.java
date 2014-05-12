package nxt;

import java.io.DataOutputStream;
import java.io.IOException;

import lejos.pc.comm.NXTConnector;

/**
 * Sends instructions to the NXT. Instructions are sent as 2 ints from -31 to
 * 31, and can be recived and executed by an NXT running ReciveInst
 * 
 * @author Johan LG
 * 
 */

public class InstructionsSender {

	private DataOutputStream dos;
	private int precision = 511;

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

	public void sendInstruction(int r, int v) {
		if (r > 511)
			r = 511;
		else if (r < -511)
			r = -511;
		if (v > 511)
			v = 511;
		else if (v < -511)
			v = -511;
		try {
			dos.writeInt((r + 512) * 1024 + v + 512);
			dos.flush();
			System.out.println("sendt " + r + " " + v);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			dos.writeInt(2000000);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
