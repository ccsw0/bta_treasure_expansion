package csweetla.treasure_expansion.block;

import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.BlockChestPainted;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.util.helper.Side;
import net.minecraft.core.world.WorldSource;

import turniplabs.halplibe.helper.TextureHelper;
import static csweetla.treasure_expansion.TreasureExpansion.MOD_ID;

public class DungeonChestBlock extends BlockChest {
	public int[] textures = new int[7];
	public DungeonChestBlock(String key, int id, Material material, String texture_name) {
		super(key, id, material);

		textures[0] = TextureHelper.getOrCreateBlockTextureIndex(MOD_ID, texture_name + "_top.png");
		textures[1] = TextureHelper.getOrCreateBlockTextureIndex(MOD_ID, texture_name + "_side.png");
		textures[2] = TextureHelper.getOrCreateBlockTextureIndex(MOD_ID, texture_name + "_front.png");
		textures[3] = TextureHelper.getOrCreateBlockTextureIndex(MOD_ID, texture_name + "_frontleft.png");
		textures[4] = TextureHelper.getOrCreateBlockTextureIndex(MOD_ID, texture_name + "_frontright.png");
		textures[5] = TextureHelper.getOrCreateBlockTextureIndex(MOD_ID, texture_name + "_backleft.png");
		textures[6] = TextureHelper.getOrCreateBlockTextureIndex(MOD_ID, texture_name + "_backright.png");
	}

	@Override
	public int getBlockTexture(WorldSource blockAccess, int x, int y, int z, Side side) {
		int meta = blockAccess.getBlockMetadata(x, y, z);
		return indexFromSideAndMeta(side, meta);
	}
	@Override
	public int getBlockTextureFromSideAndMetadata(Side side, int meta) {
		return indexFromSideAndMeta(side, meta & 0xF0 | 0b0000_0010);
	}

	private int indexFromSideAndMeta(Side side, int meta){
		int rotation = meta & 0b00000011;
		Type type = BlockChestPainted.getTypeFromMeta(meta);
		int index;
		int sideNum = 0;
		if (side == Side.TOP || side == Side.BOTTOM){
			index = 0;
		} else {
			switch (side){
				case NORTH:
					sideNum = 0;
					break;
				case EAST:
					sideNum = 3;
					break;
				case SOUTH:
					sideNum = 2;
					break;
				case WEST:
					sideNum = 1;
					break;
			}
			int prod = (sideNum + rotation)%4;
			if (prod == 0){
				if (type == Type.SINGLE){
					index = 2;
				} else if (type == Type.LEFT){
					index = 3;
				} else {
					index = 4;
				}
			} else if (prod == 2) {
				if (type == Type.SINGLE){
					index = 1;
				} else if (type == Type.LEFT){
					index = 6;
				} else {
					index = 5;
				}
			}else {
				index = 1;
			}
		}
		return textures[index];
	}
}
