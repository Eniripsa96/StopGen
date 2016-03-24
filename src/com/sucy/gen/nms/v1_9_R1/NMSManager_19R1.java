package com.sucy.gen.nms.v1_9_R1;

import com.sucy.gen.nms.NMSManager;
import net.minecraft.server.v1_9_R1.*;
import org.bukkit.craftbukkit.v1_9_R1.CraftWorld;

import java.lang.reflect.Field;

/**
 * NMS Manager implementation for 1.9
 */
public class NMSManager_19R1
    implements NMSManager
{
    private Field chunkProvider;
    private Field chunkLoader;

    /**
     * Sets up reflection and entity registration
     */
    public NMSManager_19R1()
        throws Exception
    {
        chunkProvider = World.class.getDeclaredField("chunkProvider");
        chunkProvider.setAccessible(true);

        chunkLoader = ChunkProviderServer.class.getDeclaredField("chunkLoader");
        chunkLoader.setAccessible(true);
    }

    /**
     * Stops chunks from generating in a given world
     *
     * @param world world to stop chunks from generating in
     */
    public void stopChunks(org.bukkit.World world)
    {
        try
        {
            WorldServer nmsWorld = ((CraftWorld) world).getHandle();
            ChunkProviderServer provider = nmsWorld.getChunkProviderServer();

            NoChunkProvider wrapper = new NoChunkProvider(nmsWorld, (IChunkLoader) chunkLoader.get(provider), provider.chunkGenerator);
            chunkProvider.set(nmsWorld, wrapper);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
