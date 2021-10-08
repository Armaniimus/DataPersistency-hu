package domain;

import javax.persistence.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="reiziger")
public class Reiziger {
    @Id
    @Column(name = "reiziger_id")
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboorteDatum;

    @OneToOne
    @Transient
    private Adres adresObj;

    @OneToMany
    @Transient
    private List<OVChipkaart> ovChipkaart;

    public Reiziger() {

    }

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboorteDatum) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboorteDatum = geboorteDatum;
    }

    public String getName() {
        if (this.tussenvoegsel != "" && this.tussenvoegsel != null) {
            return this.voorletters + " " + this.tussenvoegsel + " " + this.achternaam;
        } else {
            return this.voorletters + " " + this.achternaam;
        }
    }

    public String toString() {
        String AdresObjString = "";

        if (this.adresObj != null) {
            AdresObjString += "Adres" + this.adresObj.getInfo();
        } else {
            AdresObjString += "null";
        }

        String OvChipkaartListString = "";
        if (this.ovChipkaart != null && !this.ovChipkaart.isEmpty()) {
            OvChipkaartListString += "OvChipkaartenList[";
            for (int i = 0; i <this.ovChipkaart.size(); i++) {
                if (i > 1) {
                    OvChipkaartListString += ", ";
                }
                OvChipkaartListString += " OvChipkaart" + this.ovChipkaart.get(i).getInfoFromReiziger();
            }
            OvChipkaartListString += " ]";
        } else {
            OvChipkaartListString = "null";
        }

        String resultString = "Reiziger{ ";
        resultString += __internalGetInfo() + ", ";
        resultString += AdresObjString + ", ";
        resultString += OvChipkaartListString;
        resultString += " }";

        return resultString;
    }

//    public String getInfo() {
//        return  "{ " + this.__internalGetInfo() + " }";
//    }

    public String getInfoFromAdres() {
        String OvChipkaartListString = "";
        if (this.ovChipkaart != null && !this.ovChipkaart.isEmpty()) {
            OvChipkaartListString += "OvChipkaartenList[";
            for (int i = 0; i <this.ovChipkaart.size(); i++) {
                if (i > 1) {
                    OvChipkaartListString += ", ";
                }
                OvChipkaartListString += " OvChipkaart" + this.ovChipkaart.get(i).getInfoFromReiziger();
            }
            OvChipkaartListString += " ]";
        } else {
            OvChipkaartListString = "null";
        }

        return  "{ " + this.__internalGetInfo() + OvChipkaartListString + " }";
    }

    public String getInfoFromOvchipkaart() {
        return  "{ " + this.__internalGetInfo() + " Adres" + adresObj.getInfo() + " }";
    }

    private String __internalGetInfo() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String gDatumStr = dateFormat.format(this.geboorteDatum);

        String reizigerStr = "";
        reizigerStr += this.id + ", ";
        reizigerStr += this.voorletters + ", ";
        reizigerStr += this.tussenvoegsel + ", ";
        reizigerStr += this.achternaam + ", ";
        reizigerStr += gDatumStr;
        return reizigerStr;
    }

    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setVoorletters(String voorletters) {
        this.voorletters = voorletters;
    }
    public String getVoorletters() {
        return voorletters;
    }

    public void setTussenvoegsel(String tussenvoegsel) {
        this.tussenvoegsel = tussenvoegsel;
    }
    public String getTussenvoegsel() {
        return tussenvoegsel;
    }

    public void setAchternaam(String achternaam) {
        this.achternaam = achternaam;
    }
    public String getAchternaam() {
        return achternaam;
    }

    public void setGeboorteDatum(Date geboorteDatum) {
        this.geboorteDatum = geboorteDatum;
    }
    public Date getGeboorteDatum() {
        return geboorteDatum;
    }

    public void setAdres(Adres adresObj, boolean relationCalled) {
        this.adresObj = adresObj;
        if (!relationCalled){
            this.adresObj.setReiziger(this, true);
        }
    }
    public Adres getAdres() {
        return adresObj;
    }

    public void setOvChipkaartList(ArrayList<OVChipkaart> ovChipkaartList, boolean relationCalled) {
        this.ovChipkaart = ovChipkaartList;
        if (!relationCalled) {
            for (OVChipkaart ovChipkaart : this.ovChipkaart) {
                ovChipkaart.setReiziger(this, true);
            }
        }
    }
    public List<OVChipkaart> getOvChipkaart() {return this.ovChipkaart;}
}
