package daos;

import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;
import interfaces.OVChipkaartDAOInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAO implements OVChipkaartDAOInterface {
    private Session session;

    public void setSession(Session session) {
        if (this.session == null) {
            this.session = session;
        }
    }

    @Override
    public boolean save(OVChipkaart ovChipkaart) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.persist(ovChipkaart);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public boolean update(OVChipkaart ovChipkaart) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.merge(ovChipkaart);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public boolean delete(OVChipkaart ovChipkaart) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.remove(ovChipkaart);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public ArrayList<OVChipkaart> findAll() {
        return null;
    }

    public OVChipkaart findById(int id) {
        String hql = "FROM OVChipkaart AS A WHERE A.id = :ID";
        Query query = session.createQuery(hql);
        query.setParameter("ID",id);
        List results = query.list();

        if (results.size() == 0) {
            return null;
        }

        return (OVChipkaart) results.get(0);
    }
}
