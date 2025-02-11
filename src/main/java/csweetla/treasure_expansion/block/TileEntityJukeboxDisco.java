package csweetla.treasure_expansion.block;

import net.minecraft.core.block.entity.TileEntityJukebox;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;


import static csweetla.treasure_expansion.TreasureExpansion.blockDiscoJukebox;

public class TileEntityJukeboxDisco extends TileEntityJukebox {

	private int playtime_ticks = 0;

	private static final HashMap< @NotNull Item,Integer> disc_playtime_ticks = new HashMap<>();

	private void play_rand_disc() {
		if (worldObj != null) {
			playtime_ticks = 1;
			Item rand_record = Item.getItem(Items.RECORD_13.id + worldObj.rand.nextInt(disc_playtime_ticks.size()));
            assert rand_record != null;
            blockDiscoJukebox.getLogic().playRecord(worldObj, x, y, z, rand_record.id);
			worldObj.playBlockEvent(null, 1005, x, y, z, rand_record.id);
			playtime_ticks = disc_playtime_ticks.get(rand_record);
		}
	}

	public void tick() {
		if (worldObj != null && worldObj.getBlockId(x, y, z) == blockDiscoJukebox.id()) {
			if (worldObj.getBlockMetadata(x, y, z) == 1)
				playtime_ticks--;
			if (worldObj.getClosestPlayer(x,y,z,60) != null && (worldObj.getBlockMetadata(x, y, z) == 0 || playtime_ticks < 0) && !worldObj.isClientSide) {
				play_rand_disc();
			}
		}
	}

	@Override
	public void dropContents(World world, int x, int y, int z) {
		if (this.record != 0) {
			world.playBlockEvent(1005, x, y, z, 0);
			world.playRecord(null, null, x, y, z);
			this.record = 0;
			this.setChanged();
			world.setBlockMetadataWithNotify(x, y, z, 0);
			playtime_ticks = 0;
		}
	}

	private static int playtimeToTicks(int minutes, int seconds) {
		return (60 * minutes + seconds) * 20;
	}

	static {
		disc_playtime_ticks.put(Items.RECORD_13,playtimeToTicks(2,58));
		disc_playtime_ticks.put(Items.RECORD_BLOCKS,playtimeToTicks(5,45));
		disc_playtime_ticks.put(Items.RECORD_CAT,playtimeToTicks(3,5));
		disc_playtime_ticks.put(Items.RECORD_CHIRP,playtimeToTicks(3,5));
		disc_playtime_ticks.put(Items.RECORD_DOG,playtimeToTicks(2,27));
		disc_playtime_ticks.put(Items.RECORD_FAR,playtimeToTicks(2,54));
		disc_playtime_ticks.put(Items.RECORD_MALL,playtimeToTicks(3,17));
		disc_playtime_ticks.put(Items.RECORD_MELLOHI,playtimeToTicks(1,36));
		disc_playtime_ticks.put(Items.RECORD_STAL,playtimeToTicks(2,30));
		disc_playtime_ticks.put(Items.RECORD_STRAD,playtimeToTicks(3,8));
		disc_playtime_ticks.put(Items.RECORD_WAIT,playtimeToTicks(3,57));
		disc_playtime_ticks.put(Items.RECORD_WARD,playtimeToTicks(4,11));
	}

}
