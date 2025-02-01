package csweetla.treasure_expansion;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.Global;
import turniplabs.halplibe.helper.SoundHelper;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class TreasureExpansionClient implements ClientModInitializer, ClientStartEntrypoint {

//	private void initializeSounds() {
////		if (!Global.isServer) {
//		SoundHelper.addSound(MOD_ID, "rope_whoosh.ogg");
////		}
//	}

	@Override
	public void onInitializeClient() {

		//initializeSounds();
	}

	@Override
	public void beforeClientStart() {

	}

	@Override
	public void afterClientStart() {
		SoundRepository.registerNamespace(MOD_ID);
	}
}
