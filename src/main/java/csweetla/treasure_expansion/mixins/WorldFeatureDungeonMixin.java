package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.world_features.WorldFeatureDiscoDungeon;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureDungeon;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Random;

@Mixin(value = WorldFeatureDungeon.class, remap = false)
public abstract class WorldFeatureDungeonMixin {

	@Inject(method = "place", at = @At("HEAD"), cancellable = true)
	public void place(World world, Random random, int x, int y, int z, CallbackInfoReturnable<Boolean> cir) {
		if (random.nextInt(8) == 0) {
			WorldFeatureDiscoDungeon dd = new WorldFeatureDiscoDungeon();
			boolean placed = dd.place(world, random, x, y, z);
//			if (placed)
//				System.out.println("Placed disco: " + x +" "+y+" "+z);
			cir.setReturnValue(placed);
		}
	}

}
