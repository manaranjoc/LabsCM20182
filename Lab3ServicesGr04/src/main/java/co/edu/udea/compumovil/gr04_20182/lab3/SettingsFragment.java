package co.edu.udea.compumovil.gr04_20182.lab3;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings_screen,rootKey);

        Preference picture = findPreference("profile");
        picture.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getContext())
                                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                    @Override
                                    public void onImageSelected(Uri uri) {
                                        // here is selected uri
                                        String profileUri = uri.toString();
                                        int id = getPreferenceScreen().getSharedPreferences().getInt("id",-1);
                                        UsersDbHelper usersDbHelper = new UsersDbHelper(getActivity().getApplicationContext());
                                        SQLiteDatabase db = usersDbHelper.getWritableDatabase();
                                        String criterio = UserContract.Column.ID+"= "+id;
                                        ContentValues values = new ContentValues();
                                        values.put(UserContract.Column.IMAGE,profileUri);

                                        db.updateWithOnConflict(UserContract.TABLE, values, criterio, null, SQLiteDatabase.CONFLICT_IGNORE);
                                    }
                                })
                                .create();

                        tedBottomPicker.show(getFragmentManager());

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(getContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }


                };
                TedPermission.with(getContext())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
                return false;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        int id = sharedPreferences.getInt("id",-1);
        UsersDbHelper usersDbHelper = new UsersDbHelper(getActivity().getApplicationContext());
        SQLiteDatabase db = usersDbHelper.getWritableDatabase();
        if(s.equals("email")){
            String criterio = UserContract.Column.ID+"= "+id;
            ContentValues values = new ContentValues();
            values.put(UserContract.Column.EMAIL,sharedPreferences.getString("email", ""));

            db.updateWithOnConflict(UserContract.TABLE, values, criterio, null, SQLiteDatabase.CONFLICT_IGNORE);
        }else if(s.equals("name")){
            String criterio = UserContract.Column.ID+"= "+id;
            ContentValues values = new ContentValues();
            values.put(UserContract.Column.NAME,sharedPreferences.getString("name", ""));

            db.updateWithOnConflict(UserContract.TABLE, values, criterio, null, SQLiteDatabase.CONFLICT_IGNORE);
        }else if(s.equals("password")){
            String criterio = UserContract.Column.ID+"= "+id;
            ContentValues values = new ContentValues();
            values.put(UserContract.Column.PASSWORD,sharedPreferences.getString("password", ""));

            db.updateWithOnConflict(UserContract.TABLE, values, criterio, null, SQLiteDatabase.CONFLICT_IGNORE);
        }else if(s.equals("interval")){
            Intent service = new Intent(getContext(), ReceiverService.class);
            service.putExtra("interval", Integer.parseInt(sharedPreferences.getString("interval", "60")));
            getContext().startService(service);
        }
    }
}
