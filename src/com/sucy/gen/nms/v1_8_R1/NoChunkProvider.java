/**
 * MineNight
 * com.sucy.minenight.nms.v1_9_R1.NoChunkProvider
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
package com.sucy.gen.nms.v1_8_R1;

import com.sucy.gen.StopGen;
import net.minecraft.server.v1_8_R1.*;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_8_R1.util.LongHash;
import org.bukkit.event.world.ChunkLoadEvent;

/**
 * A modified chunk provider that prevents the generation of chunks
 */
public class NoChunkProvider
    extends ChunkProviderServer
{
    /**
     * Makes a new chunk provider
     *
     * @param worldserver    world reference
     * @param ichunkloader   chunk loader from normal provider
     * @param chunkgenerator chunk generator from normal provider
     */
    public NoChunkProvider(WorldServer worldserver, IChunkLoader ichunkloader, IChunkProvider chunkgenerator)
    {
        super(worldserver, ichunkloader, chunkgenerator);
    }

    /**
     * Changes the normal chunk loading to just grab a "NoChunk" object if
     * a new chunk needed to be generated
     *
     * @param i chunk X pos
     * @param j chunk Y pos
     *
     * @return loaded chunk
     */
    @Override
    public Chunk originalGetChunkAt(int i, int j)
    {
        this.unloadQueue.remove(i, j);
        Chunk chunk = this.chunks.get(LongHash.toLong(i, j));
        if (chunk == null) {
            this.world.timings.syncChunkLoadTimer.startTiming();
            chunk = loadChunk(i, j);
            boolean newChunk = false;
            boolean empty = false;

            if (chunk == null) {
                if (this.chunkProvider == null)
                    chunk = this.emptyChunk;
                else if (StopGen.shouldGenerate(world.getWorld().getName(), i, j))
                {
                    try {
                        chunk = this.chunkProvider.getOrCreateChunk(i, j);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.a(throwable, "Exception generating new chunk");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Chunk to be generated");

                        crashreportsystemdetails.a("Location", String.format("%d,%d", i, j));
                        crashreportsystemdetails.a("Position hash", LongHash.toLong(i, j));
                        crashreportsystemdetails.a("Generator", this.chunkProvider.getName());
                        throw new ReportedException(crashreport);
                    }
                }
                else
                {
                    chunk = new NoChunk(world, i, j);
                    empty = true;
                }
                newChunk = true;
            }

            this.chunks.put(LongHash.toLong(i, j), chunk);
            chunk.addEntities();

            Server server = this.world.getServer();
            if (server != null && !empty)
            {
                server.getPluginManager().callEvent(new ChunkLoadEvent(chunk.bukkitChunk, newChunk));
            }

            for (int x = -2; x < 3; x++) {
                for (int z = -2; z < 3; z++) {
                    if ((x != 0) || (z != 0))
                    {
                        Chunk neighbor = getChunkIfLoaded(chunk.locX + x, chunk.locZ + z);
                        if (neighbor != null) {
                            neighbor.setNeighborLoaded(-x, -z);
                            chunk.setNeighborLoaded(x, z);
                        }
                    }
                }
            }

            chunk.loadNearby(this, this, i, j);
            this.world.timings.syncChunkLoadTimer.stopTiming();
        }

        return chunk;
    }

    /**
     * Saves a chunk that isn't an empty chunk
     *
     * @param chunk chunk to save
     */
    @Override
    public void saveChunk(Chunk chunk)
    {
        if (!(chunk instanceof NoChunk))
            super.saveChunk(chunk);
    }
}

