package com.example.calculategpa_petrauni.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.calculategpa_petrauni.R;
import com.example.calculategpa_petrauni.UIClasses.ProgressBarAnimation;

public class Result extends AppCompatActivity implements ResultView{

    TextView result, average, rate;
    ProgressBar bar;
    ResultPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES?R.style.AppThemeDark:R.style.AppTheme);
        setContentView(R.layout.activity_result);

        result = findViewById(R.id.result);
        average = findViewById(R.id.average);
        rate = findViewById(R.id.rate);
        bar = findViewById(R.id.progressBar);

        Intent i = getIntent();

        presenter = new ResultPresenter(this, this);

        presenter.showResult(
                i.getStringExtra("semester average"),
                i.getStringExtra("new average"),
                i.getStringExtra("in100"),
                i.getIntExtra("passedHours", 0)
        );
    }

    public void back(View view) { presenter.back(); }

    @Override
    public void showResult(String semesterGPA, String overallGPA, String outOf100, int newPassedHours, String rate) {
                this.rate.setText(rate);
        average.setText(overallGPA+"\n"+outOf100+"%");

        ProgressBarAnimation anim = new ProgressBarAnimation(bar, 0, (((Float.parseFloat(overallGPA)+1)*100)*20));
        anim.setDuration(1000);
        bar.startAnimation(anim);

        String message_to_show = "";
        message_to_show = message_to_show.concat(getResources().getString(R.string.sem_avg) + "\t" + semesterGPA) + "\n";
        message_to_show = message_to_show.concat(getResources().getString(R.string.newAvg) + "\t" + overallGPA + "\n");
        message_to_show = message_to_show.concat(getResources().getString(R.string.newPassedHours) + "\t" + newPassedHours);

        result.setText(message_to_show);
    }

    @Override
    public void back() {
        finish();
    }
}
