package co.edu.udea.compumovil.gr04_20182.lab1;

import android.Manifest;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import java.net.URI;
import java.util.ArrayList;

import gun0912.tedbottompicker.TedBottomPicker;

public class ComidasActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comidas);

        sharedPreferences = getApplicationContext().getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        recuperar();

        Button mealGallery = findViewById(R.id.meal_gallery);

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
                                        editor.putString("uri",uri.toString());
                                        editor.apply();

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
                editor.commit();
            }
        });


    }

    private void updateTextFields(){

        EditText nameMeal = findViewById(R.id.name_meal);
        EditText priceMeal = findViewById(R.id.price_meal);
        EditText ingredientsMeal = findViewById(R.id.ingredients_meal);
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
        editor.putString("name", name.getText().toString());
        editor.putString("price", price.getText().toString());
        editor.putString("ingredients", ingredients.getText().toString());
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
            TextView mealSchedule = findViewById(R.id.meal_schedule);
            mealSchedule.setText(schedule);
            editor.putString("mealSchedule", schedule);
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
            typeOfMeal.setText(R.string.entrance);
        }else if (mainDish){
            typeOfMeal.setText(R.string.main_dish);
        }else{
            Toast noMealMessage = Toast.makeText(getApplicationContext(),"No type of meal selected", Toast.LENGTH_SHORT);
            noMealMessage.show();
        }
        editor.putString("typeOfMeal",typeOfMeal.getText().toString());
    }

    public void showTimePickerDialog(View v){
        DialogFragment newFragment = new TimePickerFragment();
        FragmentManager manager = getFragmentManager();
        newFragment.show(manager, "timePicker");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_sub,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.clean:
                cleanAll();
                return true;
            case R.id.exit:
                finish();
                System.exit(0);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void cleanAll(){
        TextView typeOfMeal = findViewById(R.id.type_of_meal),
                name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price),
                preparationTime = findViewById(R.id.preparation_time),
                mealSchedule = findViewById(R.id.meal_schedule);

        typeOfMeal.setText(R.string.type_of_plate);
        name.setText(R.string.dish_name);
        ingredients.setText(R.string.ingredients);
        price.setText(R.string.price);
        preparationTime.setText(R.string.preparation_time_acronym);
        mealSchedule.setText(R.string.schedule);

        ImageView imagePreview = findViewById(R.id.imagePreview);

        imagePreview.setImageResource(android.R.color.transparent);

        EditText nameMeal = findViewById(R.id.name_meal),
            priceMeal = findViewById(R.id.price_meal),
            ingredientsMeal = findViewById(R.id.ingredients_meal);

        nameMeal.setText("");
        priceMeal.setText("");
        ingredientsMeal.setText("");

        CheckBox morning = findViewById(R.id.morning),
                afternoon = findViewById(R.id.afternoon),
                night = findViewById(R.id.night);

        morning.setChecked(false);
        afternoon.setChecked(false);
        night.setChecked(false);

        RadioButton entrance =  findViewById(R.id.entrance);
        RadioButton mainDish = findViewById(R.id.main_dish);

        entrance.setChecked(false);
        mainDish.setChecked(false);

        editor.clear();
        editor.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView typeOfMeal = findViewById(R.id.type_of_meal),
                name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price),
                preparationTime = findViewById(R.id.preparation_time),
                mealSchedule = findViewById(R.id.meal_schedule);

        ImageView imagePreview = findViewById(R.id.imagePreview);

        EditText nameMeal = findViewById(R.id.name_meal),
                priceMeal = findViewById(R.id.price_meal),
                ingredientsMeal = findViewById(R.id.ingredients_meal);

        CheckBox morning = findViewById(R.id.morning),
                afternoon = findViewById(R.id.afternoon),
                night = findViewById(R.id.night);

        RadioButton entrance =  findViewById(R.id.entrance);
        RadioButton mainDish = findViewById(R.id.main_dish);

        outState.putString("typeOfMeal",typeOfMeal.getText().toString());
        outState.putString("name", name.getText().toString());
        outState.putString("ingredients", ingredients.getText().toString());
        outState.putString("price", price.getText().toString());
        outState.putString("preparationTime", preparationTime.getText().toString());
        outState.putString("mealSchedule", mealSchedule.getText().toString());

        outState.putString("nameMeal", nameMeal.getText().toString());
        outState.putString("priceMeal", priceMeal.getText().toString());
        outState.putString("ingredientsMeal", ingredientsMeal.getText().toString());
        outState.putBoolean("morning", morning.isChecked());
        outState.putBoolean("afternoon", afternoon.isChecked());
        outState.putBoolean("night", night.isChecked());

        outState.putBoolean("entrance", entrance.isChecked());
        outState.putBoolean("mainDish", mainDish.isChecked());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView typeOfMeal = findViewById(R.id.type_of_meal),
                name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price),
                preparationTime = findViewById(R.id.preparation_time),
                mealSchedule = findViewById(R.id.meal_schedule);

        ImageView imagePreview = findViewById(R.id.imagePreview);

        EditText nameMeal = findViewById(R.id.name_meal),
                priceMeal = findViewById(R.id.price_meal),
                ingredientsMeal = findViewById(R.id.ingredients_meal);

        CheckBox morning = findViewById(R.id.morning),
                afternoon = findViewById(R.id.afternoon),
                night = findViewById(R.id.night);

        RadioButton entrance =  findViewById(R.id.entrance);
        RadioButton mainDish = findViewById(R.id.main_dish);

        typeOfMeal.setText(savedInstanceState.getString("typeOfMeal"));
        name.setText(savedInstanceState.getString("name"));
        ingredients.setText(savedInstanceState.getString("ingredients"));
        price.setText(savedInstanceState.getString("price"));
        preparationTime.setText(savedInstanceState.getString("preparationTime"));
        mealSchedule.setText(savedInstanceState.getString("mealSchedule"));

        nameMeal.setText(savedInstanceState.getString("nameMeal"));
        priceMeal.setText(savedInstanceState.getString("priceMeal"));
        ingredientsMeal.setText(savedInstanceState.getString("ingredientsMeal"));
        morning.setChecked(savedInstanceState.getBoolean("morning"));
        afternoon.setChecked(savedInstanceState.getBoolean("afternoon"));
        night.setChecked(savedInstanceState.getBoolean("night"));
        entrance.setChecked(savedInstanceState.getBoolean("entrance"));
        mainDish.setChecked(savedInstanceState.getBoolean("mainDish"));
    }

    private void recuperar(){
        TextView typeOfMeal = findViewById(R.id.type_of_meal),
                name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price),
                preparationTime = findViewById(R.id.preparation_time),
                mealSchedule = findViewById(R.id.meal_schedule);

        ImageView imagePreview = findViewById(R.id.imagePreview);

        typeOfMeal.setText(sharedPreferences.getString("typeOfMeal",getString(R.string.type_of_plate)));
        name.setText(sharedPreferences.getString("name",getString(R.string.dish_name)));
        ingredients.setText(sharedPreferences.getString("ingredients",getString(R.string.ingredients)));
        price.setText(sharedPreferences.getString("price",getString(R.string.price)));
        preparationTime.setText(sharedPreferences.getString("preparationTime",getString(R.string.preparation_time_acronym)));
        mealSchedule.setText(sharedPreferences.getString("mealSchedule",getString(R.string.schedule)));
        Uri imageUri;
        try{
            imageUri = Uri.parse(sharedPreferences.getString("uri","sad"));
            imagePreview.setImageURI(imageUri);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
}
