package domain;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class OVChipkaart {
    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private Double saldo;
    private int reizigerId;
    private Reiziger reizigerObj;
    private ArrayList<Product> productList;

    public OVChipkaart(int kaartNummer, Date geldigTot, int klasse, Double saldo, int reizigerId) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reizigerId = reizigerId;
    }

    public String toString() {
        String reizigerStr = "";
        if (this.reizigerObj != null) {
            reizigerStr += "Reiziger" + this.reizigerObj.getInfoFromOvchipkaart();
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
        if (this.productList != null && !this.productList.isEmpty()) {
            productListString += "ProductList[";
            for (int i = 0; i <this.productList.size(); i++) {
                if (i > 1) {
                    productListString += ", ";
                }
                productListString += " Product" + this.productList.get(i).getInfo();
            }
            productListString += " ]";
        } else {
            productListString = "null";
        }

        return  productListString;
    }


    public String getInfoFromProduct() {
        return  "{ " + this.__internalGetInfo() + ", Reiziger" + this.reizigerObj.getInfoFromOvchipkaart() + " }";
    }


    private String __internalGetInfo() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String geldigTotStr = dateFormat.format(this.geldigTot);

        String string = "";
        string += this.kaartNummer  + ", ";
        string += geldigTotStr + ", ";
        string += this.klasse + ", ";
        string += this.saldo + ", ";
        string += this.reizigerId;

        return string;
    }

    public int getKaartNummer() {return kaartNummer;}
    public void setKaartNummer(int kaartNummer) {this.kaartNummer = kaartNummer;}

    public Date getGeldigTot() {return geldigTot;}
    public void setGeldigTot(Date geldigTot) {this.geldigTot = geldigTot;}

    public int getKlasse() {return klasse;}
    public void setKlasse(int klasse) {this.klasse = klasse;}

    public Double getSaldo() {return saldo;}
    public void setSaldo(Double saldo) {this.saldo = saldo;}

    public int getReizigerId() {return reizigerId;}
    public void setReizigerId(int reizigerId) {this.reizigerId = reizigerId;}

    public Reiziger getReiziger() {return reizigerObj;}
    public void setReiziger(Reiziger reizigerObj, boolean relationCalled) {
        this.reizigerObj = reizigerObj;

        if (!relationCalled) {
            ArrayList<OVChipkaart> ovChipkaartList = new ArrayList();
            ovChipkaartList.add(this);
            this.reizigerObj.setOvChipkaartList(ovChipkaartList, true);
        }
    }

    public ArrayList<Product> getProductList() {return productList;}
    public void setProductList(ArrayList<Product> productList, boolean relationCalled) {
        this.productList = productList;

        if (!relationCalled) {
            for (Product product : this.productList) {
                ArrayList<OVChipkaart> ovChipkaartList = new ArrayList();
                ovChipkaartList.add(this);
                product.setOvChipkaartList(ovChipkaartList, true);
            }
        }
    }
}
