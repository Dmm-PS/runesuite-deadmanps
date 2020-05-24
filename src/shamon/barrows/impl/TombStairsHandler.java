package shamon.barrows.impl;

import shamon.barrows.data.TombStairData;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;

public class TombStairsHandler {

	private final Client client;
	
	private Task stairTask;

	public TombStairsHandler(Client client) {
		this.client = client;
	}

	public boolean useStairs(int objectID) {
		TombStairData stair = TombStairData.lookup(objectID);
		if (stair != null) {
			
			if(stairTask != null) {
				stairTask.cancel();
			}
			
			stairTask = new Task(1, true) {

				@Override public void execute() {
					if (client.goodDistance(stair.getPosition().getX(), stair.getPosition().getY(), client.absX,
							client.absY, 1)) {
						cancel();
					}
				}

				@Override public void onCancel() {
					client.getBarrows().getNpcController().despawnBrothers();
					stair.teleportAbove(client);
				}
			};

			TaskHandler.submit(stairTask);
			return true;
		}
		return false;
	}
}
