package co.edu.udea.compumovil.gr04_20182.lab2;

import android.provider.BaseColumns;

public class UserContract {

    public static final String DB_NAME = "dishandmeals.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "users";

    public class Column{
        public static final String ID = BaseColumns._ID;
        public static final String NAME = "name";
        public static final String EMAIL = "email";
        public static final String PASSWORD = "password";
    }
}
