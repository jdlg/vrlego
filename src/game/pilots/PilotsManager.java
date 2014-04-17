package game.pilots;

import java.util.ArrayList;

import com.jme3.input.InputManager;

import nxt.NXTConnectionFactory;

public class PilotsManager {
	
	private NXTConnectionFactory connectionFactory;
	private Player player;
	private ArrayList<AI> AIList;
	
	public PilotsManager(InputManager inputManager, /*int numberOfPlayers,*/ int numberOfAIs) {
		connectionFactory = new NXTConnectionFactory();
		player = new Player(inputManager, connectionFactory.makeConnection());
		// TODO Fill up AIList (AIFactory?)
	}

}
