package com.sucy.gen;

import org.bukkit.configuration.ConfigurationSection;

/**
 * Settings for an individual world
 */
public class WorldSettings
{
    private ShapeType type;

    private double width, height;
    private double x, z;

    /**
     * Loads settings from config data
     *
     * @param config data to load from
     * @throws Exception
     */
    public WorldSettings(ConfigurationSection config)
        throws Exception
    {
        type = ShapeType.valueOf(config.getString("type").toUpperCase());

        // Extra options for shapes
        if (type != ShapeType.ALL)
        {
            String[] size = config.getString("size").split(", ");
            String[] center = config.getString("center").split(", ");

            width = Integer.parseInt(size[0]) / 16.0;
            height = Integer.parseInt(size[1]) / 16.0;

            x = Integer.parseInt(center[0]) / 16.0;
            z = Integer.parseInt(center[1]) / 16.0;
        }
    }

    /**
     * Checks if a chunk should generate at the given location
     *
     * @param i chunk X coordinate
     * @param j chunk Z coordinate
     * @return true if should generate, false otherwise
     */
    public boolean shouldGenerate(int i, int j)
    {
        switch (type)
        {
            case ALL:
                return false;
            case BOX:
                return StrictMath.abs(i - x) <= width && StrictMath.abs(j - z) <= height;
            case RADIUS:
                double dx = i - x;
                double dz = j - z;
                return (dx * dx) / (width * width) + (dz * dz) / (height * height) <= 1;
        }
        return true;
    }
}
