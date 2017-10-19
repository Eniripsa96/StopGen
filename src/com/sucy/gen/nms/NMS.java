package com.sucy.gen.nms;

import com.sucy.gen.nms.v1_11_R1.NMSManager_111R1;
import com.sucy.gen.nms.v1_12_R1.NMSManager_112R1;
import com.sucy.gen.nms.v1_8_R1.NMSManager_18R1;
import com.sucy.gen.nms.v1_8_R2.NMSManager_18R2;
import com.sucy.gen.nms.v1_8_R3.NMSManager_18R3;
import com.sucy.gen.nms.v1_9_R1.NMSManager_19R1;
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
                else if (pack.equals("v1_11_R1"))
                    manager = new NMSManager_111R1();
                else if (pack.equals("v1_12_R1"))
                    manager = new NMSManager_112R1();
                else
                    Bukkit.getLogger().info(pack);
            }
            else Bukkit.getLogger().info("No match");
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
