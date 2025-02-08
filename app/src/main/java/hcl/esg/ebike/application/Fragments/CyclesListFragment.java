package hcl.esg.ebike.application.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import hcl.esg.ebike.application.Adapter.Adapter;
import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.R;

public class CyclesListFragment extends Fragment {

    private RecyclerView recyclerView;
    private Adapter mainAdapter;
    private SearchView searchView;
    private DatabaseReference cyclesRef;

    public CyclesListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cycles_list, container, false);

        recyclerView = view.findViewById(R.id.rv);
        searchView = view.findViewById(R.id.searchView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cyclesRef = FirebaseDatabase.getInstance().getReference().child("cycles");

        FirebaseRecyclerOptions<Cycles> options =
                new FirebaseRecyclerOptions.Builder<Cycles>()
                        .setQuery(cyclesRef, Cycles.class)
                        .build();

        mainAdapter = new Adapter(options, getContext());
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
        Query searchQuery = cyclesRef.orderByChild("cid").startAt(searchText).endAt(searchText + "\uf8ff");

        FirebaseRecyclerOptions<Cycles> options =
                new FirebaseRecyclerOptions.Builder<Cycles>()
                        .setQuery(searchQuery, Cycles.class)
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
