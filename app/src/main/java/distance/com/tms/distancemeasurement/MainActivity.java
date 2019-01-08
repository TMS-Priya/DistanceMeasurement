package distance.com.tms.distancemeasurement;

import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import javax.xml.datatype.Duration;

public class MainActivity extends AppCompatActivity implements SensorEventListener,StepListener {
    private TextView txtSteps;
    private StepDetector simpleStepDetector;
    private SensorManager sensorManager;
    private Sensor accel;
    private static final String TEXT_NUM_STEPS = "Number of Steps travelled by you: ";
    private int numSteps;
    Button btnStart;
    Button btnReset;
    TextView sensorListTxt;
    TextView distanceTxt;
    Double multiplyFactor;
    long startTime;
    Float height,weight,age;
    String gender;
    SharedPreferences preferences;
    float distance;
    TextView calorieBurnText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preferences=PreferenceManager.getDefaultSharedPreferences(this);
        getDetails();

        // Get an instance of the SensorManager
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accel = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
        distanceTxt=findViewById(R.id.distance);
        calorieBurnText=findViewById(R.id.calorie_burn_txt);
        txtSteps = (TextView) findViewById(R.id.tv_steps);
        sensorListTxt=findViewById(R.id.sensor_list);
        btnStart = (Button) findViewById(R.id.btn_start);
        btnReset=findViewById(R.id.btn_reset);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnStart.getText().toString().equalsIgnoreCase("start")){
                    startTime=System.currentTimeMillis();
                    btnStart.setText("stop");
                    sensorManager.registerListener(MainActivity.this, accel, SensorManager.SENSOR_DELAY_FASTEST);

                }else if(btnStart.getText().toString().equalsIgnoreCase("stop")){
                    startTime=System.currentTimeMillis()-startTime;
                    btnStart.setText("start");
                    sensorManager.unregisterListener(MainActivity.this);
                    String durationStr=String.valueOf(startTime);
                    Float timeInDuration=Float.parseFloat(durationStr)/3600000;
                    Float caloriesBurn=getCaloriesBurn(height,age,weight,gender,timeInDuration,distance);
                    calorieBurnText.setText("Congratulation you burn "+String.valueOf(caloriesBurn)+ " calories");

                }

            }
        });
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(btnStart.getText().toString().equalsIgnoreCase("stop")){
                    Toast.makeText(MainActivity.this,"First you should stop the counting...",Toast.LENGTH_LONG).show();
                }else if(btnStart.getText().toString().equalsIgnoreCase("start")){
                    numSteps=0;
                    distanceTxt.setText("Ready to count your distance");
                    txtSteps.setText("");
                    calorieBurnText.setText("");
                }

            }
        });

        List<Sensor> sensorList  =sensorManager.getSensorList(Sensor.TYPE_ALL);
        StringBuilder sensorTxt = new StringBuilder();

        for (Sensor currentSensor : sensorList ) {
            sensorTxt.append(currentSensor.getName()).append(
                    System.getProperty("line.separator"));
        }
        sensorListTxt.setText(sensorTxt);

    }

    private void getDetails() {

        if(!preferences.getString("multiplyFactor","").equals("")){
            multiplyFactor=Double.parseDouble(preferences.getString("multiplyFactor",""));
            height=Float.parseFloat(preferences.getString("height",""));
            weight=Float.parseFloat(preferences.getString("weight",""));
            age=Float.parseFloat(preferences.getString("age",""));
            gender=preferences.getString("gender","");
        }


    }


    private void getDistance(int steps) {

        Double h=height*multiplyFactor;
        distance= (float)(steps*h)/(float)100000;
        distanceTxt.setText("Distance travelled by You.. "+String.valueOf(distance)+"Km");

    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // nothing done here
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);


        }
    }

    @Override
    public void step(long timeNs) {
        numSteps++;
        txtSteps.setText(TEXT_NUM_STEPS+ numSteps);
        getDistance(numSteps);


    }

    // calorie calculation method ......

    private Float getCaloriesBurn(float height, float age, float weight, String gender, float durationInSeconds, float strideLengthInMetres) {
        float priyaRestingMetabolicRateVal = convertKilocaloriesToMlKmin(priyaRestingMetabolicRate(gender, weight, age,   convertMetresToCentimetre(height)), weight);

        Double kmToM=strideLengthInMetres*0.621;
        float speedInMph =kmToM.floatValue() / durationInSeconds;
        float metValue = getMetForActivity(speedInMph);
        float constant = 3.5f;
        float correctedMets = metValue * (constant / priyaRestingMetabolicRateVal);
        return correctedMets * durationInSeconds * weight;
    }
    private static float getMetForActivity(float speedInMph) {
        if (speedInMph < 2.0) {
            return 2.0f;
        } else if (Float.compare(speedInMph, 2.0f) == 0) {
            return 2.8f;
        } else if (Float.compare(speedInMph, 2.0f) > 0 && Float.compare(speedInMph, 2.7f) <= 0) {
            return 3.0f;
        } else if (Float.compare(speedInMph, 2.8f) > 0 && Float.compare(speedInMph, 3.3f) <= 0) {
            return 3.5f;
        } else if (Float.compare(speedInMph, 3.4f) > 0 && Float.compare(speedInMph, 3.5f) <= 0) {
            return 4.3f;
        } else if (Float.compare(speedInMph, 3.5f) > 0 && Float.compare(speedInMph, 4.0f) <= 0) {
            return 5.0f;
        } else if (Float.compare(speedInMph, 4.0f) > 0 && Float.compare(speedInMph, 4.5f) <= 0) {
            return 7.0f;
        } else if (Float.compare(speedInMph, 4.5f) > 0 && Float.compare(speedInMph, 5.0f) <= 0) {
            return 8.3f;
        } else if (Float.compare(speedInMph, 5.0f) > 0) {
            return 9.8f;
        }
        return 0;
    }
    public float convertKilocaloriesToMlKmin(float kilocalories, float weightKgs) {
        float kcalMin = kilocalories / 1440;
        kcalMin /= 5;

        return ((kcalMin / (weightKgs)) * 1000);
    }
    public float convertMetresToCentimetre(float metres) {
        return metres * 100;
    }

    public float priyaRestingMetabolicRate(String gender, float weightKg, float age, float heightCm) {
        if (gender.equals("female")) {
            return 655.0955f + (1.8496f * heightCm) + (9.5634f * weightKg) - (4.6756f * age);
        } else {
            return 66.4730f + (5.0033f * heightCm) + (13.7516f * weightKg) - (6.7550f * age);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.right_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     //   preferences.edit().clear().apply();
        startActivity(new Intent(MainActivity.this,UserDetailPage.class));
        finish();
        return true;

    }
}

/*   // this code for pedometer step count. this one need step counter sensor
public class MainActivity extends AppCompatActivity implements SensorEventListener{
    Spinner genderSpinner;
    String[] gender=new String[]{"male","female","Third Gender"};
    Double multiplyFactor;
    SensorManager sManager;
    private long steps = 0;
    EditText heightEd;
    TextInputLayout heightLayout;
    TextView distanceTxt;
    Sensor stepSensor;
    boolean running=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        initialize();

    }

    private void initialize() {
        genderSpinner=findViewById(R.id.gender_spinner);
        heightEd=findViewById(R.id.height_edittext);
        heightLayout=findViewById(R.id.height_ed_layout);
        distanceTxt=findViewById(R.id.distance);
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


    }


    @Override
    protected void onResume() {

        super.onResume();
        running=true;
        stepSensor = sManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
        if(stepSensor!=null){
            sManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_NORMAL);
        }else{
            Toast.makeText(this,"sensor not found!",Toast.LENGTH_LONG).show();
        }



    }

    @Override
    protected void onStop() {
        super.onStop();
        running=false;
        sManager.unregisterListener(this, stepSensor);
    }




    private void getDistance(Long steps) {
        if(heightEd.getText().toString().equals("")&&heightEd.getText().toString()==null){
            heightLayout.setError("give your height to show distance");
        }else{
            int height=Integer.parseInt(heightEd.getText().toString());
            Double h=height*multiplyFactor;
            float distance = (float)(steps*h)/(float)100000;
            distanceTxt.setText(String.valueOf(distance));

        }

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(running){
            Sensor sensor = sensorEvent.sensor;
            float[] values = sensorEvent.values;
            int value = -1;

            if (values.length > 0) {
                value = (int) values[0];
            }


            if (sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
                steps++;
            }
            Toast.makeText(MainActivity.this,String.valueOf(steps),Toast.LENGTH_LONG).show();
            if(steps!=0){
                getDistance(steps);
            }
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
           // nothing
    }
}
*/
