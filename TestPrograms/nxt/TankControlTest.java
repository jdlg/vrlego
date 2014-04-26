package nxt;

public class TankControlTest {

	public static void main(String[] args) throws InterruptedException {
		int w = 2000;
		InstructionsSender sender = new InstructionsSender("usb://");
		sender.sendInstruction(-31, 31);
		Thread.sleep(w);
		sender.sendInstruction(31, -31);
		Thread.sleep(w);
		sender.sendInstruction(0, 31);
		Thread.sleep(w);
		sender.sendInstruction(0, 0);
		sender.close();
	}
}
