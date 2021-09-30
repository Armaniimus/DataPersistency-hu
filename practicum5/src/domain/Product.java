package domain;

import java.util.ArrayList;

public class Product {
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private Double prijs;
    private ArrayList<OVChipkaart> OvChipkaartList;

    public Product( int product_nummer, String naam, String beschrijving, Double prijs) {
        this.product_nummer=product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public String toString() {
        String OvChipkaartListString = "";
        if (this.OvChipkaartList != null && !this.OvChipkaartList.isEmpty()) {
            OvChipkaartListString += "OvChipkaartenList[";
            for (int i=0; i <this.OvChipkaartList.size(); i++) {
                if (i > 1) {
                    OvChipkaartListString += ", ";
                }
                OvChipkaartListString += " OvChipkaart" + this.OvChipkaartList.get(i).getInfoFromProduct();
            }
            OvChipkaartListString += " ]";
        } else {
            OvChipkaartListString = "null";
        }

        String resultString = "Product{ ";
        resultString += __internalGetInfo() + ", ";
        resultString += OvChipkaartListString;
        resultString += " }";

        return resultString;
    }

    public String getInfo() {
        return  "{ " + this.__internalGetInfo() + " }";
    }

    private String __internalGetInfo() {
        String reizigerStr = "";
        reizigerStr += this.product_nummer + ", ";
        reizigerStr += this.naam + ", ";
        reizigerStr += this.beschrijving + ", ";
        reizigerStr += this.prijs;
        return reizigerStr;
    }

    public int getProduct_nummer() { return product_nummer; }
    public void setProduct_nummer(int product_nummer) { this.product_nummer = product_nummer; }

    public String getNaam() { return naam; }
    public void setNaam(String naam) { this.naam = naam; }

    public String getBeschrijving() { return beschrijving; }
    public void setBeschrijving(String beschrijving) { this.beschrijving = beschrijving; }

    public Double getPrijs() { return prijs; }
    public void setPrijs(Double prijs) { this.prijs = prijs; }

    public ArrayList<OVChipkaart> getOvChipkaartList() { return OvChipkaartList; }
    public void setOvChipkaartList(ArrayList<OVChipkaart> ovChipkaartList, boolean relationCalled) {
        this.OvChipkaartList = ovChipkaartList;

        for (OVChipkaart ovChipkaart : this.OvChipkaartList) {
            ArrayList<Product> productList = new ArrayList();
            productList.add(this);
            ovChipkaart.setProductList(productList, true);
        }
    }


}
