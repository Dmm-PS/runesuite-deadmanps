package wind.model.players.content;

import wind.model.players.Client;

/**
 * @author Division Tracking ip's that logged in on your account.
 */

public class ConnectedFrom {

	public static boolean addConnectedFrom(Client client, String host) {
		if (client != null) {
			if (client.lastConnectedFrom.contains(host)) {
				removeHostFromList(client, host);
				return client.lastConnectedFrom.add(host);
			}
			if (!client.lastConnectedFrom.contains(host)) {
				return client.lastConnectedFrom.add(host);
			}
		}
		return false;
	}

	public static boolean removeHostFromList(Client client, String host) {
		if (client != null) {
			return client.lastConnectedFrom.remove(host);
		}
		return false;
	}
}