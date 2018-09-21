package co.edu.udea.compumovil.gr04_20182.lab2;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {


    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView name = view.findViewById(R.id.names);
        TextView email = view.findViewById(R.id.email);
        ImageView imageView = view.findViewById(R.id.profile_picture);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Logged", Context.MODE_PRIVATE);
        String savedEmail = sharedPreferences.getString("email", null);

        email.setText(savedEmail);

        UsersDbHelper dbHelper = new UsersDbHelper(getContext());
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String consultaSQL = "select * from "+UserContract.TABLE;

        Cursor cursor = db.rawQuery(consultaSQL, null);

        while (cursor.moveToNext()){
            if(cursor.getString(cursor.getColumnIndex(UserContract.Column.EMAIL)).equals(savedEmail)){
                name.setText(cursor.getString(cursor.getColumnIndex(UserContract.Column.NAME)));
                try {
                    Uri imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(UserContract.Column.IMAGE)));
                    imageView.setImageURI(imageUri);
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            }
        }


    }
}
