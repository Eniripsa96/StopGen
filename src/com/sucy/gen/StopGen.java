/**
 * StopGen
 * com.sucy.gen.StopGen
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
