import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Reiziger {
    private int id;
    private String voorletters;
    private String tussenvoegsel;
    private String achternaam;
    private Date geboorteDatum;
    private Adres adresObj;
    private ArrayList<OVChipkaart> OvChipkaartList;

    public Reiziger(int id, String voorletters, String tussenvoegsel, String achternaam, Date geboorteDatum, Adres adres) {
        this.id = id;
        this.voorletters = voorletters;
        this.tussenvoegsel = tussenvoegsel;
        this.achternaam = achternaam;
        this.geboorteDatum = geboorteDatum;
        this.adresObj = adres;

        if (this.adresObj != null) {
            this.adresObj.setReiziger(this);
        }
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
        if (this.OvChipkaartList != null && !this.OvChipkaartList.isEmpty()) {
            OvChipkaartListString += "OvChipkaartenList[";
            for (int i=0; i <this.OvChipkaartList.size(); i++) {
                if (i > 1) {
                    OvChipkaartListString += ", ";
                }
                OvChipkaartListString += " OvChipkaart" + this.OvChipkaartList.get(i).getInfo();
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

    public String getInfo() {
        return  "{ " + this.__internalGetInfo() + " }";
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

    public void setAdres(Adres adresObj) {
        this.adresObj = adresObj;
    }
    public Adres getAdres() {
        return adresObj;
    }

    public void setOvChipkaartList(ArrayList<OVChipkaart> ovChipkaartList) { this.OvChipkaartList = ovChipkaartList;}
    public ArrayList<OVChipkaart> getOvChipkaartList() {return this.OvChipkaartList;}
}
