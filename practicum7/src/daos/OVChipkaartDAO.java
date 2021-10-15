package daos;

import domain.OVChipkaart;
import domain.Product;
import interfaces.OVChipkaartDAOInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
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
    public void delete(OVChipkaart ovChipkaart) {
        if (ovChipkaart.getReiziger() != null) {
            this.reizigerDAO.deleteFromOvChipkaart(ovChipkaart.getReiziger());
        } else {
            this.deleteAndDecouple(ovChipkaart);
        }
    }

    private void deleteAndDecouple(OVChipkaart ovChipkaart) {
        this.__decoupleProducts(ovChipkaart);
        this.__deleteOne(ovChipkaart);
    }

    private void __decoupleProducts(OVChipkaart ovChipkaart) {
        List<Product> producten = ovChipkaart.getProduct();
        for (int i = 0; i < producten.size(); i++) {
            producten.get(i).removeOneOVChipkaart(ovChipkaart);
            this.productDAO.update(producten.get(i));
        }

        ArrayList producten2 = new ArrayList();
        ovChipkaart.setProductList(producten2, false);

        this.update(ovChipkaart);
    }

    public void deleteFromReiziger(OVChipkaart ovChipkaart) {
        this.deleteAndDecouple(ovChipkaart);
    }

    private void __deleteOne(OVChipkaart ovChipkaart) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.remove(ovChipkaart);
            transaction.commit();

        } catch(Exception e) {
            System.err.println( e.getMessage() );
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
