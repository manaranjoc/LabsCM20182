package co.edu.udea.compumovil.gr04_20182.lab2;

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
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT,%s TEXT,%s TEXT,%s TEXT)",
                DishContract.TABLE, DishContract.Column.ID,
                DishContract.Column.NAME,
                DishContract.Column.TYPE,
                DishContract.Column.PRICE,
                DishContract.Column.TIME,
                DishContract.Column.IMAGE);

        sqLiteDatabase.execSQL(sql);

        ContentValues values = new ContentValues();
        values.put(DishContract.Column.NAME, "Pepperoni");
        values.put(DishContract.Column.TYPE, "Main Dish");
        values.put(DishContract.Column.PRICE, "30000");
        values.put(DishContract.Column.TIME, "30 min");
        values.put(DishContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/pizza_peperonni");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);

        values.put(DishContract.Column.NAME, "Pepperoni1");
        values.put(DishContract.Column.TYPE, "Main Dish");
        values.put(DishContract.Column.PRICE, "30000");
        values.put(DishContract.Column.TIME, "30 min");
        values.put(DishContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/pizza_peperonni");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);

        values.put(DishContract.Column.NAME, "Pepperoni2");
        values.put(DishContract.Column.TYPE, "Main Dish");
        values.put(DishContract.Column.PRICE, "30000");
        values.put(DishContract.Column.TIME, "30 min");
        values.put(DishContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/pizza_peperonni");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);

        values.put(DishContract.Column.NAME, "Pepperoni3");
        values.put(DishContract.Column.TYPE, "Main Dish");
        values.put(DishContract.Column.PRICE, "30000");
        values.put(DishContract.Column.TIME, "30 min");
        values.put(DishContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/pizza_peperonni");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);

        values.put(DishContract.Column.NAME, "Pepperoni4");
        values.put(DishContract.Column.TYPE, "Main Dish");
        values.put(DishContract.Column.PRICE, "30000");
        values.put(DishContract.Column.TIME, "30 min");
        values.put(DishContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/pizza_peperonni");

        sqLiteDatabase.insert(DishContract.TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+DishContract.TABLE);
        onCreate(sqLiteDatabase);
    }
}
