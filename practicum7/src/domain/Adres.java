package domain;

import javax.persistence.*;

@Entity
public class Adres {
    @Id
    @Column(name = "adres_id")
    private int    id;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private String postcode;


    @OneToOne(cascade = {CascadeType.ALL})
    @JoinColumn(name = "reiziger_id")
    private Reiziger reiziger;

    public Adres() {

    }

    public Adres (int id, String straat, String huisnummer, String woonplaats, String postcode) {
        this.id = id;
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.woonplaats = woonplaats;
        this.postcode = postcode;
    }

    private String __internalGetInfo() {
        String adresStr = "";
        adresStr += this.id + ", ";
        adresStr += this.straat + ", ";
        adresStr += this.huisnummer + ", ";
        adresStr += this.woonplaats + ", ";
        adresStr += this.postcode + ", ";

        return adresStr;
    }

    public String getInfo() {
        return  "{ " + this.__internalGetInfo() + " }";
    }

    public String toString() {
        String adresStr = "Adres{ ";
        adresStr += this.__internalGetInfo() + ", ";

        if (reiziger == null) {
            adresStr += "NULL";
        } else {
            adresStr += "Reiziger" + this.reiziger.getInfoFromAdres();
        }

        adresStr += " }";

        return adresStr;
    }

    public int getId() {return id;}
    public void setId(int id) {this.id = id;}

    public String getStraat() {return straat;}
    public void setStraat(String straat) {this.straat = straat;}

    public String getHuisnummer() {return huisnummer;}
    public void setHuisnummer(String huisnummer) {this.huisnummer = huisnummer;}

    public String getWoonplaats() {return woonplaats;}
    public void setWoonplaats(String woonplaats) {this.woonplaats = woonplaats;}

    public String getPostcode() {return postcode;}
    public void setPostcode(String postcode) {this.postcode = postcode;}

    public Reiziger getReiziger() {return this.reiziger;}
    public void setReiziger(Reiziger reiziger, boolean relationCalled) {
        this.reiziger = reiziger;
        if (!relationCalled) {
            reiziger.setAdres(this, true);
        }
    }
}
