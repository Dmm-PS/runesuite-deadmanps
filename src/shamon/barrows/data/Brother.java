package shamon.barrows.data;

import static shamon.barrows.data.BarrowsConstants.AHRIMS_ID;
import static shamon.barrows.data.BarrowsConstants.DHAROKS_ID;
import static shamon.barrows.data.BarrowsConstants.GUTHANS_ID;
import static shamon.barrows.data.BarrowsConstants.KARILS_ID;
import static shamon.barrows.data.BarrowsConstants.TORAGS_ID;
import static shamon.barrows.data.BarrowsConstants.VERACS_ID;

import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import shamon.barrows.data.rewards.BrotherEquipmentRewards;

public enum Brother {

	AHRIM(
			AHRIMS_ID,
			0x1,
			BrotherEquipmentRewards.AHRIMS_HOOD,
			BrotherEquipmentRewards.AHRIMS_ROBE_SKIRT,
			BrotherEquipmentRewards.AHRIMS_ROBE_TOP,
			BrotherEquipmentRewards.AHRIMS_STAFF),
	DHAROK(
			DHAROKS_ID,
			0x2,
			BrotherEquipmentRewards.DHAROKS_HELM,
			BrotherEquipmentRewards.DHAROKS_PLATELEGS,
			BrotherEquipmentRewards.DHAROKS_PLATEBODY,
			BrotherEquipmentRewards.DHAROKS_GREATAXE),
	GUTHAN(
			GUTHANS_ID,
			0x4,
			BrotherEquipmentRewards.GUTHANS_HELM,
			BrotherEquipmentRewards.GUTHANS_CHAINSKIRT,
			BrotherEquipmentRewards.GUTHANS_PLATEBODY,
			BrotherEquipmentRewards.GUTHANS_WARSPEAR),
	KARIL(
			KARILS_ID,
			0x8,
			BrotherEquipmentRewards.KARILS_COIF,
			BrotherEquipmentRewards.KARILS_SKIRT,
			BrotherEquipmentRewards.KARILS_TOP,
			BrotherEquipmentRewards.KARILS_CROSSBOW),
	TORAG(
			TORAGS_ID,
			0x10,
			BrotherEquipmentRewards.TORAGS_HELM,
			BrotherEquipmentRewards.TORAGS_PLATELEGS,
			BrotherEquipmentRewards.TORAGS_PLATEBODY,
			BrotherEquipmentRewards.TORAGS_HAMMERS),
	VERAC(
			VERACS_ID,
			0x20,
			BrotherEquipmentRewards.VERACS_HELM,
			BrotherEquipmentRewards.VERACS_PLATESKIRT,
			BrotherEquipmentRewards.VERACS_BRASSARD,
			BrotherEquipmentRewards.VERACS_FLAIL);

	private final int brotherID;

	/**
	 * Bit position.
	 */
	private final int mask;

	private final BrotherEquipmentRewards[] gear;

	private Brother(int brotherID, int mask, BrotherEquipmentRewards... gear) {
		this.brotherID = brotherID;
		this.mask = mask;
		this.gear = gear;
	}

	private static final Map<Integer, Brother> BROTHERS = Arrays.stream(Brother.values()).collect(
			Collectors.toMap(Brother::getID, Function.identity()));

	public static final Brother lookup(int brotherID) {
		return BROTHERS.get(brotherID);
	}

	public int getMask() {
		return mask;
	}

	public int getID() {
		return brotherID;
	}

	public BrotherEquipmentRewards[] getGear() {
		return gear;
	}
}
