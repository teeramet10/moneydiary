package app.teeramet.money.moneydiary.classmoney;

import java.util.ArrayList;

/**
 * Created by barbie on 1/31/2017.
 */

public class Pager {
    ArrayList<Balance> balanceArrayList;
    String title;
    int year;

    public ArrayList<Balance> getBalanceArrayList() {
        return balanceArrayList;
    }

    public void setBalanceArrayList(ArrayList<Balance> balanceArrayList) {
        this.balanceArrayList = balanceArrayList;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Pager(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
