package co.edu.udea.compumovil.gr04_20182.lab4;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UsersDbHelper extends SQLiteOpenHelper {

    private static final String TAG = UsersDbHelper.class.getSimpleName();

    public UsersDbHelper(Context context){
        super(context, UserContract.DB_NAME, null, UserContract.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String sql = String
                .format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT,%s TEXT,%s TEXT UNIQUE,%s TEXT,%s TEXT)",
                        UserContract.TABLE,
                        UserContract.Column.ID,
                        UserContract.Column.NAME,
                        UserContract.Column.EMAIL,
                        UserContract.Column.PASSWORD,
                        UserContract.Column.IMAGE);
        Log.d(TAG, "onCreate with SQL: "+sql);
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists "+UserContract.TABLE);
        onCreate(sqLiteDatabase);
    }
}
