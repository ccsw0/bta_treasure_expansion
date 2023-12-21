package csweetla.fun_treasure.item;

import csweetla.fun_treasure.FunTreasure;
import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;

import java.util.Random;

import static csweetla.fun_treasure.FunTreasure.MOD_ID;

public class EscapeRopeItem extends Item {

	static int MAX_ESCAPE_TRIES = 35;
	static Random rand = new Random();
	static int[] banned_landing_blocks = {
		0,
		Block.fluidWaterStill.id,
		Block.fluidWaterFlowing.id,
		Block.fluidLavaStill.id,
		Block.fluidLavaFlowing.id,
		Block.algae.id,
		Block.spikes.id,
		Block.fire.id,
		Block.cobweb.id,
		Block.cactus.id,
		Block.ice.id,
		Block.leavesOak.id,
		Block.leavesOakRetro.id,
		Block.leavesPine.id,
		Block.leavesBirch.id,
		Block.leavesEucalyptus.id,
		Block.leavesShrub.id,
		Block.leavesCherry.id,
		Block.leavesCherryFlowering.id,
	};
	public EscapeRopeItem(String name, int id) {
		super(name, id);
		this.maxStackSize = 1;
		this.setMaxDamage(4);
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

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		int y;
		int rand_pos_offset = 50;

		if (world.dimension != Dimension.overworld) {
			entityplayer.addChatMessage("Escape Rope only works in the overworld!");
			return itemstack;
		}
		else if (entityplayer.y >= 128)
		{
			// TODO: Better detection of underground player
			entityplayer.addChatMessage("Escape Rope only works underground!");
			return itemstack;
		}

		// TODO: there are maybe some methods in world that could do this better
		for (int i = 0; i < MAX_ESCAPE_TRIES; i++) {
			y = world.getHeightBlocks();
			int x = (int) entityplayer.x - rand_pos_offset/2 + rand.nextInt(rand_pos_offset);
			int z = (int) entityplayer.z - rand_pos_offset/2 + rand.nextInt(rand_pos_offset);
			while (world.isAirBlock(x, y, z) && y > 128) {
				--y;
			}
			FunTreasure.LOGGER.info("x: " + x + " y: " + y + " z: " + z);
			int block_id = world.getBlockId(x,y,z);
			if (acceptable_landing_block(block_id)) {
				// TODO: won't work in multiplayer I think, fix
				entityplayer.absMoveTo(x + 0.5F, y + 3, z + 0.5F, 0F, 0F);
				entityplayer.addChatMessage("Escaped successfully!");
				// TODO: snap sound when rope breaks
				world.playSoundAtEntity(entityplayer,MOD_ID +".rope_whoosh",1.0F,1.0F );
				itemstack.damageItem(1, entityplayer);
				return itemstack;
			}
		}

		entityplayer.addChatMessage("Can't find an escape here!!!");
		return itemstack;
	}
}
