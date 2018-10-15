package co.edu.udea.compumovil.gr04_20182.lab3;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrinkDetailFragment extends Fragment {

    TextView name, ingredients, price;
    ImageView detailImage;
    Button favorite;

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

        detailImage = view.findViewById(R.id.detail_drink_image);

        Bundle objectDrink = getArguments();
        final DrinkPojo drinkPojo = (DrinkPojo) objectDrink.getSerializable("object");



        name.setText(drinkPojo.getName());
        price.setText(drinkPojo.getPrice());
        detailImage.setImageURI(drinkPojo.getImageUri());


        favorite = view.findViewById(R.id.drink_favorite);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DrinkDbHelper drinkDbHelper = new DrinkDbHelper(getContext());
                SQLiteDatabase db = drinkDbHelper.getWritableDatabase();

                String criterio = DrinkContract.Column.ID+"= "+drinkPojo.getId();

                ContentValues values = new ContentValues();
                values.put(DrinkContract.Column.FAVORITE,!drinkPojo.isFavorite());

                db.updateWithOnConflict(DrinkContract.TABLE, values, criterio, null, SQLiteDatabase.CONFLICT_IGNORE);
            }
        });

        return view;
    }

}
