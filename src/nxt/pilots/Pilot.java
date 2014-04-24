package nxt.pilots;

import nxt.InstructionsSender;

public abstract class Pilot {
	protected InstructionsSender sender;

	protected void drive(int rotation, int speed) {
		sender.sendInstruction(rotation, speed);
	}
	
	public void close(){
		sender.close();
	}
}
