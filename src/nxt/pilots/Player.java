package nxt.pilots;

import nxt.InstructionsSender;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;

public class Player extends Pilot {
		
	public Player(InputManager inputManager, InstructionsSender sender) {
		this.sender = sender;
		
		//TODO create separate class for handling input?
		inputManager.addMapping("right", new KeyTrigger(KeyInput.KEY_RIGHT));
		inputManager.addMapping("up", new KeyTrigger(KeyInput.KEY_UP));
		inputManager.addMapping("left", new KeyTrigger(KeyInput.KEY_LEFT));
		inputManager.addMapping("down", new KeyTrigger(KeyInput.KEY_DOWN));
		inputManager.addListener(new ActionListener() {
			
			int value = 255, r = 0, u = 0, l = 0, d = 0;

			@Override
			public void onAction(String name, boolean press, float arg2) {
				int x = 0, y = 0;
				if (press) {
					if (name.equals("right"))
						r = value;
					if (name.equals("up"))
						u = value;
					if (name.equals("left"))
						l = value;
					if (name.equals("down"))
						d = value;
				} else {
					if (name.equals("right"))
						r = 0;
					if (name.equals("up"))
						u = 0;
					if (name.equals("left"))
						l = 0;
					if (name.equals("down"))
						d = 0;
				}

				x = r - l;
				y = d - u;
				drive(x, y);
			}
		}, "right", "up", "left", "down");
	}
}
