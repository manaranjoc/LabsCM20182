package co.edu.udea.compumovil.gr04_20182.lab3;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ReceiverService extends Service {

    private Timer timer = new Timer();
    private int interval = 60;
    public static final String UPDATE_ACTION_DISH = "co.edu.udea.compumovil.gr04_20182.lab3.updatedish";
    public static final String UPDATE_ACTION_DRINK = "co.edu.udea.compumovil.gr04_20182.lab3.updatedrink";

    private String TAG = "Receiver Service Http";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //httpGet("http://www.mocky.io/v2/5bb69bd22e00007b00683715", "foods");
        //httpGet("http://www.mocky.io/v2/5bb69ce32e00004d00683718", "drinks");
        Log.d(TAG, interval+"");
        if(intent != null) {
            interval = intent.getIntExtra("interval", 60);
            doSomethingRepeatedly();
        }else {
            interval = Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("interval", "60"));
        }
        Log.d(TAG, interval + "");
        return START_STICKY;
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
                        Intent intent = new Intent(UPDATE_ACTION_DISH);
                        sendBroadcast(intent);
                        Intent intent2 = new Intent(UPDATE_ACTION_DRINK);
                        sendBroadcast(intent2);
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
            createDishDatabase(update);
        }else{
            Drink[] update = gson.fromJson(jsonArray, Drink[].class);
            createDrinkDatabase(update);
        }


    }

    private void createDishDatabase(Food[] dishList) {
        DishDbHelper dishDbHelper = new DishDbHelper(this);
        SQLiteDatabase db = dishDbHelper.getWritableDatabase();
        for (Food dishPojo : dishList) {
            ContentValues values = new ContentValues();

            values.put(DishContract.Column.NAME, dishPojo.getName());
            values.put(DishContract.Column.IMAGE, dishPojo.getImage());
            values.put(DishContract.Column.TYPE, dishPojo.getType());
            values.put(DishContract.Column.PRICE, dishPojo.getCurrency()+dishPojo.getPrice());
            values.put(DishContract.Column.TIME, dishPojo.getTime()+"min");

            String schedule = mealScheduleUpdate(dishPojo.getSchedule());
            values.put(DishContract.Column.SCHEDULE, schedule);
            String ingredients = "";
            for (String ingredient : dishPojo.getIngredients()) {
                ingredients =ingredients+", "+ingredient;
            }
            values.put(DishContract.Column.INGREDIENTS, ingredients);
            //values.put(DishContract.Column.FAVORITE, false);

            try {
                db.insertWithOnConflict(DishContract.TABLE, null, values, SQLiteDatabase.CONFLICT_ABORT);
            } catch (Exception e) {
                Log.d(TAG, "Dish already exist in the database");
            }
        }
        db.close();
        Intent intent = new Intent(UPDATE_ACTION_DISH);
        sendBroadcast(intent);
    }

    public void createDrinkDatabase(Drink[] drinkList){
        DrinkDbHelper drinkDbHelper = new DrinkDbHelper(this);
        SQLiteDatabase db = drinkDbHelper.getWritableDatabase();

        for(Drink drinkPojo: drinkList) {
            ContentValues values = new ContentValues();

            values.put(DrinkContract.Column.NAME, drinkPojo.getName());
            values.put(DrinkContract.Column.PRICE, drinkPojo.getCurrency()+drinkPojo.getPrice());
            values.put(DrinkContract.Column.IMAGE, drinkPojo.getImage());
            //values.put(DrinkContract.Column.INGREDIENTS, drinkPojo.getIngredients());
            //values.put(DrinkContract.Column.FAVORITE, drinkPojo.isFavorite());

            try {
                db.insertWithOnConflict(DrinkContract.TABLE, null, values, SQLiteDatabase.CONFLICT_ABORT);
            } catch (Exception e) {
                Log.d(TAG, "Drink already exist in the database");
            }
        }

        db.close();
        Intent intent = new Intent(UPDATE_ACTION_DRINK);
        sendBroadcast(intent);
    }

    private String mealScheduleUpdate(List<Boolean> checkSchedule){

        String schedule = "";

        if (checkSchedule.get(0)){
            schedule+=" Mor,";
        }
        if (checkSchedule.get(1)){
            schedule+=" Aft,";
        }
        if (checkSchedule.get(2)){
            schedule+=" Nig";
        }

        if(schedule.endsWith(",")){
            schedule = schedule.substring(0,schedule.length()-1);
        }

        return schedule;
    }
}
