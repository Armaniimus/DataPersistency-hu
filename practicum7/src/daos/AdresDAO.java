package daos;

import domain.Adres;
import interfaces.AdresDAOInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AdresDAO implements AdresDAOInterface {
    private Session session;
    private ReizigerDAO reizigerDAO;

    public void setReizigerDAO(ReizigerDAO reizigerDAO) {
        this.reizigerDAO = reizigerDAO;
    }

    public void setSession(Session session) {
        if (this.session == null) {
            this.session = session;
        }
    }

    private Transaction getTransaction() {
        Transaction transaction = session.getTransaction();
        try {
            if (!transaction.isActive()) {
                transaction = session.beginTransaction();
            }
        } catch(Exception err) {
            System.out.println(err.getMessage());
        }

        return transaction;
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
    public void delete(Adres adres) {
        if (adres != null) {
            this.reizigerDAO.deleteFromAdres(adres.getReiziger());
        }
    }

    public void deleteFromReiziger(Adres adres) {
        if (adres != null) {
            this.__deleteOne(adres);
        }
    }

    private void __deleteOne(Adres adres) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.remove(adres);
            transaction.commit();

        } catch(Exception e) {
            System.err.println( e.getMessage() );
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
