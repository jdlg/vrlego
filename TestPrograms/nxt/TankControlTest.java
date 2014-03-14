package nxt;

public class TankControlTest {

	public static void main(String[] args) throws InterruptedException {
		int w = 2000;
		SendInstructions sender = new SendInstructions("usb://");
//		sender.sendInstruction(31, 0);
//		Thread.sleep(w);
		sender.sendInstruction(-31, 31);
		Thread.sleep(w);
		sender.sendInstruction(31, -31);
		Thread.sleep(w);
		sender.sendInstruction(0, 31);
		Thread.sleep(w);
		sender.sendInstruction(0, 0);
		sender.close();

		// byte[] inst = new byte[2];
		// inst[0] = 127;
		// inst[1] = 0;
		// sender.sendInstruction(inst);
		// inst[0] = 127;
		// inst[1] = 127;
		// sender.sendInstruction(inst);
		// inst[0] = 0;
		// inst[1] = -127;
		// sender.sendInstruction(inst);
		// inst[0] = 0;
		// inst[1] = 0;
		// sender.sendInstruction(inst);

	}
}
