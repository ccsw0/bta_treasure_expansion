package csweetla.treasure_expansion.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicJukebox;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.entity.TileEntityActivator;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Direction;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;


public class BlockLogicDiscoJukebox extends BlockLogicJukebox {
	public BlockLogicDiscoJukebox(Block<?> block) {
		super(block);
		block.withEntity(TileEntityJukeboxDisco::new);
	}

	@Override
	public boolean onBlockRightClicked(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced) {
		TileEntity te = world.getTileEntity(x,y,z);
		if (te instanceof TileEntityJukeboxDisco) {
			TileEntityJukeboxDisco jukeboxEnt =  (TileEntityJukeboxDisco) te;
			jukeboxEnt.displayTitle();
		}

		return true;
	}

	@Override
	public void onActivatorInteract(World world, int x, int y, int z, TileEntityActivator activator, Direction direction) {
	}


	@Nullable
	@Override
	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(Blocks.JUKEBOX)};
	}

}
