package csweetla.treasure_expansion.entity;

import net.minecraft.core.WeightedRandomLootObject;
import net.minecraft.core.entity.Entity;
import net.minecraft.core.entity.monster.MobZombie;
import net.minecraft.core.entity.player.Player;
import net.minecraft.core.item.Items;
import net.minecraft.core.util.collection.NamespaceID;
import net.minecraft.core.util.helper.DamageType;
import net.minecraft.core.util.helper.MathHelper;
import net.minecraft.core.world.World;
import net.minecraft.core.world.biome.Biome;
import net.minecraft.core.world.biome.Biomes;
import net.minecraft.core.world.season.Seasons;
import net.minecraft.core.world.weather.Weathers;
import org.jetbrains.annotations.NotNull;

import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class EntityVampire extends MobZombie {

	public EntityVampire(World world) {
		super(world);
		this.textureIdentifier = NamespaceID.getPermanent(MOD_ID, "vampire");
		this.mobDrops.clear();
		this.mobDrops.add(new WeightedRandomLootObject(Items.QUARTZ.getDefaultStack(),3,4));
		this.scoreValue = 1200;
	}

	private float easeInOutCubic(float x) {
		return x < 0.5 ? 4 * x * x * x : (float) (1 - Math.pow(-2 * x + 2, 3) / 2);
	}

	public float getAlpha() {
        if (this.world == null)
			return 1.0f;

		double closestDistance = Double.POSITIVE_INFINITY;

        for (Player p : this.world.players) {
			double currentDistance = p.distanceToSqr(this.x, this.y, this.z);
			if (currentDistance < closestDistance) {
				closestDistance = currentDistance;
			}
		}



		float player_closeness = (64f - (float) MathHelper.clamp(closestDistance, 0.0d, 64d)) / 64f;
		float player_closeness_eased = (-(MathHelper.cos(3.145f * player_closeness) - 1) / 2);
		float pos_brightness = (world.getBlockLightValue((int)x,(int)y + 1,(int)z) / 15.0f);

        return (float) Math.max(player_closeness_eased,easeInOutCubic(pos_brightness));
	}

	@Override
	public int getMaxHealth() {
		return 25;
	}

	@Override
	public void onLivingUpdate() {
		this.moveSpeed = Math.max(0.5f, 1.0f - getAlpha());

		super.onLivingUpdate();
	}


	@Override
	public boolean canSpawnHere() {
		if (this.world == null)
			return false;

		int x = (int)this.x;
		int y = (int)this.y;
		int z = (int)this.z;

        if (!super.canSpawnHere()) return false;

		Biome biome = this.world.getBlockBiome(x,y,z);
        return biome == Biomes.OVERWORLD_HELL || biome == Biomes.NETHER_NETHER || (world.canBlockSeeTheSky(x, y, z) && (this.world.getSeasonManager().getCurrentSeason() == Seasons.OVERWORLD_FALL || this.world.getCurrentWeather() == Weathers.OVERWORLD_FOG));
	}

	@Override
	public int getMaxSpawnedInChunk() {
		return 2;
	}

	@Override
	protected void attackEntity(@NotNull Entity entity, float distance) {
		if (this.attackTime <= 0 && distance < 2.0F && entity.bb.maxY > this.bb.minY && entity.bb.minY < this.bb.maxY) {
			this.attackTime = 20;
			entity.hurt(this, this.attackStrength, DamageType.COMBAT);
			this.heal(2);
		}
	}

	@Override
	public boolean hurt(Entity attacker, int i, DamageType type) {
		if (super.hurt(attacker, i, type)) {
			if (this.passenger != attacker && this.vehicle != attacker) {
				if (attacker != this) {
					this.target = attacker;
				}

				return true;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}
}
