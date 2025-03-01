package csweetla.treasure_expansion.world_features;

import net.minecraft.core.block.BlockLogicChest;
import net.minecraft.core.block.Blocks;
import net.minecraft.core.block.entity.TileEntityChest;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.item.Items;
import net.minecraft.core.world.World;
import net.minecraft.core.world.generate.feature.WorldFeatureDungeon;

import java.util.Objects;
import java.util.Random;

import static csweetla.treasure_expansion.TreasureExpansion.blockDiscoJukebox;

public class WorldFeatureDiscoDungeon extends WorldFeatureDungeon {
	public WorldFeatureDiscoDungeon() {
		super(Blocks.BRICK_CLAY.id(), Blocks.BRICK_CLAY.id(), null);
	}


	@Override
	public boolean place(World world, Random random, int x, int y, int z) {
		boolean has_ceiling = random.nextBoolean();
		int height = has_ceiling ? 4 : 3;
		int width = 2 + random.nextInt(2);
		int length = 2 + random.nextInt(2);
		int j1 = 0;


		for (int k1 = x - width - 1; k1 <= x + width + 1; k1++) {
			for (int j2 = y - 1; j2 <= y + height + 1; j2++) {
				for (int i3 = z - length - 1; i3 <= z + length + 1; i3++) {
					Material material = world.getBlockMaterial(k1, j2, i3);
					if (j2 == y - 1 && !material.isSolid()) {
						return false;
					}

					if (j2 == y + height + 1 && !material.isSolid()) {
						return false;
					}

					if ((k1 == x - width - 1 || k1 == x + width + 1 || i3 == z - length - 1 || i3 == z + length + 1)
						&& j2 == y
						&& world.isAirBlock(k1, j2, i3)
						&& world.isAirBlock(k1, j2 + 1, i3)) {
						j1++;
					}
				}
			}
		}

		if (j1 >= 1 && j1 <= 5) {
			for (int l1 = x - width - 1; l1 <= x + width + 1; l1++) {
				for (int k2 = y + height; k2 >= y - 1; k2--) {
					for (int j3 = z - length - 1; j3 <= z + length + 1; j3++) {
						if (has_ceiling && k2 == y + height) {
							world.setBlockWithNotify(l1, k2, j3, Blocks.GLOWSTONE.id());
						} else if (l1 != x - width - 1 && k2 != y - 1 && j3 != z - length - 1 && l1 != x + width + 1 && k2 != y + height + 1 && j3 != z + length + 1) {
							world.setBlockWithNotify(l1, k2, j3, 0);
						} else if (k2 >= 0 && !world.getBlockMaterial(l1, k2 - 1, j3).isSolid()) {
							world.setBlockWithNotify(l1, k2, j3, 0);
						} else if (world.getBlockMaterial(l1, k2, j3).isSolid()) {
							if (k2 == y - 1 /*&& random.nextInt(4) != 0*/) {
								world.setBlockAndMetadataWithNotify(l1, k2, j3, Blocks.WOOL.id(), random.nextInt(16));
							} else {
								world.setBlockWithNotify(l1, k2, j3, this.blockIdWalls);
							}
						}
					}
				}
			}

			for (int i2 = 0; i2 < 2; i2++) {
				for (int l2 = 0; l2 < 3; l2++) {
					int k3 = x + random.nextInt(width * 2 + 1) - width;
					int i4 = z + random.nextInt(length * 2 + 1) - length;
					if (world.isAirBlock(k3, y, i4)) {
						int j4 = 0;
						if (world.getBlockMaterial(k3 - 1, y, i4).isSolid()) {
							j4++;
						}

						if (world.getBlockMaterial(k3 + 1, y, i4).isSolid()) {
							j4++;
						}

						if (world.getBlockMaterial(k3, y, i4 - 1).isSolid()) {
							j4++;
						}

						if (world.getBlockMaterial(k3, y, i4 + 1).isSolid()) {
							j4++;
						}

						if (j4 == 1) {
							world.setBlockWithNotify(k3, y, i4, Blocks.CHEST_PLANKS_OAK.id());
							BlockLogicChest.setDefaultDirection(world, k3, y, i4);
							TileEntityChest tileentitychest = (TileEntityChest) world.getTileEntity(k3, y, i4);

							for (int k4 = 0; k4 < 8; k4++) {
								ItemStack itemstack = new ItemStack(new ItemStack(Objects.requireNonNull(Item.itemsList[Items.RECORD_13.id + random.nextInt(12)])));
								tileentitychest.setItem(random.nextInt(tileentitychest.getContainerSize()), itemstack);
							}
							break;
						}
					}
				}
			}

			world.setBlockWithNotify(x, y, z, blockDiscoJukebox.id());
//			System.out.println("New Disco Dungeon: " + x + " " + y + " " + z);
			return true;
		} else {
			return false;
		}
	}


}
