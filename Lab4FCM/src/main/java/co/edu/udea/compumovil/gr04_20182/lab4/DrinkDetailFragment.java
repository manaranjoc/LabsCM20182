package co.edu.udea.compumovil.gr04_20182.lab4;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrinkDetailFragment extends Fragment {

    TextView name, ingredients, price, detail;
    ImageView detailImage;
    Button favorite;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    ValueEventListener eventListener;


    public DrinkDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_drink_detail, container, false);

        name = view.findViewById(R.id.detail_drink_name);
        ingredients = view.findViewById(R.id.detail_drink_ingredients);
        price = view.findViewById(R.id.detail_drink_price);
        detail = view.findViewById(R.id.drink_detail);

        detailImage = view.findViewById(R.id.detail_drink_image);

        Bundle objectDrink = getArguments();
        final DrinkPojo drinkPojo = (DrinkPojo) objectDrink.getSerializable("object");

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot drinkSnapshot: dataSnapshot.getChildren()){
                    if(drinkSnapshot.child("name").getValue(String.class).equals(drinkPojo.getName())){
                        update(drinkSnapshot.getKey(), drinkPojo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        name.setText(drinkPojo.getName());
        price.setText(drinkPojo.getPrice());
        detail.setText(drinkPojo.getDescription());
        Picasso.get().load(drinkPojo.getImageUri()).placeholder(R.drawable.coke).error(R.drawable.coke).into(detailImage);



        favorite = view.findViewById(R.id.drink_favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drinkPojo.setFavorite(!drinkPojo.isFavorite());
                databaseReference.child("drinks").addValueEventListener(eventListener);

            }
        });

        return view;
    }

    public void update(String key, DrinkPojo drinkPojo){
        databaseReference.child("drinks").child(key).setValue(drinkPojo);
    }

    @Override
    public void onPause() {
        databaseReference.child("drinks").removeEventListener(eventListener);
        super.onPause();
    }
}
