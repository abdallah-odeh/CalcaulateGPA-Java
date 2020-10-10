package com.example.calculategpa_petrauni.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculategpa_petrauni.pojo.Course;
import com.example.calculategpa_petrauni.ui.Calculate;
import com.example.calculategpa_petrauni.Classes.AppConst;
import com.example.calculategpa_petrauni.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;


public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {


    private ArrayList<Course> list;
    private Context context;
    private boolean allow_last_marks = false;
    private int layout;

    public CoursesAdapter(ArrayList<Course> list, Context context, int layout) {
        this.list = list;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(layout, null);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.lastMark.setText(list.get(position).getLastMark());
        holder.courseHours.setText(list.get(position).getHours());
        holder.courseMark.setText(list.get(position).getMark());

        holder.lastMark.setEnabled(allow_last_marks);

        if (allow_last_marks)
            holder.lastMarkCard.setOnClickListener(v -> {
                final List<String> languages = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.prev_marks)));
                ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                        context,
                        android.R.layout.simple_list_item_1,languages
                ){
                    @Override
                    public View getView(int position, View convertView, @NotNull ViewGroup parent){
                        TextView text_view = (TextView) super.getView(position, convertView, parent);
                        text_view.setTypeface(ResourcesCompat.getFont(context, R.font.din_next_lt_w23_regular));
                        return text_view;
                    }
                };
                final AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
                builder.setTitle(context.getString(R.string.marks));
                builder.setSingleChoiceItems(
                        arrayAdapter, // Items list
                        -1, // Index of checked item (-1 = no selection)
                        (dialogInterface, i1) -> {
                            list.get(position).setLastMark(context.getResources().getStringArray(R.array.prev_marks)[i1]);
                            notifyAdapter();
                            dialogInterface.dismiss();
                        });
                builder.setNegativeButton(android.R.string.cancel, (dialog1, which) -> dialog1.dismiss());
                AlertDialog dialog = builder.create();
                dialog.show();

                Typeface font = ResourcesCompat.getFont(context, R.font.din_next_lt_w23_regular);
                TextView title = (TextView) dialog.getWindow().findViewById(R.id.alertTitle);
                Button cancel = (Button) dialog.getWindow().findViewById(android.R.id.button2);
                title.setTypeface(font);
                cancel.setTypeface(font);
                SharedPreferences preferences = context.getSharedPreferences(AppConst.name, MODE_PRIVATE);
                cancel.setTextColor(Color.parseColor(preferences.getBoolean(AppConst._theme, false) ? "#FFFFFFFF" : "#FF000000"));
            });
        else
            holder.lastMarkCard.setClickable(false);

        holder.courseMarkCard.setOnClickListener(v -> {
            final List<String> languages = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.marks)));
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_list_item_1,languages
            ){
                @Override
                public View getView(int position, View convertView, @NotNull ViewGroup parent){
                    TextView text_view = (TextView) super.getView(position, convertView, parent);
                    text_view.setTypeface(ResourcesCompat.getFont(context, R.font.din_next_lt_w23_regular));
                    return text_view;
                }
            };
            final AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.marks));
            builder.setSingleChoiceItems(
                    arrayAdapter, // Items list
                    -1, // Index of checked item (-1 = no selection)
                    (dialogInterface, i1) -> {
                        list.get(position).setMark(
                                context.getResources().getStringArray(R.array.marks)[i1],
                                context.getResources().getString(R.string.ignore)
                        );
                        Calculate.calculate.setEnabled(isShouldActivateButton());
                        notifyAdapter();
                        dialogInterface.dismiss();
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog1, which) -> dialog1.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();

            Typeface font = ResourcesCompat.getFont(context, R.font.din_next_lt_w23_regular);
            TextView title = (TextView) dialog.getWindow().findViewById(R.id.alertTitle);
            Button cancel = (Button) dialog.getWindow().findViewById(android.R.id.button2);
            title.setTypeface(font);
            cancel.setTypeface(font);
            SharedPreferences preferences = context.getSharedPreferences(AppConst.name, MODE_PRIVATE);
            cancel.setTextColor(Color.parseColor(preferences.getBoolean(AppConst._theme, false) ? "#FFFFFFFF" : "#FF000000"));
        });
        holder.courseHoursCard.setOnClickListener(v -> {
            final List<String> languages = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.hours)));
            ArrayAdapter arrayAdapter = new ArrayAdapter<String>(
                    context,
                    android.R.layout.simple_list_item_1,languages
            ){
                @Override
                public View getView(int position, View convertView, @NotNull ViewGroup parent){
                    TextView text_view = (TextView) super.getView(position, convertView, parent);
                    text_view.setTypeface(ResourcesCompat.getFont(context, R.font.din_next_lt_w23_regular));
                    return text_view;
                }
            };
            final AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
            builder.setTitle(context.getString(R.string.hours));
            builder.setSingleChoiceItems(
                    arrayAdapter, // Items list
                    -1, // Index of checked item (-1 = no selection)
                    (dialogInterface, i1) -> {
                        list.get(position).setHours(context.getResources().getStringArray(R.array.hours)[i1]);
                        notifyAdapter();
                        dialogInterface.dismiss();
                    });
            builder.setNegativeButton(android.R.string.cancel, (dialog1, which) -> dialog1.dismiss());
            AlertDialog dialog = builder.create();
            dialog.show();

            Typeface font = ResourcesCompat.getFont(context, R.font.din_next_lt_w23_regular);
            TextView title = (TextView) dialog.getWindow().findViewById(R.id.alertTitle);
            Button cancel = (Button) dialog.getWindow().findViewById(android.R.id.button2);
            title.setTypeface(font);
            cancel.setTypeface(font);
            SharedPreferences preferences = context.getSharedPreferences(AppConst.name, MODE_PRIVATE);
            cancel.setTextColor(Color.parseColor(preferences.getBoolean(AppConst._theme, false) ? "#FFFFFFFF" : "#FF000000"));
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        RelativeLayout lastMarkCard, courseHoursCard, courseMarkCard;
        TextView lastMark, courseHours, courseMark;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            lastMarkCard = itemView.findViewById(R.id.LM_card);
            courseMarkCard = itemView.findViewById(R.id.CM_card);
            courseHoursCard = itemView.findViewById(R.id.CH_card);

            lastMark = itemView.findViewById(R.id.LM);
            courseHours = itemView.findViewById(R.id.CH);
            courseMark = itemView.findViewById(R.id.CM);
        }
    }
    private void notifyAdapter(){
        notifyDataSetChanged();
    }

    public void activate_LM(boolean flag){
        allow_last_marks = flag;
        notifyAdapter();
    }
    private boolean isShouldActivateButton(){
        for (Course c: list)
            if (c.isSet)
                return true;
        return false;
    }
}