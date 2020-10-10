package com.example.calculategpa_petrauni.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculategpa_petrauni.Adapters.InfoAdapter;
import com.example.calculategpa_petrauni.Classes.AppConst;
import com.example.calculategpa_petrauni.R;
import com.example.calculategpa_petrauni.pojo.Info;

import java.util.ArrayList;

public class MarksInfo extends AppCompatActivity implements MarksInfoView{

    ArrayList<Info> list = new ArrayList<>();
    InfoAdapter adapter;
    TextView least;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES?R.style.AppThemeDark:R.style.AppTheme);
        setContentView(R.layout.show_info);

        RecyclerView recyclerView = findViewById(R.id.recycler);
        least = findViewById(R.id.least_success);

        int isMark = getIntent().getIntExtra(AppConst.isMark, 0);

        MarksInfoPresenter presenter = new MarksInfoPresenter(this, this);

        if(isMark == 0) presenter.showMarks();
        else if(isMark == 1) presenter.showMarksDistribution();
        else presenter.showGPAs();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new InfoAdapter(this, list, isMark < 2);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void showMarks() {
        least.setVisibility(View.VISIBLE);
        String[][] arr = new String[][]{
                {"A", "4.00"},
                {"A-", "3.67"},
                {"B+", "3.33"},
                {"B", "3.00"},
                {"B-", "2.67"},
                {"C+", "2.33"},
                {"C", "2.00"},
                {"C-", "1.67"},
                {"D+", "1.33"},
                {"D", "1.00"},
                {"D-", "0.67 " + getResources().getString(R.string.fail)},
                {"F", "0.00 " + getResources().getString(R.string.fail)},
                {"FA", "0.00 " + getResources().getString(R.string.fail_by_absent)},
                {"P", getResources().getString(R.string.passed).replace("(P)", "")},
                {"NP",getResources().getString(R.string.failed).replace("(NP)", "")},
                {"I", getResources().getString(R.string.incomplete)},
                {"W", getResources().getString(R.string.withdrawal)},
                {"T", getResources().getString(R.string.transfer)}
        };

        for(String[] item: arr)
            list.add(new Info(item));
    }
    @Override
    public void showMarksDistribution() {
        least.setVisibility(View.VISIBLE);
        least.setText(getString(R.string.marks_change_msg));
        String[][] arr = new String[][]{
                {"A", "95 - 100"},
                {"A-", "90 - 94"},
                {"B+", "85 - 89"},
                {"B", "80 - 84"},
                {"B-", "75 - 79"},
                {"C+", "70 - 74"},
                {"C", "65 - 69"},
                {"C-", "60 - 64"},
                {"D+", "55 - 59"},
                {"D", "50 - 54"},
                {"D-", "45 - 49"},
                {"F", "0 - 44"}
        };

        for(String[] item: arr)
            list.add(new Info(item));
    }
    @Override
    public void showGPAs() {
        String[][] arr = {
                {getResources().getStringArray(R.array.levels)[0],"3.89 - 4.00"},
                {getResources().getStringArray(R.array.levels)[1],"3.67 - 3.88"},
                {getResources().getStringArray(R.array.levels)[2],"3.00 - 3.66"},
                {getResources().getStringArray(R.array.levels)[3],"2.33 - 2.99"},
                {getResources().getStringArray(R.array.levels)[4],"2.00 - 2.32"},
                {getResources().getStringArray(R.array.levels)[5],"0.00 - 1.99"}};


        for(String[] item: arr)
            list.add(new Info(item));
    }
}
