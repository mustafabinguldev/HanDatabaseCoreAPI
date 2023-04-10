package tech.bingulhan.handatabasecore.db;

import org.bukkit.Bukkit;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import tech.bingulhan.handatabasecore.HanDatabaseCore;
import tech.bingulhan.handatabasecore.bll.DatabaseHelper;

import javax.persistence.Column;
import javax.persistence.Table;
import javax.persistence.Id;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author BingulHan
 */
public class HanDatabaseObject {

    private File configurationXmlFile;
    private SessionFactory sessionFactory;

    private List<Class> annotatedClassList;

    private String name;

    public String getName() {
        return this.name;
    }

    public HanDatabaseObject(File configurationXmlFile, List<Class> classes, String name) {
        annotatedClassList = new ArrayList<>();
        this.configurationXmlFile = configurationXmlFile;
        annotatedClassList.addAll(classes);

        this.name = name;
        init();
    }
    public HanDatabaseObject(File configurationXmlFile, String name, Class... clazz) {
        annotatedClassList = new ArrayList<>();
        this.configurationXmlFile = configurationXmlFile;
        Collections.addAll(annotatedClassList, clazz);

        this.name = name;
        init();
    }


    private void init() {
        Configuration configuration = new Configuration().configure(this.configurationXmlFile);
        if (annotatedClassList.size()>0) {
            for (Class tClass : annotatedClassList) {
                configuration.addAnnotatedClass(tClass);
            }
        }

        this.sessionFactory = configuration.buildSessionFactory();

        for (Class tClass : annotatedClassList) {
            Session session = sessionFactory.getCurrentSession();
            session.beginTransaction();

            String tableName = ((Table) tClass.getAnnotation(Table.class)).name();
            String idFieldName = "";
            for (Field field : tClass.getFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    idFieldName = field.getAnnotation(Column.class).name();
                }
            }

            String sql = String.format("CREATE TABLE IF NOT EXISTS`%s` (", tableName);
            sql = String.format(sql+" %s varchar(36) NOT NULL", idFieldName);

            for (Field field : tClass.getFields()) {
                if (!field.getAnnotation(Column.class).name().equals(idFieldName)) {
                    if (field.getType().toString().equals("int")) {
                        sql = String.format(sql+", %s int", field.getAnnotation(Column.class).name());
                    }
                    if (field.getType().toString().equals(String.class.toString())) {
                        sql = String.format(sql+", %s varchar(255) NOT NULL", field.getAnnotation(Column.class).name());
                    }
                }
            }

            sql=  String.format(sql+", PRIMARY KEY (%s));", idFieldName);

            session.createSQLQuery(sql).executeUpdate();
            session.getTransaction().commit();
            session.close();

        }

        Bukkit.getLogger().info("All entities loaded!");
    }


    public final void updateDb() {
        this.sessionFactory.close();
        init();
    }

    public final void addEntity(Class... classes) {
        annotatedClassList.addAll(Arrays.asList(classes));
        updateDb();
    }

    public final void deleteEntity(Class... classes) {
        annotatedClassList.removeAll(Arrays.asList(classes));
        updateDb();
    }

    public DatabaseHelper getHelper(Class clazz) {
        return new DatabaseHelper(this.sessionFactory, clazz);
    }

    public final void close() {
        sessionFactory.close();
    }



}
