package co.edu.udea.compumovil.gr04_20182.lab2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DrinkDbHelper extends SQLiteOpenHelper{

    public DrinkDbHelper(Context context){
        super(context, DrinkContract.DB_NAME, null, DrinkContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT,%s TEXT)",
                DrinkContract.TABLE, DrinkContract.Column.ID,
                DrinkContract.Column.NAME,
                DrinkContract.Column.PRICE,
                DrinkContract.Column.IMAGE);

        sqLiteDatabase.execSQL(sql);

        ContentValues values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Coke");
        values.put(DrinkContract.Column.PRICE, "4000");
        values.put(DrinkContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/coke");

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);

        values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Coke");
        values.put(DrinkContract.Column.PRICE, "4000");
        values.put(DrinkContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/coke");

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);

        values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Coke");
        values.put(DrinkContract.Column.PRICE, "4000");
        values.put(DrinkContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/coke");

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);

        values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Coke");
        values.put(DrinkContract.Column.PRICE, "4000");
        values.put(DrinkContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/coke");

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);

        values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Coke");
        values.put(DrinkContract.Column.PRICE, "4000");
        values.put(DrinkContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/coke");

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+DrinkContract.TABLE);
        onCreate(sqLiteDatabase);
    }
}
