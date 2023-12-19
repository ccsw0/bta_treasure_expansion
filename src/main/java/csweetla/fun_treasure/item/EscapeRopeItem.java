package csweetla.fun_treasure.item;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;

import java.util.Random;

public class EscapeRopeItem extends Item {

	static int MAX_ESCAPE_TRIES = 20;
	static Random rand = new Random();
	static String[] banned_landing_blocks = {
		"tile.fluid.water.still",
		"tile.fluid.water.flowing",
		"tile.fluid.lava.still",
		"tile.fluid.lava.flowing",
		"tile.spikes",
		"tile.fire",
		"tile.cobweb",
		"tile.cactus",
		"tile.ice",
		"tile.leaves.oak",
		"tile.leaves.oak.retro",
		"tile.leaves.pine",
		"tile.leaves.birch",
		"tile.leaves.cherry",
		"tile.leaves.eucalyptus",
		"tile.leaves.shrub",
		"tile.leaves.cherry.flowering",
	};
	public EscapeRopeItem(String name, int id) {
		super(name, id);
		this.maxStackSize = 1;
		this.setMaxDamage(4);
	}

	// The escape rope should always put you on solid, safe ground.
	private boolean acceptable_landing_block(String block_key) {
		for (String banned : banned_landing_blocks) {
			if (block_key.equals(banned)) {
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

		for (int i = 0; i < MAX_ESCAPE_TRIES; i++) {
			y = world.getHeightBlocks();
			int x = (int) entityplayer.x - rand_pos_offset/2 + rand.nextInt(rand_pos_offset);
			int z = (int) entityplayer.z - rand_pos_offset/2 + rand.nextInt(rand_pos_offset);
			while (world.isAirBlock(x, y, z)) {
				--y;
			}
			// if our escape is below sea level, it doesn't count
			if (y < 128) {
				continue;
			}
			String block_key = Block.getBlock(world.getBlockId(x,y,z)).getKey();
			if (acceptable_landing_block(block_key)) {
				// TODO: won't work in multiplayer I think, fix
				entityplayer.absMoveTo(x + 0.5F, y + 3, z + 0.5F, 0F, 0F);
				entityplayer.addChatMessage("Escaped successfully!");
				world.playSoundAtEntity(entityplayer,"fire.ignite",1.0F,1.0F );
				itemstack.damageItem(1, entityplayer);
				return itemstack;
			}
		}

		entityplayer.addChatMessage("Can't find an escape here!!!");
		return itemstack;
	}
}
