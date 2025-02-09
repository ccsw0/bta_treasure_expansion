package csweetla.treasure_expansion.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.sound.SoundCategory;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.PlayerServer;

import java.util.Random;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class EscapeRopeItem extends Item {

	static int MAX_ESCAPE_TRIES = 35;
	static Random rand = new Random();
	static int[] banned_landing_blocks = {
		0,
		Blocks.FLUID_WATER_STILL.id(),
		Blocks.FLUID_WATER_FLOWING.id(),
		Blocks.FLUID_LAVA_STILL.id(),
		Blocks.FLUID_LAVA_FLOWING.id(),
		Blocks.ALGAE.id(),
		Blocks.SPIKES.id(),
		Blocks.FIRE.id(),
		Blocks.COBWEB.id(),
		Blocks.CACTUS.id(),
		Blocks.ICE.id(),
		Blocks.LEAVES_OAK.id(),
		Blocks.LEAVES_OAK_RETRO.id(),
		Blocks.LEAVES_PINE.id(),
		Blocks.LEAVES_BIRCH.id(),
		Blocks.LEAVES_EUCALYPTUS.id(),
		Blocks.LEAVES_SHRUB.id(),
		Blocks.LEAVES_CHERRY.id(),
		Blocks.LEAVES_CHERRY_FLOWERING.id(),
	};

	public EscapeRopeItem(String name, int id, int durability) {
		super(NamespaceID.getPermanent(MOD_ID,name), id);
		setKey(name);
		this.maxStackSize = 1;
		this.setMaxDamage(2 * durability - 1) ;
	}

	// The escape rope should always put you on solid, safe ground.
	private boolean acceptable_landing_block(int block_id) {
		for (int banned : banned_landing_blocks) {
			if (block_id == banned) {
				return false;
			}
		}
		return true;
	}

	// teleport player in singleplayer AND multiplayer without problems
	private void teleport_safely(Player entityPlayer, double x, double y, double z) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			PlayerServer mp = (PlayerServer) entityPlayer;
			mp.playerNetServerHandler.teleportAndRotate(x, y, z, 0F, 0F);
		} else {
			entityPlayer.absMoveTo(x, y, z, 0F, 0F);
		}
	}

    @Override
	public ItemStack onUseItem(ItemStack itemstack, World world, Player entityplayer) {
		if (world.isClientSide) {
			return itemstack;
		}
		int y;
		int rand_pos_offset = 50;

		if (world.dimension != Dimension.OVERWORLD) {
			entityplayer.sendTranslatedChatMessage("message." + MOD_ID + ".escape_rope.use_nether");
			return itemstack;
		}
		else if (entityplayer.y >= world.worldType.getOceanY())
		{
			// TODO: Better detection of underground player
			entityplayer.sendTranslatedChatMessage("message." + MOD_ID + ".escape_rope.use_surface");
			return itemstack;
		}

		// TODO: there are maybe some methods in world that could do this better
		for (int i = 0; i < MAX_ESCAPE_TRIES; i++) {
			y = world.getHeightBlocks();
			int x = (int) entityplayer.x - rand_pos_offset/2 + rand.nextInt(rand_pos_offset);
			int z = (int) entityplayer.z - rand_pos_offset/2 + rand.nextInt(rand_pos_offset);
			while (world.isAirBlock(x, y, z) && y > world.worldType.getOceanY()) {
				--y;
			}

			int block_id = world.getBlockId(x,y,z);
			if (acceptable_landing_block(block_id)) {
				teleport_safely(entityplayer, x + 0.5F, y + 3.0F, z + 0.5F);
				entityplayer.sendTranslatedChatMessage("message." + MOD_ID + ".escape_rope.use_success");
				// TODO: snap sound when rope breaks
				world.playSoundEffect(entityplayer, SoundCategory.WORLD_SOUNDS,entityplayer.x,entityplayer.y,entityplayer.z,MOD_ID + ":escape_rope.use",1.0f,1.0f);
				itemstack.damageItem(2, entityplayer);
				return itemstack;
			}
		}

		entityplayer.sendTranslatedChatMessage("message." + MOD_ID + ".escape_rope.use_failure");
		return itemstack;
	}
}
