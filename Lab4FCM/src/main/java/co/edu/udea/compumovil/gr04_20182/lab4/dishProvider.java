package co.edu.udea.compumovil.gr04_20182.lab4;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;

public class dishProvider implements RemoteViewsService.RemoteViewsFactory {
    private Context context = null;
    private ArrayList<DishPojo> dishPojos = new ArrayList<>();
    private int appWidgetId;

    private DatabaseReference databaseReference;
    private FirebaseAuth auth;
    private FirebaseUser user;


    public dishProvider(Context context, Intent intent){
        this.context = context;
        appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        Log.d("Widget provider ", "Created? "+dishPojos.size());
        populateListItem();
    }

    @Override
    public void onDataSetChanged() {
        populateListItem();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return dishPojos.size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        final RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(),R.layout.widget_item);
        remoteViews.setTextViewText(R.id.nameWidget, dishPojos.get(i).getName());
        remoteViews.setTextViewText(R.id.typeWidget, dishPojos.get(i).getType());
        remoteViews.setTextViewText(R.id.priceWidget, dishPojos.get(i).getPrice());
        remoteViews.setTextViewText(R.id.timeWidget, dishPojos.get(i).getTime());
        try {
            Log.d("Widget provider ", "Runnning"+dishPojos.size());
            Bitmap b = Picasso.get().load(dishPojos.get(i).getImageUri()).placeholder(R.drawable.pizza_peperonni).error(R.drawable.pizza_peperonni).get();
            remoteViews.setImageViewBitmap(R.id.imageWidget, b);
        }catch(IOException e){
            e.printStackTrace();
        }
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    private void populateListItem(){
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("dishes");
        databaseReference.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DishPojo dishPojo = new DishPojo();
                for(DataSnapshot data: dataSnapshot.getChildren()){
                    dishPojo = data.getValue(DishPojo.class);
                }
                if(dishPojos.isEmpty() /*|| dishPojo.getName()==""*/) {
                    dishPojos.add(dishPojo);
                }else {
                    dishPojos.set(0, dishPojo);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Widget: ", databaseError.toString());
            }
        });
    }
}
