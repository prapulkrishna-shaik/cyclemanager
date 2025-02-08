package hcl.esg.ebike.application.Adapter;

import android.content.Intent;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;
import hcl.esg.ebike.application.Activities.ReasonsActivity;
import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.Models.History;
import hcl.esg.ebike.application.R;

public class SecurityAdapter extends FirebaseRecyclerAdapter<History,SecurityAdapter.myViewHolder> {


    public SecurityAdapter(@NonNull FirebaseRecyclerOptions<History> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myViewHolder holder, int position, @NonNull History model) {
        holder.cycleId.setText(model.getCycleId());
        holder.EmpId.setText(model.getEmpId());
        holder.duration.setText(model.getDuration());
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
        holder.damage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cycleId = getItem(holder.getAdapterPosition()).getCycleId();
                Intent intent = new Intent(v.getContext(), ReasonsActivity.class);
                intent.putExtra("CYCLE_ID", cycleId);
                v.getContext().startActivity(intent);


                // For example, you can show a toast message

            }
        });


        // Implement onClick listener for the "approve" button
        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform actions when "approve" button is clicked

                String cycleId = getItem(holder.getAdapterPosition()).getCycleId();
                FirebaseDatabase.getInstance().getReference().child("Returned Cycles").child(cycleId).removeValue();
                FirebaseDatabase.getInstance().getReference().child("cycles").child(cycleId).child("availability").setValue("Available");
                // For example, you can show a toast message
                Toast.makeText(v.getContext(), "Approved" + model.getCycleId(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @NonNull
    @Override
    public myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.security_item,parent,false);
        return new myViewHolder(view);
    }

    class myViewHolder extends RecyclerView.ViewHolder{


        TextView cycleId,EmpId,duration;
        Button damage,approve;
        CircleImageView cycleImage;

        public myViewHolder(@NonNull View itemView) {
            super(itemView);
            damage = itemView.findViewById(R.id.damage);
            approve = itemView.findViewById(R.id.approve);
            cycleId=(TextView)itemView.findViewById(R.id.cid);
            EmpId = (TextView)itemView.findViewById(R.id.Emp_id);
            duration = (TextView)itemView.findViewById(R.id.duration);
            cycleImage = (CircleImageView) itemView.findViewById(R.id.cyc);

        }
    }
}


