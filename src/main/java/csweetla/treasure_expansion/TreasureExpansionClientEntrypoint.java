package csweetla.treasure_expansion;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.sound.SoundRepository;
import turniplabs.halplibe.util.ClientStartEntrypoint;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class TreasureExpansionClientEntrypoint implements ClientModInitializer, ClientStartEntrypoint {

	@Override
	public void onInitializeClient() {
	}

	@Override
	public void beforeClientStart() {
	}

	@Override
	public void afterClientStart() {
		SoundRepository.registerNamespace(MOD_ID);
	}
}
