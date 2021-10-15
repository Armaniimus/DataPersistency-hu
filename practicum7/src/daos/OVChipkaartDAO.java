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
        Transaction transaction = this.getTransaction();
        try {
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
        Transaction transaction = this.getTransaction();

        try {
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
        try {
            this.__delete(ovChipkaart);

            this.reizigerDAO.deleteFromOvChipkaart(ovChipkaart.getReiziger());

            List<Product> producten = ovChipkaart.getProduct();
            for (int i = 0; i < producten.size() ; i++) {
                this.productDAO.deleteFromOvChipkaart(producten.get(i));
            }
        } catch(Exception err) {
            System.err.println( err.getMessage() );
        }
    }

    public void deleteFromReiziger(OVChipkaart ovChipkaart) {
        try {
            List<Product> producten = ovChipkaart.getProduct();
            for (int i = 0; i < producten.size() ; i++) {
                producten.get(i).removeOvChipkaart(ovChipkaart);
                this.productDAO.update( producten.get(i) );
            }

            for (int i = 0; i < producten.size() ; i++) {
                ovChipkaart.removeProduct( producten.get(i) );
            }

            this.update(ovChipkaart);
//            this.__delete(ovChipkaart);
        } catch(Exception err) {
            System.err.println( err.getMessage() );
        }
    }

//    public void deleteFromProduct(OVChipkaart ovChipkaart) {
//        try {
////            this.__delete(ovChipkaart);
////            this.reizigerDAO.deleteFromOvChipkaart(ovChipkaart.getReiziger());
//        } catch(Exception err) {
//            System.err.println( err.getMessage() );
//        }
//    }

    private boolean __delete(OVChipkaart ovChipkaart) {
        Transaction transaction = this.getTransaction();
        try {

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
