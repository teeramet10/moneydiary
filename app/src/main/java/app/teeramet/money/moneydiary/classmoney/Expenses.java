package app.teeramet.money.moneydiary.classmoney;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by barbie on 10/8/2559.
 */
public class Expenses extends Money implements Parcelable{


    public Expenses() {
        super();
    }

    @Override
    public void setPathImage(String pathImage) {
        super.setPathImage(pathImage);
    }

    public Expenses(int id, Account idAccount, Catalog idCatalog, TypeMoney idMoneyType, String strName, double price) {
        super(id, idAccount, idCatalog, idMoneyType, strName, price);
    }

    @Override
    public int getId() {
        return super.getId();
    }

    @Override
    public void setId(int id) {
        super.setId(id);
    }

    @Override
    public String getStrName() {
        return super.getStrName();
    }

    @Override
    public void setStrName(String strName) {
        super.setStrName(strName);
    }

    @Override
    public Account getAccount() {
        return super.getAccount();
    }

    @Override
    public void setAccount(Account idAccount) {
        super.setAccount(idAccount);
    }

    @Override
    public Catalog getCatalog() {
        return super.getCatalog();
    }

    @Override
    public void setCatalog(Catalog catalog) {
        super.setCatalog(catalog);
    }

    @Override
    public TypeMoney getMoneyType() {
        return super.getMoneyType();
    }

    @Override
    public void setMoneyType(TypeMoney idMoneyType) {
        super.setMoneyType(idMoneyType);
    }

    @Override
    public String getDescript() {
        return super.getDescript();
    }

    @Override
    public void setDescript(String descript) {
        super.setDescript(descript);
    }

    @Override
    public long getDate() {
        return super.getDate();
    }

    @Override
    public void setDate(long date) {
        super.setDate(date);
    }

    @Override
    public long getTime() {
        return super.getTime();
    }

    @Override
    public void setTime(long time) {
        super.setTime(time);
    }

    @Override
    public double getPrice() {
        return super.getPrice();
    }

    @Override
    public void setPrice(double price) {
        super.setPrice(price);
    }

    @Override
    public String getNamelocation() {
        return super.getNamelocation();
    }

    @Override
    public void setNamelocation(String namelocation) {
        super.setNamelocation(namelocation);
    }

    @Override
    public double getLattitude() {
        return super.getLattitude();
    }

    @Override
    public void setLattitude(double lattitude) {
        super.setLattitude(lattitude);
    }

    @Override
    public double getLongtitude() {
        return super.getLongtitude();
    }

    @Override
    public void setLongtitude(double longtitude) {
        super.setLongtitude(longtitude);
    }

    @Override
    public String getPathImage() {
        return super.getPathImage();
    }

    @Override
    public double calculate(double value) {
        return price-=value;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
