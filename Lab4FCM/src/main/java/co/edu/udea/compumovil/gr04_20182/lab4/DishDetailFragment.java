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
public class DishDetailFragment extends Fragment {

    TextView name, schedule, type, time, ingredients, price, detail;
    ImageView detailImage;
    Button favorite;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    ValueEventListener eventListener;

    public DishDetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dish_detail, container, false);

        name = view.findViewById(R.id.dish_name);
        schedule = view.findViewById(R.id.dish_schedule);
        type = view.findViewById(R.id.dish_type);
        time = view.findViewById(R.id.dish_time);
        ingredients = view.findViewById(R.id.dish_ingredients);
        price = view.findViewById(R.id.dish_price);
        detail = view.findViewById(R.id.dish_detail);

        detailImage = view.findViewById(R.id.detail_dish_image);

        Bundle objectDish = getArguments();
        final DishPojo dishPojo = (DishPojo) objectDish.getSerializable("object");

        name.setText(dishPojo.getName());
        schedule.setText(dishPojo.getSchedule());
        type.setText(dishPojo.getType());
        time.setText(dishPojo.getTime());
        ingredients.setText(dishPojo.getIngredients());
        price.setText(dishPojo.getPrice());
        detail.setText(dishPojo.getDescription());
        Picasso.get().load(dishPojo.getImageUri()).placeholder(R.drawable.pizza_peperonni).error(R.drawable.pizza_peperonni).into(detailImage);

        favorite = view.findViewById(R.id.dish_favorites);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dishSnapshot: dataSnapshot.getChildren()){
                    if(dishSnapshot.child("name").getValue(String.class).equals(dishPojo.getName())){
                        update(dishSnapshot.getKey(), dishPojo);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dishPojo.setFavorite(!dishPojo.isFavorite());
                databaseReference.child("dishes").addValueEventListener(eventListener);

            }
        });

        return view;
    }

    public void update(String key, DishPojo dishPojo){
        databaseReference.child("dishes").child(key).setValue(dishPojo);
    }

    @Override
    public void onPause() {
        databaseReference.child("dishes").removeEventListener(eventListener);
        super.onPause();
    }

}
