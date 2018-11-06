package co.edu.udea.compumovil.gr04_20182.lab4;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

/**
 * Implementation of App Widget functionality.
 */
public class lastDishWidget extends AppWidgetProvider {

    static RemoteViews updateWidgetListView(Context context, int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.last_dish_widget);

        Intent svcIntent = new Intent(context, dishWidgetService.class);
        svcIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        svcIntent.setData(Uri.parse(svcIntent.toUri(Intent.URI_INTENT_SCHEME)));
        views.setRemoteAdapter(R.id.widget_list_view,svcIntent);
        views.setEmptyView(R.id.widget_list_view, R.layout.widget_item);

        return views;
        /*DishPojo dishPojo = dataSnapshot.getValue(DishPojo.class);
        views.setTextViewText(R.id.nameWidget, dishPojo.getName());
        views.setTextViewText(R.id.typeWidget, dishPojo.getType());
        views.setTextViewText(R.id.priceWidget, dishPojo.getPrice());
        views.setTextViewText(R.id.timeWidget, dishPojo.getTime());*/
        //Picasso.get().load(dishPojo.getImageUri()).into(views, R.id.imageWidget,appWidgetIds);
    }

    @Override
    public void onUpdate(final Context context, final AppWidgetManager appWidgetManager, final int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId,remoteViews);

        }
        super.onUpdate(context, appWidgetManager,appWidgetIds);
        /*DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query lastQuery = databaseReference.child("dishes").orderByKey().limitToLast(1);
        lastQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Widget: ", databaseError.toString());
            }
        });*/

    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

