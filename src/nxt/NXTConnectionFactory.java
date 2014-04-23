package nxt;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class NXTConnectionFactory {

	/**
	 * Connects to the first found NXT
	 * 
	 * @param connectionType
	 *            Supported types:
	 *            <ul>
	 *            <li>"usb"</li>
	 *            <li>"btspp"</li>
	 *            </ul>
	 * @return
	 */
	public InstructionsSender makeConnection(String connectionType) {

		InstructionsSender sender = new InstructionsSender(connectionType
				+ "://");

		return sender;
	}

}
