package daos;

import domain.Adres;
import interfaces.AdresDAOInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AdresDAO implements AdresDAOInterface {
    private Session session;

    public void setSession(Session session) {
        if (this.session == null) {
            this.session = session;
        }
    }

    @Override
    public boolean save(Adres adres) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.persist(adres);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public boolean update(Adres adres) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.merge(adres);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public boolean delete(Adres adres) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.remove(adres);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    public Adres findById(int id) {
        String hql = "FROM Adres AS A WHERE A.id = :ID";
        Query query = session.createQuery(hql);
        query.setParameter("ID",id);
        List results = query.list();

        if (results.size() == 0) {
            return null;
        }

        return (Adres) results.get(0);
    }

    @Override
    public List<Adres> findAll() {
        String hql = "FROM Adres";
        Query query = session.createQuery(hql);
        List results = query.list();

        return results;
    }
}
