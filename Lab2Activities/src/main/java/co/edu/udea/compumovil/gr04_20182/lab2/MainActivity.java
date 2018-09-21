package co.edu.udea.compumovil.gr04_20182.lab2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);

        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

        this.startActivity(intent);
    }
}
