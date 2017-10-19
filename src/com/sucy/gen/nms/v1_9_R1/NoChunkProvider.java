package com.sucy.gen.nms.v1_9_R1;

import com.sucy.gen.StopGen;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.Server;
import org.bukkit.craftbukkit.v1_9_R1.util.LongHash;
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

            Server server = this.world.getServer();
            if (server != null && !empty)
            {
                server.getPluginManager().callEvent(new ChunkLoadEvent(chunk.bukkitChunk, newChunk));
            }

            for (int x = -2; x < 3; x++)
            {
                for (int z = -2; z < 3; z++)
                {
                    if ((x != 0) || (z != 0))
                    {
                        Chunk neighbor = getLoadedChunkAtWithoutMarkingActive(chunk.locX + x, chunk.locZ + z);
                        if (neighbor != null)
                        {
                            neighbor.setNeighborLoaded(-x, -z);
                            chunk.setNeighborLoaded(x, z);
                        }
                    }
                }
            }
            chunk.loadNearby(this, this.chunkGenerator);
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

