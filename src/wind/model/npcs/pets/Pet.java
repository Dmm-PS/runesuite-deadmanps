package wind.model.npcs.pets;

import wind.model.npcs.NPC;
import wind.model.npcs.NPCHandler;
import wind.model.npcs.pets.PetHandler.petData;
import wind.model.players.Client;

/**
 * @author Jordon
 * @author Animeking1120
 * @author Erick
 * @author MGT Madness, started editing at 11-12-2013.
 *         <p>
 *         Pet system.
 */
public class Pet {

	/**
	 * Find if this NPC is a pet to pickup.
	 * 
	 * @param player
	 *            The player interacting with the the NPC.
	 * @param npcType
	 *            The NPC type being interacted with.
	 */
	public static void pickUpPetRequirements(Client player, int npcType) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] == null)
				continue;
		}  
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (player.petID == npcType) {
					if (NPCHandler.npcs[i].spawnedBy == player.playerId && NPCHandler.npcs[i].spawnedBy > 0) {
						pickUpPet(player, player.petID);
						break;
					}
				}
			}
		}
	}

	/**
	 * Pick up the pet and place in inventory of the player.
	 * 
	 * @param player
	 *            The player picking up the pet.
	 * @param pet
	 *            The identity of the pet being picked up
	 */
	public static void pickUpPet(Client player, int pet) {
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] == null)
				continue;
		}  
		for (int i = 0; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] != null) {
				if (NPCHandler.npcs[i].spawnedBy == player.getId()) {
					deletePet(NPCHandler.npcs[i]);
				}
				for (petData x : PetHandler.petData.values()) {
					if (player.petID == x.getNpcID()) {
						int itemID = x.getItem();
						player.getItems().addItem(itemID, 1);
					}
				}
				player.startAnimation(827);
				player.setPetSummoned(false);
				player.petID = -1;
			}
		}
		player.sendMessage("You pick up your pet.");
	}
	public void forceFacePlayer() {	}

	/**
	 * Summon the pet.
	 * 
	 * @param player
	 *            The player who summoned the pet.
	 * @param npcType
	 *            The pet being summoned.
	 * @param x
	 *            The x coord of the pet.
	 * @param y
	 *            The x coord of the pet.
	 * @param heightLevel
	 *            The height of the pet.
	 */
	public static void summonPet(Client player, int npcType, int x, int y,
			int heightLevel) {
		int slot = -1;
		for (int i = 1; i < NPCHandler.maxNPCs; i++) {
			if (NPCHandler.npcs[i] == null) {
				slot = i;
				break;
			}
		}
		if (slot == -1) {
			return;
		}
		NPC newNPC = new NPC(slot, npcType);
		NPCHandler.npcs[slot] = newNPC;
		newNPC.absX = x;
		newNPC.absY = y;
		newNPC.makeX = x;
		newNPC.makeY = y;
		newNPC.heightLevel = heightLevel;
		newNPC.walkingType = 0;
		newNPC.HP = 0;
		newNPC.MaxHP = 0;
		newNPC.maxHit = 0;
		newNPC.attack = 0;
		newNPC.defence = 0;
		newNPC.spawnedBy = player.getId();
		newNPC.underAttack = true;
		newNPC.facePlayer(player.getId());
		newNPC.summoned = true;
		newNPC.summonedBy = player.getId();
		player.petID = npcType;
		player.petIndex = player.getId();
		player.setPetSummoned(true);
	}

	/**
	 * Delete the current pet to summon a new one. This is for cases such as the
	 * Player is too far away from Pet, so the pet gets deleted and summoned
	 * close to player. Or if pet is picked up.
	 */
	public static void deletePet(NPC pet) {
		pet.absX = -1;
		pet.absY = -1;
		pet.makeX = -1;
		pet.makeY = -1;
		pet.heightLevel = -1;
		pet.walkingType = -1;
		pet.HP = -1;
		pet.MaxHP = -1;
		pet.maxHit = -1;
		pet.attack = -1;
		pet.defence = -1;
		pet.isDead = true;
		pet.applyDead = true;
	}

	/**
	 * Spawn the pet for the player that just logged in
	 * 
	 * @param player
	 *            The associated player.
	 */
	public static void ownerLoggedIn(Client player) {
		if (player.getPetSummoned() && player.petID > 0) {
			NPCHandler.summonPet(player, player.petID, player.absX, player.absY - 1,
					player.heightLevel);
			player.sendMessage("Your loyal pet finds you!");
		}
	}

}