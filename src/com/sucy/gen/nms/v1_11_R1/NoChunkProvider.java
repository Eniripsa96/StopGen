package com.sucy.gen.nms.v1_11_R1;

import com.sucy.gen.StopGen;
import net.minecraft.server.v1_11_R1.Chunk;
import net.minecraft.server.v1_11_R1.ChunkCoordIntPair;
import net.minecraft.server.v1_11_R1.ChunkGenerator;
import net.minecraft.server.v1_11_R1.ChunkProviderServer;
import net.minecraft.server.v1_11_R1.CrashReport;
import net.minecraft.server.v1_11_R1.CrashReportSystemDetails;
import net.minecraft.server.v1_11_R1.IChunkLoader;
import net.minecraft.server.v1_11_R1.ReportedException;
import net.minecraft.server.v1_11_R1.WorldServer;
import org.bukkit.craftbukkit.v1_11_R1.util.LongHash;

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
        Chunk chunk = getOrLoadChunkAt(i, j);

        if (chunk == null)
        {
            chunk = loadChunk(i, j);
            boolean newChunk = false;
            boolean empty = false;
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
                    newChunk = true;
                }
                else
                {
                    chunk = new NoChunk(world, i, j);
                    empty = true;
                }
            }

            this.chunks.put(LongHash.toLong(i, j), chunk);
            chunk.addEntities();
            chunk.loadNearby(this, chunkGenerator, newChunk);
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

