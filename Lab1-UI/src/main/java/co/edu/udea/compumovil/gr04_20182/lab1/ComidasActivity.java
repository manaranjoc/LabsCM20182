package co.edu.udea.compumovil.gr04_20182.lab1;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class ComidasActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comidas);

        Button mealGallery = findViewById(R.id.mealGallery);

        mealGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(ComidasActivity.this)
                                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                    @Override
                                    public void onImageSelected(Uri uri) {
                                        // here is selected uri
                                        ImageView imagePreview = findViewById(R.id.imagePreview);
                                        imagePreview.setImageURI(uri);
                                    }
                                })
                                .create();

                        tedBottomPicker.show(getSupportFragmentManager());

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(ComidasActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }


                };
                TedPermission.with(ComidasActivity.this)
                        .setPermissionListener(permissionlistener)
                        .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                        .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .check();
            }
        });

        Button updatePreviewMeal = findViewById(R.id.updatePreviewMeal);
        updatePreviewMeal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText nameMeal = findViewById(R.id.nameMeal);
                EditText priceMeal = findViewById(R.id.priceMeal);
                EditText ingredientsMeal = findViewById(R.id.ingredientsMeal);

                TextView name = findViewById(R.id.name);
                TextView price = findViewById(R.id.price);
                TextView ingredients = findViewById(R.id.ingredients);
                name.setText(nameMeal.getText());
                price.setText(priceMeal.getText());
                ingredients.setText(ingredientsMeal.getText());
            }
        });


    }
}
