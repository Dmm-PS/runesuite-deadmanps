package wind.model.players.content;

import wind.model.items.ItemAssistant;
import wind.model.players.Client;
import wind.model.shops.ShopAssistant;
import wind.task.Task;
import wind.task.TaskHandler;
import wind.util.Misc;

/**
 * Handles The ItemLottery
 * 
 * @author - Aintaro Heavily Modified - Play Boy Rewrote by Sunny++
 */

public class ItemLottery {

	private Client c;

	public ItemLottery(Client c) {
		this.c = c;
	}

	int winningItem;

	int[] lotteryItems = { 1149, 1187, 2615, 2617, 2619, 2621, 2623, 2625, 2627, 2629,
			3476, 3477, 3478 };
	
	int[] rareItems = { 3481, 3483, 3485, 3486, 3488, 12526, 12829, };
	
	int[] rareItems2 = { 11802, 11804, 11806, 11808, 12389, 12833,  12391, 12422, 12424, 12426, 12603, 12605, 12607,
			12816, 12819, 12823,  12827, 11785, 12646, 12647, 12648, 12931, 12936};

	public int handleLotteryItems() {
		return lotteryItems[(int) (Math.random() * lotteryItems.length)];
	}

	public int handleLotteryRares() {
		return rareItems[(int) (Math.random() * rareItems.length)];
	}

	public int handleLotteryRares2() {
		return rareItems2[(int) (Math.random() * rareItems2.length)];
	}

	public boolean hasWon = (!(Misc.random(20) == 1));

	public void Run() {
		TaskHandler.submit(new Task(1, true) {
			int pStage = 10;

			@Override
			public void execute() {
				switch (pStage) {

				case 1:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 2:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 3:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryRares(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryRares();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 4:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 5:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryRares2(), 0, 1);
					if (Misc.random(15) == 1) {
						hasWon = true;
						winningItem = handleLotteryRares2();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 6:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 7:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 8:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 9:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
					} else {
						hasWon = false;
					}
					break;

				case 10:
					if (c.disconnected) {
						this.cancel();
						c.pkp += 50;
						return;
					}
					c.getPA().sendFrame34a(16002, handleLotteryItems(), 0, 1);
					if (Misc.random(10) == 1) {
						hasWon = true;
						winningItem = handleLotteryItems();
						c.getPA().sendFrame34a(15002, winningItem, 0, 1);
						c.getItems().addItem(winningItem, 1);
						c.getPA().showInterface(15000);
						SendMessages();
						CloseInterface();
						// c.sendMessage("10");
					} else {
						hasWon = false;
						// c.sendMessage("10.10");
					}
					break;

				default:
					if (!hasWon) {
						pStage = 10;
						// c.sendMessage("Default 0");
					} else if (hasWon) {
						pStage = 0;
						// c.sendMessage("Default 0.0");
					}
					break;
				}

				if (pStage == 0 || hasWon) {
					this.cancel();
					System.gc();
				}
				if (pStage > 0 && !hasWon) {
					pStage--;
				}
			}

			@Override
			public void onCancel() {
				pStage = 0;
			}

		});
		c.getPA().sendFrame126("", 16003);
		c.getPA().showInterface(16000);
	}

	public void tryLottery() {
		if (c.getEquipment().freeSlots() >= 1) {
			if (c.getItems().playerHasItem(995, 10000000)) {
				c.getItems().deleteItem(995, 10000000);
				Run();
			} else {
				c.getDH().sendDialogues(226, 220);
			}
		} else {
			c.getPA().removeAllWindows();
			c.getPA().sendStatement(
					"You need atleast 1 free spot in your inventory.");
		}
	}

	private void SendMessages() {
		c.getItems();
		c.getItems();
		if (ItemAssistant.getItemName(winningItem).endsWith("s")) {
			c.getItems();
			c.getItems();
			c.getPA()
					.sendFrame126(
							"You've won "
									+ ItemAssistant.getItemName(winningItem)
									+ "!", 15003);
			c.getItems();
			c.getItems();
			c.sendMessage("[<col=2784FF>Item Lottery</col>] Congratulations! You have won <col=2784FF>"
					+ ItemAssistant.getItemName(winningItem) + "</col>!");
		} else {
			c.getItems();
			c.getItems();
			c.getPA().sendFrame126(
					"You've won a " + ItemAssistant.getItemName(winningItem)
							+ "!", 15003);
			c.getItems();
			c.getItems();
			c.sendMessage("[<col=2784FF>Item Lottery</col>] Congratulations! You have won a <col=2784FF>"
					+ ItemAssistant.getItemName(winningItem) + "</col>!");
		}
		PhatStack();
	}

	private void PhatStack() {
		c.getShops();
		if (ShopAssistant.getItemShopValue(winningItem) > 99999999) {
			c.getItems();
			c.getItems();
			if (ItemAssistant.getItemName(winningItem).endsWith("s")) {
				c.getItems();
				c.getItems();
				c.getPA().yell(
						"[<col=2784FF>Item Lottery</col>] <col=2784FF>"
								+ c.playerName
								+ " </col>has just won <col=2784FF>"
								+ ItemAssistant.getItemName(winningItem)
								+ "</col>!");
			} else {
				c.getItems();
				c.getItems();
				c.getPA().yell(
						"[<col=2784FF>Item Lottery</col>] <col=2784FF>"
								+ c.playerName
								+ " </col>has just won a <col=2784FF>"
								+ ItemAssistant.getItemName(winningItem)
								+ "</col>!");
			}
			MyBallsAreSoHappy();
			Achievements.BigRewardPK();
		}
	}

	private void MyBallsAreSoHappy() {
		c.getPA().handleBigfireWork(c);
		c.startAnimation(2109);
	}

	private void CloseInterface() {
		TaskHandler.submit(new Task(1, true) {
			int pStage = 1;

			@Override
			public void execute() {
				if (pStage == 1) {
					c.getPA().removeAllWindows();
				}
				if (pStage == 0) {
					this.cancel();
				}
				if (pStage > 0) {
					pStage--;
				}
			}

			@Override
			public void onCancel() {
				pStage = 0;
			}

		});
	}
}