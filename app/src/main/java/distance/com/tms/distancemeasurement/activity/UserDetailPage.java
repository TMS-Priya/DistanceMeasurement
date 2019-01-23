package distance.com.tms.distancemeasurement.activity;


import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import distance.com.tms.distancemeasurement.R;

public class UserDetailPage extends AppCompatActivity {
    Spinner genderSpinner;
    String[] gender=new String[]{"male","female","transgender"};
    double multiplyFactor;
    EditText heightEd;
    TextInputLayout heightLayout;
    EditText weightEd;
    TextInputLayout weightLayout;
    EditText ageEd;
    TextInputLayout ageLayout;
    EditText fullNameEd;
    TextInputLayout fullNameLayout;
    Button saveDetailBtn;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_page);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        if(getIntent()!=null){
            if(getIntent().getIntExtra("flag",0)!=1){
                checkPreference();
            }
        }else{
            checkPreference();
        }

        initialize();
    }

    private void checkPreference() {
       if(preferences.getString("height","").length()>0) {
           startActivity(new Intent(UserDetailPage.this,MainActivity.class));
           finish();
       }
    }

    private void initialize() {
        genderSpinner=findViewById(R.id.gender_spinner);
        heightEd=findViewById(R.id.height_edittext);
        heightLayout=findViewById(R.id.height_ed_layout);
        weightEd=findViewById(R.id.weight_edittext);
        weightLayout=findViewById(R.id.weight_ed_layout);
        ageEd=findViewById(R.id.age_edittext);
        ageLayout=findViewById(R.id.age_ed_layout);
        saveDetailBtn=findViewById(R.id.save_detail_btn);
        fullNameEd=findViewById(R.id.name_edittext);
        fullNameLayout=findViewById(R.id.name_ed_layout);
        ArrayAdapter genderAdapter=new ArrayAdapter<>(this,android.R.layout.simple_dropdown_item_1line,gender);
        genderSpinner.setAdapter(genderAdapter);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(genderSpinner.getSelectedItem().toString().equals("female")){
                    multiplyFactor=0.413;
                }else{
                    multiplyFactor=0.415;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                // /nothing
            }
        });
        saveDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!heightEd.getText().toString().equals("")&&!weightEd.getText().toString().equals("")&&!ageEd.getText().toString().equals("")){
                   SharedPreferences.Editor editor= preferences.edit();
                   editor.putString("height",heightEd.getText().toString().trim());
                   editor.putString("weight",weightEd.getText().toString().trim());
                   editor.putString("age",ageEd.getText().toString().trim());
                   editor.putString("gender",genderSpinner.getSelectedItem().toString().trim());
                   editor.putString("multiplyFactor",String.valueOf(multiplyFactor));
                   editor.putString("userName",fullNameEd.getText().toString().trim());
                   editor.apply();
                   startActivity(new Intent(UserDetailPage.this,MainActivity.class));
                   finish();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(preferences.getString("height","").length()>0) {
            startActivity(new Intent(UserDetailPage.this,MainActivity.class));
            finish();
        }else
            finish();
    }
}
