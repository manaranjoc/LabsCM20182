package co.edu.udea.compumovil.gr04_20182.lab3;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DishDbHelper extends SQLiteOpenHelper{

    public DishDbHelper(Context context){
        super(context, DishContract.DB_NAME, null, DishContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT UNIQUE,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s BIT,%s TEXT)",
                DishContract.TABLE, DishContract.Column.ID,
                DishContract.Column.NAME,
                DishContract.Column.TYPE,
                DishContract.Column.PRICE,
                DishContract.Column.TIME,
                DishContract.Column.IMAGE,
                DishContract.Column.SCHEDULE,
                DishContract.Column.INGREDIENTS,
                DishContract.Column.FAVORITE,
                DishContract.Column.DESCRIPTION);

        sqLiteDatabase.execSQL(sql);

        ContentValues values = new ContentValues();
        values.put(DishContract.Column.NAME, "Cheese");
        values.put(DishContract.Column.TYPE, "Main Dish");
        values.put(DishContract.Column.PRICE, "35000");
        values.put(DishContract.Column.TIME, "35 min");
        values.put(DishContract.Column.IMAGE, "http://www.jolpani.com/wp-content/uploads/2018/04/pizza.jpeg");
        values.put(DishContract.Column.SCHEDULE, "Morning");
        values.put(DishContract.Column.INGREDIENTS, "Only Cheese");
        values.put(DishContract.Column.FAVORITE, 0);
        values.put(DishContract.Column.DESCRIPTION, "Heavy Smell");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);

        values.put(DishContract.Column.NAME, "Pepperoni");
        values.put(DishContract.Column.TYPE, "Main Dish");
        values.put(DishContract.Column.PRICE, "30000");
        values.put(DishContract.Column.TIME, "30 min");
        values.put(DishContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/pizza_peperonni");
        values.put(DishContract.Column.SCHEDULE, "Morning");
        values.put(DishContract.Column.INGREDIENTS, "Carne");
        values.put(DishContract.Column.FAVORITE, 0);
        values.put(DishContract.Column.DESCRIPTION, "Sweet Meat");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);

        values.put(DishContract.Column.NAME, "Chocolate");
        values.put(DishContract.Column.TYPE, "Dessert");
        values.put(DishContract.Column.PRICE, "20000");
        values.put(DishContract.Column.TIME, "15 min");
        values.put(DishContract.Column.IMAGE, "https://t2.rg.ltmcdn.com/es/images/6/1/8/img_pizza_de_fresas_con_chocolate_58816_600.jpg");
        values.put(DishContract.Column.SCHEDULE, "Afternoon, Noon");
        values.put(DishContract.Column.INGREDIENTS, "Chocolate");
        values.put(DishContract.Column.FAVORITE, 0);
        values.put(DishContract.Column.DESCRIPTION, "Sweet Dream");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);

        values.put(DishContract.Column.NAME, "Tropical");
        values.put(DishContract.Column.TYPE, "Main Dish");
        values.put(DishContract.Column.PRICE, "50000");
        values.put(DishContract.Column.TIME, "20 min");
        values.put(DishContract.Column.IMAGE, "http://www.eltoquepizzeriagranada.oferto.co/files/galerias/b120140905054836.jpg");
        values.put(DishContract.Column.SCHEDULE, "Morning");
        values.put(DishContract.Column.INGREDIENTS, "Cherry, Pineapple");
        values.put(DishContract.Column.FAVORITE, 0);
        values.put(DishContract.Column.DESCRIPTION, "Tropical Taste");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);

        values.put(DishContract.Column.NAME, "Cheese Fingers");
        values.put(DishContract.Column.TYPE, "Entrance");
        values.put(DishContract.Column.PRICE, "5000");
        values.put(DishContract.Column.TIME, "5 min");
        values.put(DishContract.Column.IMAGE, "https://noshingwiththenolands.com/wp-content/uploads/2016/06/Mozzarella-Cheese-Sticks-by-Noshing-With-The-Nolands-square-Small.jpg");
        values.put(DishContract.Column.SCHEDULE, "Afternoon");
        values.put(DishContract.Column.INGREDIENTS, "Cheese, Bread");
        values.put(DishContract.Column.FAVORITE, 0);
        values.put(DishContract.Column.DESCRIPTION, "Very elastic");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+DishContract.TABLE);
        onCreate(sqLiteDatabase);
    }
}
