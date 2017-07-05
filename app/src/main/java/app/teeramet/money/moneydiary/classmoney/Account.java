package app.teeramet.money.moneydiary.classmoney;

/**
 * Created by barbie on 17/8/2559.
 */
public class Account {
    int id;
    String name;
    String image;
    public Account() {
    }

    public Account(int id, String name ) {
        this.id = id;
        this.name=name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String  getImage() {
        return image;
    }

    public void setImage(String  image) {
        this.image = image;
    }
}
