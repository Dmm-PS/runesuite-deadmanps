package shamon.barrows.impl;

import java.util.Arrays;
import java.util.Optional;

import shamon.barrows.data.BarrowsConstants;
import shamon.barrows.data.MoundData;
import wind.model.players.Client;
import wind.task.Task;
import wind.task.TaskHandler;

public class MoundHandler {

	private final Client client;

	private Task digTask;

	public MoundHandler(Client client) {
		this.client = client;
	}

	public boolean dig(int itemID) {
		if (itemID == BarrowsConstants.SPADE_ID) {
			Optional<MoundData> findMound = getMound(client);

			if (digTask != null) {
				digTask.cancel();
			}

			digTask = new Task(1, false) {
				@Override public void onSubmit() {
					client.startAnimation(BarrowsConstants.SPADE_ANIMATION_ID);
					client.sendMessage(BarrowsConstants.BARROWS_DIG_MESSAGE);
				}

				@Override public void execute() {
					findMound.ifPresent(m -> m.teleportBelow(client));
					cancel();
				}

				@Override public void onCancel() {
					client.startAnimation(BarrowsConstants.STOP_ANIMATION_ID);
				}
			};
			TaskHandler.submit(digTask);

			return findMound.isPresent();
		}
		return false;
	}

	private Optional<MoundData> getMound(Client client) {
		Optional<MoundData> mound = Arrays.stream(MoundData.MOUNDS).filter(m -> m.checkBounds(client)).findFirst();
		return mound;
	}

}
