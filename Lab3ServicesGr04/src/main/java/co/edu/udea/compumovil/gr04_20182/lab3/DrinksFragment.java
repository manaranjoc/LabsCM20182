package co.edu.udea.compumovil.gr04_20182.lab3;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrinksFragment extends Fragment {

    private ArrayList<DrinkPojo> drinkList;
    RecyclerView recyclerView;

    Activity activity;
    CommunicationDetailsDrinkFragment interfaceComunication;

    public DrinksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drinks, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.drinks_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Bundle bundle = getArguments();

        if(bundle!=null && bundle.containsKey("object")) {
            drinkList = (ArrayList<DrinkPojo>) bundle.getSerializable("object");
        }else{
            getAll();
        }

        DrinkAdapter drinkAdapter = new DrinkAdapter(drinkList);

        drinkAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                interfaceComunication.sendDrink(drinkList.get(recyclerView.getChildAdapterPosition(view)));

            }
        });

        recyclerView.setAdapter(drinkAdapter);
    }

    public void getAll(){
        DrinkDbHelper drinkDbHelper = new DrinkDbHelper(getContext());
        SQLiteDatabase db = drinkDbHelper.getReadableDatabase();
        String consultaSQL = "select * from "+DrinkContract.TABLE;

        Cursor cursor = db.rawQuery(consultaSQL, null);

        drinkList = new ArrayList<>();
        DrinkPojo drink = null;

        while (cursor.moveToNext()){
            drink = new DrinkPojo();
            drink.setId(cursor.getInt(cursor.getColumnIndex(DishContract.Column.ID)));
            drink.setName(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.NAME)));
            drink.setPrice(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.PRICE)));
            drink.setImageUri(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.IMAGE)));
            drink.setIngredients(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.INGREDIENTS)));
            drink.setFavorite(cursor.getInt(cursor.getColumnIndex(DrinkContract.Column.FAVORITE))>0);

            drinkList.add(drink);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof Activity){
            this.activity = (Activity) context;
            interfaceComunication = (CommunicationDetailsDrinkFragment) this.activity;
        }
    }
}
