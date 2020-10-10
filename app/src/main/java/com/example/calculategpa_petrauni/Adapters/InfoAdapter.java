package com.example.calculategpa_petrauni.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculategpa_petrauni.R;
import com.example.calculategpa_petrauni.pojo.Info;

import java.util.ArrayList;

public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.VH> {

    private Context activity;
    private ArrayList<Info> list;
    private boolean marks;

    public InfoAdapter(Context activity, ArrayList<Info> list, boolean marks) {
        this.activity = activity;
        this.list = list;
        this.marks = marks;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.info_card, null);
        return new VH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull VH vh, int i) {
        vh.right.setText(list.get(i).getRight());
        vh.left.setText(list.get(i).getLeft());

        vh.left.setTextAlignment(marks ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_VIEW_END);
        vh.right.setTextAlignment(marks ? View.TEXT_ALIGNMENT_CENTER : View.TEXT_ALIGNMENT_VIEW_START);

        vh.itemView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class VH extends RecyclerView.ViewHolder{

        TextView right;
        TextView left;
        public VH(@NonNull View itemView) {
            super(itemView);
            right = itemView.findViewById(R.id.mark_level);
            left =  itemView.findViewById(R.id.avg_value);
        }
    }
}
