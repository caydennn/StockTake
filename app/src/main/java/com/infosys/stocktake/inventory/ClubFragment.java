package com.infosys.stocktake.inventory;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.infosys.stocktake.R;
import com.infosys.stocktake.firebase.StockTakeFirebase;
import com.infosys.stocktake.inventory.items.ClubRecyclerViewAdapter;
import com.infosys.stocktake.inventory.items.ItemRecyclerViewAdapter;
import com.infosys.stocktake.models.Club;
import com.infosys.stocktake.models.Item;

import java.util.ArrayList;

//import info.androidhive.viewpager2.R;

public class ClubFragment extends Fragment {

    private TextView textView;
    private ArrayList<String> mClubNames = new ArrayList<>();
    private ArrayList<String> mClubImages = new ArrayList<>();
    private static final String TAG = "Club Fragment";
    private StockTakeFirebase<Club> stockTakeFirebase;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.inventory_list,container,false);
    String message = getArguments().getString("message");
    stockTakeFirebase = new StockTakeFirebase<Club>(Club.class, "clubs");
    return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {

        super.onActivityCreated(savedInstanceState);
        populateItems();
    }

    private void populateItems(){
        Log.d(TAG,"Populating items...");
        Task<ArrayList<Club>> populateTask = stockTakeFirebase.getCollection();
        populateTask.addOnSuccessListener(new OnSuccessListener<ArrayList<Club>>() {
            @Override
            public void onSuccess(ArrayList<com.infosys.stocktake.models.Club> clubs) {
                Log.d(TAG,"Accessed firebase! populating items now...");
                for(Club club:clubs){
                    mClubNames.add(club.getClubName());
                    initRecyclerView();
                }
            }
        });
        populateTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e(TAG,"Failed to retrieve items :(, exception is: ", e);
            }
        });

    }


    private void initRecyclerView(){
        RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        ClubRecyclerViewAdapter recyclerAdapter = new ClubRecyclerViewAdapter(mClubNames,mClubImages, getActivity());
        recyclerView.setAdapter(recyclerAdapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
    }
}