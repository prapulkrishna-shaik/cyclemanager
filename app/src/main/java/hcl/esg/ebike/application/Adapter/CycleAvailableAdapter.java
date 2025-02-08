package hcl.esg.ebike.application.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import de.hdodenhof.circleimageview.CircleImageView;
import hcl.esg.ebike.application.Models.Cycles;
import hcl.esg.ebike.application.R;

public class CycleAvailableAdapter extends FirebaseRecyclerAdapter<Cycles, CycleAvailableAdapter.MyViewHolder> {

    public CycleAvailableAdapter(@NonNull FirebaseRecyclerOptions<Cycles> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Cycles model) {
        holder.id.setText(model.getCid());
        holder.color.setText(model.getColor());
        holder.location.setText(model.getLocation());

        // Set image based on color
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
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cycle_item, parent, false);
        return new MyViewHolder(view);
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id, color, location;
        CircleImageView cycleImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.cid);
            color = itemView.findViewById(R.id.color);
            location = itemView.findViewById(R.id.Location);
            cycleImage = itemView.findViewById(R.id.cyc);
        }
    }
}
