package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.entity.EntityVampire;
import net.minecraft.core.entity.SpawnListEntry;
import net.minecraft.core.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(value = Biome.class, remap = false)
public abstract class BiomeMixin {

	@Shadow
	protected List<SpawnListEntry> spawnableWaterCreatureList;
	@Shadow
	protected List<SpawnListEntry> spawnableCreatureList;

	@Shadow
	protected List<SpawnListEntry> spawnableMonsterList;

	@Inject(method = "<init>", remap = false, at = @At("TAIL"))
	private void addMobs(CallbackInfo ci) {
		spawnableMonsterList.add(new SpawnListEntry(EntityVampire.class, 4));
	}
}
