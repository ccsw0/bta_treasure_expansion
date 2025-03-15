package csweetla.treasure_expansion.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.core.block.entity.TileEntityJukebox;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemDiscMusic;
import net.minecraft.core.item.Items;
import net.minecraft.core.lang.I18n;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Objects;

import static csweetla.treasure_expansion.TreasureExpansion.blockDiscoJukebox;

public class TileEntityJukeboxDisco extends TileEntityJukebox {

	private static final HashMap<@NotNull Item, Integer> disc_playtime_ticks = new HashMap<>();

	static {
//		disc_playtime_ticks.put(Items.RECORD_13, playtimeToTicks(2, 58));
		disc_playtime_ticks.put(Items.RECORD_BLOCKS, playtimeToMillisecond(5, 45));
		disc_playtime_ticks.put(Items.RECORD_CAT, playtimeToMillisecond(3, 5));
		disc_playtime_ticks.put(Items.RECORD_CHIRP, playtimeToMillisecond(3, 5));
		disc_playtime_ticks.put(Items.RECORD_DOG, playtimeToMillisecond(2, 27));
		disc_playtime_ticks.put(Items.RECORD_FAR, playtimeToMillisecond(2, 54));
		disc_playtime_ticks.put(Items.RECORD_MALL, playtimeToMillisecond(3, 17));
		disc_playtime_ticks.put(Items.RECORD_MELLOHI, playtimeToMillisecond(1, 36));
		disc_playtime_ticks.put(Items.RECORD_STAL, playtimeToMillisecond(2, 30));
		disc_playtime_ticks.put(Items.RECORD_STRAD, playtimeToMillisecond(3, 8));
		disc_playtime_ticks.put(Items.RECORD_WAIT, playtimeToMillisecond(3, 57));
		disc_playtime_ticks.put(Items.RECORD_WARD, playtimeToMillisecond(4, 11));
	}

	private long playtime_start = 0;
	private long playtime_millisec = 0;

	private static int playtimeToMillisecond(int minutes, int seconds) {
		return (60 * minutes + seconds) * 1000;
	}

	private boolean discFinished() {
		long curr_time = System.currentTimeMillis();
		return curr_time - playtime_millisec + 500 > playtime_start;
	}

	private void playDisc() {
		if (worldObj != null) {
			playtime_start = System.currentTimeMillis();
			Item rand_record = Item.getItem(Items.RECORD_CAT.id + (Math.abs(x ^ z) % disc_playtime_ticks.size()));
			this.record = Objects.requireNonNull(rand_record).id;
            blockDiscoJukebox.getLogic().playRecord(worldObj, x, y, z, rand_record.id);
			worldObj.playBlockEvent(null, 1005, x, y, z, rand_record.id);
			playtime_millisec = disc_playtime_ticks.get(rand_record);
		}
	}

	public void tick() {
		if (worldObj != null && worldObj.getBlockId(x, y, z) == blockDiscoJukebox.id()) {
			if (worldObj.getClosestPlayer(x, y, z, 60) != null && (worldObj.getBlockMetadata(x, y, z) == 0 || discFinished()) && !worldObj.isClientSide) {
				playDisc();
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
			playtime_millisec = 0;
		}
	}

	public void displayTitle() {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER)
			return;

		ItemDiscMusic disc = (ItemDiscMusic) Item.getItem(this.record);
		if (disc == null)
			return;
		String soundPath = disc.recordName;
		String author = disc.recordAuthor;

		if (soundPath != null) {
			if (author != null && !author.isEmpty()) {
				Minecraft.getMinecraft().hudIngame.setRecordPlayingMessage(author + " - " + I18n.getInstance().translateKey(soundPath));
			} else {
				Minecraft.getMinecraft().hudIngame.setRecordPlayingMessage(I18n.getInstance().translateKey(soundPath));
			}
		}
	}

}
