package app.teeramet.money.moneydiary.classmoney;

/**
 * Created by barbie on 1/28/2017.
 */

public class TopCatalog {
    private Catalog catalog;
    public double value;

    public TopCatalog() {
    }

    public TopCatalog(Catalog catalog, double value) {
        this.catalog = catalog;
        this.value = value;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void increaseValue(double value){
        this.value+=value;
    }
}
