package csweetla.treasure_expansion.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogicJukebox;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import org.jetbrains.annotations.Nullable;


public class BlockLogicDiscoJukebox extends BlockLogicJukebox {
	public BlockLogicDiscoJukebox(Block<?> block) {
		super(block);
		block.withEntity(TileEntityJukeboxDisco::new);
	}

	@Nullable
	@Override
	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(Blocks.JUKEBOX)};
	}

}
