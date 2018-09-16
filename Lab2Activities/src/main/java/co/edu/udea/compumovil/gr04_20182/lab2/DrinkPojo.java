package co.edu.udea.compumovil.gr04_20182.lab2;

import android.net.Uri;

public class DrinkPojo {

    private String name;
    private String price;
    private Uri imageUri;

    public DrinkPojo() { }

    public DrinkPojo(String name, String price, String imageDir) {
        this.name = name;
        this.price = price;
        this.imageUri = Uri.parse(imageDir);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageDir) {
        this.imageUri = Uri.parse(imageDir);
    }
}
