package co.edu.udea.compumovil.gr04_20182.lab2;


import android.content.Intent;
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
import android.widget.Adapter;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DishFragment extends Fragment {

    private ArrayList<DishPojo> dishList;
    RecyclerView recyclerView;

    public DishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dish, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.dish_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getAll();
        DishAdapter dishAdapter = new DishAdapter(dishList);

        dishAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DishDetail.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("drink", dishList.get(recyclerView.getChildAdapterPosition(view)));

                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(dishAdapter);

    }

    public void getAll(){
        DishDbHelper dishDbHelper = new DishDbHelper(getContext());
        SQLiteDatabase db = dishDbHelper.getReadableDatabase();
        String consultaSQL = "select * from "+DishContract.TABLE;

        Cursor cursor = db.rawQuery(consultaSQL, null);

        dishList = new ArrayList<>();
        DishPojo dish = null;

        while (cursor.moveToNext()){
            dish = new DishPojo();
            dish.setName(cursor.getString(cursor.getColumnIndex(DishContract.Column.NAME)));
            dish.setType(cursor.getString(cursor.getColumnIndex(DishContract.Column.TYPE)));
            dish.setPrice(cursor.getString(cursor.getColumnIndex(DishContract.Column.PRICE)));
            dish.setTime(cursor.getString(cursor.getColumnIndex(DishContract.Column.TIME)));
            dish.setImageUri(cursor.getString(cursor.getColumnIndex(DishContract.Column.IMAGE)));

            dishList.add(dish);
        }

    }
}
