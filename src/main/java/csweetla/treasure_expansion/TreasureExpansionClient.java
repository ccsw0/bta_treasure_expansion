package csweetla.treasure_expansion;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.core.Global;
import turniplabs.halplibe.helper.SoundHelper;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class TreasureExpansionClient implements ClientModInitializer {

	private void initializeSounds() {
		if (!Global.isServer) {
			SoundHelper.Client.addSound(MOD_ID, "rope_whoosh.ogg");
		}
	}

	@Override
	public void onInitializeClient() {
		initializeSounds();
	}
}
