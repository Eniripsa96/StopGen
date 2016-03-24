package com.sucy.gen.nms;

import org.bukkit.World;

/**
 * Handles tapping into the NMS framework for setting up and using custom entities
 */
public interface NMSManager
{
    /**
     * Stops chunks from generating in a given world
     *
     * @param world world to stop chunks from generating in
     */
    public void stopChunks(World world);
}
