package fr.enzias.easyscore.scoreboard;

import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreBoardManager {

    private static final Map<UUID, BukkitRunnable> tasks = new HashMap<UUID, BukkitRunnable>();
    private final UUID uuid;
    public ScoreBoardManager(UUID uuid) {
        this.uuid = uuid;
    }

    public void setID(BukkitRunnable runnable){
        tasks.put(uuid,runnable);
    }

    public BukkitRunnable getID(){
        return tasks.get(uuid);
    }

    public boolean hasID(){
        return tasks.containsKey(uuid);
    }

    public void stop(){

        (tasks.get(uuid)).cancel();
        tasks.remove(uuid);
    }

}
