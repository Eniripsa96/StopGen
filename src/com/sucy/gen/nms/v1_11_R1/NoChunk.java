package com.sucy.gen.nms.v1_11_R1;

import net.minecraft.server.v1_11_R1.BlockPosition;
import net.minecraft.server.v1_11_R1.Blocks;
import net.minecraft.server.v1_11_R1.EnumSkyBlock;
import net.minecraft.server.v1_11_R1.IBlockData;
import net.minecraft.server.v1_11_R1.World;

/**
 * Represents a chunk that wasn't generated and won't be saved
 */
public class NoChunk extends net.minecraft.server.v1_11_R1.Chunk
{
    /**
     * Initializes the chunk
     *
     * @param world world references
     * @param i     chunk X pos
     * @param j     chunk Z pos
     */
    public NoChunk(World world, int i, int j)
    {
        super(world, i, j);
    }

    /**
     * All blocks are air anyway, so make it faster by just returning that
     *
     * @param i block X pos
     * @param j block Y pos
     * @param k block Z pos
     *
     * @return AIR
     */
    @Override
    public IBlockData a(int i, int j, int k)
    {
        return Blocks.AIR.getBlockData();
    }

    /**
     * Ignore block changes so players don't build in this zone
     *
     * @param blockposition block position
     * @param iblockdata    block data
     *
     * @return null
     */
    @Override
    public IBlockData a(BlockPosition blockposition, IBlockData iblockdata)
    {
        return null;
    }

    @Override
    public void a(EnumSkyBlock enumskyblock, BlockPosition blockposition, int i) { }
}
