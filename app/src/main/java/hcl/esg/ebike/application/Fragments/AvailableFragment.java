package hcl.esg.ebike.application.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import hcl.esg.ebike.application.Adapter.Adapter;
import hcl.esg.ebike.application.Adapter.CycleAvailableAdapter;
import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.R;

public class AvailableFragment extends Fragment {

    RecyclerView recyclerView;
    CycleAvailableAdapter mainAdapter;

    public AvailableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_availble, container, false);

        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Query query = FirebaseDatabase.getInstance().getReference().child("cycles")
                .orderByChild("availability").equalTo("Available");

        FirebaseRecyclerOptions<Cycles> options =
                new FirebaseRecyclerOptions.Builder<Cycles>()
                        .setQuery(query, Cycles.class)
                        .build();

        mainAdapter = new CycleAvailableAdapter(options);
        recyclerView.setAdapter(mainAdapter);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainAdapter.stopListening();
    }
}