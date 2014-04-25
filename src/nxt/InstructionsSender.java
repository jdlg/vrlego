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
	private int precision = 255;

	public InstructionsSender(String deviceURL) {
		NXTConnector connector = new NXTConnector();
		System.out.println("Looking for NXT");
		while (!connector.connectTo(deviceURL)) {
		}
		System.out.println("NXT connected");
		dos = new DataOutputStream(connector.getOutputStream());
	}

	public void sendInstruction(int r, int v) {
		if (r > 255) r = 255;
		else if (r < -255) r =-255;
		if (v > 255) v = 255;
		else if (v < -255) v =-255;
		try {
			dos.writeInt((r + 256) * 512 + v + 256);
			dos.flush();
			System.out.println("sendt " + r + " " + v);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			dos.writeInt(100000);
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
