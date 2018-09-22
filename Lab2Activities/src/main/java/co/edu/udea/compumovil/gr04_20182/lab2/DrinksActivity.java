package co.edu.udea.compumovil.gr04_20182.lab2;

import android.Manifest;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

public class DrinksActivity extends AppCompatActivity {

    DrinkPojo drinkPojo = new DrinkPojo();

    Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drinks);

        Button mealGallery = findViewById(R.id.meal_gallery);

        mealGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PermissionListener permissionlistener = new PermissionListener() {
                    @Override
                    public void onPermissionGranted() {
                        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(DrinksActivity.this)
                                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                                    @Override
                                    public void onImageSelected(Uri uri) {
                                        // here is selected uri
                                        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("bebidas", context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("uri",uri.toString());
                                        editor.apply();

                                        ImageView imagePreview = findViewById(R.id.imagePreview);
                                        imagePreview.setImageURI(uri);

                                        drinkPojo.setImageUri(uri.toString());

                                    }
                                })
                                .create();

                        tedBottomPicker.show(getSupportFragmentManager());

                    }

                    @Override
                    public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                        Toast.makeText(DrinksActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                    }


                };
                TedPermission.with(DrinksActivity.this)
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
                savePreferences();
                drinkPojo.setFavorite(false);

                createDrinkDatabase();
            }
        });

        loadPreferences();
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
        drinkPojo.setName(nameMeal.getText().toString());
        TextView price = findViewById(R.id.price);
        drinkPojo.setPrice(priceMeal.getText().toString());
        TextView ingredients = findViewById(R.id.ingredients);
        drinkPojo.setIngredients(ingredientsMeal.getText().toString());
        name.setText(nameMeal.getText());
        price.setText(priceMeal.getText());
        ingredients.setText(ingredientsMeal.getText());
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
        TextView name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price);

        name.setText(R.string.dish_name);
        ingredients.setText(R.string.ingredients);
        price.setText(R.string.price);

        ImageView imagePreview = findViewById(R.id.imagePreview);

        imagePreview.setImageResource(android.R.color.transparent);

        EditText nameMeal = findViewById(R.id.name_meal),
            priceMeal = findViewById(R.id.price_meal),
            ingredientsMeal = findViewById(R.id.ingredients_meal);

        nameMeal.setText("");
        priceMeal.setText("");
        ingredientsMeal.setText("");

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        TextView name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price);

        ImageView imagePreview = findViewById(R.id.imagePreview);

        EditText nameMeal = findViewById(R.id.name_meal),
                priceMeal = findViewById(R.id.price_meal),
                ingredientsMeal = findViewById(R.id.ingredients_meal);

        outState.putString("name", name.getText().toString());
        outState.putString("ingredients", ingredients.getText().toString());
        outState.putString("price", price.getText().toString());

        outState.putString("nameMeal", nameMeal.getText().toString());
        outState.putString("priceMeal", priceMeal.getText().toString());
        outState.putString("ingredientsMeal", ingredientsMeal.getText().toString());


    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        TextView name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price);

        ImageView imagePreview = findViewById(R.id.imagePreview);

        EditText nameMeal = findViewById(R.id.name_meal),
                priceMeal = findViewById(R.id.price_meal),
                ingredientsMeal = findViewById(R.id.ingredients_meal);

        name.setText(savedInstanceState.getString("name"));
        ingredients.setText(savedInstanceState.getString("ingredients"));
        price.setText(savedInstanceState.getString("price"));

        nameMeal.setText(savedInstanceState.getString("nameMeal"));
        priceMeal.setText(savedInstanceState.getString("priceMeal"));
        ingredientsMeal.setText(savedInstanceState.getString("ingredientsMeal"));
    }

    public void loadPreferences(){
        SharedPreferences preferencias= getSharedPreferences("bebidas", context.MODE_PRIVATE);
        TextView name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price);


    name.setText(preferencias.getString("name", getString(R.string.drink)));
        ingredients.setText(preferencias.getString("ingredients", getString(R.string.ingredients)));
        price.setText(preferencias.getString("price", getString(R.string.price)));
        ImageView imagePreview = findViewById(R.id.imagePreview);

        Uri imageUri;
        try{
            imageUri = Uri.parse(preferencias.getString("uri","sad"));
            imagePreview.setImageURI(imageUri);
        }catch (IllegalArgumentException e){
            e.printStackTrace();
        }
    }
    public void savePreferences(){
        TextView name = findViewById(R.id.name),
                ingredients = findViewById(R.id.ingredients),
                price = findViewById(R.id.price);


        SharedPreferences preferencias= getSharedPreferences("bebidas", context.MODE_PRIVATE);
        SharedPreferences.Editor editor= preferencias.edit();
        editor.putString("name", name.getText().toString());
        editor.putString("ingredients", ingredients.getText().toString());
        editor.putString("price", price.getText().toString());

        editor.commit();
    }

    public void createDrinkDatabase(){
        DrinkDbHelper drinkDbHelper = new DrinkDbHelper(this);
        SQLiteDatabase db = drinkDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(DrinkContract.Column.NAME, drinkPojo.getName());
        values.put(DrinkContract.Column.PRICE, drinkPojo.getPrice());
        values.put(DrinkContract.Column.IMAGE,drinkPojo.getImageUri().toString());
        values.put(DrinkContract.Column.INGREDIENTS, drinkPojo.getIngredients());
        values.put(DrinkContract.Column.FAVORITE, drinkPojo.isFavorite());

        try {
            db.insertWithOnConflict(DrinkContract.TABLE, null, values, SQLiteDatabase.CONFLICT_ABORT);
        }catch (Exception e){
            Toast.makeText(getApplicationContext(), "The Drinks is already in the database", Toast.LENGTH_SHORT).show();
        }finally {
            db.close();
        }
    }
}
