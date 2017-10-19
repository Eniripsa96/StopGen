/**
 * StopGen
 * com.sucy.minenight.nms.v1_9_R1.EmptyChunk
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Steven Sucy
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.sucy.gen.nms.v1_12_R1;

import net.minecraft.server.v1_12_R1.BlockPosition;
import net.minecraft.server.v1_12_R1.Blocks;
import net.minecraft.server.v1_12_R1.Chunk;
import net.minecraft.server.v1_12_R1.ChunkGenerator;
import net.minecraft.server.v1_12_R1.EnumSkyBlock;
import net.minecraft.server.v1_12_R1.IBlockData;
import net.minecraft.server.v1_12_R1.IChunkProvider;
import net.minecraft.server.v1_12_R1.World;

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
    public IBlockData a(int i, int j, int k)
    {
        return Blocks.AIR.getBlockData();
    }

    /**
     * Ignore block changes so players don't build in this zone
     *
     * @param blockPosition block position
     * @param blockData    block data
     *
     * @return null
     */
    @Override
    public IBlockData a(BlockPosition blockPosition, IBlockData blockData)
    {
        return null;
    }

    @Override
    public void a(EnumSkyBlock enumskyblock, BlockPosition blockposition, int i) { }

    /**
     * No need to populate the chunk
     *
     * @param chunkGenerator chunk generator
     */
    @Override
    public void a(ChunkGenerator chunkGenerator) { }

    /**
     * Handles marking loaded flags for neighbors
     *
     * @param iChunkProvider not used
     * @param chunkGenerator not used
     * @param newChunk       not used
     */
    @Override
    public void loadNearby(IChunkProvider iChunkProvider, ChunkGenerator chunkGenerator, boolean newChunk)
    {
        for (int x = -2; x < 3; x++)
        {
            for (int z = -2; z < 3; z++)
            {
                if ((x != 0) || (z != 0))
                {
                    Chunk neighbor = getWorld().getChunkIfLoaded(locX + x, locZ + z);
                    if (neighbor != null)
                    {
                        neighbor.setNeighborLoaded(-x, -z);
                        setNeighborLoaded(x, z);
                    }
                }
            }
        }
    }
}
