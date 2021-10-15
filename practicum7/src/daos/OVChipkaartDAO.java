package daos;

import domain.OVChipkaart;
import domain.Product;
import interfaces.OVChipkaartDAOInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class OVChipkaartDAO implements OVChipkaartDAOInterface {
    private Session session;
    private ReizigerDAO reizigerDAO;
    private ProductDAO productDAO;

    public void setReizigerDAO(ReizigerDAO reizigerDAO) {
        this.reizigerDAO = reizigerDAO;
    }

    public void setProductDAO(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

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
    public List<OVChipkaart> findAll() {
        String hql = "FROM OVChipkaart ";
        Query query = session.createQuery(hql);
        List results = query.list();

        return results;
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
