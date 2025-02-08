package hcl.esg.ebike.application.Fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.Objects;

import hcl.esg.ebike.application.Models.History;
import hcl.esg.ebike.application.R;

public class UserHomeFragment extends Fragment {

    private Button request_btn, return_btn;
    private DatabaseReference database, db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_home, container, false);

        Fragment fragment = new mapfragment();
        requireActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();

        database = FirebaseDatabase.getInstance().getReference().child("cycles");
        db = FirebaseDatabase.getInstance().getReference().child("Requested Cycles").child("empId");

        request_btn = view.findViewById(R.id.request_btn);
        return_btn = view.findViewById(R.id.return_btn);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("users");
            Query query = userRef.orderByChild("email").equalTo(email);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                            String empId = userSnapshot.child("emp_id").getValue(String.class);
                            if (empId != null) {
                                DatabaseReference requestedCyclesRef = FirebaseDatabase.getInstance().getReference("Requested Cycles");
                                requestedCyclesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        boolean isCycleRequested = false;
                                        for (DataSnapshot cycleSnapshot : snapshot.getChildren()) {
                                            History history = cycleSnapshot.getValue(History.class);
                                            if (history != null && empId.equals(history.getEmpId())) {
                                                isCycleRequested = true;
                                                break;
                                            }
                                        }
                                        request_btn.setEnabled(!isCycleRequested);
                                        return_btn.setEnabled(isCycleRequested);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("UserHomeFragment", "Database error: " + error.getMessage());
                                    }
                                });
                            } else {
                                Log.e("UserHomeFragment", "Employee ID is null for the current user");
                            }
                        }
                    } else {
                        Log.e("UserHomeFragment", "User data does not exist for the current user");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("UserHomeFragment", "Database error: " + databaseError.getMessage());
                }
            });
        } else {
            Log.e("UserHomeFragment", "Current user is null");
        }

        request_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator intentIntegrator = IntentIntegrator.forSupportFragment(UserHomeFragment.this);
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.setPrompt("Scan QR code");
                intentIntegrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
                intentIntegrator.initiateScan();
            }
        });

        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
        String cycleId = sharedPreferences.getString("cycleId", null);
        if (cycleId != null) {
            return_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    returnCycle(cycleId);
                }
            });
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);

        if (intentResult != null) {
            String cycleID = intentResult.getContents();

            if (cycleID != null) {
                SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("cycleId", cycleID);
                editor.apply();

                DatabaseReference cycleRef = FirebaseDatabase.getInstance().getReference().child("cycles").child(cycleID);
                DatabaseReference damagedCyclesRef = FirebaseDatabase.getInstance().getReference().child("Damaged Cycles").child(cycleID);

                cycleRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            damagedCyclesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot damagedSnapshot) {
                                    if (damagedSnapshot.exists()) {
                                        showAlert("Cycle is damaged");
                                    } else {
                                        String availability = dataSnapshot.child("availability").getValue(String.class);
                                        if ("Available".equals(availability)) {
                                            fetchCycleData(cycleID);
                                        } else {
                                            showAlert("Cycle is in use");
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    showAlert("Database error: " + databaseError.getMessage());
                                }
                            });
                        } else {
                            showAlert("Cycle does not exist");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showAlert("Database error: " + databaseError.getMessage());
                    }
                });
            } else {
                showAlert("Invalid QR code scanned. Please try again.");
            }
        } else {
            showAlert("Scan failed. Please try again.");
        }
    }

    private void fetchCycleData(String cycleId) {
        DatabaseReference cycleRef = FirebaseDatabase.getInstance().getReference().child("cycles").child(cycleId);
        cycleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String color = snapshot.child("color").getValue(String.class); // Fetch color



                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                    String empId = sharedPreferences.getString("empId", null);

                    History requestedCycle = new History(cycleId, empId, color); // Include allotTime and color
                    FirebaseDatabase.getInstance().getReference("Requested Cycles").child(cycleId).setValue(requestedCycle);
                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).push().setValue(requestedCycle);

                    cycleRef.child("availability").setValue("Not available");

                    // Store the color in SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("cycleColor", color);
                    editor.apply();

                    showAlert("Cycle was allocated");

                    // Navigate to UserHomeFragment with updated data
                    UserHomeFragment userHomeFragment = new UserHomeFragment();
                    Bundle args = new Bundle();
                    args.putString("cycleId", cycleId);
                    userHomeFragment.setArguments(args);

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, userHomeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    showAlert("Cycle not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showAlert("Database error: " + error.getMessage());
            }
        });
    }

    private void returnCycle(String cycleId) {
        DatabaseReference cycleRef = FirebaseDatabase.getInstance().getReference("Requested Cycles").child(cycleId);
        cycleRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("my_preferences", Context.MODE_PRIVATE);
                    String empId = sharedPreferences.getString("empId", null);
                    String allotTime = snapshot.child("alloatTime").getValue(String.class); // Corrected key name to "allotTime"
                    String color = snapshot.child("color").getValue(String.class); // Get color

                    if (allotTime == null) {
                        allotTime = "Default Time"; // Provide a default value or handle appropriately
                    }

                    History history = new History(cycleId, empId, allotTime, color); // Include color
                    FirebaseDatabase.getInstance().getReference("History").push().setValue(history);
                    FirebaseDatabase.getInstance().getReference(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).push().setValue(history);
                    FirebaseDatabase.getInstance().getReference("Returned Cycles").child(cycleId).setValue(history);
                    FirebaseDatabase.getInstance().getReference().child("Requested Cycles").child(cycleId).removeValue();
                    showAlert("Cycle was returned");

                    UserHomeFragment userHomeFragment = new UserHomeFragment();
                    Bundle args = new Bundle();
                    args.putString("cycleId", cycleId);
                    userHomeFragment.setArguments(args);

                    FragmentManager fragmentManager = requireActivity().getSupportFragmentManager();
                    FragmentTransaction transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.fragment_container, userHomeFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                } else {
                    showAlert("Cycle not found");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showAlert("Database error: " + error.getMessage());
            }
        });
    }

    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);
        builder.setView(dialogView);

        TextView alertTitle = dialogView.findViewById(R.id.alertTitle);
        TextView alertMessage = dialogView.findViewById(R.id.alertMessage);
        Button alertButton = dialogView.findViewById(R.id.alertButton);

        alertMessage.setText(message);

        AlertDialog alertDialog = builder.create();

        alertButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
}
