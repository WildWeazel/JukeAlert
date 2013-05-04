package com.untamedears.JukeAlert.manager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.bukkit.Location;
import org.bukkit.World;

import com.untamedears.JukeAlert.JukeAlert;
import com.untamedears.JukeAlert.model.Snitch;
import com.untamedears.JukeAlert.storage.JukeAlertLogger;
import java.util.HashMap;

public class SnitchManager {

    private JukeAlert plugin;
    private JukeAlertLogger logger;
    private Map<World, Map<Location, Snitch>> snitches;
    
    //Stores the highest autoincrement snitch_id number
    private Integer lastSnitchID;

    public SnitchManager() {
        plugin = JukeAlert.getInstance();
        logger = plugin.getJaLogger();
    }

    public void loadSnitches() {
        snitches = logger.getAllSnitches();
    }

    public void saveSnitches() {
        /*for (Entry<World, Map<Location, Snitch>> world : snitches.entrySet()) {
        	for (Entry<Location, Snitch> location : world.entrySet()) {*/

        //TODO: saveSnitches
        //logger.saveAllSnitches();
    }

    public Map<World, Map<Location, Snitch>> getAllSnitches() {
        return snitches;
    }

    public void setSnitches(Map<World, Map<Location, Snitch>> snitches) {
        this.snitches = snitches;
    }

    public List<Snitch> getSnitchesByWorld(World world) {
        return new ArrayList<Snitch>(snitches.get(world).values());
    }

    public Snitch getSnitch(World world, Location location) {
        return snitches.get(world).get(location);
    }

    public void addSnitch(Snitch snitch) {
        World world = snitch.getLoc().getWorld();
        if(snitches.get(world) == null) {
        	Map<Location, Snitch> map = new HashMap<Location, Snitch>();
        	map.put(snitch.getLoc(), snitch);
        	snitches.put(snitch.getLoc().getWorld(), map);
        } else {
            snitches.get(world).put(snitch.getLoc(), snitch);
        }
    }
	
    public void removeSnitch(Snitch snitch) {
        snitches.get(snitch.getLoc().getWorld()).remove(snitch.getLoc());
        plugin.getJaLogger().logSnitchBreak(snitch.getLoc().getWorld().getName(), snitch.getLoc().getBlockX(), snitch.getLoc().getBlockY(), snitch.getLoc().getBlockZ());
    }
}
