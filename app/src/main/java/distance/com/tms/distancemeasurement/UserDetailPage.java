package distance.com.tms.distancemeasurement;


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
    Button saveDetailBtn;
    SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail_page);
        preferences= PreferenceManager.getDefaultSharedPreferences(this);
        checkPreference();
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
                   editor.putString("height",heightEd.getText().toString());
                   editor.putString("weight",weightEd.getText().toString());
                   editor.putString("age",ageEd.getText().toString());
                   editor.putString("gender",genderSpinner.getSelectedItem().toString());
                   editor.putString("multiplyFactor",String.valueOf(multiplyFactor));
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
