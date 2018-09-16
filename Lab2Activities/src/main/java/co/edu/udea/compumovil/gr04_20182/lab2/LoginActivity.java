package co.edu.udea.compumovil.gr04_20182.lab2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
        boolean isUserLogged = sharedPreferences.getBoolean("Logged", false);
        if(isUserLogged){
            Intent intent = new Intent(LoginActivity.this, PrincipalActivity.class);
            LoginActivity.this.startActivity(intent);
        }else {
            Button login = findViewById(R.id.login);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EditText email = findViewById(R.id.email);
                    EditText password = findViewById(R.id.password);

                    UsersDbHelper usersDbHelper = new UsersDbHelper(getApplicationContext());
                    SQLiteDatabase db = usersDbHelper.getWritableDatabase();

                    String consultaSQL = "select * from " + UserContract.TABLE;

                    Cursor cursor = db.rawQuery(consultaSQL, null);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    boolean flag = true;
                    while (cursor.moveToNext()) {
                        if (cursor.getString(cursor.getColumnIndex(UserContract.Column.EMAIL)).equals(email.getText().toString())) {
                            if (cursor.getString(cursor.getColumnIndex(UserContract.Column.PASSWORD)).equals(password.getText().toString())) {
                                SharedPreferences sharedPreferences = getSharedPreferences("Logged", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                editor.putBoolean("Logged", true);
                                editor.putString("email", email.getText().toString());
                                editor.commit();

                                LoginActivity.this.startActivity(intent);
                                flag = false;
                            } else {
                                break;
                            }
                        }
                    }
                    if (flag) {
                        Toast.makeText(getApplicationContext(), "Wrong Email or Password", Toast.LENGTH_SHORT).show();
                    }
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
        }
    }
}
