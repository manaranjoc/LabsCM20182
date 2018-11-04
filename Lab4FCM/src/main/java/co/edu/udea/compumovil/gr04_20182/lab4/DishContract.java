package co.edu.udea.compumovil.gr04_20182.lab4;

import android.provider.BaseColumns;

public class DishContract {

    public static final String DB_NAME = "dishes.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "dishes";

    public class Column{
        public static final String ID = BaseColumns._ID;

        public static final String NAME = "name";
        public static final String TYPE = "type";
        public static final String PRICE = "price";
        public static final String TIME = "time";
        public static final String IMAGE = "image";
        public static final String SCHEDULE = "schedule";
        public static final String INGREDIENTS = "ingredients";
        public static final String FAVORITE = "favorite";
        public static final String DESCRIPTION = "description";
    }

}
