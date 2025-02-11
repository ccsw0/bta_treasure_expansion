package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.world_features.WorldFeatureDiscoDungeon;
import net.minecraft.core.block.BlockLogicSand;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.world.World;
import net.minecraft.core.world.chunk.Chunk;
import net.minecraft.core.world.generate.chunk.ChunkGenerator;
import net.minecraft.core.world.generate.chunk.perlin.overworld.ChunkGeneratorOverworld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Random;

@Mixin(value = ChunkGenerator.class, remap = false)
public class ChunkGeneratorMixin {

	@Shadow
	@Final
	protected World world;


//	@Inject(method = "generate", at = @At("TAIL"))
//	public final void generate(int chunkX, int chunkZ, CallbackInfoReturnable<Chunk> cir) {
//		ChunkGenerator thisAs = (ChunkGenerator) (Object) this;
//		if (StructureBackports.mineshafts_enabled && thisAs instanceof ChunkGeneratorOverworld) {
//			this.mineshaftGenerator.generate(this.world.getChunkProvider(), this.world, chunkX, chunkZ);
//		} else if (StructureBackports.nether_forts_enabled && thisAs instanceof ChunkGeneratorNether) {
//			this.fortGenerator.generate(this.world.getChunkProvider(), this.world, chunkX, chunkZ);
//		}
//	}

	@Inject(method = "decorate", at = @At("HEAD"))
	public final void decorate(Chunk chunk, CallbackInfo ci) {
		ChunkGenerator thisAs = (ChunkGenerator) (Object) this;
		if (thisAs instanceof ChunkGeneratorOverworld) {
			this.world.scheduledUpdatesAreImmediate = true;
			int chunkX = chunk.xPosition;
			int chunkZ = chunk.zPosition;
			int minY = this.world.getWorldType().getMinY();
			int maxY = this.world.getWorldType().getMaxY();
			int rangeY = maxY + 1 - minY;
			float oreHeightModifier = (float)rangeY / 128.0F;
			BlockLogicSand.fallInstantly = true;
			int x = chunkX * 16;
			int z = chunkZ * 16;
			int y = this.world.getHeightValue(x + 16, z + 16);

			Random rand = new Random(this.world.getRandomSeed());
			long l1 = rand.nextLong() / 2L * 2L + 1L;
			long l2 = rand.nextLong() / 2L * 2L + 1L;
			rand.setSeed((long)chunkX * l1 + (long)chunkZ * l2 ^ this.world.getRandomSeed());

			if (rand.nextInt(5) == 0) return;
			for (int k1 = 0; (float)k1 < 8.0F * oreHeightModifier; k1++) {
				int j5 = x + rand.nextInt(16) + 8;
				int k8 = minY + rand.nextInt(rangeY);
				int j11 = z + rand.nextInt(16) + 8;
				new WorldFeatureDiscoDungeon(Blocks.BRICK_CLAY.id(), Blocks.BRICK_CLAY.id(), null).place(this.world, rand, j5, k8, j11);
			}
		}
	}
}
