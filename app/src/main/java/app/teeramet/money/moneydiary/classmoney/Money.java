package app.teeramet.money.moneydiary.classmoney;

/**
 * Created by barbie on 10/8/2559.
 */
abstract public class Money{
    int id;
    String strName;
    Account idAccount;
    Catalog catalog;
    TypeMoney idMoneyType;
    String descript;
    long date;
    long time;
    double price;
    String namelocation;
    double lattitude;
    double longtitude;
    String pathImage;

    public Money() {
    }

    public Money(int id, Account idAccount, Catalog catalog, TypeMoney idMoneyType, String strName, double price) {
        this.id = id;
        this.idAccount = idAccount;
        this.catalog = catalog;
        this.idMoneyType = idMoneyType;
        this.strName = strName;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStrName() {
        return strName;
    }

    public void setStrName(String strName) {
        this.strName = strName;
    }

    public Account getAccount() {
        return idAccount;
    }

    public void setAccount(Account idAccount) {
        this.idAccount = idAccount;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public TypeMoney getMoneyType() {
        return idMoneyType;
    }

    public void setMoneyType(TypeMoney idMoneyType) {
        this.idMoneyType = idMoneyType;
    }

    public String getDescript() {
        return descript;
    }

    public void setDescript(String descript) {
        this.descript = descript;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getNamelocation() {
        return namelocation;
    }

    public void setNamelocation(String namelocation) {
        this.namelocation = namelocation;
    }

    public double getLattitude() {
        return lattitude;
    }

    public void setLattitude(double lattitude) {
        this.lattitude = lattitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getPathImage() {
        return pathImage;
    }

    public void setPathImage(String pathImage) {
        this.pathImage = pathImage;
    }

    public abstract double calculate(double value);

}
