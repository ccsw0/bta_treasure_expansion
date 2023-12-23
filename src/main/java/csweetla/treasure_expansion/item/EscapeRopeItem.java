package csweetla.treasure_expansion.item;

import net.minecraft.core.block.Block;
import net.minecraft.core.entity.player.EntityPlayer;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.Dimension;
import net.minecraft.core.world.World;
import net.minecraft.server.entity.player.EntityPlayerMP;

import java.util.Random;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

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

	public EscapeRopeItem(String name, int id, int durability) {
		super(name, id);
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
	private void teleport_safely(EntityPlayer entityPlayer, double x, double y, double z) {
		if (entityPlayer instanceof EntityPlayerMP) {
			EntityPlayerMP mp = (EntityPlayerMP) entityPlayer;
			mp.playerNetServerHandler.teleportTo(x, y, z, 0F, 0F);
		} else {
			entityPlayer.absMoveTo(x, y, z, 0F, 0F);
		}
	}

	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer) {
		if (world.isClientSide) {
			return itemstack;
		}
		int y;
		int rand_pos_offset = 50;

		if (world.dimension != Dimension.overworld) {
			entityplayer.addChatMessage("message." + MOD_ID + ".escape_rope.use_nether");
			return itemstack;
		}
		else if (entityplayer.y >= 128)
		{
			// TODO: Better detection of underground player
			entityplayer.addChatMessage("message." + MOD_ID + ".escape_rope.use_surface");
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

			int block_id = world.getBlockId(x,y,z);
			if (acceptable_landing_block(block_id)) {
				teleport_safely(entityplayer, x + 0.5F, y + 3.0F, z + 0.5F);
				entityplayer.addChatMessage("message." + MOD_ID + ".escape_rope.use_success");
				// TODO: snap sound when rope breaks
				world.playSoundAtEntity(entityplayer,MOD_ID +".rope_whoosh",1.0F,1.0F );

				itemstack.damageItem(2, entityplayer);
				return itemstack;
			}
		}

		entityplayer.addChatMessage("message." + MOD_ID + ".escape_rope.use_failure");
		return itemstack;
	}
}
