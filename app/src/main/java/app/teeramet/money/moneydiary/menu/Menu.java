package app.teeramet.money.moneydiary.menu;

/**
 * Created by barbie on 31/8/2559.
 */
public class Menu {
    Object menu;
    int image;
    String pathname;

    public Menu() {
    }

    public Menu(Object menu, int image) {
        this.menu = menu;
        this.image = image;
    }

    public Menu(Object menu, String pathname) {
        this.menu = menu;
        this.pathname = pathname;
    }

    public Object getMenu() {
        return menu;
    }

    public void setMenu(Object menu) {
        this.menu = menu;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getPathname() {
        return pathname;
    }

    public void setPathname(String pathname) {
        this.pathname = pathname;
    }
}
