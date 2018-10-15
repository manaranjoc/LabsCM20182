package co.edu.udea.compumovil.gr04_20182.lab3;

import android.net.Uri;

import java.io.Serializable;

public class DrinkPojo implements Serializable{

    private int id;
    private String name;
    private String price;
    private String imageUri;
    private String ingredients;
    private boolean favorite;

    public DrinkPojo() { }

    public DrinkPojo(int id, String name, String price, String imageDir, String ingredients, Boolean favorite) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUri = imageDir;
        this.ingredients = ingredients;
        this.favorite = favorite;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageDir) {
        this.imageUri = imageDir;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
