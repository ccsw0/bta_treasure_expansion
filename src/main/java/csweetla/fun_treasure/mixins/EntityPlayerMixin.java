package csweetla.fun_treasure.mixins;

import net.minecraft.core.achievement.stat.StatList;
import net.minecraft.core.player.gamemode.Gamemode;
import net.minecraft.core.player.inventory.InventoryPlayer;
import net.minecraft.core.world.World;
import net.minecraft.core.entity.player.EntityPlayer;

import csweetla.fun_treasure.FunTreasure;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(value = net.minecraft.core.entity.player.EntityPlayer.class, remap = false)
public abstract class EntityPlayerMixin extends net.minecraft.core.entity.EntityLiving {
	@Shadow
	public InventoryPlayer inventory;

	@Shadow
	public Gamemode gamemode;

	//public const int MAX_FALL

	public EntityPlayerMixin(World world) {super(world);}

	@Unique
	public boolean piston_boots_equipped()
	{
		return this.inventory.armorInventory[0] != null && this.inventory.armorInventory[0].itemID == FunTreasure.armorItemPistonBoots.id;
	}
	/**
	 * @author Csweetla
	 * @reason boosted jump when wearing items that should boost jump
	 */
	@Overwrite
	public void jump() {
		this.yd = 0.42;
		if (piston_boots_equipped()) {
			this.yd = 0.42 * 1.75;
			if (this.gamemode == Gamemode.survival) {
				this.world.playSoundAtEntity(((EntityPlayer) (Object) this), "tile.piston.out", 0.03F, world.rand.nextFloat() * 0.25F + 0.6F);
			}
		}
		((EntityPlayer)(Object)this).addStat(StatList.jumpStat, 1);
	}

	/**
	 * @author csweetla
	 * @reason don't cause fall damage if wearing piston boots and the fall is small
	 */
	@Overwrite
	public void causeFallDamage(float f) {

		if (f >= 2.0F) {
			((EntityPlayer)(Object)this).addStat(StatList.distanceFallenStat, (int)Math.round((double)f * 100.0));
		}
		if (piston_boots_equipped()) {
			if (f < 6.3F){
				return;
			}
			else {
				f -= 4.0F;
			}
		}
		super.causeFallDamage(f);
	}
}
