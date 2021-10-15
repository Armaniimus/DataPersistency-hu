package utility;

import daos.AdresDAO;
import daos.OVChipkaartDAO;
import daos.ProductDAO;
import daos.ReizigerDAO;
import domain.Adres;
import domain.OVChipkaart;
import domain.Product;
import domain.Reiziger;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class TestDataManager {
    private AdresDAO adresDAO;;
    private ReizigerDAO reizigerDAO;
    private OVChipkaartDAO ovChipkaartDAO;
    private ProductDAO productDAO;

    public TestDataManager(AdresDAO adresDAO, ReizigerDAO reizigerDAO, OVChipkaartDAO ovChipkaartDAO, ProductDAO productDAO) {
        this.adresDAO = adresDAO;
        this.reizigerDAO = reizigerDAO;
        this.ovChipkaartDAO = ovChipkaartDAO;
        this.productDAO = productDAO;
    }

    public void deleteOldTestData(int id) {
        __deleteOldTestData(id);
        __deleteOldTestData(id);
        __deleteOldTestData(id);
    }

    private void __deleteOldTestData(int id) {
        Adres oldAdres = adresDAO.findById(id);
        if (oldAdres != null) {
            adresDAO.delete(oldAdres);
        }

        Product oldProduct = productDAO.findById(id);
        if (oldProduct != null) {
            productDAO.delete(oldProduct);
        }

        OVChipkaart oldOVChipkaart = ovChipkaartDAO.findById(id);
        if (oldOVChipkaart != null) {
            ovChipkaartDAO.delete(oldOVChipkaart);
        }

        Reiziger oldReiziger = reizigerDAO.findById(id);
        if (oldReiziger != null) {
            reizigerDAO.delete(oldReiziger);
        }
    }

    private Reiziger createTestData(int id) {
        Adres adres = new Adres(id, "","", "","");
        Reiziger reiziger = new Reiziger(id, "","","", Date.valueOf("2022-12-01") );
        OVChipkaart newOv = new OVChipkaart(id, Date.valueOf("2022-12-01"), 1, 25.50 );
        Product ovProduct = new Product(id, "", "", 14.50);

        ArrayList<Product> productArrayList = new ArrayList<>();
        productArrayList.add(ovProduct);

        reiziger.setAdres(adres, false);
        newOv.setReiziger(reiziger, false);
        newOv.setProductList(productArrayList, false);

        return reiziger;
    }

    public Adres getAdres(int id) {
        Reiziger reiziger = createTestData(id);
        return reiziger.getAdres();
    }

    public Reiziger getReiziger(int id) {
        return createTestData(id);
    }

    public OVChipkaart getOvChipkaart(int id) {
        Reiziger reiziger = createTestData(id);
        List<OVChipkaart> ovList = reiziger.getOvChipkaart();
        return ovList.get(0);
    }

    public Product getProduct(int id) {
        OVChipkaart ov = getOvChipkaart(id);
        List<Product> productList = ov.getProduct();

        return productList.get(0);
    }
}
