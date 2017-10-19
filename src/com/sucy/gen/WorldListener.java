package com.sucy.gen;

import com.sucy.gen.nms.NMS;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;

/**
 * Handles listening for new worlds
 */
public class WorldListener implements Listener
{
    /**
     * Applies plugin mechanics when a world is loaded
     *
     * @param event event details
     */
    @EventHandler
    public void onLoad(WorldInitEvent event)
    {
        if (StopGen.isEnabled(event.getWorld()))
            NMS.getManager().stopChunks(event.getWorld());
    }
}
