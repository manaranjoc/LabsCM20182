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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DishFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    SwipeRefreshLayout mSwipeRefreshLayout;
    private ArrayList<DishPojo> dishList;
    RecyclerView recyclerView;

    DishAdapter dishAdapter;

    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    Activity activity;
    CommunicationDetailsDishFragment communicationDetailsDishFragment;

    private BroadcastReceiver broadcastService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            updateView();
        }
    };

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

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_dish_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary),
                getResources().getColor(android.R.color.holo_green_dark),
                getResources().getColor(android.R.color.holo_orange_dark),
                getResources().getColor(android.R.color.holo_blue_dark));

        mSwipeRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                //mSwipeRefreshLayout.setRefreshing(true);

                //loadRecyclerViewData();
            }
        });

        Bundle bundle = getArguments();

        if(bundle!=null && bundle.containsKey("object")){
            dishList = (ArrayList<DishPojo>) bundle.getSerializable("object");
            updateView();
        }else{
            getAll();
        }

    }

    public void getAll(){
        dishList = new ArrayList<>();
        database.child("dishes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot dishSnapshot: dataSnapshot.getChildren()){
                    dishList.add(dishSnapshot.getValue(DishPojo.class));
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
            communicationDetailsDishFragment = (CommunicationDetailsDishFragment) this.activity;
        }
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(true);
        updateView();
    }

    private void updateView(){
        getAll();

        dishAdapter = new DishAdapter(dishList);

        dishAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                communicationDetailsDishFragment.sendDish(dishList.get(recyclerView.getChildAdapterPosition(view)));
            }
        });

        recyclerView.setAdapter(dishAdapter);

        mSwipeRefreshLayout.setRefreshing(false);
    }
}
