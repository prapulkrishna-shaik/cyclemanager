package hcl.esg.ebike.application.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import hcl.esg.ebike.application.Adapter.HistoryAdapter;
import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.Models.History;
import hcl.esg.ebike.application.R;

public class HistoryFragment extends Fragment {

    RecyclerView recyclerView;
    HistoryAdapter mainAdapter;
    private DatabaseReference cyclesRef;
    private SearchView searchView;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        searchView = view.findViewById(R.id.searchView);


        cyclesRef = FirebaseDatabase.getInstance().getReference().child("History");

        FirebaseRecyclerOptions<History> options =
                new FirebaseRecyclerOptions.Builder<History>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("History"), History.class)
                        .build();

        mainAdapter = new HistoryAdapter(options);
        recyclerView.setAdapter(mainAdapter);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchCycles(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchCycles(newText);
                return false;
            }
        });


        return view;
    }
    private void searchCycles(String searchText) {
        Query searchQuery = cyclesRef.orderByChild("cycleId").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<History> options =
                new FirebaseRecyclerOptions.Builder<History>()
                        .setQuery(searchQuery, History.class)
                        .build();

        mainAdapter.updateOptions(options);
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
