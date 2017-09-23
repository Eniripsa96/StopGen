/**
 * StopGen
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
package com.sucy.gen.nms.v1_12_R1;

import com.sucy.gen.StopGen;
import net.minecraft.server.v1_12_R1.Chunk;
import net.minecraft.server.v1_12_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_12_R1.ChunkGenerator;
import net.minecraft.server.v1_12_R1.ChunkProviderServer;
import net.minecraft.server.v1_12_R1.CrashReport;
import net.minecraft.server.v1_12_R1.CrashReportSystemDetails;
import net.minecraft.server.v1_12_R1.IChunkLoader;
import net.minecraft.server.v1_12_R1.ReportedException;
import net.minecraft.server.v1_12_R1.WorldServer;

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
    public NoChunkProvider(WorldServer worldserver, IChunkLoader ichunkloader, ChunkGenerator chunkgenerator)
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
        Chunk chunk = originalGetOrLoadChunkAt(i, j);
        if (chunk == null)
        {
            chunk = loadChunk(i, j);
            if (chunk == null)
            {
                if (StopGen.shouldGenerate(world.getWorld().getName(), i, j))
                {
                    try
                    {
                        chunk = this.chunkGenerator.getOrCreateChunk(i, j);
                    }
                    catch (Throwable throwable)
                    {
                        CrashReport crashreport = CrashReport.a(throwable, "Exception generating new chunk");
                        CrashReportSystemDetails crashreportsystemdetails = crashreport.a("Chunk to be generated");

                        crashreportsystemdetails.a("Location", String.format("%d,%d", i, j));
                        crashreportsystemdetails.a("Position hash", ChunkCoordIntPair.a(i, j));
                        crashreportsystemdetails.a("Generator", this.chunkGenerator);
                        throw new ReportedException(crashreport);
                    }
                }
                else
                    chunk = new NoChunk(world, i, j);
            }

            this.chunks.put(ChunkCoordIntPair.a(i, j), chunk);
            chunk.addEntities();
            chunk.loadNearby(this, this.chunkGenerator, true);
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

