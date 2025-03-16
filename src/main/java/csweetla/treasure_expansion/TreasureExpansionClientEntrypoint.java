package csweetla.treasure_expansion;

import csweetla.treasure_expansion.entity.EntityVampire;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.guidebook.mobs.MobInfoRegistry;
import net.minecraft.client.sound.SoundRepository;
import net.minecraft.core.item.Items;
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

		MobInfoRegistry.register(EntityVampire.class, "mob." + MOD_ID + ".vampire.name", "mob." + MOD_ID + ".vampire.desc", 25, 1200,
			new MobInfoRegistry.MobDrop[]{
				new MobInfoRegistry.MobDrop(Items.QUARTZ.getDefaultStack(), 1.0F, 3, 4),
				new MobInfoRegistry.MobDrop(Items.DUST_REDSTONE.getDefaultStack(), 0.5F, 0, 2)
			}
		);

	}
}
