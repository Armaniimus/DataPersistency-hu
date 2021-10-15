package daos;


import domain.OVChipkaart;
import domain.Reiziger;
import interfaces.ReizigerDAOInterface;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ReizigerDAO implements ReizigerDAOInterface {
    private Session session;
    private AdresDAO adresDAO;
    private OVChipkaartDAO ovChipkaartDAO;

    public void setAdresDAO(AdresDAO adresDAO) {
        this.adresDAO = adresDAO;
    }

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
    public boolean save(Reiziger reiziger) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.persist(reiziger);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public boolean update(Reiziger reiziger) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.merge(reiziger);
            transaction.commit();

            return true;
        } catch(Exception e) {
            System.err.println( e.getMessage() );
            return false;
        }
    }

    @Override
    public void delete(Reiziger reiziger) {
        if (reiziger != null) {
            this.adresDAO.deleteFromReiziger( reiziger.getAdres() );

            List<OVChipkaart> ovList = reiziger.getOvChipkaart();
            for (int i = 0; i < ovList.size() ; i++) {
                this.ovChipkaartDAO.deleteFromReiziger( ovList.get(i) );
            }

            this.adresDAO.deleteFromReiziger(reiziger.getAdres());

            this.__deleteOne(reiziger);
        }
    }

    public void deleteFromAdres(Reiziger reiziger) {
        this.delete(reiziger);
    }

    public void deleteFromOvChipkaart(Reiziger reiziger) {
        this.delete(reiziger);
    }

    private void __deleteOne(Reiziger reiziger) {
        Transaction transaction;
        try {
            transaction = session.beginTransaction();
            session.remove(reiziger);
            transaction.commit();

        } catch(Exception e) {
            System.err.println( e.getMessage() );
        }
    }

    @Override
    public List<Reiziger> findAll() {
        String hql = "FROM Reiziger";
        Query query = session.createQuery(hql);
        List results = query.list();

        return results;
    }

    @Override
    public Reiziger findById(int id) {
        String hql = "FROM Reiziger AS A WHERE A.id = :ID";
        Query query = session.createQuery(hql);
        query.setParameter("ID",id);
        List results = query.list();

        if (results.size() == 0) {
            return null;
        }

        return (Reiziger) results.get(0);
    }

    @Override
    public List<Reiziger> findByGeboorteDatum(String dateString) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = formatter.parse(dateString);

            String hql = "FROM Reiziger AS A WHERE A.geboorteDatum = :date";
            Query query = session.createQuery(hql);
            query.setParameter("date", date );
            List results = query.list();

            return results;
        } catch(Exception err) {
            System.err.println( err.getMessage() );
            return null;
        }

    }
}
