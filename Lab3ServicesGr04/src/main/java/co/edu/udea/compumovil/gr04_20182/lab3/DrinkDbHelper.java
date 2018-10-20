package co.edu.udea.compumovil.gr04_20182.lab3;

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
        String sql = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT UNIQUE,%s TEXT,%s TEXT,%s TEXT,%s BIT)",
                DrinkContract.TABLE, DrinkContract.Column.ID,
                DrinkContract.Column.NAME,
                DrinkContract.Column.PRICE,
                DrinkContract.Column.IMAGE,
                DrinkContract.Column.INGREDIENTS,
                DrinkContract.Column.FAVORITE);

        sqLiteDatabase.execSQL(sql);

        ContentValues values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Coke");
        values.put(DrinkContract.Column.PRICE, "4000");
        values.put(DrinkContract.Column.IMAGE, "android.resource://co.edu.udea.compumovil.gr04_20182.lab2/drawable/coke");
        values.put(DrinkContract.Column.INGREDIENTS, "Not Known");
        values.put(DrinkContract.Column.FAVORITE, 0);

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);

        values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Pepsi");
        values.put(DrinkContract.Column.PRICE, "3000");
        values.put(DrinkContract.Column.IMAGE, "https://secure.footprint.net/cupages/pepsi/3_cans-min.png");
        values.put(DrinkContract.Column.INGREDIENTS, "Coke");
        values.put(DrinkContract.Column.FAVORITE, 0);

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);

        values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Soda");
        values.put(DrinkContract.Column.PRICE, "2000");
        values.put(DrinkContract.Column.IMAGE, "https://http2.mlstatic.com/agua-sifon-soda-sierra-de-los-padres-x2-lts-o-bot-x225lt-D_NQ_NP_901225-MLA26856607049_022018-F.jpg");
        values.put(DrinkContract.Column.INGREDIENTS, "Water");
        values.put(DrinkContract.Column.FAVORITE, 0);

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);

        values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Fanta");
        values.put(DrinkContract.Column.PRICE, "3000");
        values.put(DrinkContract.Column.IMAGE, "https://www.cocacoladechile.cl/content/dam/journey/cl/es/private/historias/productos/fanta-productos-tropicalizada-2.rendition.584.327.jpg");
        values.put(DrinkContract.Column.INGREDIENTS, "Orange");
        values.put(DrinkContract.Column.FAVORITE, 0);

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);

        values = new ContentValues();
        values.put(DrinkContract.Column.NAME, "Canada Dry");
        values.put(DrinkContract.Column.PRICE, "3500");
        values.put(DrinkContract.Column.IMAGE, "https://www.canadadry.com/images/share-img.jpg");
        values.put(DrinkContract.Column.INGREDIENTS, "Smell and taste of Canada");
        values.put(DrinkContract.Column.FAVORITE, 0);

        sqLiteDatabase.insert(DrinkContract.TABLE, null, values);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+DrinkContract.TABLE);
        onCreate(sqLiteDatabase);
    }
}
