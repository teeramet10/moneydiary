package app.teeramet.money.moneydiary.classmoney;

/**
 * Created by barbie on 15/8/2559.
 */
public class Catalog {
    int id;
    String strNameList;
    int idtypemoney;
    String icon;

    public Catalog() {
    }

    public Catalog(int id, String strNameList) {
        this.id = id;
        this.strNameList = strNameList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrNameList() {
        return strNameList;
    }

    public void setStrNameList(String strNameList) {
        this.strNameList = strNameList;
    }

    public int getIdtypemoney() {
        return idtypemoney;
    }

    public void setIdtypemoney(int idtypemoney) {
        this.idtypemoney = idtypemoney;
    }

    public String getPathIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
