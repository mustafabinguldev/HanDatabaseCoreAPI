The Minecraft database plugin API is a tool for developers to create plugins that interact with the game's database. It uses Hibernate ORM to simplify database programming and includes classes for creating database connections, executing queries, and retrieving results. With this API, developers can create plugins to store and retrieve game data, such as player statistics and game items.



How to add database?
```
        File xmlConfigFile = new File(getDataFolder(), "exampleConfig.xml");
        HanDatabaseObject databaseObject = new HanDatabaseObject(xmlConfigFile, "skywars", PlayerScore.class);
        HanDatabaseCore.addDatabase(databaseObject);
```


How to add, remove, fetch and update objects from database?
```
        DatabaseHelper<PlayerScore> databaseHelper = getDatabase("skywars").get().getHelper(PlayerScore.class);

        //The first parameter must be 1.
        //Player Name, Kills, Deaths
        
        PlayerScore score = databaseHelper.get(player.getName());
        if (score == null) {

            databaseHelper.create(1, player.getName(), 0, 0);   
            
        }else {
            
            score.kills += 1;
            databaseHelper.update(score);
            
        }
        
        // databaseHelper.delete(score);
```

PlayerScore.class

```
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

//All annotations need to be put.
@Entity 
@Table(name = "scores")
public class PlayerScore {
    
    //Tables of objects that do not contain IDs are not created. 
    //Putting Id in String is a must.
    @Id
    @Column(name = "uuid")
    public String uuid;
    
    @Column(name = "kills")
    public int kills;
    
    @Column(name = "deaths")
    public int deaths;
    
    //Two constructor blocks must be placed, one must contain all variables, 
    //one must be empty, the first constructor block must be empty.
    public PlayerScore() {
        super();
    }
    
    public PlayerScore(String uuid, int kills, int deaths) {
        this.uuid = uuid;
        this.kills = kills;
        this.deaths = deaths;
    }
}
```

exampleConfig.xml
```
<!DOCTYPE hibernate-configuration PUBLIC
        "-//Hibernate/Hibernate Configuration DTD 3.0//EN"
        "http://www.hibernate.org/dtd/hibernate-configuration-3.0.dtd">

<hibernate-configuration>

    <session-factory>

        <!-- JDBC Database connection settings -->
        <property name="connection.driver_class">com.mysql.cj.jdbc.Driver</property>
        <property name="connection.url">jdbc:mysql://localhost:3306/test?useSSL=false&amp;serverTimezone=UTC</property>
        <property name="connection.username">test</property>
        <property name="connection.password">test</property>

        <!-- JDBC connection pool settings -->
        <property name="connection.pool_size">1</property>

        <!-- Select our SQL dialect -->
        <property name="dialect">org.hibernate.dialect.MySQLDialect</property>

        <!-- Echo the SQL to stdout -->
        <property name="show_sql">false</property>

        <!-- Format the SQL -->
        <property name="format_sql">false</property>

        <!-- Set the current session context -->
        <property name="current_session_context_class">thread</property>

        <!-- Handles the entity table changes -->
        <property name="hibernate.hbm2ddl.auto">update</property>


    </session-factory>

</hibernate-configuration>
```

