package co.edu.udea.compumovil.gr04_20182.lab4;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class RegisterActivity extends AppCompatActivity{

    private String uriApp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Button changeImage = findViewById(R.id.change_image);

        changeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(getApplicationContext())
                                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                    @Override
                                    public void onImageSelected(Uri uri) {
                                        // here is selected uri
                                        uriApp = uri.toString();
                                        ImageView imagePreview = findViewById(R.id.image_preview);
                                        imagePreview.setImageURI(uri);

                                    }
                                })
                                .create();

                        tedBottomPicker.show(getSupportFragmentManager());

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(getApplicationContext(), "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }


                };
                TedPermission.with(getApplicationContext())
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });

        Button update = findViewById(R.id.save);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText name = findViewById(R.id.dish_name);
                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);

                UsersDbHelper usersDbHelper = new UsersDbHelper(getApplicationContext());
                SQLiteDatabase db = usersDbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();

                values.put(UserContract.Column.NAME, name.getText().toString());
                values.put(UserContract.Column.EMAIL, email.getText().toString());
                values.put(UserContract.Column.PASSWORD, password.getText().toString());
                values.put(UserContract.Column.IMAGE, uriApp);
                boolean flag = false;
                try {
                    db.insertWithOnConflict(UserContract.TABLE, null, values, SQLiteDatabase.CONFLICT_ABORT);
                    db.close();
                    flag = true;
                }catch (Exception e){
                    Toast.makeText(getApplicationContext(), "The User is already in the database", Toast.LENGTH_SHORT).show();
                }
                if(flag){
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    RegisterActivity.this.startActivity(intent);
                    RegisterActivity.this.finish();
                }


            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("Uri", uriApp);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        uriApp = savedInstanceState.getString("Uri");

        ImageView profilePic = findViewById(R.id.image_preview);
        Uri imageUri;
        try{
            imageUri = Uri.parse(uriApp);
            profilePic.setImageURI(imageUri);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
