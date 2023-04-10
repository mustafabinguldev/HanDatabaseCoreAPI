package tech.bingulhan.handatabasecore.bll;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author BingulHan
 * @param <T>
 */
public class DatabaseHelper<T> {

    private SessionFactory sessionFactory;

    private Class<?> clazz;

    public DatabaseHelper(SessionFactory sessionFactory, Class<T> clazz) {
        this.sessionFactory = sessionFactory;
        this.clazz = clazz;
    }

    public T get(String id) {

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        T result = (T) session.get(this.clazz, id);

        session.getTransaction().commit();
        session.close();
        return result;

    }

    public T create(int constId, Object... obs) throws SecurityException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {

        try {

            Session session = this.sessionFactory.openSession();
            session.beginTransaction();

            T object = (T) clazz.getConstructors()[constId].newInstance(obs);

            session.save(object);
            session.getTransaction().commit();
            session.close();

        }catch (Exception exception) {
            exception.printStackTrace();
        }


        return null;

    }

    public void update(T object) {

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        session.update(object);

        session.getTransaction().commit();
        session.close();

    }

    public void delete(T object) {

        Session session = this.sessionFactory.openSession();
        session.beginTransaction();

        session.delete(object);

        session.getTransaction().commit();
        session.close();

    }

    public boolean isPresent(String idKey) {
        if (get(idKey) == null) {
            return false;
        }

        return true;

    }

}
