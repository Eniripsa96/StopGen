/**
 * StopGen
 * com.sucy.gen.WorldSettings
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
            case GRID:
                return ((i -x) % 10) + ((j - z) % 10) == 0;
        }
        return true;
    }
}
