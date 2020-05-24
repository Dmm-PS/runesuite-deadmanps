package shamon.barrows;

import java.util.ArrayList;
import java.util.List;

import shamon.barrows.data.BarrowsConstants;
import shamon.barrows.data.Brother;
import shamon.barrows.data.rewards.BrotherEquipmentRewards;
import shamon.barrows.data.rewards.KillCountRewards;
import shamon.barrows.data.rewards.Loot;
import shamon.barrows.data.rewards.RegularRewardsData;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

public class BarrowsChest {
	/**
	 * Boolean for keeping track of the chest is looted.
	 * This is also an end game check. If you dig in a
	 * mound and the chest is looted, start a new game.
	 */
	private boolean looted; // Save this.
	private boolean interacted = false;

	private Task openTask;

	private final Client client;
	public Object BARROWS_KEY= new Object();

	public static final int BARROWS_CHEST = 20973;

	public BarrowsChest(Client client) {
		this.client = client;
	}

	public boolean openChest(int objectID, int objectX, int objectY) {
		if (objectID != BARROWS_CHEST) {
			return false;
		}

		if (openTask != null) {
			TaskHandler.cancel(BARROWS_KEY);
		}

		openTask = new Task(1, true) {
			@Override public void execute() {
				if (client.goodDistance(objectX, objectY, client.absX, client.absY, 2)) {
					client.getBarrows().getChest().lootChest();
					cancel();
				}
			}
		};
		openTask.attach(BARROWS_KEY);
		TaskHandler.submit(openTask);
		return true;
	}

	public void lootChest() {
		Barrows barrows = client.getBarrows();
		BarrowsNpcController npcController = barrows.getNpcController();

		Brother brother = npcController.getTargetBrother();

		// Check if the target brother is killed and spawned a if not, spawn him.
		if (!npcController.checkIfKilled(brother) && !npcController.checkIfSpawned(brother)) {
			npcController.spawnBrother(brother);
		}
		if (npcController.checkIfKilled(brother)){	
			if (!hasLooted()) {
				if (!interacted) {
					interacted = true;
				} else {
					rollRewards();
					setLooted(true);
					client.getDH().sendDialogues(352, -1);
				}
			} else {
				client.sendMessage(BarrowsConstants.EMPTY_CHEST);
				client.getDH().sendDialogues(353, -1);
			}
		}
	}
	private void rollRewards() {
		Barrows barrows = client.getBarrows();
		BarrowsNpcController npcController = barrows.getNpcController();

		for (int roll = 0; roll < npcController.countBrothersKilled(); roll++) {
			rollRegularItems();
		}
		rollUncommonItems();
		rollBrotherEquipment();
		if (npcController.countBrothersKilled()==6){
			bonusRollBrotherEquipment();
		}

	}

	private void rollRegularItems() {
		RegularRewardsData regularItems = RegularRewardsData.REWARDS[(int) (Math.random() * RegularRewardsData.REWARDS.length)];
		client.getItems().addItem(regularItems.getItemID(), regularItems.getAmount());
	}

	private void rollUncommonItems() {
		KillCountRewards regularItems = KillCountRewards.REWARDS[(int) (Math.random() * KillCountRewards.REWARDS.length)];
		rollForUncommon(regularItems);
	}

	private void rollBrotherEquipment() {
		BrotherEquipmentRewards item = getRandomBarrowsEquipment();
		rollForEquipment(item);
	}
	
	private void bonusRollBrotherEquipment() {
		BrotherEquipmentRewards item = getRandomBarrowsEquipment();
		bonusRollForEquipment(item);
	}
	
	private void bonusRollForEquipment(Loot loot) {
		double random = 0;
			random = Misc.random(100);
			if (random >= 98){
				client.getItems().addItem(loot.getItemID(), loot.getAmount());
			}
		}


	private void rollForEquipment(Loot loot) {
		double random = 0;
		Barrows barrows = client.getBarrows();
		BarrowsNpcController npcController = barrows.getNpcController();
		if (npcController.countBrothersKilled()==1)	{	
			random = Misc.random(100);
			if (random >= 98){
				client.getItems().addItem(loot.getItemID(), loot.getAmount());
				return;
			}
		} else if (npcController.countBrothersKilled()==2)	{	
			random = Misc.random(100);
			if (random >= 96){
				client.getItems().addItem(loot.getItemID(), loot.getAmount());
				return;
			}
		} else 	if (npcController.countBrothersKilled()==3)	{	
			random = Misc.random(100);
			if (random >= 94){
				client.getItems().addItem(loot.getItemID(), loot.getAmount());
				return;
			}
		} else	if (npcController.countBrothersKilled()==4)	{	
			random = Misc.random(100);
			if (random >= 92){
				client.getItems().addItem(loot.getItemID(), loot.getAmount());
				return;
			}
		} else 	if (npcController.countBrothersKilled()>=5)	{	
			random = Misc.random(100);
			if (random >= 90){
				client.getItems().addItem(loot.getItemID(), loot.getAmount());
				return;
			}
		}
	}
	private void rollForUncommon(Loot loot) {
		double random = Misc.random(40);

		if (Loot.getChance(client) > random) {
			client.getItems().addItem(loot.getItemID(), loot.getAmount());
		}
	}

	private BrotherEquipmentRewards getRandomBarrowsEquipment() {
		List<BrotherEquipmentRewards> items = new ArrayList<>();

		Barrows barrows = client.getBarrows();
		BarrowsNpcController npcController = barrows.getNpcController();

		// Add killed brothers loot to a list and shuffle it.
		for (Brother brother : Brother.values()) {
			if (npcController.checkIfKilled(brother)) {
				for (BrotherEquipmentRewards item : brother.getGear()) {
					items.add(item);
				}
			}
		}
		return items.get((int) (Math.random() * items.size()));
	}

	public void reset() {
		setLooted(false);
		interacted = false;
	}

	public void setLooted(boolean looted) {
		this.looted = looted;
	}

	public boolean hasLooted() {
		return looted;
	}
}
