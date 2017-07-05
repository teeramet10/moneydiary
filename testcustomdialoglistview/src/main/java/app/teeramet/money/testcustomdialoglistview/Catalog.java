package app.teeramet.money.testcustomdialoglistview;

/**
 * Created by barbie on 15/8/2559.
 */
public class Catalog {
    String strNameList;
    int idtypemoney;
    int icon;

    public Catalog() {
    }

    public Catalog( String strNameList) {

        this.strNameList = strNameList;
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

    public int getPathIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }
}
