package csweetla.treasure_expansion.mixins;

import csweetla.treasure_expansion.ModAchievements;
import csweetla.treasure_expansion.ModItemTags;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockLogic;
import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.player.inventory.container.Container;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static net.minecraft.core.block.BlockLogicChest.getInventory;

@Mixin(value = BlockLogicChest.class, remap = false)
public abstract class BlockLogicChestMixin extends BlockLogic {

	public BlockLogicChestMixin(Block<?> block, Material material) {
		super(block, material);
	}

	@Inject( method = "onBlockRightClicked", at = @At("RETURN"))
	public void earnAchievements(World world, int x, int y, int z, Player player, Side side, double xPlaced, double yPlaced, CallbackInfoReturnable<Boolean> cir) {
		Container c = getInventory(world, x, y, z);
		if (cir.getReturnValue() == true && c != null ) {
			for (int i = 0; i < c.getContainerSize(); i++) {
				ItemStack it = c.getItem(i);
				if (it instanceof ItemStack && ModItemTags.earnsTreasureAchievement.appliesTo(it.getItem()))
					player.triggerAchievement(ModAchievements.FIND_TREASURE);
			}
		}
	}
}
