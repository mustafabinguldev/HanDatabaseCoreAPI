package tech.bingulhan.handatabasecore;

import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import tech.bingulhan.handatabasecore.bll.DatabaseHelper;
import tech.bingulhan.handatabasecore.db.HanDatabaseObject;

import java.io.File;
import java.util.HashSet;
import java.util.Optional;

/***
 * @author BingulHan
 */
public final class HanDatabaseCore extends JavaPlugin implements Listener {

    private static HashSet<HanDatabaseObject> databases;

    public static void addDatabase(HanDatabaseObject database) {
        databases.add(database);
    }

    public static HashSet<HanDatabaseObject> getDatabases() {
        return databases;
    }

    public static Optional<HanDatabaseObject> getDatabase(String name) {
        return databases.stream().filter(database -> database.getName().equals(name)).findAny();
    }


    @Override
    public void onEnable() {

        this.databases = new HashSet<>();
        saveResource("exampleConfig.xml", false);

    }

    @Override
    public void onDisable() {
        for (HanDatabaseObject database : databases) {
            database.close();
        }
    }

}
