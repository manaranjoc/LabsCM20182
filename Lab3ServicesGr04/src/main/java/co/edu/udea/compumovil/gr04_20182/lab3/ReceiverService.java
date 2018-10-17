package co.edu.udea.compumovil.gr04_20182.lab3;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

public class ReceiverService extends Service {

    private Timer timer = new Timer();
    private int interval = 60;

    private String TAG = "Receiver Service Http";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        httpGet("http://www.mocky.io/v2/5bb69bd22e00007b00683715", "foods");
        httpGet("http://www.mocky.io/v2/5bb69ce32e00004d00683718", "drinks");
        Log.d(TAG, interval+"");
        interval = intent.getIntExtra("interval",60);
        doSomethingRepeatedly();
        Log.d(TAG, interval+"");


        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void doSomethingRepeatedly(){
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                httpGet("http://www.mocky.io/v2/5bb69bd22e00007b00683715", "foods");
                httpGet("http://www.mocky.io/v2/5bb69ce32e00004d00683718", "drinks");
                Log.d("Service", "Running only running");
            }
        }, 0,interval*1000);
    }

    public void httpGet(String url, final String memberName){
        RequestQueue queue = Volley.newRequestQueue(this);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        parserData(response.toString(), memberName);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error: "+error.toString());
                    }
                }
        );
        queue.add(jsonObjectRequest);
    }

    private void parserData(String data, String memberName){
        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject) jsonParser.parse(data);
        JsonArray jsonArray = jo.getAsJsonArray(memberName);

        Gson gson = new Gson();
        if(memberName.equals("foods")){
            Food[] update = gson.fromJson(jsonArray, Food[].class);
        }else{
            Drink[] update = gson.fromJson(jsonArray, Drink[].class);
        }


    }
}
