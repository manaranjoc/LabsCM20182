package co.edu.udea.compumovil.gr04_20182.lab4;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    private static final int RC_SIGN_IN = 9001;

    private Boolean saveLogin;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Button login = findViewById(R.id.login);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.email);
                EditText password = findViewById(R.id.password);

                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    Log.d("Login: ", "signInWithEmail: success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    updateUI(user);
                                }else{
                                    Log.w("Login: ", "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

                    /*UsersDbHelper usersDbHelper = new UsersDbHelper(getApplicationContext());
                    SQLiteDatabase db = usersDbHelper.getWritableDatabase();

                    String consultaSQL = "select * from " + UserContract.TABLE;

                    Cursor cursor = db.rawQuery(consultaSQL, null);

                    Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
                    boolean flag = true;
                    while (cursor.moveToNext()) {
                        if (cursor.getString(cursor.getColumnIndex(UserContract.Column.EMAIL)).equals(email.getText().toString())) {
                            if (cursor.getString(cursor.getColumnIndex(UserContract.Column.PASSWORD)).equals(password.getText().toString())) {
                                SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
                                SharedPreferences sharedPreferencesS = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                SharedPreferences.Editor editorS = sharedPreferencesS.edit();
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putBoolean("Logged", true);
                                editor.putString("email", email.getText().toString());
                                editorS.putInt("id", cursor.getInt(cursor.getColumnIndex(UserContract.Column.ID)));
                                editor.commit();
                                editorS.commit();

                                LoginActivity.this.startActivity(intent);

                                flag = false;
                            } else {
                                break;
                            }
                        }
                    }
                    if (flag) {
                        Toast.makeText(getApplicationContext(), "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                    }*/
            }
        });

        Button register = findViewById(R.id.register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent registerIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(registerIntent);
            }
        });


        findViewById(R.id.button_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!saveLogin) {
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                }
                signIn();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.setDefaultValues(this, R.xml.settings_screen, false);
        SharedPreferences sharedPreferencesS = PreferenceManager.getDefaultSharedPreferences(this);
        saveLogin = sharedPreferencesS.getBoolean("save_login", true);

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(saveLogin) {
            updateUI(currentUser);
        }else{
            if(currentUser!=null){
                mAuth.signOut();
            }
        }
    }

    public void updateUI(FirebaseUser currentUser){


        /*SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        boolean isUserLogged = sharedPreferences.getBoolean("Logged", false);*/
        if(currentUser != null){
            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            LoginActivity.this.startActivity(intent);
        }
    }

    private void signIn(){
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Login: ", "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d("Login: ", "ConnectionFailed");
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct){
        Log.d("Login: ", "firebaseAuthWithGooogle:" + acct.getId());
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d("Login: ", "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w("Login: ", "signInWithCredential", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("Login: ", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                    }
                });
    }
}
