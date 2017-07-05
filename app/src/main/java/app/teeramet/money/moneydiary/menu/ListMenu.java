package app.teeramet.money.moneydiary.menu;

/**
 * Created by barbie on 31/8/2559.
 */
public class ListMenu {
    String namelist;
    int imageint;

    public ListMenu() {
    }

    public ListMenu(String namelist, int imageint) {
        this.namelist = namelist;
        this.imageint = imageint;
    }

    public String getNamelist() {
        return namelist;
    }

    public void setNamelist(String namelist) {
        this.namelist = namelist;
    }

    public int getImageint() {
        return imageint;
    }

    public void setImageint(int imageint) {
        this.imageint = imageint;
    }
}
