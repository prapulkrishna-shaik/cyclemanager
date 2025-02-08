package hcl.esg.ebike.application.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hcl.esg.ebike.application.Models.Reasons;
import hcl.esg.ebike.application.R;

public class ReasonsActivity extends AppCompatActivity {

    CheckBox c1, c2, c3, c4, c5, c6, c7, c8;
    FirebaseDatabase database;
    DatabaseReference reference, cyclesReference;
    Reasons reasons;
    Button button;
    String cycleColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reasons);

        Intent intent = getIntent();
        String cycleId = intent.getStringExtra("CYCLE_ID");

        database = FirebaseDatabase.getInstance();
        reference = database.getReference().child("Damaged Cycles").child(cycleId);
        cyclesReference = database.getReference().child("cycles").child(cycleId);

        reasons = new Reasons();
        c1 = findViewById(R.id.reason1);
        c2 = findViewById(R.id.reason2);
        c3 = findViewById(R.id.reason3);
        c4 = findViewById(R.id.reason4);
        c5 = findViewById(R.id.reason5);
        c6 = findViewById(R.id.reason6);
        c7 = findViewById(R.id.reason7);
        c8 = findViewById(R.id.reason8);
        button = findViewById(R.id.submit);

        // Initial state of the button
        button.setEnabled(false);

        // Listener to check if any checkbox is selected
        View.OnClickListener checkboxListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkIfAnyCheckboxSelected();
            }
        };

        c1.setOnClickListener(checkboxListener);
        c2.setOnClickListener(checkboxListener);
        c3.setOnClickListener(checkboxListener);
        c4.setOnClickListener(checkboxListener);
        c5.setOnClickListener(checkboxListener);
        c6.setOnClickListener(checkboxListener);
        c7.setOnClickListener(checkboxListener);
        c8.setOnClickListener(checkboxListener);

        // Set the onClickListener for the button
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                retrieveAndStoreCycleData(cycleId);
            }
        });
    }

    private void retrieveAndStoreCycleData(String cycleId) {
        cyclesReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cycleColor = snapshot.child("color").getValue(String.class);
                storeDamagedCycleData(cycleId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ReasonsActivity.this, "Error retrieving cycle color", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void storeDamagedCycleData(String cycleId) {
        if (c1.isChecked()) reasons.setDamage1("Tire Punctures");
        if (c2.isChecked()) reasons.setDamage2("Brakes Failure");
        if (c3.isChecked()) reasons.setDamage3("Gear Failures");
        if (c4.isChecked()) reasons.setDamage4("Loose Bolts");
        if (c5.isChecked()) reasons.setDamage5("Chain Damage");
        if (c6.isChecked()) reasons.setDamage6("Pedals Damage");
        if (c7.isChecked()) reasons.setDamage7("Physical problems");
        if (c8.isChecked()) reasons.setDamage8("Other");



        // Store cycle ID and color separately
        reference.child("cycleId").setValue(cycleId);
        reference.child("color").setValue(cycleColor);

        // Store reasons in the "reasons" child node
        reference.child("reasons").setValue(reasons);

        // Remove cycle from "Returned Cycles"
        FirebaseDatabase.getInstance().getReference().child("Returned Cycles").child(cycleId).removeValue();
        Toast.makeText(this, "Reported to admin the cycle is damaged", Toast.LENGTH_SHORT).show();

        // Navigate to SecurityNavigation
        Intent intent = new Intent(ReasonsActivity.this, SecurityNavigation.class);
        startActivity(intent);
        finish();
    }

    // Method to check if any checkbox is selected
    private void checkIfAnyCheckboxSelected() {
        button.setEnabled(c1.isChecked() || c2.isChecked() || c3.isChecked() || c4.isChecked() ||
                c5.isChecked() || c6.isChecked() || c7.isChecked() || c8.isChecked());
    }
}
