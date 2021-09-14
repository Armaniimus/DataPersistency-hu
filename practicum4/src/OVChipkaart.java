import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class OVChipkaart {
    private int kaartNummer;
    private Date geldigTot;
    private int klasse;
    private Double saldo;
    private int reizigerId;
    private Reiziger reizigerObj;

    public OVChipkaart(int kaartNummer, Date geldigTot, int klasse, Double saldo, int reizigerId, Reiziger reiziger) {
        this.kaartNummer = kaartNummer;
        this.geldigTot = geldigTot;
        this.klasse = klasse;
        this.saldo = saldo;
        this.reizigerId = reizigerId;

        if (reizigerObj != null) {
            reizigerObj = reiziger;
        }
    }

    public String toString() {
        String string = "OvChipkaart{ ";

        string += __internalGetInfo() + ", ";
        if (this.reizigerObj != null) {
            string += "Reiziger" + this.reizigerObj.getInfo();
        } else {
            string += ", null";
        }

        string += " }";
        return string;
    }

    public String getInfo() {
        return  "{ " + this.__internalGetInfo() + " }";
    }

    private String __internalGetInfo() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String geldigTotStr = dateFormat.format(this.geldigTot);

        String string = "";
        string += this.kaartNummer  + ", ";
        string += geldigTotStr;
        string += this.klasse + ", ";
        string += this.saldo + ", ";
        string += this.reizigerId + ", ";

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

    public Reiziger getReizigerObj() {return reizigerObj;}
    public void setReizigerObj(Reiziger reizigerObj) {this.reizigerObj = reizigerObj;}
}
