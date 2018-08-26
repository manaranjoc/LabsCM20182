package co.edu.udea.compumovil.gr04_20182.lab1;

import android.Manifest;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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
                updateTextFields();
                mealScheduleUpdate();
                typeOfMealUpdate();
            }
        });


    }

    private void updateTextFields(){

        EditText nameMeal = findViewById(R.id.nameMeal);
        EditText priceMeal = findViewById(R.id.priceMeal);
        EditText ingredientsMeal = findViewById(R.id.ingredientsMeal);
        if(nameMeal.getText().length() == 0 || priceMeal.getText().length() == 0 || ingredientsMeal.getText().length() == 0){
            Toast noMealMessage = Toast.makeText(getApplicationContext(),"One or more text inputs weren't filled", Toast.LENGTH_SHORT);
            noMealMessage.show();
            return;
        }
        TextView name = findViewById(R.id.name);
        TextView price = findViewById(R.id.price);
        TextView ingredients = findViewById(R.id.ingredients);
        name.setText(nameMeal.getText());
        price.setText(priceMeal.getText());
        ingredients.setText(ingredientsMeal.getText());
    }

    private void mealScheduleUpdate(){
        CheckBox morning = findViewById(R.id.morning);
        CheckBox afternoon = findViewById(R.id.afternoon);
        CheckBox night = findViewById(R.id.night);

        String schedule = "";

        if (morning.isChecked()){
            schedule+=" Mor,";
        }
        if (afternoon.isChecked()){
            schedule+=" Aft,";
        }
        if (night.isChecked()){
            schedule+=" Nig";
        }
        if(schedule.endsWith(",")){
            schedule = schedule.substring(0,schedule.length()-1);
        }
        if(!schedule.isEmpty()){
            TextView mealSchedule = findViewById(R.id.mealSchedule);
            mealSchedule.setText(schedule);
        }else{
            Toast noMealMessage = Toast.makeText(getApplicationContext(),"No schedule was selected", Toast.LENGTH_SHORT);
            noMealMessage.show();
        }
    }

    private void typeOfMealUpdate(){
        boolean entrance = ((RadioButton) findViewById(R.id.entrance)).isChecked();
        boolean mainDish = ((RadioButton) findViewById(R.id.main_dish)).isChecked();

        TextView typeOfMeal = findViewById(R.id.type_of_meal);
        if(entrance){
            typeOfMeal.setText("Entrance");
        }else if (mainDish){
            typeOfMeal.setText("Main Dish");
        }else{
            Toast noMealMessage = Toast.makeText(getApplicationContext(),"No type of meal selected", Toast.LENGTH_SHORT);
            noMealMessage.show();
        }
    }

    public void showTimePickerDialog(View v){
        DialogFragment newFragment = new TimePickerFragment();
        FragmentManager manager = getFragmentManager();
        newFragment.show(manager, "timePicker");
    }
}
