package hcl.esg.ebike.application.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.app.AlertDialog;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.R;

public class AddCycles extends AppCompatActivity {
    private EditText edit_cid, edit_location;
    private RadioGroup radio_group_colors;
    private ImageView cycle_image;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_cycles);

        edit_cid = findViewById(R.id.edit_cid);
        edit_location = findViewById(R.id.edit_location);
        radio_group_colors = findViewById(R.id.radio_group_colors);
        cycle_image = findViewById(R.id.cycle_image);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        radio_group_colors.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_black) {
                cycle_image.setImageResource(R.drawable.black_cycle);
            } else if (checkedId == R.id.radio_green) {
                cycle_image.setImageResource(R.drawable.green_cycle);
            } else if (checkedId == R.id.radio_blue) {
                cycle_image.setImageResource(R.drawable.blue_cycle);
            } else if (checkedId == R.id.radio_red) {
                cycle_image.setImageResource(R.drawable.red_cycle);
            } else if (checkedId == R.id.radio_yellow) {
                cycle_image.setImageResource(R.drawable.yellow_cycle);
            }
        });
    }

    public void sendData(View view) {
        String cid = edit_cid.getText().toString().trim();
        String location = edit_location.getText().toString().trim();
        String availability = "Available"; // Set availability to true by default

        int selectedColorId = radio_group_colors.getCheckedRadioButtonId();
        RadioButton selectedColorRadioButton = findViewById(selectedColorId);
        String color = selectedColorRadioButton != null ? selectedColorRadioButton.getText().toString() : "";

        if (cid.isEmpty() || location.isEmpty() || color.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if cycle with given CID already exists
        mDatabase.child("cycles").child(cid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // Show alert dialog that the cycle already exists
                    new AlertDialog.Builder(AddCycles.this)
                            .setTitle("Cycle Exists")
                            .setMessage("A cycle with this ID already exists. Please use a different ID.")
                            .setPositiveButton(android.R.string.ok, null)
                            .show();
                } else {
                    // Create a new Cycles object
                    Cycles cycles = new Cycles(cid, availability, color, location);

                    // Save cycle data to Firebase
                    mDatabase.child("cycles").child(cycles.getCid()).setValue(cycles)
                            .addOnSuccessListener(aVoid -> Toast.makeText(AddCycles.this, "Cycle Added", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(AddCycles.this, "Failed to add cycle", Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(AddCycles.this, "Database error: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
