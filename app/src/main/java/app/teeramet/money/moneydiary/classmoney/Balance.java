package app.teeramet.money.moneydiary.classmoney;

/**
 * Created by barbie on 1/31/2017.
 */

public class Balance {

    private String month;
    private double money;
    private int imageId;


    public Balance(String month, double money) {
        this.month=month;
        this.money = money;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

}
