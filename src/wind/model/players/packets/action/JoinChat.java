package wind.model.players.packets.action;

import wind.Server;
import wind.model.players.Client;
import wind.model.players.PacketType;
import wind.util.Misc;
import wind.world.Clan;

public class JoinChat implements PacketType {

	@Override
	public void processPacket(Client player, int packetType, int packetSize) {
		String owner = Misc.longToPlayerName2(player.getInStream().readQWord())
				.replaceAll("_", " ");
		if (owner != null && owner.length() > 0) {
			if (player.clan == null) {
				Clan clan = Server.clanManager.getClan(owner);
				if (clan != null) {
					clan.addMember(player);
				} else if (owner.equalsIgnoreCase(player.playerName)) {
					Server.clanManager.create(player);
				} else {
					player.sendMessage(Misc.formatPlayerName(owner)
							+ " has not created a clan yet.");
				}
				player.getPA().refreshSkill(21);
				player.getPA().refreshSkill(22);
				player.getPA().refreshSkill(23);
			}
		}
	}

}