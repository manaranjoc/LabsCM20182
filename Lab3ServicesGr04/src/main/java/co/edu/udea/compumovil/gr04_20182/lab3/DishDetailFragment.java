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
public class DishDetailFragment extends Fragment {

    TextView name, schedule, type, time, ingredients, price;
    ImageView detailImage;
    Button favorite;

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

        detailImage = view.findViewById(R.id.detail_dish_image);

        Bundle objectDish = getArguments();
        final DishPojo dishPojo = (DishPojo) objectDish.getSerializable("object");

        name.setText(dishPojo.getName());
        schedule.setText(dishPojo.getSchedule());
        type.setText(dishPojo.getType());
        time.setText(dishPojo.getTime());
        ingredients.setText(dishPojo.getIngredients());
        price.setText(dishPojo.getPrice());
        detailImage.setImageURI(dishPojo.getImageUri());

        favorite = view.findViewById(R.id.dish_favorites);

        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DishDbHelper dishDbHelper = new DishDbHelper(getContext());
                SQLiteDatabase db = dishDbHelper.getWritableDatabase();

                String criterio = DishContract.Column.ID+"= "+dishPojo.getId();

                ContentValues values = new ContentValues();
                values.put(DishContract.Column.FAVORITE,!dishPojo.isFavorite());

                db.updateWithOnConflict(DishContract.TABLE, values, criterio, null, SQLiteDatabase.CONFLICT_IGNORE);

            }
        });

        return view;
    }

}
