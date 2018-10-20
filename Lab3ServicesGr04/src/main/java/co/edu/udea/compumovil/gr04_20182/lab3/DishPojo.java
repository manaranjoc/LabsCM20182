package co.edu.udea.compumovil.gr04_20182.lab3;

import android.net.Uri;

import java.io.Serializable;

public class DishPojo implements Serializable{

    private String name;
    private String type;
    private String price;
    private String time;
    private String imageUri;
    private String schedule;
    private String ingredients;
    private boolean favorite;
    private int id;
    private String description;

    public DishPojo() { }

    public DishPojo(int id, String name, String type, String price, String time, String imageDir, String schedule, String ingredients, Boolean favorite, String description) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.time = time;
        this.imageUri = imageDir;
        this.schedule = schedule;
        this.ingredients = ingredients;
        this.favorite = favorite;
        this.id = id;
        this.description = description;
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

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageDir) {
        this.imageUri = imageDir;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
