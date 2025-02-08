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

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;
import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.R;

public class Adapter extends FirebaseRecyclerAdapter<Cycles, Adapter.myViewHolder> {

    private Context mContext;

    public Adapter(@NonNull FirebaseRecyclerOptions<Cycles> options, Context context) {
        super(options);
        mContext = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Cycles model) {
        holder.id.setText(model.getCid());
        holder.color.setText(model.getColor());
        holder.available.setText(model.getAvailability());
        holder.location.setText(model.getLocation());

        // Set image based on cycle color
        int imageResource = getImageResourceForColor(model.getColor());
        Glide.with(mContext).load(imageResource).into(holder.cycle);

        holder.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showUpdateDialog(model);
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteItem(position);
            }
        });
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);
        return new myViewHolder(view);
    }

    private void showUpdateDialog(Cycles model) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setTitle("Update Cycle");

        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.update_dialog, null);

        EditText etColor = view.findViewById(R.id.etColor);
        EditText etAvailability = view.findViewById(R.id.etAvailability);
        EditText etLocation = view.findViewById(R.id.etLocation);

        etColor.setText(model.getColor());
        etAvailability.setText(model.getAvailability());
        etLocation.setText(model.getLocation());

        builder.setView(view);

        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String color = etColor.getText().toString();
                String availability = etAvailability.getText().toString();
                String location = etLocation.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cycles").child(model.getCid());
                databaseReference.child("color").setValue(color);
                databaseReference.child("availability").setValue(availability);
                databaseReference.child("location").setValue(location);

                showAlert("Cycle Updated Successfully");
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

    private void deleteItem(int position) {
        getSnapshots().getSnapshot(position).getRef().removeValue();
        showAlert("Cycle Deleted Successfully");
    }

    public void updateOptions(FirebaseRecyclerOptions<Cycles> options) {
        super.updateOptions(options); // Correctly update the options in the parent class
        startListening(); // Restart listening for changes
    }

    private int getImageResourceForColor(String color) {
        switch (color.toLowerCase()) {
            case "black":
                return R.drawable.black_cycle;
            case "green":
                return R.drawable.green_cycle;
            case "blue":
                return R.drawable.blue_cycle;
            case "red":
                return R.drawable.red_cycle;
            case "yellow":
                return R.drawable.yellow_cycle;
            default:
                return R.drawable.logo;
        }
    }

    static class myViewHolder extends RecyclerView.ViewHolder {

        TextView id, color, available, location;
        CircleImageView cycle;
        Button btnUpdate, btnDelete;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.cid);
            color = itemView.findViewById(R.id.color);
            available = itemView.findViewById(R.id.availability);
            location = itemView.findViewById(R.id.Location);
            btnUpdate = itemView.findViewById(R.id.btnUpdate);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            cycle = itemView.findViewById(R.id.cyc);
        }
    }

    private void showAlert(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }
}
