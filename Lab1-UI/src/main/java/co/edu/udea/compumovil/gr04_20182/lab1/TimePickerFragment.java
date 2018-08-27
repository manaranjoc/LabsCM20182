package co.edu.udea.compumovil.gr04_20182.lab1;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.TimePicker;


public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        int hour = 0;
        int minute = 0;

        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hours, int minutes){
        TextView preparationTime = getActivity().findViewById(R.id.preparation_time);
        String min = " min";
        String horas = " h ";
        if(hours == 0){
            preparationTime.setText(minutes+min);
        }else{
            preparationTime.setText(hours+horas+minutes+min);
        }

    }
}
