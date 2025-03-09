/*
Copyright © 2025 https://github.com/Fabii08?tab=repositories  
All rights reserved.  
*/
package de.fabi.morphPlugin;


import org.bukkit.plugin.java.JavaPlugin;

public class MorphPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        getCommand("morph").setExecutor(new MorphCommand(this));
        getServer().getPluginManager().registerEvents(new MorphGUIListener(this), this);
        getServer().getPluginManager().registerEvents(new MorphResetListener(this), this);
        ItemPermissionListener listener = new ItemPermissionListener();
        getServer().getPluginManager().registerEvents(listener, this);
        getCommand("morphitems").setExecutor(new MorphItemsCommand(listener.getMorphItems()));


    }


    public static String Prefix() {
        return "<gradient:#30D5C8:#13F771>ParadiseNetwork</gradient> <gray>|</gray> ";
    }
}
