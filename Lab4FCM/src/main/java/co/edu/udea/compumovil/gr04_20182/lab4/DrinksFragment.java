package co.edu.udea.compumovil.gr04_20182.lab4;


import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrinksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<DrinkPojo> drinkList;
    RecyclerView recyclerView;

    DrinkAdapter drinkAdapter;

    Activity activity;
    CommunicationDetailsDrinkFragment interfaceComunication;

    private BroadcastReceiver broadcastService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_drinks_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_blue_dark));
        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {

            }
        });

        Bundle bundle = getArguments();

        if(bundle!=null && bundle.containsKey("object")) {
            drinkList = (ArrayList<DrinkPojo>) bundle.getSerializable("object");
        }else{
            getAll();
        }

        drinkAdapter = new DrinkAdapter(drinkList);

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
            drink.setDescription(cursor.getString(cursor.getColumnIndex(DrinkContract.Column.DESCRIPTION)));

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

    @Override
    public void onRefresh() {
        loadRecyclerView();
    }

    private void loadRecyclerView(){
        mSwipeRefreshLayout.setRefreshing(true);

        getContext().registerReceiver(broadcastService, new IntentFilter(ReceiverService.UPDATE_ACTION_DRINK));
        Intent intent = new Intent(getActivity(), ReceiverService.class);
        intent.putExtra("interval", Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(getActivity()).getString("interval","60")));
        getActivity().startService(intent);
    }

    private void updateView(){
        getAll();

        drinkAdapter = new DrinkAdapter(drinkList);

        drinkAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                interfaceComunication.sendDrink(drinkList.get(recyclerView.getChildAdapterPosition(view)));

            }
        });

        recyclerView.setAdapter(drinkAdapter);

        getContext().unregisterReceiver(broadcastService);

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
