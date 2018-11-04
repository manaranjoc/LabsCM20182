package co.edu.udea.compumovil.gr04_20182.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CommunicationDetailsDrinkFragment, CommunicationDetailsDishFragment{
    int fragment = 0;
    int fragmetBack = 0;
    private FloatingActionsMenu fabMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fabDish = (FloatingActionButton) findViewById(R.id.fab_dish);
        FloatingActionButton fabDrink = (FloatingActionButton) findViewById(R.id.fab_drink);
        fabMenu = findViewById(R.id.fab);

        fabDish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this,DishActivity.class);
                startActivity(intent);
                fabMenu.collapse();
            }
        });
        fabDrink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PrincipalActivity.this,DrinksActivity.class);
                startActivity(intent);
                fabMenu.collapse();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent = new Intent(this, ReceiverService.class);
        intent.putExtra("interval", Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("interval", "60")));
        Toast.makeText(this, PreferenceManager.getDefaultSharedPreferences(this).getString("interval", "60"), Toast.LENGTH_LONG).show();

        startService(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(fragment == 1){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(findViewById(R.id.list_fragment)!=null){
                ft.replace(R.id.list_fragment, new DishFragment(), "DISH");
            }else {
                ft.replace(R.id.principal_fragment, new DishFragment(), "DISH");
            }
            ft.commit();
        }else if(fragment == 2){
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(findViewById(R.id.list_fragment)!=null){
                ft.replace(R.id.list_fragment, new DrinksFragment(), "DRINK");
            }else {
                ft.replace(R.id.principal_fragment, new DrinksFragment(), "DRINK");
            }
            ft.commit();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.principal, menu);

        MenuItem searchItem = menu.findItem(R.id.item_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(getSupportFragmentManager().findFragmentByTag("DISH") != null && getSupportFragmentManager().findFragmentByTag("DISH").isVisible()){

                    DishFragment dishFragment = new DishFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object",searchDishList(newText));
                    dishFragment.setArguments(bundle);
                    if(fragmetBack>0){
                        getSupportFragmentManager().popBackStack();
                        fragmetBack--;
                    }
                    if(findViewById(R.id.list_fragment)!=null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment, dishFragment, "DISH").addToBackStack(null).commit();
                    }else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.principal_fragment, dishFragment, "DISH").addToBackStack(null).commit();
                    }
                    fragmetBack++;
                }else if(getSupportFragmentManager().findFragmentByTag("DRINK") != null && getSupportFragmentManager().findFragmentByTag("DRINK").isVisible()){
                    DrinksFragment drinksFragment = new DrinksFragment();
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("object",searchDrinkList(newText));
                    drinksFragment.setArguments(bundle);
                    if(fragmetBack>0){
                        getSupportFragmentManager().popBackStack();
                        fragmetBack--;
                    }
                    if(findViewById(R.id.list_fragment)!=null){
                        getSupportFragmentManager().beginTransaction().replace(R.id.list_fragment, drinksFragment, "DRINK").addToBackStack(null).commit();
                    }else {
                        getSupportFragmentManager().beginTransaction().replace(R.id.principal_fragment, drinksFragment, "DRINK").addToBackStack(null).commit();
                    }
                    fragmetBack++;
                }
                return false;
            }


        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            //fabMenu.setEnabled(false);
            fabMenu.setVisibility(View.GONE);

            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.principal_fragment, new SettingsFragment());
            ft.commit();

            fragment = 0;
        }else if(id == R.id.action_close){
            //TODO: agregar metodo de salida de sección.
            SharedPreferences sharedPreferences = this.getSharedPreferences("Logged", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("Logged", false);
            editor.putString("email", null);
            editor.commit();

            Intent closeIntent = new Intent(PrincipalActivity.this, LoginActivity.class);
            PrincipalActivity.this.startActivity(closeIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_dish_list) {
            fabMenu.setVisibility(View.VISIBLE);
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(findViewById(R.id.list_fragment)!=null){
                ft.replace(R.id.list_fragment, new DishFragment(), "DISH");
            }else {
                ft.replace(R.id.principal_fragment, new DishFragment(), "DISH");
            }
            fragment = 1;
            ft.commit();
        } else if (id == R.id.nav_drink_list) {
            fabMenu.setVisibility(View.VISIBLE);
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            if(findViewById(R.id.list_fragment)!=null){
                ft.replace(R.id.list_fragment, new DrinksFragment(), "DRINK");
            }else {
                ft.replace(R.id.principal_fragment, new DrinksFragment(), "DRINK");
            }
            fragment = 2;
            ft.commit();
        } else if (id == R.id.nav_profile) {
            fabMenu.setVisibility(View.GONE);
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.principal_fragment, new ProfileFragment());
            ft.commit();
            fragment = 0;

        } else if (id == R.id.nav_settings) {
            fabMenu.setVisibility(View.GONE);
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.principal_fragment, new SettingsFragment());
            ft.commit();

            fragment = 0;

        } else if (id == R.id.nav_close) {
            //TODO: Agregar cerrado de sección
            SharedPreferences sharedPreferences = this.getSharedPreferences("Logged", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("Logged", false);
            editor.putString("email", null);
            editor.commit();

            Intent closeIntent = new Intent(PrincipalActivity.this, LoginActivity.class);
            closeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            PrincipalActivity.this.startActivity(closeIntent);
            PrincipalActivity.this.finish();
        } else if (id == R.id.nav_about) {
            fabMenu.setVisibility(View.GONE);
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.principal_fragment, new AboutFragment());
            ft.commit();

            fragment = 0;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void sendDrink(DrinkPojo drinkPojo) {
        DrinkDetailFragment drinkDetailFragment = new DrinkDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",drinkPojo);
        drinkDetailFragment.setArguments(bundle);
        if(findViewById(R.id.list_fragment)!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, drinkDetailFragment).addToBackStack(null).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.principal_fragment, drinkDetailFragment).addToBackStack(null).commit();
        }
    }

    @Override
    public void sendDish(DishPojo dishPojo) {
        DishDetailFragment dishDetailFragment = new DishDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", dishPojo);
        dishDetailFragment.setArguments(bundle);
        if(findViewById(R.id.list_fragment)!=null){
            getSupportFragmentManager().beginTransaction().replace(R.id.detail_fragment, dishDetailFragment).addToBackStack(null).commit();
        }else {
            getSupportFragmentManager().beginTransaction().replace(R.id.principal_fragment, dishDetailFragment).addToBackStack(null).commit();
        }
    }

    public ArrayList<DishPojo> searchDishList(String newText){
        ArrayList<DishPojo> dishList = getAllDish();
        ArrayList<DishPojo> tempDish = new ArrayList<>();
        for(DishPojo item: dishList){
            if(item.getName().toLowerCase().contains(newText.toLowerCase())||item.getPrice().toLowerCase().contains(newText.toLowerCase())){
                tempDish.add(item);
            }
        }
        return tempDish;
    }

    public ArrayList<DrinkPojo> searchDrinkList(String newText){
        ArrayList<DrinkPojo> drinkList = getAllDrink();
        ArrayList<DrinkPojo> tempDrink = new ArrayList<>();
        for(DrinkPojo item: drinkList){
            if(item.getName().toLowerCase().contains(newText.toLowerCase())||item.getPrice().toLowerCase().contains(newText.toLowerCase())){
                tempDrink.add(item);
            }
        }
        return tempDrink;
    }

    public ArrayList<DishPojo> getAllDish(){
        DishDbHelper dishDbHelper = new DishDbHelper(this);
        SQLiteDatabase db = dishDbHelper.getReadableDatabase();
        String consultaSQL = "select * from "+DishContract.TABLE;

        Cursor cursor = db.rawQuery(consultaSQL, null);

        ArrayList<DishPojo> dishList = new ArrayList<>();
        DishPojo dish = null;

        while (cursor.moveToNext()){
            dish = new DishPojo();
            dish.setId(cursor.getInt(cursor.getColumnIndex(DishContract.Column.ID)));
            dish.setName(cursor.getString(cursor.getColumnIndex(DishContract.Column.NAME)));
            dish.setType(cursor.getString(cursor.getColumnIndex(DishContract.Column.TYPE)));
            dish.setPrice(cursor.getString(cursor.getColumnIndex(DishContract.Column.PRICE)));
            dish.setTime(cursor.getString(cursor.getColumnIndex(DishContract.Column.TIME)));
            dish.setImageUri(cursor.getString(cursor.getColumnIndex(DishContract.Column.IMAGE)));
            dish.setSchedule(cursor.getString(cursor.getColumnIndex(DishContract.Column.SCHEDULE)));
            dish.setIngredients(cursor.getString(cursor.getColumnIndex(DishContract.Column.INGREDIENTS)));
            dish.setFavorite(cursor.getInt(cursor.getColumnIndex(DishContract.Column.FAVORITE))>0);
            dish.setDescription(cursor.getString(cursor.getColumnIndex(DishContract.Column.DESCRIPTION)));

            dishList.add(dish);
        }
        return dishList;
    }

    public ArrayList<DrinkPojo> getAllDrink(){
        DrinkDbHelper drinkDbHelper = new DrinkDbHelper(this);
        SQLiteDatabase db = drinkDbHelper.getReadableDatabase();
        String consultaSQL = "select * from "+DrinkContract.TABLE;

        Cursor cursor = db.rawQuery(consultaSQL, null);

        ArrayList<DrinkPojo> drinkList = new ArrayList<>();
        DrinkPojo drink = null;

        while (cursor.moveToNext()){
            drink = new DrinkPojo();
            drink.setId(cursor.getInt(cursor.getColumnIndex(DishContract.Column.ID)));
            drink.setName(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.NAME)));
            drink.setPrice(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.PRICE)));
            drink.setImageUri(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.IMAGE)));
            drink.setIngredients(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.INGREDIENTS)));
            drink.setFavorite(cursor.getInt(cursor.getColumnIndex(DrinkContract.Column.FAVORITE))>0);
            drink.setDescription(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.DESCRIPTION)));

            drinkList.add(drink);
        }
        return drinkList;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("fragment",fragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fragment = savedInstanceState.getInt("fragment");
    }
}
