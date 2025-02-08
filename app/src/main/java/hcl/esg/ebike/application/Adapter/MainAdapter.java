package hcl.esg.ebike.application.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import hcl.esg.ebike.application.R;
import hcl.esg.ebike.application.Models.User;

public class MainAdapter extends FirebaseRecyclerAdapter<User,MainAdapter.myViewHolder> {
    private Context mContext;


    public MainAdapter(@NonNull FirebaseRecyclerOptions<User> options,Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull User model) {

        holder.id.setText(model.getEmp_id());
        holder.email.setText(model.getEmail());
        holder.name.setText(model.getName());
        holder.UserRole.setText(model.getUserRole());
        holder.btnUpdate.setOnClickListener(v -> showUpdateDialog(model));

        // Handle delete button click
        holder.btnDelete.setOnClickListener(v -> deleteItem(getRef(position).getKey()));

    }
    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.name_item,parent,false);
        return new myViewHolder(view);
    }

    private void showUpdateDialog(User model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Update User");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.update_user_dialog, null);

        EditText etName = view.findViewById(R.id.etName); // Assuming these are the correct IDs in your layout
        EditText etEmail = view.findViewById(R.id.etEmail);
        EditText etUserRole = view.findViewById(R.id.etRole);

        etName.setText(model.getName());
        etEmail.setText(model.getEmail());
        etUserRole.setText(model.getUserRole());

        builder.setView(view);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String userRole = etUserRole.getText().toString();

                String empId = model.getEmp_id();
                if (empId != null && !empId.isEmpty()) {
                    // Query the database to find the user with the specific emp_id
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("users");
                    usersRef.orderByChild("emp_id").equalTo(empId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                                userSnapshot.getRef().child("name").setValue(name);
                                userSnapshot.getRef().child("email").setValue(email);
                                userSnapshot.getRef().child("userRole").setValue(userRole)
                                        .addOnSuccessListener(aVoid -> showAlert("User Updated Successfully"))
                                        .addOnFailureListener(e -> showAlert("Failed to update user: " + e.getMessage()));
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            showAlert("Failed to update user: " + databaseError.getMessage());
                        }
                    });
                } else {
                    showAlert("Employee ID is invalid.");
                }
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create().show();
    }



    private void deleteItem(String userId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(userId);
        databaseReference.removeValue()
                .addOnSuccessListener(aVoid -> showAlert("Cycle Deleted Successfully"))
                .addOnFailureListener(e -> showAlert("Failed to delete cycle: " + e.getMessage()));
    }
    public void updateOptions(FirebaseRecyclerOptions<User> options) {
        super.updateOptions(options); // Correctly update the options in the parent class
        startListening(); // Restart listening for changes
    }




    class myViewHolder extends RecyclerView.ViewHolder{


        TextView name,id,email,UserRole;
        Button btnUpdate,btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);


            id=(TextView)itemView.findViewById(R.id.emp_id);
            email=(TextView)itemView.findViewById(R.id.email);
            name=(TextView)itemView.findViewById(R.id.name);
            UserRole =(TextView) itemView.findViewById(R.id.role);
            btnUpdate = (Button) itemView.findViewById(R.id.btnUpdate);
            btnDelete = (Button) itemView.findViewById(R.id.btnDelete);

        }
    }
    private void showAlert(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
