package hcl.esg.ebike.application.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import hcl.esg.ebike.application.Activities.Login;
import hcl.esg.ebike.application.R;

public class ProfileFragment extends Fragment {

    private TextView textUid, textEmail, textName;
    private Button LogOut;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        // Initialize TextViews and Button
        textUid = view.findViewById(R.id.uid);
        textEmail = view.findViewById(R.id.email);
        textName = view.findViewById(R.id.name);
        LogOut = view.findViewById(R.id.logout);

        // Retrieve user data from Firebase Realtime Database
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        assert user != null;
        String email = user.getEmail();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

        Query query = reference.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String uid = snapshot.child("emp_id").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String name = snapshot.child("name").getValue(String.class);

                        if (uid != null && email != null && name != null) {
                            textUid.setText(uid);
                            textEmail.setText(email);
                            textName.setText(name);
                        } else {
                            Log.e("Firebase", "One or more values retrieved from Firebase are null");
                        }
                    }
                } else {
                    Log.e("Firebase", "No data found for the current user");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Firebase", "Error retrieving data from Firebase: " + error.getMessage());
            }
        });

        // Set onClickListener for LogOut button
        LogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Handle logout
                FirebaseAuth.getInstance().signOut();
                // Remove email from SharedPreferences
                SharedPreferences preferences = requireActivity().getSharedPreferences("YOUR_PREFS_NAME", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.remove("email");
                editor.apply();
                Intent intent = new Intent(requireActivity(), Login.class);
                startActivity(intent);
                requireActivity().finish();
            }
        });

        return view;
    }
}
