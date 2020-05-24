package wind.model.npcs.pets;

import wind.model.npcs.NPCHandler;
import wind.model.players.Client;

public class PetHandler {
	/**
	 * 
	 * @author Biocide
	 *
	 */
	public enum petData {
		GREEN_ZULRAH(12921, 2130),
		RED_ZULRAH(12939, 2131),
		BLUE_ZULRAH(12940, 2132),
		KRIL(12652, 6647),
		ZILYANA(12651, 6646),
		GRAARDOR(12650, 6644),
		KREEARA(12649, 6643),
		KRAKEN(12655, 6640),
		SMOKEDEVIL(12648, 6639),
		CHAOSELEMENTAL(11995, 5907),
		KALPHITEPRINCESS(12647, 6637),
		MOLE(12646, 6651),
		REX(12645, 6630),
		PRIME(12644, 6629),
		SUPREME(12643, 6628),
		PRINCEKBD(12653, 6652),
		PENANCE(12703, 6642),
		CALLISTOJR(13178, 5558),
		SCORPIAS_OFFSPRING(13181, 5561),
		VENENATIS_SPIDERLING(13177, 5557),
		PETDARKCORE(12816, 318),
		PURPLE_VETTIONJR(13179, 5536),
		ORANGE_VETIONJR(13180, 5537)
		;
		private int item;
		private int npcID;

		private petData(int item, int npcID) {
			this.setItem(item);
			this.setNpcID(npcID);
		}

		public int getItem() {
			return item;
		}

		public void setItem(int item) {
			this.item = item;
		}

		public int getNpcID() {
			return npcID;
		}

		public void setNpcID(int npcID) {
			this.npcID = npcID;
		}
	}
	//
	public static void morph(Client c, int newNPCID) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] == null) {
				continue;
			}
			if (NPCHandler.npcs[i].summonedBy == c.getId()) {
				Pet.deletePet(NPCHandler.npcs[i]);
				c.petID = newNPCID;
				c.petSummoned = true;
				c.setColorSelect("none");
				break;
			}
		}
		c.getPA().removeAllWindows();
	}
}
