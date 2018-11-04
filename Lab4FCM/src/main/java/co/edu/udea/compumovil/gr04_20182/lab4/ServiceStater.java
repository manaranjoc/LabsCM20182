package co.edu.udea.compumovil.gr04_20182.lab4;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

public class ServiceStater extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        /*Intent timer = new Intent(context, ServiceStater.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 23432424, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+6000, pendingIntent);*/

        Toast.makeText(context, "Iniciando Servicio Back",
                Toast.LENGTH_LONG).show();

        Log.d("ServiceStarter ", "Corriendo");

        Intent i = new Intent("co.edu.udea.compumovil.gr04_20182.lab3.ReceiverService");
        i.setClass(context, ReceiverService.class);
        i.putExtra("interval", Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(context).getString("interval","60")));
        context.startService(i);

    }
}
