package co.edu.udea.compumovil.gr04_20182.lab2;

import android.net.Uri;

public class DishPojo {

    private String name;
    private String type;
    private String price;
    private String time;
    private Uri imageUri;

    public DishPojo() { }

    public DishPojo(String name, String type, String price, String time, String imageDir) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.time = time;
        this.imageUri = Uri.parse(imageDir);

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageDir) {
        this.imageUri = Uri.parse(imageDir);
    }
}
