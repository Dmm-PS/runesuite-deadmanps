package wind.model.players.content.skills.impl;

import wind.Config;
import wind.model.items.ItemAssistant;
import wind.model.players.Client;

/**
 * EC
 * 
 * @author Zack/Optimum
 *
 */
public class Runecrafting {

	/**
	 * Rune essence id
	 */
	public final static int RUNE_ESS = 1436;

	/**
	 * This method handles and crafts the runes
	 * 
	 * @param the
	 *            player
	 * @param Level
	 *            required to do the craftRunes
	 * @param The
	 *            item to add - Runes
	 * @param The
	 *            amount of runes to add
	 * @param The
	 *            experience to add
	 * @param Tiara
	 *            required to do
	 */
	public void craftRunes(Client player, int levReq, int itemToAdd,
			int amountToAdd, int xpToAdd, int tiara, int divide) {
		int setAmount = player.getItems().getItemAmount(RUNE_ESS);
		if (!player.getItems().playerHasItem(RUNE_ESS)) {
			player.sendMessage("You need a rune essence to do this.");
			return;
		}
		for (int i = 0; i < setAmount; i++) {
			if (!(player.playerEquipment[player.playerHat] == tiara)) {
				player.getItems();
				player.sendMessage("You need a "
						+ ItemAssistant.getItemName(tiara) + " to do this!");
				return;
			}
			if (player.playerLevel[player.playerRunecrafting] < levReq) {
				player.sendMessage("You need a Runecrafting level of " + levReq
						+ " to do this.");
				return;
			}
			int level = player.getPA().getLevelForXP(player.playerXP[20]);
			int totalAmount = amountToAdd * (level / divide);
			player.getPA().addSkillXP(xpToAdd * Config.RUNECRAFTING_EXPERIENCE,
					player.playerRunecrafting);
			player.getItems().getItemAmount(RUNE_ESS);
			player.getItems().deleteItem(RUNE_ESS, 28);
			player.getItems().addItem(itemToAdd, totalAmount);
		}
	}
}