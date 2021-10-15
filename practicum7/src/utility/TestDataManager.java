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

    public void deleteOldTestData() {
        __deleteOldTestData();
        __deleteOldTestData();
        __deleteOldTestData();
    }

    private void __deleteOldTestData() {
        Adres oldAdres = adresDAO.findById(1945);
        if (oldAdres != null) {
            adresDAO.delete(oldAdres);
        }

        Product oldProduct = productDAO.findById(1945);
        if (oldProduct != null) {
            productDAO.delete(oldProduct);
        }

        OVChipkaart oldOVChipkaart = ovChipkaartDAO.findById(1945);
        if (oldOVChipkaart != null) {
            ovChipkaartDAO.delete(oldOVChipkaart);
        }

        Reiziger oldReiziger = reizigerDAO.findById(1945);
        if (oldReiziger != null) {
            reizigerDAO.delete(oldReiziger);
        }
    }

    private Reiziger createTestData() {
        Adres adres = new Adres(1945, "","", "","");
        Reiziger reiziger = new Reiziger(1945, "","","", Date.valueOf("2022-12-01") );
        OVChipkaart newOv = new OVChipkaart(1945, Date.valueOf("2022-12-01"), 1, 25.50 );
        Product ovProduct = new Product(1945, "", "", 14.50);

        ArrayList<Product> productArrayList = new ArrayList<>();
        productArrayList.add(ovProduct);

        reiziger.setAdres(adres, false);
        newOv.setReiziger(reiziger, false);
        newOv.setProductList(productArrayList, false);

        return reiziger;
    }

    public Adres getAdres() {
        Reiziger reiziger = createTestData();
        return reiziger.getAdres();
    }

    public Reiziger getReiziger() {
        return createTestData();
    }

    public OVChipkaart getOvChipkaart() {
        Reiziger reiziger = createTestData();
        List<OVChipkaart> ovList = reiziger.getOvChipkaart();
        return ovList.get(0);
    }

    public Product getProduct() {
        OVChipkaart ov = getOvChipkaart();
        List<Product> productList = ov.getProduct();

        return productList.get(0);
    }
}
