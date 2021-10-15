package domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="product")
public class Product {
    @Id
    @Column(name = "product_nummer")
    private int product_nummer;
    private String naam;
    private String beschrijving;
    private Double prijs;

    @ManyToMany( cascade = {CascadeType.ALL} )
    @JoinTable( name = "ov_chipkaart_product",
        joinColumns = { @JoinColumn(name="kaart_nummer") },
        inverseJoinColumns = { @JoinColumn(name="product_nummer") }
    )
    private List<OVChipkaart> ovChipkaart;

    public Product() {

    }

    public Product( int product_nummer, String naam, String beschrijving, Double prijs) {
        this.product_nummer=product_nummer;
        this.naam = naam;
        this.beschrijving = beschrijving;
        this.prijs = prijs;
    }

    public String toString() {
        String OvChipkaartListString = "";
        if (this.ovChipkaart != null && !this.ovChipkaart.isEmpty()) {
            OvChipkaartListString += "OvChipkaartenList[";
            for (int i = 0; i <this.ovChipkaart.size(); i++) {
                if (i > 1) {
                    OvChipkaartListString += ", ";
                }
                OvChipkaartListString += " OvChipkaart" + this.ovChipkaart.get(i).getInfoFromProduct();
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

    public List<OVChipkaart> getOvChipkaart() { return ovChipkaart; }
    public void setOvChipkaartList(ArrayList<OVChipkaart> ovChipkaartList, boolean relationCalled) {
        this.ovChipkaart = ovChipkaartList;

        for (OVChipkaart ovChipkaart : this.ovChipkaart) {
            ArrayList<Product> productList = new ArrayList();
            productList.add(this);
            ovChipkaart.setProductList(productList, true);
        }
    }


}
