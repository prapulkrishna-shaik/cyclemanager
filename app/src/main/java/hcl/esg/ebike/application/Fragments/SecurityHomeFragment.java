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

import hcl.esg.ebike.application.Adapter.HomeAdapter;
import hcl.esg.ebike.application.Adapter.SecurityAdapter;
import hcl.esg.ebike.application.Models.History;
import hcl.esg.ebike.application.R;

public class SecurityHomeFragment extends Fragment {

    RecyclerView recyclerView;
    SecurityAdapter mainAdapter;

    public SecurityHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_security_home, container, false);

        recyclerView = view.findViewById(R.id.rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        FirebaseRecyclerOptions<History> options =
                new FirebaseRecyclerOptions.Builder<History>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Returned Cycles"), History.class)
                        .build();

        mainAdapter = new SecurityAdapter(options);
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
