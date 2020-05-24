package com.johnmatczak.task;

import wind.Config;
import wind.model.players.PlayerHandler;
import wind.model.players.saving.PlayerSaving;
import wind.task.Task;

public class AutoSaveTask extends Task {

	public AutoSaveTask() {
		super(Config.AUTOSAVE_TIME * 60, false);
	}

	@Override
	public void execute() {
		if (PlayerHandler.getPlayerCount() >= Config.AUTOSAVE_MINIMUM_ONLINE) {
			PlayerSaving.initialize();
		}
	}
	
}
