package daos;

import domain.OVChipkaart;
import domain.Product;
import interfaces.ProductDAOInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class ProductDAO implements ProductDAOInterface {
    private Session session;
    private OVChipkaartDAO ovChipkaartDAO;

    public void setOvChipkaartDAO(OVChipkaartDAO ovChipkaartDAO) {
        this.ovChipkaartDAO = ovChipkaartDAO;
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
    public boolean save( Product product) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.persist(product);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public boolean update( Product product) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.merge(product);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public void delete( Product product ) {

        this.__deleteOne(product);
    }

    private void deleteAndDecouple(Product product) {
        this.__decoupleProducts(product);
        this.__deleteOne(product);
    }

    private void __decoupleProducts(Product product) {
        List<OVChipkaart> ovList = product.getOvChipkaart();
        for (int i = 0; i < ovList.size(); i++) {
            ovList.get(i).removeOneProduct(product);
            this.ovChipkaartDAO.update(ovList.get(i));
        }

        ArrayList ovList2 = new ArrayList();
        product.setOvChipkaartList(ovList2, false);

        this.update(product);
    }

    private void __deleteOne( Product product ) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.remove(product);
            transaction.commit();

        } catch(Exception e) {
            System.err.println( e.getMessage() );
        }
    }

    @Override
    public List<Product> findAll() {
        String hql = "FROM Product";
        Query query = session.createQuery(hql);
        List results = query.list();

        return results;
    }

    public Product findById(int id) {
        String hql = "FROM Product AS A WHERE A.id = :ID";
        Query query = session.createQuery(hql);
        query.setParameter("ID",id);
        List results = query.list();

        if (results.size() == 0) {
            return null;
        }

        return (Product) results.get(0);
    }
}
