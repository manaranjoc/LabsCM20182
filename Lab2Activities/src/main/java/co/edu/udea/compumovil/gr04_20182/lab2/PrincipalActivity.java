package co.edu.udea.compumovil.gr04_20182.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CommunicationDetailsDrinkFragment, CommunicationDetailsDishFragment{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_principal);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
            return true;
        }else if(id == R.id.action_close){
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
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.principal_fragment, new DishFragment());
            ft.commit();
        } else if (id == R.id.nav_drink_list) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.principal_fragment, new DrinksFragment());
            ft.commit();
        } else if (id == R.id.nav_profile) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

            ft.replace(R.id.principal_fragment, new ProfileFragment());
            ft.commit();

        } else if (id == R.id.nav_settings) {
            android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.replace(R.id.principal_fragment, new SettingsFragment());
            ft.commit();

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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

        getSupportFragmentManager().beginTransaction().replace(R.id.principal_fragment,drinkDetailFragment).addToBackStack(null).commit();
    }

    @Override
    public void sendDish(DishPojo dishPojo) {
        DishDetailFragment dishDetailFragment = new DishDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object", dishPojo);
        dishDetailFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.principal_fragment,dishDetailFragment).addToBackStack(null).commit();
    }
}
