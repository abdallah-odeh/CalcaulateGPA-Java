package com.example.calculategpa_petrauni.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.calculategpa_petrauni.Classes.AppConst;
import com.example.calculategpa_petrauni.R;
import com.example.calculategpa_petrauni.pojo.ContactMessage;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ContactMe extends AppCompatActivity {

    EditText name, email, msg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES?R.style.AppThemeDark:R.style.AppTheme);
        setContentView(R.layout.activity_report_problem);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        msg = findViewById(R.id.report_msg);
    }

    public void Send(View view) {
        if (isValidInput())
        {
            DatabaseReference mDatabase;
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(AppConst.reports).push().setValue(new ContactMessage(email.getText().toString(), msg.getText().toString(), name.getText().toString()));
            Toast.makeText(ContactMe.this, R.string.report_sent, Toast.LENGTH_SHORT).show();
            clearFields();
        }
        else
            Toast.makeText(ContactMe.this, R.string.emptyReportMsg, Toast.LENGTH_SHORT).show();
    }

    private boolean isValidInput(){
        if(name.getText().toString().isEmpty())
            return false;
        return !msg.getText().toString().isEmpty();
    }

    private void clearFields(){
        name.setText("");
        email.setText("");
        msg.setText("");
    }
}
