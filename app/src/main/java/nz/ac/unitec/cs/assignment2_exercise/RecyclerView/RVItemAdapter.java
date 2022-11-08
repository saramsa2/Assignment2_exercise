package nz.ac.unitec.cs.assignment2_exercise.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Map;

import nz.ac.unitec.cs.assignment2_exercise.R;

public class RVItemAdapter extends RecyclerView.Adapter<RVItemAdapter.RVItemHolder> {

    List<Map<String, Object>> items;

    public RVItemAdapter(List<Map<String, Object>> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RVItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_view, parent, false);
        return new RVItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RVItemHolder holder, int position) {
        if(items.get(position).get("name") != null) {
            holder.tvItemName.setText(items.get(position).get("name").toString());
        } else {
            holder.tvItemName.setText("-");
        }
        if(items.get(position).get("phonetic") != null) {
            holder.tvItemPhonetic.setText(items.get(position).get("phonetic").toString());
        } else {
            holder.tvItemPhonetic.setText("-");
        }
        if(items.get(position).get("meaning") != null) {
            holder.tvItemDefinition.setText(items.get(position).get("meaning").toString());
        } else {
            holder.tvItemDefinition.setText("-");
        }
        if(items.get(position).get("synonym") != null) {
            holder.tvItemSynonym.setText(items.get(position).get("synonym").toString());
        } else {
            holder.tvItemSynonym.setText("-");
        }

    }

    @Override
    public int getItemCount() {
        return this.items.size();
    }

    class RVItemHolder extends RecyclerView.ViewHolder {

        LinearLayout rvItemFrame;
        TextView tvItemName, tvItemPhonetic, tvItemDefinition, tvItemSynonym;

        public RVItemHolder(@NonNull View itemView) {
            super(itemView);

            rvItemFrame = itemView.findViewById(R.id.rv_item_frame);
            tvItemName = itemView.findViewById(R.id.tv_item_name);
            tvItemPhonetic = itemView.findViewById(R.id.tv_item_phonetic);
            tvItemDefinition = itemView.findViewById(R.id.tv_item_definitions);
            tvItemSynonym = itemView.findViewById(R.id.tv_item_synonyms);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rvItemFrame.getHeight() == 120) {
                        ViewGroup.LayoutParams myParams = rvItemFrame.getLayoutParams();
                        myParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        rvItemFrame.setLayoutParams(myParams);
                    } else {
                        ViewGroup.LayoutParams myParams = rvItemFrame.getLayoutParams();
                        myParams.height = 120;
                        rvItemFrame.setLayoutParams(myParams);
                    }

                }
            });
        }
    }
}
