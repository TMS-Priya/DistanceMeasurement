package distance.com.tms.distancemeasurement.activity;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import distance.com.tms.distancemeasurement.POJO_Class.RecordDto;
import distance.com.tms.distancemeasurement.R;
import distance.com.tms.distancemeasurement.db.DBHelper;

public class RecordsPage extends AppCompatActivity {
    List<RecordDto> recordDtos;
    DBHelper dbHelper;
    SharedPreferences preferences;
    String userName;
    RecordAdapter recordAdapter;
    TextView noRecordTxt;
    RelativeLayout recordLay;
    ListView recordList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_records_page);
        preferences=PreferenceManager.getDefaultSharedPreferences(this);
        userName=preferences.getString("userName","");
        inti();

        getDetails();
    }

    private void inti() {
        noRecordTxt=findViewById(R.id.no_record_txt);
        recordLay=findViewById(R.id.record_layout);
        recordList=findViewById(R.id.record_list);
    }

    private void getDetails() {
        dbHelper=new DBHelper(this);
        recordDtos=new ArrayList<>();
        recordDtos=dbHelper.getAllMessages(userName);
        if(recordDtos.size()>0){
            recordLay.setVisibility(View.VISIBLE);
            noRecordTxt.setVisibility(View.GONE);
            recordAdapter=new RecordAdapter(this,recordDtos);
            recordList.setAdapter(recordAdapter);

        }else
            {
            recordLay.setVisibility(View.GONE);
            noRecordTxt.setVisibility(View.VISIBLE);
        }

    }
}
