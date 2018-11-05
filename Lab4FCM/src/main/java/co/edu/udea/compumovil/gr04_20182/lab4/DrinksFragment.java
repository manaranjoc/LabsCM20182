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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrinksFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<DrinkPojo> drinkList;
    RecyclerView recyclerView;

    DrinkAdapter drinkAdapter;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

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

            updateView();
        }else{
            getAll();
        }


    }

    public void getAll(){
        drinkList = new ArrayList<>();
        database.child("drinks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot drinkSnapshot: dataSnapshot.getChildren()){
                    drinkList.add(drinkSnapshot.getValue(DrinkPojo.class));
                }
                updateView();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        mSwipeRefreshLayout.setRefreshing(true);
        updateView();
    }

    private void updateView(){

        drinkAdapter = new DrinkAdapter(drinkList);

        drinkAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                interfaceComunication.sendDrink(drinkList.get(recyclerView.getChildAdapterPosition(view)));

            }
        });

        recyclerView.setAdapter(drinkAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
