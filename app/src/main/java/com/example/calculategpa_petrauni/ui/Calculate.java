package com.example.calculategpa_petrauni.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.calculategpa_petrauni.Adapters.CoursesAdapter;
import com.example.calculategpa_petrauni.Classes.AppConst;
import com.example.calculategpa_petrauni.Classes.BL;
import com.example.calculategpa_petrauni.R;
import com.example.calculategpa_petrauni.UIClasses.CustomTypeFaceSpan;
import com.example.calculategpa_petrauni.pojo.Course;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import hotchemi.android.rate.AppRate;


public class Calculate extends AppCompatActivity implements CalculateView {
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.settings)
    ImageView settings;
    @BindView(R.id.lastGPA)
    EditText lastGPA;
    @BindView(R.id.lastHours)
    EditText lastHours;
    @BindView(R.id.recycler)
    RecyclerView recycler;
    @BindView(R.id.left_border)
    View leftBorder;
    @BindView(R.id.right_border)
    View rightBorder;
    @BindView(R.id.top_border)
    View topBorder;
    @BindView(R.id.bottom_border)
    View bottomBorder;
    @BindView(R.id.addCourse)
    Button addCourse;
    @BindView(R.id.calculate)
    Button calculate;
    @BindView(R.id.clear)
    Button clear;

    /**
     * Views
     */
    /*private EditText passedMarks, previousAverage;
    @SuppressLint("StaticFieldLeak")
    public static Button calculate;
    private RecyclerView recyclerView;
    static View[] borders = new View[4];
    public TextView status;*/

    /**
     * Variables
     */
    private ArrayList<Course> list;
    private CoursesAdapter CoursesAdapter;
    CalculatePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES ? R.style.AppThemeDark : R.style.AppTheme);
        setContentView(R.layout.calculate);
        ButterKnife.bind(this);

        /**
         * Attaching variables to views
         */
        /*recyclerView = findViewById(R.id.recycler);
        passedMarks = findViewById(R.id.CH2);
        previousAverage = findViewById(R.id.previousAverage);
        calculate = findViewById(R.id.cal3);
        borders[0] = findViewById(R.id.right_border);
        borders[1] = findViewById(R.id.top_border);
        borders[2] = findViewById(R.id.left_border);
        borders[3] = findViewById(R.id.bottom_border);
        status = findViewById(R.id.tv_status);*/


        AppRate.with(this)
                .setInstallDays(1)
                .setLaunchTimes(3)
                .setRemindInterval(7)
                .monitor();
        AppRate.showRateDialogIfMeetsConditions(this);

        status.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                recreate();
            }
        });
        lastGPA.addTextChangedListener(textWatcher);
        lastHours.addTextChangedListener(textWatcher);

        list = new ArrayList<>();
        CoursesAdapter = new CoursesAdapter(list, this, R.layout.course_grids);

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(CoursesAdapter);

        hideBorders(getGrids());

        presenter = new CalculatePresenter(this, this);
        presenter.reset();

        settings.setOnClickListener(v -> presenter.showMenu());
        addCourse.setOnClickListener(v -> presenter.addCourse());
        clear.setOnClickListener(v -> presenter.reset());
        calculate.setOnClickListener(v -> {
            double average;
            int hours;
            try {
                average = Double.parseDouble(lastGPA.getText().toString().isEmpty() ? "0" : lastGPA.getText().toString());
                hours = Integer.parseInt(lastHours.getText().toString().isEmpty() ? "0" : lastHours.getText().toString());
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(this, getString(R.string.number_format_exception), Toast.LENGTH_LONG).show();
                return;
            }
            if (average > 4)
                Toast.makeText(this, getString(R.string.warn2), Toast.LENGTH_SHORT).show();
            else {
                if ((lastGPA.getText().toString().isEmpty() && !lastHours.getText().toString().isEmpty()) || (!lastGPA.getText().toString().isEmpty() && lastHours.getText().toString().isEmpty()))
                    Toast.makeText(this, getString(R.string.warn1), Toast.LENGTH_SHORT).show();
                else
                    presenter.getResult(average, hours, list);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) backFromSettings();
    }

    /**
     * Listeners
     */
    TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            try {
                boolean shouldAllow =
                        !lastHours.getText().toString().isEmpty() &&
                                !lastGPA.getText().toString().isEmpty() &&
                                Double.parseDouble(lastGPA.getText().toString()) > 0;

                CoursesAdapter.activate_LM(shouldAllow);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                Toast.makeText(Calculate.this, getString(R.string.number_format_exception), Toast.LENGTH_LONG).show();
            }
        }
    };

    /**
     * To apply what came back from settings page
     */
    private void backFromSettings() {
        SharedPreferences preferences = getSharedPreferences(AppConst.name, MODE_PRIVATE);
        hideBorders(preferences.getBoolean(AppConst._grids, true));
        loadLocale();
        recreate();
    }

    /**
     * To handle showing and hiding the borders
     *
     * @return shouldShowBorders?
     */
    private boolean getGrids() {
        SharedPreferences preferences = getSharedPreferences(AppConst.name, MODE_PRIVATE);
        return preferences.getBoolean(AppConst._grids, true);
    }

    public void hideBorders(boolean show) {
        int visibility = show ? View.VISIBLE : View.INVISIBLE;
        rightBorder.setVisibility(visibility);
        topBorder.setVisibility(visibility);
        bottomBorder.setVisibility(visibility);
        leftBorder.setVisibility(visibility);
        CoursesAdapter = new CoursesAdapter(list, this, show ? R.layout.course_grids : R.layout.course_without_grids);
        recycler.setAdapter(CoursesAdapter);
    }

    private void applyFontToMenuItem(MenuItem mi) {
        Typeface font = ResourcesCompat.getFont(this, R.font.din_next_lt_w23_regular);
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypeFaceSpan("", font, getResources().getColor(R.color.item)), 0, mNewTitle.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }

    /**
     * Language handling
     *
     * @param language to change app language
     */
    private void setLocale(String language) {
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            config.setLocale(new Locale(language));
        else
            config.locale = new Locale(language);
        resources.updateConfiguration(config, dm);
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
        SharedPreferences.Editor editor = getSharedPreferences(AppConst.name, MODE_PRIVATE).edit();
        editor.putString(AppConst._language, language);
        editor.apply();
    }

    private void loadLocale() {
        SharedPreferences preferences = getSharedPreferences(AppConst.name, MODE_PRIVATE);
        setLocale(preferences.getString(AppConst._language, "ar"));
    }

    @Override
    public void onCalculateGPA(BL bl) {
        Intent i = new Intent(this, Result.class);
        i.putExtra("new average", bl.getOverallAverage());
        i.putExtra("semester average", bl.getSemAverage());
        i.putExtra("in100", bl.getAvg100());
        i.putExtra("passedHours", bl.getNewPassedHours());
        startActivity(i);
    }

    @Override
    public void addCourse() {
        list.add(new Course(
                getResources().getString(R.string.ignore),
                getResources().getStringArray(R.array.hours)[2],
                getResources().getString(R.string.ignore),
                getString(R.string.ignore)
        ));
        CoursesAdapter.notifyDataSetChanged();
        Toast.makeText(this, getResources().getString(R.string.done), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void reset() {
        list.clear();
        for (int i = 0; i < 7; i++) {
            Course course = new Course();
            course.setMark(getResources().getString(R.string.ignore), getString(R.string.ignore));
            course.setHours(getResources().getStringArray(R.array.hours)[2]);
            course.setLastMark(getResources().getString(R.string.ignore));
            list.add(course);
        }
        lastHours.setText("");
        lastGPA.setText("");
        CoursesAdapter.notifyDataSetChanged();
        calculate.setEnabled(false);
    }

    @Override
    public void showMenu() {
        PopupMenu popup = new PopupMenu(this, findViewById(R.id.settings));
        popup.setOnMenuItemClickListener(item -> {
            Intent i = new Intent(Calculate.this, MarksInfo.class);
            switch (item.getItemId()) {
                case R.id.marks_info:
                    i.putExtra(AppConst.isMark, 0);
                    startActivity(i);
                    break;
                case R.id.marks_distribution:
                    i.putExtra(AppConst.isMark, 1);
                    startActivity(i);
                    break;
                case R.id.gpa_info:
                    i.putExtra(AppConst.isMark, 2);
                    startActivity(i);
                    break;
                case R.id.settings:
                    startActivityForResult(new Intent(Calculate.this, Settings.class), 1);
                    break;
                default:
                    return false;
            }
            return true;
        });
        popup.inflate(R.menu.menu);
        popup.show();

        Menu menu = popup.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem mi = menu.getItem(i);
            applyFontToMenuItem(mi);
        }
    }
}
