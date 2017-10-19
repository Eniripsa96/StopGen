package com.sucy.gen;

import com.sucy.gen.nms.NMS;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;

/**
 * Simple plugin stopping chunk generation according to settings
 */
public class StopGen extends JavaPlugin
{
    private static HashMap<String, WorldSettings> settings = new HashMap<String, WorldSettings>();

    private static boolean allChunks;

    /**
     * Enables the plugin, setting up NMS utilities and loading settings
     */
    @Override
    public void onEnable()
    {
        NMS.initialize();
        if (!NMS.isSupported())
        {
            getLogger().severe("Failed to setup NMS - plugin will not work and will be disabled");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        saveDefaultConfig();
        allChunks = getConfig().getBoolean("all-worlds", false);

        // Load settings
        ConfigurationSection data = getConfig().getConfigurationSection("worlds");
        for (String key : data.getKeys(false))
        {
            try
            {
                settings.put(key, new WorldSettings(data.getConfigurationSection(key)));
            }
            catch (Exception ex)
            {
                getLogger().warning("Invalid config settings for world " + key);
            }
        }

        for (World world : getServer().getWorlds())
            if (isEnabled(world))
                NMS.getManager().stopChunks(world);

        getServer().getPluginManager().registerEvents(new WorldListener(), this);
    }

    /**
     * Checks whether or not the world is enabled in the settings
     *
     * @param world world to check
     * @return true if enabled
     */
    public static boolean isEnabled(World world)
    {
        return allChunks || settings.containsKey(world.getName());
    }

    /**
     * Checks if the chunk should generate
     *
     * @param worldName world generating in
     * @param i         chunk X pos
     * @param j         chunk Y pos
     * @return true if should gen, false otherwise
     */
    public static boolean shouldGenerate(String worldName, int i, int j)
    {
        return !allChunks && settings.get(worldName).shouldGenerate(i, j);
    }
}
