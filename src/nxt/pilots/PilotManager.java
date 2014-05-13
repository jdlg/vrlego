package nxt.pilots;

import com.jme3.input.InputManager;

import nxt.NXTConnectionFactory;

public class PilotManager {
	
	private NXTConnectionFactory connectionFactory;
	private Player player;
//	private ArrayList<AI> AIList;
	
	public PilotManager(InputManager inputManager/*, int numberOfPlayers, int numberOfAIs*/) {
		connectionFactory = new NXTConnectionFactory();
		player = new Player(inputManager, connectionFactory.makeConnection("usb"));
		// TODO Fill up AIList (AIFactory?)
	}
	
	public void closeAll(){
		player.close();
		//TODO close AIs
	}

}
