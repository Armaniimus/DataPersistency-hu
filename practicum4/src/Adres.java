public class Adres {
    private int    id;
    private String postcode;
    private String huisnummer;
    private String straat;
    private String woonplaats;
    private int    reizigerId;
    private Reiziger reizigerObj;

    public Adres (int id, String postcode, String huisnummer, String straat, String woonplaats, int reizigerId, Reiziger reizigerObj) {
        this.id = id;
        this.postcode = postcode;
        this.huisnummer = huisnummer;
        this.straat = straat;
        this.woonplaats = woonplaats;
        this.reizigerId = reizigerId;
        this.reizigerObj = reizigerObj;
        if (reizigerObj != null) {
            this.reizigerObj.setAdres(this);
        }
    }

    private String __internalGetInfo() {
        String adresStr = "";
        adresStr += this.id + ", ";
        adresStr += this.straat + ", ";
        adresStr += this.huisnummer + ", ";
        adresStr += this.postcode + ", ";
        adresStr += this.woonplaats + ", ";
        adresStr += this.reizigerId;

        return adresStr;
    }

    public String getInfo() {
        return  "{ " + this.__internalGetInfo() + " }";
    }

    public String toString() {
        String adresStr = "Adres{ ";
        adresStr += this.__internalGetInfo() + ", ";

        if (reizigerObj == null) {
            adresStr += "NULL";
        } else {
            adresStr += "Reiziger" + this.reizigerObj.getInfo();
        }

        adresStr += " }";

        return adresStr;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getStraat() {
        return straat;
    }
    public void setStraat(String straat) {
        this.straat = straat;
    }
    public String getHuisnummer() {
        return huisnummer;
    }
    public void setHuisnummer(String huisnummer) {
        this.huisnummer = huisnummer;
    }
    public String getWoonplaats() {
        return woonplaats;
    }
    public void setWoonplaats(String woonplaats) {
        this.woonplaats = woonplaats;
    }
    public String getPostcode() {
        return postcode;
    }
    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
    public int getReizigerId() {
        return reizigerId;
    }
    public void setReizigerId(int reiziger_id) {
        this.reizigerId = reiziger_id;
    }
    public Reiziger getReiziger() {
        return reizigerObj;
    }
    public void setReiziger(Reiziger reiziger) {
        this.reizigerObj = reiziger;
    }
}
