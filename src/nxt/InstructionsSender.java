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

	public InstructionsSender(String deviceURL) {
		NXTConnector connector = new NXTConnector();
		if (!connector.connectTo(deviceURL)) {
			System.out.println("Not connected");
			System.exit(0);
		}
		dos = new DataOutputStream(connector.getOutputStream());
	}

	public void sendInstruction(byte[] inst) {
		try {
			dos.write(inst);
			dos.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendInstruction(int x, int y) {
		try {
			dos.writeInt((x + 32) * 64 + y + 32);
			dos.flush();
			System.out.println("sendt " + x + " " + y);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void close() {
		try {
			dos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
