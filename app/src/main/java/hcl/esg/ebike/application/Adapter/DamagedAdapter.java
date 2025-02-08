package hcl.esg.ebike.application.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.R;

public class DamagedAdapter extends FirebaseRecyclerAdapter<Cycles, DamagedAdapter.myViewHolder> {

    public DamagedAdapter(@NonNull FirebaseRecyclerOptions<Cycles> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull Cycles model) {
        holder.id.setText(model.getCycleId());

        // Fetch the reasons for this cycle
        DatabaseReference reasonRef = FirebaseDatabase.getInstance().getReference()
                .child("Damaged Cycles")
                .child(model.getCycleId())
                .child("reasons");

        reasonRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                StringBuilder reasonsText = new StringBuilder();
                for (DataSnapshot reasonSnapshot : dataSnapshot.getChildren()) {
                    reasonsText.append(reasonSnapshot.getValue(String.class)).append("\n");
                }
                holder.reasons.setText(reasonsText.toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(holder.itemView.getContext(), "Failed to load reasons", Toast.LENGTH_SHORT).show();
            }
        });

        holder.Repaired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cycleId = getItem(holder.getAdapterPosition()).getCycleId();
                FirebaseDatabase.getInstance().getReference().child("Damaged Cycles").child(cycleId).removeValue();
                FirebaseDatabase.getInstance().getReference().child("cycles").child(cycleId).child("availability").setValue("Available");
                Toast.makeText(v.getContext(), "Repaired Updated Database", Toast.LENGTH_SHORT).show();
            }
        });
        switch (model.getColor().toLowerCase()) {
            case "blue":
                holder.cycleImage.setImageResource(R.drawable.blue_cycle);
                break;
            case "black":
                holder.cycleImage.setImageResource(R.drawable.black_cycle);
                break;
            case "green":
                holder.cycleImage.setImageResource(R.drawable.green_cycle);
                break;
            case "red":
                holder.cycleImage.setImageResource(R.drawable.red_cycle);
                break;
            case "yellow":
                holder.cycleImage.setImageResource(R.drawable.yellow_cycle);
                break;
            default:
                holder.cycleImage.setImageResource(R.mipmap.ic_launcher); // default image if no match
                break;
        }
    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.damage_item, parent, false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder {
        TextView id, reasons;
        Button Repaired;
        CircleImageView cycleImage;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.cid);
            reasons = itemView.findViewById(R.id.reasons);
            Repaired = itemView.findViewById(R.id.repair);
            cycleImage = itemView.findViewById(R.id.cyc);
        }
    }
}
