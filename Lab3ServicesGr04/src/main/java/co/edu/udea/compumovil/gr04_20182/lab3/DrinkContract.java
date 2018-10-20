package co.edu.udea.compumovil.gr04_20182.lab3;

import android.provider.BaseColumns;

public class DrinkContract {

    public static final String DB_NAME = "drinks.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "drinks";

    public class Column{
        public static final String ID = BaseColumns._ID;

        public static final String NAME = "name";
        public static final String PRICE = "price";
        public static final String IMAGE = "image";
        public static final String INGREDIENTS = "ingredients";
        public static final String FAVORITE = "favorite";
        public static final String DESCRIPTION = "description";
    }
}
