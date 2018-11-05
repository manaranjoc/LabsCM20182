package co.edu.udea.compumovil.gr04_20182.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;

public class PrincipalActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, CommunicationDetailsDrinkFragment, CommunicationDetailsDishFragment ,GoogleApiClient.OnConnectionFailedListener{
    int fragment = 0;
    int fragmetBack = 0;
    private FloatingActionsMenu fabMenu;

    private FirebaseAuth mFirebaseAuth;

    private GoogleApiClient mGoogleApiClient;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(PreferenceManager.getDefaultSharedPreferences(this).getBoolean("allow_notifications", false)){
            FirebaseMessaging.getInstance().subscribeToTopic("notifications");
        }else{
            FirebaseMessaging.getInstance().unsubscribeFromTopic("notifications");
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();

        mFirebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

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
            public boolean onQueryTextChange(final String newText) {
                if(getSupportFragmentManager().findFragmentByTag("DISH") != null && getSupportFragmentManager().findFragmentByTag("DISH").isVisible()){
                    databaseReference.child("dishes").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<DishPojo> dishList = new ArrayList<>();
                            for(DataSnapshot dishSnapshot: dataSnapshot.getChildren()){
                                dishList.add(dishSnapshot.getValue(DishPojo.class));
                            }
                            dishList = searchDishList(dishList, newText);
                            searchDish(dishList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                }else if(getSupportFragmentManager().findFragmentByTag("DRINK") != null && getSupportFragmentManager().findFragmentByTag("DRINK").isVisible()){
                    databaseReference.child("drinks").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            ArrayList<DrinkPojo> drinkList = new ArrayList<>();
                            for(DataSnapshot drinkSnapshot: dataSnapshot.getChildren()){
                                drinkList.add(drinkSnapshot.getValue(DrinkPojo.class));
                            }
                            drinkList = searchDrinkList(drinkList, newText);
                            searchDrink(drinkList);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
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
            /*SharedPreferences sharedPreferences = this.getSharedPreferences("Logged", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("Logged", false);
            editor.putString("email", null);
            editor.commit();*/

            mFirebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);

            Intent closeIntent = new Intent(PrincipalActivity.this, LoginActivity.class);
            closeIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            PrincipalActivity.this.startActivity(closeIntent);
            PrincipalActivity.this.finish();
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
            /*SharedPreferences sharedPreferences = this.getSharedPreferences("Logged", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            editor.putBoolean("Logged", false);
            editor.putString("email", null);
            editor.commit();*/

            mFirebaseAuth.signOut();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient);

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

    public ArrayList<DishPojo> searchDishList(ArrayList<DishPojo> dishList, String newText){
        ArrayList<DishPojo> tempDish = new ArrayList<>();
        for(DishPojo item: dishList){
            if(item.getName().toLowerCase().contains(newText.toLowerCase())||item.getPrice().toLowerCase().contains(newText.toLowerCase())){
                tempDish.add(item);
            }
        }
        return tempDish;
    }

    public void searchDish(ArrayList<DishPojo> dishes){
        DishFragment dishFragment = new DishFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",dishes);
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
    }

    public ArrayList<DrinkPojo> searchDrinkList(ArrayList<DrinkPojo> drinkList, String newText){
        ArrayList<DrinkPojo> tempDrink = new ArrayList<>();
        for(DrinkPojo item: drinkList){
            if(item.getName().toLowerCase().contains(newText.toLowerCase())||item.getPrice().toLowerCase().contains(newText.toLowerCase())){
                tempDrink.add(item);
            }
        }
        return tempDrink;
    }

    public void searchDrink(ArrayList<DrinkPojo> drinkPojos){
        DrinksFragment drinksFragment = new DrinksFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("object",drinkPojos);
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Principal: ", "ConnectionFailed");
    }
}
