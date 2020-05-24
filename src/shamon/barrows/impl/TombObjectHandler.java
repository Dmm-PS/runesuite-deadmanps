package shamon.barrows.impl;

import shamon.barrows.BarrowsNpcController;
import shamon.barrows.data.BarrowsConstants;
import shamon.barrows.data.Brother;
import shamon.barrows.data.TombData;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;

public class TombObjectHandler {

	private final Client client;

	private Task openTask;

	public TombObjectHandler(Client client) {
		this.client = client;
	}

	public boolean openTomb(int objectID, int objectX, int objectY) {
		TombData tomb = TombData.lookup(objectID);

		if (tomb == null) {
			return false;
		}

		if (openTask != null) {
			openTask.cancel();
		}

		openTask = new Task(1, true) {
			@Override public void execute() {
				if (client.goodDistance(objectX, objectY, client.absX, client.absY, 3)) {
					cancel();
				}
			}

			@Override public void onCancel() {
				openTomb(tomb);
			}
		};
		TaskHandler.submit(openTask);
		return true;
	}

	private void openTomb(TombData tomb) {
		Brother brother = tomb.getBrother();
		BarrowsNpcController npcController = client.getBarrows().getNpcController();

		if (brother == npcController.getTargetBrother()) {
			client.getDH().sendDialogues(350, -1);
		} else {
			if (npcController.checkIfSpawned(tomb.getBrother()) || npcController.checkIfKilled(tomb.getBrother())) {
				client.sendMessage(BarrowsConstants.NOTHING_FOUND);
			} else {
				npcController.spawnBrother(brother);
			}
		}
	}

}
