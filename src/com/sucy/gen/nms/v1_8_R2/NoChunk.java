package com.sucy.gen.nms.v1_8_R2;

import net.minecraft.server.v1_8_R2.*;

/**
 * Represents a chunk that wasn't generated and won't be saved
 */
public class NoChunk extends Chunk
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
    public Block getTypeAbs(int i, int j, int k)
    {
        return Blocks.AIR;
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
}
