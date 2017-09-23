/**
 * StopGen
 * com.sucy.minenight.nms.NMS
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
package com.sucy.gen.nms;

import com.sucy.gen.nms.v1_10_R1.NMSManager_110R1;
import com.sucy.gen.nms.v1_11_R1.NMSManager_111R1;
import com.sucy.gen.nms.v1_12_R1.NMSManager_112R1;
import com.sucy.gen.nms.v1_8_R1.NMSManager_18R1;
import com.sucy.gen.nms.v1_8_R2.NMSManager_18R2;
import com.sucy.gen.nms.v1_8_R3.NMSManager_18R3;
import com.sucy.gen.nms.v1_9_R1.NMSManager_19R1;
import com.sucy.gen.nms.v1_9_R2.NMSManager_19R2;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles setting up and grabbing the manager for NMS functions
 */
public class NMS
{
    private static NMSManager manager;

    /**
     * Initializes the NMS functions
     */
    public static void initialize()
    {
        try
        {
            Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
            if (matcher.find()) {
                String pack = matcher.group();
                if (pack.equals("v1_8_R1"))
                    manager = new NMSManager_18R1();
                else if (pack.equals("v1_8_R2"))
                    manager = new NMSManager_18R2();
                else if (pack.equals("v1_8_R3"))
                    manager = new NMSManager_18R3();
                else if (pack.equals("v1_9_R1"))
                    manager = new NMSManager_19R1();
                else if (pack.equals("v1_9_R2"))
                    manager = new NMSManager_19R2();
                else if (pack.equals("v1_10_R1"))
                    manager = new NMSManager_110R1();
                else if (pack.equals("v1_11_R1"))
                    manager = new NMSManager_111R1();
                else if (pack.equals("v1_12_R1"))
                    manager = new NMSManager_112R1();
                else
                    Bukkit.getLogger().info("StopGen doesn't support the version " + pack);
            }
            else Bukkit.getLogger().info("Unable to determine the server version");
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    /**
     * Checks whether or not NMS functions are supported
     *
     * @return true if supported, false otherwise
     */
    public static boolean isSupported()
    {
        return manager != null;
    }

    /**
     * Retrieves the active manager for NMS classes
     *
     * @return NMS manager
     */
    public static NMSManager getManager()
    {
        return manager;
    }
}
