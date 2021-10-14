package domain;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="ov_chipkaart")
public class OVChipkaart {
    @Id
    @Column(name = "kaart_nummer")
    private int kaart_nummer;

    @Column(name = "geldig_tot")
    private Date geldigTot;
    private int klasse;
    private Double saldo;

    @ManyToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;

    @ManyToMany(mappedBy = "ovChipkaart", cascade = {CascadeType.ALL} )
    private List<Product> product;

    public OVChipkaart() {

    }

    public OVChipkaart(int kaart_nummer, Date geldigTot, int klasse, Double saldo, int reiziger_id) {
        this.kaart_nummer = kaart_nummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
    }

    public String toString() {
        String reizigerStr = "";
        if (this.reiziger != null) {
            reizigerStr += "Reiziger" + this.reiziger.getInfoFromOvchipkaart();
        } else {
            reizigerStr += "null";
        }

        String string = "OvChipkaart{ " + this.__internalGetInfo() + ", " + reizigerStr + ", " + this.__getProductString() + " }";;

        return string;
    }

    public String getInfoFromReiziger() {
        return  "{ " + this.__internalGetInfo() + ", " + this.__getProductString() + " }";
    }

    private String __getProductString() {
        String productListString = "";
        if (this.product != null && !this.product.isEmpty()) {
            productListString += "ProductList[";
            for (int i = 0; i <this.product.size(); i++) {
                if (i > 1) {
                    productListString += ", ";
                }
                productListString += " Product" + this.product.get(i).getInfo();
            }
            productListString += " ]";
        } else {
            productListString = "null";
        }

        return  productListString;
    }


    public String getInfoFromProduct() {
        return  "{ " + this.__internalGetInfo() + ", Reiziger" + this.reiziger.getInfoFromOvchipkaart() + " }";
    }


    private String __internalGetInfo() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String geldigTotStr = dateFormat.format(this.geldigTot);

        String string = "";
        string += this.kaart_nummer + ", ";
        string += geldigTotStr + ", ";
        string += this.klasse + ", ";
        string += this.saldo + ", ";

        return string;
    }

    public int getKaart_nummer() {return kaart_nummer;}
    public void setKaart_nummer(int kaart_nummer) {this.kaart_nummer = kaart_nummer;}

    public Date getGeldigTot() {return geldigTot;}
    public void setGeldigTot(Date geldigTot) {this.geldigTot = geldigTot;}

    public int getKlasse() {return klasse;}
    public void setKlasse(int klasse) {this.klasse = klasse;}

    public Double getSaldo() {return saldo;}
    public void setSaldo(Double saldo) {this.saldo = saldo;}

    public Reiziger getReiziger() {return reiziger;}
    public void setReiziger(Reiziger reizigerObj, boolean relationCalled) {
        this.reiziger = reizigerObj;

        if (!relationCalled) {
            ArrayList<OVChipkaart> ovChipkaartList = new ArrayList();
            ovChipkaartList.add(this);
            this.reiziger.setOvChipkaartList(ovChipkaartList, true);
        }
    }

    public List<Product> getProduct() {return product;}
    public void setProductList(ArrayList<Product> productList, boolean relationCalled) {
        this.product = productList;

        if (!relationCalled) {
            for (Product product : this.product) {
                ArrayList<OVChipkaart> ovChipkaartList = new ArrayList();
                ovChipkaartList.add(this);
                product.setOvChipkaartList(ovChipkaartList, true);
            }
        }
    }
}
