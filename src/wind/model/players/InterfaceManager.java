package wind.model.players;

import java.util.ArrayList;
import java.util.List;


/**
 * 
 * @author Fuzen Seth
 *
 */
public class InterfaceManager {

	private final Client player;

	private List<Client> interfaces;

	public InterfaceManager(Client player) {
		this.player = player;
		if (interfaces == null)
			this.interfaces = new ArrayList<>();
	}

	public void sendInterface(int id) {
		player.getPlayerAssistant().sendInterface(id);
	}

	public void sendString(String s, int id) {
		player.getPlayerAssistant().sendString(s, id);
	}

	public void sendChatInterface(int id) {
		player.getPlayerAssistant().sendChatBoxInterface(id);
	}

	public void sendCameraShake(boolean b, final int magnitude) {
		if (b)
			player.getPA().sendCameraShake(1, magnitude, 12, 10);
		else
			player.getPlayerAssistant().resetCameraShake();
	}

	public void closeInterfaces() {
		player.getPlayerAssistant().closeAllWindows();
	}

	public void sendItemOnInterface(int interfaceChild, int zoom, int itemId) {
		player.getPlayerAssistant().itemOnInterface(interfaceChild, zoom,
				itemId);
	}
}

