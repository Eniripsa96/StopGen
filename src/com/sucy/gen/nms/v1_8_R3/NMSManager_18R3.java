package com.sucy.gen.nms.v1_8_R3;

import com.sucy.gen.nms.NMSManager;
import net.minecraft.server.v1_8_R3.ChunkProviderServer;
import net.minecraft.server.v1_8_R3.IChunkLoader;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;

import java.lang.reflect.Field;

/**
 * NMS Manager implementation for 1.9
 */
public class NMSManager_18R3
    implements NMSManager
{
    private Field chunkProvider;
    private Field chunkLoader;

    /**
     * Sets up reflection and entity registration
     */
    public NMSManager_18R3()
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
            ChunkProviderServer provider = nmsWorld.chunkProviderServer;

            NoChunkProvider wrapper = new NoChunkProvider(nmsWorld, (IChunkLoader) chunkLoader.get(provider), provider.chunkProvider);
            chunkProvider.set(nmsWorld, wrapper);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }
}
