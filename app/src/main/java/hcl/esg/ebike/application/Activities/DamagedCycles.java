package hcl.esg.ebike.application.Activities;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;



import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.FirebaseDatabase;

import hcl.esg.ebike.application.Adapter.DamagedAdapter;
import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.R;

public class DamagedCycles extends AppCompatActivity {

    RecyclerView recyclerView;
    DamagedAdapter mainAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_damaged_cycles);

        recyclerView = (RecyclerView)findViewById(R.id.rvv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions<Cycles> options =
                new FirebaseRecyclerOptions.Builder<Cycles>()
                        .setQuery(FirebaseDatabase.getInstance().getReference().child("Damaged Cycles"), Cycles.class)
                        .build();

        mainAdapter = new DamagedAdapter(options);
        recyclerView.setAdapter(mainAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mainAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mainAdapter.stopListening();
}



}