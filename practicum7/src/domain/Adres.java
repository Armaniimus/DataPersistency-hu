package domain;

import javax.persistence.*;


@Entity
@Table(name="adres")
public class Adres {
    @Id
    @Column(name = "adres_id")
    private int    id;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private String postcode;
    private int reiziger_id;

    @OneToOne
    @Transient
    private Reiziger reiziger;

    public Adres() {

    }

    public Adres (int id, String straat, String huisnummer, String woonplaats, String postcode, int reiziger_id) {
        this.id = id;
        this.straat = straat;
        this.huisnummer = huisnummer;
        this.woonplaats = woonplaats;
        this.postcode = postcode;
        this.reiziger_id = reiziger_id;
    }

    private String __internalGetInfo() {
        String adresStr = "";
        adresStr += this.id + ", ";
        adresStr += this.straat + ", ";
        adresStr += this.huisnummer + ", ";
        adresStr += this.woonplaats + ", ";
        adresStr += this.postcode + ", ";
        adresStr += this.reiziger_id;

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

    public int getReiziger_id() {return this.reiziger_id;}
    public void setReiziger_id(int reiziger_id) {this.reiziger_id = reiziger_id;}

    public Reiziger getReiziger() {return this.reiziger;}
    public void setReiziger(Reiziger reiziger, boolean relationCalled) {
        this.reiziger = reiziger;
        if (!relationCalled) {
            reiziger.setAdres(this, true);
        }
    }
}
